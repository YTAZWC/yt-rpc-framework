package top.ytazwc.provider;

import top.ytazwc.config.RpcServiceConfig;

/**
 * @author yt
 * 2024/6/14
 * 存储和提供服务
 */
public interface ServiceProvider {

    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);

    void publishService(RpcServiceConfig rpcServiceConfig);

}
