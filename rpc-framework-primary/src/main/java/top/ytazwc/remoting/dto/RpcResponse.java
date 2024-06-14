package top.ytazwc.remoting.dto;

import lombok.*;
import top.ytazwc.enums.ResponseRpcCode;

import java.io.Serializable;

/**
 * @author 花木凋零成兰
 * @title ResponseRpc
 * @date 2024/5/23 14:21
 * @package top.ytazwc.remoting.dto
 * @description rpc响应实体类
 * 当服务端通过 RequestRpc 中的相关数据调用到目标服务的目标方法之后
 * 调用结果通过ResponseRpc返回给客户端
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 715745410605631232L;
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应体
     */
    private T data;

    /**
     * 响应成功
     * @param data 响应数据
     * @param requestId 响应的请求id
     * @return 返回成功响应结果
     * @param <T> 类型参数
     */
    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        // 设置响应结果编码为成功
        response.setCode(ResponseRpcCode.SUCCESS.getCode());
        // 设置响应成功信息
        response.setMessage(ResponseRpcCode.SUCCESS.getMessage());
        // 设置请求id
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    /**
     * 响应失败
     * @param responseRpcCode 响应信息类
     * @return 响应失败结果
     * @param <T> 类型参数
     */
    public static <T> RpcResponse<T> fail(ResponseRpcCode responseRpcCode) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(responseRpcCode.getCode());
        response.setMessage(responseRpcCode.getMessage());
        return response;
    }

}
