package top.ytazwc.provider;

import top.ytazwc.config.RpcServiceConfig;

/**
 * @author yt
 * 2024/6/14
 * 存储和提供服务
 */
public interface ServiceProvider {

    /**
     * 新增服务
     * @param rpcServiceConfig  RPC服务相关属性
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 获取服务类
     * @param rpcServiceName RPC服务名
     * @return 服务类
     */
    Object getService(String rpcServiceName);

    /**
     * 发布服务
     * @param rpcServiceConfig RPC服务配置属性
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

}
