package top.ytazwc.registry;

import top.ytazwc.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @author 花木凋零成兰
 * @title ServiceRegistry
 * @date 2024/5/23 15:20
 * @package top.ytazwc.registry
 * @description 服务注册统一接口
 * 具体注册方式 继承该接口
 */
@SPI
public interface ServiceRegistry {

    /**
     * 服务注册方法 注册服务到注册中心
     * @param rpcServiceName 完整的服务名称(class name+group+version)
     * @param inetSocketAddress 服务所在地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
