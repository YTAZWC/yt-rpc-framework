package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title RpcRequestTransportType
 * @date 2024/6/17 11:00
 * @package top.ytazwc.enums
 * @description TODO
 */
@AllArgsConstructor
@Getter
public enum RpcRequestTransportType {

    NETTY("netty"),
    SOCKET("socket");

    private final String name;
}
