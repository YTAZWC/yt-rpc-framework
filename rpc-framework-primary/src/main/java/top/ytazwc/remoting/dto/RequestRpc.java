package top.ytazwc.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author 花木凋零成兰
 * @title RequestRpc
 * @date 2024/5/23 13:39
 * @package top.ytazwc.dto
 * @description rpc请求实体类
 * 对远程方法进行调用时，需要传输RequestRpc给对方
 * RequestRpc包含了要调用的目标方法和类的名称、参数等数据
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RequestRpc implements Serializable {

    private static final long serialVersionUID = 1905122041950251280L;
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 调用所属接口名
     */
    private String interfaceName;
    /**
     * 调用方法名
     */
    private String methodName;
    /**
     * 方法参数
     */
    private Object[] parameters;
    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 服务版本 方便以后进行升级
     */
    private String version;
    /**
     * 用于处理一个接口有多个类实现的情况
     */
    private String group;

    /**
     * 获取调用服务名称(接口名+组名+版本)
     */
    public String getPpcServiceName() {
        // 服务名 = 接口名+组+版本
        return this.getInterfaceName() + this.getGroup() +this.getVersion();
    }

}
