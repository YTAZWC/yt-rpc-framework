package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title ServiceDiscoveryType
 * @date 2024/6/16 16:52
 * @package top.ytazwc.enums
 * @description 服务中心类型
 */
@AllArgsConstructor
@Getter
public enum ServiceDiscoveryType {

    ZK("zk");

    private final String name;

}
