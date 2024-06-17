package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title RpcConfigType
 * @date 2024/6/16 16:58
 * @package top.ytazwc.enums
 * @description RPC 配置文件配置
 */
@AllArgsConstructor
@Getter
public enum RpcConfigType {
    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
