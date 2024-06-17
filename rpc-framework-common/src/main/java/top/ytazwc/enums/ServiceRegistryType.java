package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title ServiceRegistryType
 * @date 2024/6/16 17:18
 * @package top.ytazwc.enums
 * @description 服务注册类型
 */
@AllArgsConstructor
@Getter
public enum ServiceRegistryType {

    ZK("zk");

    private final String name;

}
