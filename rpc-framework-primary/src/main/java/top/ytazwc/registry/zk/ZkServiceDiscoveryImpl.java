package top.ytazwc.registry.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import top.ytazwc.enums.LoadBalanceType;
import top.ytazwc.enums.RpcErrorMessage;
import top.ytazwc.exception.RpcException;
import top.ytazwc.extension.ExtensionLoader;
import top.ytazwc.loadbalance.LoadBalance;
import top.ytazwc.registry.ServiceDiscovery;
import top.ytazwc.registry.zk.util.CuratorUtils;
import top.ytazwc.remoting.dto.RpcRequest;
import top.ytazwc.utils.CollectionUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author yt
 * 2024/6/14
 * 服务发现，根据完整的服务名称获取其下的所有子节点 然后根据具体的负载均衡策略取出对应服务地址
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(LoadBalanceType.LOADBALANCE.getName());
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }



}
