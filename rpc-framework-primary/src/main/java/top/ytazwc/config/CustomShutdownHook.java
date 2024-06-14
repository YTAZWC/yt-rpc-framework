package top.ytazwc.config;

import lombok.extern.slf4j.Slf4j;
import top.ytazwc.utils.threadpool.ThreadPoolFactoryUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author 花木凋零成兰
 * @title CustomShutdownHook
 * @date 2024/6/14 22:11
 * @package top.ytazwc.config
 * @description 关闭服务器后，执行注销所有服务等操作
 */
@Slf4j
public class CustomShutdownHook {

    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    /**
     * 获取单例
     * @return 返回单例
     */
    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    /**
     * 注销所有服务
     */
    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        // 向Java虚拟机注册一个关闭钩子线程
        // 虚拟机开始关闭时 钩子线程会被执行
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 9998);
                // 清除Zookeeper管理的服务
//                CuratorUtils.clearRegistry();
            } catch (UnknownHostException e) {
            }
            // 删除线程池
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }

}
