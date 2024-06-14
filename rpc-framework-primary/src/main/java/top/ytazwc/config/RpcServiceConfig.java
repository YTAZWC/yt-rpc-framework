package top.ytazwc.config;

import lombok.*;

/**
 * @author yt
 * 2024/6/14
 * rpc服务信息配置
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class RpcServiceConfig {

    /**
     * 服务版本
     */
    private String version = "";

    /**
     *当接口有多个实现类时，按组区分
     */
    private String group = "";

    /**
     * 目标服务
     */
    private Object service;

    // 获取RPC服务名：目标服务基础名 + 所在组 + 版本
    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    // 获取服务基本名称
    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

}
