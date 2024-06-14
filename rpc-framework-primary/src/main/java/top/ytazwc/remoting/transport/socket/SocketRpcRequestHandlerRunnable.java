package top.ytazwc.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.remoting.dto.RpcRequest;
import top.ytazwc.remoting.dto.RpcResponse;
import top.ytazwc.remoting.handler.RpcRequestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author 花木凋零成兰
 * @title SocketRpcRequestHandlerRunnable
 * @date 2024/6/14 22:23
 * @package top.ytazwc.remoting.transport.socket
 * @description 服务端处理 客户端有关业务
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable{
    // 客户端连接套接字
    private final Socket socket;
    // RPC请求 处理器
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        // 当前线程 处理服务器信息
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            // 读取客户端请求信息
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 获取服务调用结果
            Object result = rpcRequestHandler.handle(rpcRequest);
            // 返回响应结果
            objectOutputStream.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            // 刷新 为下次输出做准备
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
