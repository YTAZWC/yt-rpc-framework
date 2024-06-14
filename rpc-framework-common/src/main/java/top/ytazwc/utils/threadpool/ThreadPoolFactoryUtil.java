package top.ytazwc.utils.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author yt
 * 2024/6/14
 * 创建线程池的工具类
 */
@Slf4j
public final class ThreadPoolFactoryUtil {

    /**
     * 通过 线程名前缀(threadNamePrefix) 来区分不同线程池；
     * 可以把相同threadNamePrefix的线程池 看作是在同一业务场景服务
     * key: threadNamePrefix
     * value: threadPool
     */
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    // 私有构造
    private ThreadPoolFactoryUtil() {
    }

    // 创建默认配置的非保护线程的线程池
    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix) {
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        return createCustomThreadPoolIfAbsent(customThreadPoolConfig, threadNamePrefix, false);
    }

    // 尝试创建自定义配置的非保护线程的线程池
    public static ExecutorService createCustomThreadPoolIfAbsent(String threadNamePrefix, CustomThreadPoolConfig customThreadPoolConfig) {
        return createCustomThreadPoolIfAbsent(customThreadPoolConfig, threadNamePrefix, false);
    }

    /**
     * 查看缓存中是否有对应前缀的线程池，若不存在则创建对应配置的线程池并缓存，然后返回
     * @param customThreadPoolConfig 自定义线程池配置
     * @param threadNamePrefix 线程池前缀
     * @param daemon 是否为保护线程
     * @return 执行结果
     */
    public static ExecutorService createCustomThreadPoolIfAbsent(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        // 如果缓存的线程池中不存在该前缀的线程池，则创建对应该前缀的线程池
        ExecutorService threadPool = THREAD_POOLS.computeIfAbsent(
                threadNamePrefix,
                k -> createThreadPool(customThreadPoolConfig, threadNamePrefix, daemon));
        // 如果 创建的自定义线程池已经被 关闭 则重新创建一个
        if (threadPool.isShutdown() || threadPool.isTerminated()) {
            THREAD_POOLS.remove(threadNamePrefix);
            threadPool = createThreadPool(customThreadPoolConfig, threadNamePrefix, daemon);
            THREAD_POOLS.put(threadNamePrefix, threadPool);
        }
        return threadPool;
    }

    /**
     * 关闭所有线程池
     */
    public static void shutDownAllThreadPool() {
        log.info("class shutDownAllThreadPool method");
        THREAD_POOLS.entrySet().parallelStream().forEach(entry -> {
            ExecutorService executorService = entry.getValue();
            // 关闭线程池
            executorService.shutdown();
            // 输出线程池关闭信息，isTerminated()判断当前线程池的任务是否在关闭后都已完成 true表示已完成
            log.info("shut down thread poll [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try {
                // 尝试 在指定时间内 等待结束；执行结束则返回true，否则返回false
                boolean termination = executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("Thread pool never terminated");
                executorService.shutdownNow();
            }
        });
    }

    /**
     * 根据自定义线程池参数 创建线程池
     * @param customThreadPoolConfig 自定义线程池参数
     * @param threadNamePrefix 线程池前缀
     * @param daemon 是否为保护线程
     * @return 创建的线程池
     */
    private static ExecutorService createThreadPool(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        // 根据线程前缀和是否为保护线程 获取对应的线程工厂
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        // 实例化一个线程池
        return new ThreadPoolExecutor(
                customThreadPoolConfig.getCorePoolSize(),
                customThreadPoolConfig.getMaximumPoolSize(),
                customThreadPoolConfig.getKeepAliveTime(),
                customThreadPoolConfig.getUnit(),
                customThreadPoolConfig.getWorkQueue(),
                threadFactory);
    }

    /**
     * 创建 ThreadFactory(提供一个创建线程的统一方式),如果threadNamePrefix不为空
     * 则使用自建ThreadFactory，否则使用defaultThreadFactory
     * @param threadNamePrefix 作为创建的线程名字的前缀
     * @param daemon 指定是否为 Daemon Thread(守护线程)
     * @return ThreadFactory
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        if (threadNamePrefix != null) {
            if (daemon != null) {
                return new ThreadFactoryBuilder()
                        // -%d 符号会被线程编码替换 即据此生成有唯一后缀的线程名称
                        .setNameFormat(threadNamePrefix + "-%d")
                        // 判断新创建的线程是否为守护线程
                        // 守护线程会在所有非守护线程结束时自动终止
                        .setDaemon(daemon).build();
            } else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        // prefix为空表示不需要自定义线程名前缀 遵循JVM默认行为产生没有特定前缀的线程
        return Executors.defaultThreadFactory();
    }

    // 打印对应线程池的状态
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        // 创建定时任务执行器 周期性执行指定的任务 初始延迟为0s；后续为1秒
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(
                1, createThreadFactory("print-thread-pool-status", false));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("=========ThreadPool Status=========");
            // 线程池大小
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            // 活动线程数
            log.info("Active Threads: [{}]", threadPool.getActiveCount());
            // 完成的任务数
            log.info("Number of Tasks: [{}]", threadPool.getCompletedTaskCount());
            // 队列中的任务数
            log.info("Number of Tasks in Queue: [{}]", threadPool.getQueue().size());
        }, 0, 1, TimeUnit.SECONDS);
    }

}
