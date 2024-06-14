package top.ytazwc.registry.zk;

import lombok.extern.slf4j.Slf4j;
import top.ytazwc.registry.ServiceDiscovery;
import top.ytazwc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author yt
 * 2024/6/14
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        return null;
    }
}
