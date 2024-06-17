package top.ytazwc.loadbalance;

import top.ytazwc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @author 花木凋零成兰
 * @title LoadBalance
 * @date 2024/6/16 17:24
 * @package top.ytazwc.loadbalance
 * @description 负载均衡策略接口
 */
public interface LoadBalance {
    /**
     * 从现有的服务列表中选择一个服务
     * @param serviceUrlList 现有服务列表
     * @param rpcRequest rpc 请求
     * @return 目标服务地址
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);

}
