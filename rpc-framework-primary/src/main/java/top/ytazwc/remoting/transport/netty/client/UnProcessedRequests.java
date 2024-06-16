package top.ytazwc.remoting.transport.netty.client;

import top.ytazwc.remoting.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 花木凋零成兰
 * @title UnProcessedRequests
 * @date 2024/6/15 15:52
 * @package top.ytazwc.remoting.transport.netty
 * @description 服务器未处理的请求
 */
public class UnProcessedRequests {
    // 缓存未处理的响应
    // CompletableFuture是一个用于异步编程的工具类，它可以用于表示一个异步计算的结果
    private static final Map<String, CompletableFuture<RpcResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    /**
     * 新增缓存 服务器未处理的请求
     * @param requestId 请求id
     * @param future 响应
     */
    public void put(String requestId, CompletableFuture<RpcResponse<Object>> future) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    /**
     * 接收到RPC响应时，根据请求id寻找对应待处理的 业务
     * 并将其标记为已完成并设置响应结果
     * 从而实现异步调用的结果传递和流程推进
     * @param rpcResponse
     */
    public void complete(RpcResponse<Object> rpcResponse) {
        // 寻找对应未处理的响应
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if (future != null) {
            // 若有未处理的响应 则进行处理
            // 将rpcResponse作为结果设置给这个CompletableFuture
            // 通知所有等待该结果的线程/任务，它们可以继续执行了，因为结果已准备好。
            future.complete(rpcResponse);
        } else {
            // 否则抛出异常
            throw new IllegalStateException();
        }
    }

}