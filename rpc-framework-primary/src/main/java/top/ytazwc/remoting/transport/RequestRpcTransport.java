package top.ytazwc.remoting.transport;

import top.ytazwc.remoting.dto.RpcRequest;

/**
 * @author 花木凋零成兰
 * @title RequestRpcTransport
 * @date 2024/5/23 14:55
 * @package top.ytazwc.remoting.transport
 * @description 发送RPC请求的接口 用于顶层控制网络请求的方式
 * 不同的方式只需继承该接口即可
 */
public interface RequestRpcTransport {
    /**
     * 发送PRC请求并获取响应结果
     * @param rpcRequest 请求体
     * @return 响应结果
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
