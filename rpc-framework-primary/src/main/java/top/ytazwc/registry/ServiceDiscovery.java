package top.ytazwc.registry;

import top.ytazwc.extension.SPI;
import top.ytazwc.remoting.dto.RequestRpc;

import java.net.InetSocketAddress;

/**
 * @author 花木凋零成兰
 * @title ServiceDiscovery
 * @date 2024/5/23 15:23
 * @package top.ytazwc.registry
 * @description 服务查找
 */
@SPI
public interface ServiceDiscovery {

    /**
     * 根据服务名查找对应服务所在地址
     * @param requestRpc
     * @return
     */
    InetSocketAddress lookupService(RequestRpc requestRpc);

}
