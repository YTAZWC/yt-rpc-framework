package top.ytazwc.utils.threadpool;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author yt
 * 2024/6/14
 * 线程池自定义配置 可根据具体业务场景修改配置参数
 */
@Getter
@Setter
public class CustomThreadPoolConfig {

    /**
     * 线程池默认参数
     */
    // 默认核心池大小
    private static final int DEFAULT_CORE_POOL_SIZE = 0;
    // 默认线程池最大数
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 100;
    // 默认保持活动的时间
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;
    // 默认时间单位
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
    // 默认阻塞队列容量
    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;
    // 阻塞队列容量
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 可配置参数
     */
    // 核心池大小
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    // 最大线程数
    private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE;
    // 保持活跃的时间
    private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    // 时间单位
    private TimeUnit unit = DEFAULT_TIME_UNIT;
    // 使用有界队列
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);


}
