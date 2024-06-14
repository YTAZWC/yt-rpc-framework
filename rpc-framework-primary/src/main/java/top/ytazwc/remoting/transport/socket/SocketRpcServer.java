package top.ytazwc.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import top.ytazwc.config.RpcServiceConfig;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.provider.ServiceProvider;
import top.ytazwc.provider.impl.ZkServiceProviderImpl;
import top.ytazwc.utils.threadpool.ThreadPoolFactoryUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

/**
 * @author yt
 * 2024/6/14
 * Socket服务端，用于等待客户端连接，获取客户端发送的rpc请求
 * 拿到请求后 执行对应的方法，并将执行结果响应返回给客户端
 */
@Slf4j
public class SocketRpcServer {
    // 线程池
    private final ExecutorService threadPool;
    // 目标服务提供
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        // 线程池前缀为 rpc服务线程池
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    // 注册服务 根据服务配置信息
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            // 获得随机端口
            String host = InetAddress.getLocalHost().getHostAddress();;
            server.bind(new InetSocketAddress(host, 9998));
//            CustomShutdownHook.getCustomShutdownHook().clearAll();
        } catch (IOException e) {
            log.error("occur IOException: ", e);
        }
    }

}
