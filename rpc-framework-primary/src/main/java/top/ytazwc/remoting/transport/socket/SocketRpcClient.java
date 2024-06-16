package top.ytazwc.remoting.transport.socket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.ytazwc.exception.RpcException;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.registry.ServiceDiscovery;
import top.ytazwc.registry.zk.ZkServiceDiscoveryImpl;
import top.ytazwc.remoting.dto.RpcRequest;
import top.ytazwc.remoting.transport.RequestRpcTransport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author 花木凋零成兰
 * @title SocketRpcClient
 * @date 2024/5/23 15:05
 * @package top.ytazwc.remoting.transport.socket
 * @description 发送请求的客户端; 将请求发送给服务端(目标方法所在服务器)
 * 当知道服务端的地址时,通过该类发送RPC请求到服务端
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RequestRpcTransport {

    // 服务发现
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
//        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        // TODO ExtensionLoader
//        this.serviceDiscovery = new ZkServiceDiscoveryImpl();
        this.serviceDiscovery = SingletonFactory.getInstance(ZkServiceDiscoveryImpl.class);
    }

    /**
     * 发生RPC请求
     * @param rpcRequest 请求体
     * @return 服务响应结果
     */
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        // 根据请求信息来查找对应服务
        InetSocketAddress socketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            // 客户端建立连接
            socket.connect(socketAddress);
            // 通过输出流 向服务端写入请求数据
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(rpcRequest);
            // 通过输入流 从服务端读取响应数据 并返回
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("服务调用失败: ", e);
        }
    }
}
