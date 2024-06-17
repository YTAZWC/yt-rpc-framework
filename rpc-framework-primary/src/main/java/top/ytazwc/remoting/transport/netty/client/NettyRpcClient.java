package top.ytazwc.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.ytazwc.enums.CompressType;
import top.ytazwc.enums.SerializationType;
import top.ytazwc.enums.ServiceDiscoveryType;
import top.ytazwc.extension.ExtensionLoader;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.registry.ServiceDiscovery;
import top.ytazwc.remoting.constants.RpcConstants;
import top.ytazwc.remoting.dto.RpcMessage;
import top.ytazwc.remoting.dto.RpcRequest;
import top.ytazwc.remoting.dto.RpcResponse;
import top.ytazwc.remoting.transport.RequestRpcTransport;
import top.ytazwc.remoting.transport.netty.codec.RpcMessageDecoder;
import top.ytazwc.remoting.transport.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author 花木凋零成兰
 * @title NettyRpcClient
 * @date 2024/6/15 15:48
 * @package top.ytazwc.remoting.transport.netty
 * @description NettyRpcClient
 */
@Slf4j
public class NettyRpcClient implements RequestRpcTransport {

    // 服务发现 用于客户端获取服务列表
    private final ServiceDiscovery serviceDiscovery;
    // 处理未响应的业务
    private final UnProcessedRequests unprocessedRequests;
    // 用于缓存和获取连接
    private final ChannelProvider channelProvider;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyRpcClient() {
        // 初始化 EventLoopGroup、Bootstrap 等资源
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 连接的超时期限。如果超过此时间或无法建立连接，则连接将失败。
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 如果 15 秒内没有向服务器发送任何数据，则发送检测信号请求
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
        // TODO ExtensionLoader
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension(ServiceDiscoveryType.ZK.getName());
//        this.serviceDiscovery = SingletonFactory.getInstance(ZkServiceDiscoveryImpl.class);
        this.unprocessedRequests = SingletonFactory.getInstance(UnProcessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 连接服务器并获取通道，以便您可以向服务器发送RPC消息
     * 知道服务器地址后，可以通过NettyClient连接服务端
     * 有了 Channel 后能发送数据到服务端
     * @param inetSocketAddress 连接地址
     * @return 返回连接结果Channel
     */
    @SneakyThrows   // 隐式抛出异常
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        // 异步处理连接操作
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener)future -> {
            if (future.isSuccess()) {
                log.info("The Client has connected [{}] successful!", inetSocketAddress);
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        // 阻塞 直到异步连接操作完成 返回连接成功的Channel对象
        // 可能会抛出InterruptedException和ExecutionException异常
        return completableFuture.get();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 构建返回值
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // 获取服务地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        // 获取服务地址对应的通道
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // 通道可用 新增未处理 服务请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder()
                    .data(rpcRequest)
                    .codec(SerializationType.HESSIAN.getCode())
                    .compress(CompressType.GZIP.getCode())
                    .messageType(RpcConstants.RESPONSE_TYPE)
                    .build();
            // 发送数据
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener)future -> {
                if (future.isSuccess()) {
                    // 发送成功 打印日志发送消息
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed: ", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }

    /**
     * 根据连接地址，执行连接操作；并获取连接通道
     * @param inetSocketAddress 连接地址
     * @return 对应通道
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

}
