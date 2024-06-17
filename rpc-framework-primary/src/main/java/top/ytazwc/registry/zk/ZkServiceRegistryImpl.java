package top.ytazwc.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import top.ytazwc.registry.ServiceRegistry;
import top.ytazwc.registry.zk.util.CuratorUtils;

import java.net.InetSocketAddress;

/**
 * @author 花木凋零成兰
 * @title ZkServiceRegistryImpl
 * @date 2024/6/15 20:42
 * @package top.ytazwc.registry.zk
 * @description Zookeeper 服务注册
 */
public class ZkServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        // 服务注册地址 完整的服务名称作为根节点 对应服务地址作为子节点
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
