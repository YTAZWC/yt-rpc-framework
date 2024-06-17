package top.ytazwc.remoting.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.ytazwc.enums.CompressType;
import top.ytazwc.enums.SerializationType;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.remoting.constants.RpcConstants;
import top.ytazwc.remoting.dto.RpcMessage;
import top.ytazwc.remoting.dto.RpcResponse;

import java.net.InetSocketAddress;

/**
 * @author 花木凋零成兰
 * @title NettyRpcClientHandler
 * @date 2024/6/15 16:34
 * @package top.ytazwc.remoting.transport.netty.client
 * @description 自定义客户端 ChannelHandler 以处理服务器发送的数据
 * TODO 待理解学习
 */
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {
    // 未处理的服务器请求
    private final UnProcessedRequests unProcessedRequests;
    // RPC客户端
    private final NettyRpcRequestClient nettyRpcClient;

    public NettyRpcClientHandler() {
        this.unProcessedRequests = SingletonFactory.getInstance(UnProcessedRequests.class);
        this.nettyRpcClient = SingletonFactory.getInstance(NettyRpcRequestClient.class);
    }

    // 读取服务器传输的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        try {
            log.info("服务端收到信息: [{}]", msg);
            if (msg instanceof RpcMessage) {
                RpcMessage message = (RpcMessage) msg;
                byte messageType = message.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    // 消息类型为请求 打印请求数据
                    log.info("heart [{}]", message.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE) {
                    // 消息类型为响应 获取响应数据
                    RpcResponse<Object> rpcResponse = (RpcResponse<Object>) message.getData();
                    unProcessedRequests.complete(rpcResponse);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    // 心跳机制 保证客户端和服务端的连接不被切断 避免重连
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
                // 设置序列化类型
                rpcMessage.setCodec(SerializationType.PROTOSTUFF.getCode());
                // 设置压缩类型
                rpcMessage.setCompress(CompressType.GZIP.getCode());
                // 设置消息类型
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                // 设置消息携带数据
                rpcMessage.setData(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

    // 在处理客户端消息时发生异常时调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client catch exception: ", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
