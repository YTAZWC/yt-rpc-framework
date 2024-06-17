package top.ytazwc.loadbalance;

import top.ytazwc.remoting.dto.RpcRequest;
import top.ytazwc.utils.CollectionUtil;

import java.util.List;

/**
 * @author 花木凋零成兰
 * @title AbstractLoadBalance
 * @date 2024/6/16 17:26
 * @package top.ytazwc.loadbalance
 * @description 负载均衡策略抽象类
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RpcRequest rpcRequest) {
        if (CollectionUtil.isEmpty(serviceAddresses)) {
            return null;
        }
        // 若集合列表中只有一个服务 则直接返回
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest);

}
