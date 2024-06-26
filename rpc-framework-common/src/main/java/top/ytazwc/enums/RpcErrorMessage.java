package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author yt
 * 2024/6/14
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMessage {

    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    SERVICE_CAN_NOT_FOUND("没有找到指定服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误!请求和响应不匹配");

    private final String message;
}
