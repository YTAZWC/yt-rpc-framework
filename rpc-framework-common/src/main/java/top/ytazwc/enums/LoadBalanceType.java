package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title LoadBalanceType
 * @date 2024/6/16 17:33
 * @package top.ytazwc.enums
 * @description 负载均衡策略
 */
@AllArgsConstructor
@Getter
public enum LoadBalanceType {
    LOADBALANCE("loadBalance");

    private final String name;
}
