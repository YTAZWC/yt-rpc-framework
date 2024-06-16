package top.ytazwc.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ytazwc.config.CustomShutdownHook;
import top.ytazwc.config.RpcServiceConfig;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.provider.ServiceProvider;
import top.ytazwc.provider.impl.ZkServiceProviderImpl;
import top.ytazwc.remoting.transport.netty.codec.RpcMessageDecoder;
import top.ytazwc.remoting.transport.netty.codec.RpcMessageEncoder;
import top.ytazwc.utils.RuntimeUtil;
import top.ytazwc.utils.threadpool.ThreadPoolFactoryUtil;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author 花木凋零成兰
 * @title NettyRpcServer
 * @date 2024/6/15 20:35
 * @package top.ytazwc.remoting.transport.netty.server
 * @description Netty RPC服务端
 * 监听客户端连接;根据客户端消息调用相应的方法，然后将结果返回给客户端
 * TODO 待理解学习
 */
@Slf4j
@Component
public class NettyRpcServer {

    public static final int PORT = 9998;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);

    /**
     * 注册服务
     * @param rpcServiceConfig RPC 服务配置
     */
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @SneakyThrows
    public void start() {
        // 启动服务前 先注销所有服务
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        // 服务地址
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("server-handler-group", false)
        );
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法, 作用是尽量发送大数据快，减少网络传输
                    // TCP_NODELAY 参数的作用是控制是否启用 Nagle 算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 表示系统用于临时存在三次握手的请求的队列的最大长度，如果连接建立频繁
                    // 服务器处理创建新连接较慢时，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 30 秒之内没有收到客户端请求的话就关闭连接
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            p.addLast(new RpcMessageEncoder());
                            p.addLast(new RpcMessageDecoder());
                            p.addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                        }
                    });
            // 绑定端口 同步等待绑定成功
            ChannelFuture channelFuture = b.bind(host, PORT).sync();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server: " + e);
        } finally {
            log.error("shutdown bossGroup and workGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }
    }

}
