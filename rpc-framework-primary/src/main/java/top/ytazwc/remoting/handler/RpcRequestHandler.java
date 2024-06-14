package top.ytazwc.remoting.handler;

import lombok.extern.slf4j.Slf4j;
import top.ytazwc.factory.SingletonFactory;
import top.ytazwc.provider.ServiceProvider;
import top.ytazwc.provider.impl.ZkServiceProviderImpl;
import top.ytazwc.remoting.dto.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 花木凋零成兰
 * @title RpcRequestHandler
 * @date 2024/6/14 22:26
 * @package top.ytazwc.remoting.handler
 * @description RPC 请求处理器
 */
@Slf4j
public class RpcRequestHandler {

    /**
     * 服务提供者
     */
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }


    /**
     * 根据客户端请求信息 获取到目标服务
     * 再根据目标服务与客户端请求信息 去执行对应方法
     * @param rpcRequest 客户端请求
     * @return 目标方法执行结果
     */
    public Object handle(RpcRequest rpcRequest) {
        // 根据客户端请求信息 获取目标服务
        Object service = serviceProvider.getService(rpcRequest.getPpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 获取调用目标方法的执行结果
     * @param rpcRequest 客户端请求
     * @param service 服务类
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            // 根据目标服务类的类型以及目标方法名以及目标方法的参数类型 获取目标方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 执行目标方法 获得执行结果
            result = method.invoke(service, rpcRequest.getParameters());
            // 输出目标服务的目标方法执行成功信息
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
