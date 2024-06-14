package top.ytazwc.exception;

import top.ytazwc.enums.RpcErrorMessage;

/**
 * @author yt
 * 2024/6/14
 * 封装RPC框架异常
 */
public class RpcException extends RuntimeException {
    // 错误信息类型+信息
    public RpcException(RpcErrorMessage rpcErrorMessage, String info) {
        super(rpcErrorMessage.getMessage() + ":" + info);
    }
    // 错误信息 + 异常原因
    public RpcException(String info, Throwable cause) {
        super(info, cause);
    }

    // 发生异常所属类型
    public RpcException(RpcErrorMessage rpcErrorMessage) {
        super(rpcErrorMessage.getMessage());
    }

}
