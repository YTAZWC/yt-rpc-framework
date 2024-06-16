package top.ytazwc.remoting.transport.netty.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import top.ytazwc.enums.CompressType;
import top.ytazwc.enums.RpcResponseCode;
import top.ytazwc.enums.SerializationType;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.remoting.constants.RpcConstants;
import top.ytazwc.remoting.dto.RpcMessage;
import top.ytazwc.remoting.dto.RpcRequest;
import top.ytazwc.remoting.dto.RpcResponse;
import top.ytazwc.remoting.handler.RpcRequestHandler;

/**
 * @author 花木凋零成兰
 * @title NettyRpcServerHandler
 * @date 2024/6/15 22:59
 * @package top.ytazwc.remoting.transport.netty.server
 * @description 自定义服务端 处理器，处理客户端发送的数据
 * 处理RPC请求，处理完之后将得到的响应结果传输给客户端
 * TODO 待学习理解
 */
@Slf4j
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    // 读取内容
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        try {
            if (msg instanceof RpcMessage) {
                log.info("服务端收到消息: [{}]", msg);
                // 获取消息类型
                byte messageType = ((RpcMessage)msg).getMessageType();
                RpcMessage rpcMessage = new RpcMessage();
                // 消息编码
                rpcMessage.setCodec(SerializationType.HESSIAN.getCode());
                // 压缩类型
                rpcMessage.setCompress(CompressType.GZIP.getCode());
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    rpcMessage.setData(RpcConstants.PONG);
                    rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                } else {
                    RpcRequest rpcRequest = (RpcRequest) ((RpcMessage)msg).getData();
                    // 执行目标方法（客户端需要执行的方法）并返回方法结果
                    Object result = rpcRequestHandler.handle(rpcRequest);
                    // 打印调用目标方法返回结果信息
                    log.info(String.format("server get result: %s", result.toString()));
                    rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                        rpcMessage.setData(rpcResponse);
                    } else {
                        RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCode.FAIL);
                        rpcMessage.setData(rpcResponse);
                        log.error("not writable now, message dropped");
                    }
                }
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } finally {
            // 确保 ByteBuf 已释放，否则可能会出现内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent)evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("发生空闲检查，因此请关闭连接");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("服务端异常");
        cause.printStackTrace();
        ctx.close();
    }
}
