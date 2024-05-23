package top.ytazwc.remoting.transport.socket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.ytazwc.remoting.dto.RequestRpc;
import top.ytazwc.remoting.transport.RequestRpcTransport;

/**
 * @author 花木凋零成兰
 * @title SocketRpcClient
 * @date 2024/5/23 15:05
 * @package top.ytazwc.remoting.transport.socket
 * @description 发送请求的客户端
 * 当知道服务端的地址时,通过该类发送RPC请求到服务端
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RequestRpcTransport {

    // private final ServiceDiscovery serviceDiscovery;

//    public SocketRpcClient() {
//        this.serviceDiscovery = ;
//    }

    @Override
    public Object sendRpcRequest(RequestRpc requestRpc) {
        // 根据请求实体获取
        return null;
    }
}
