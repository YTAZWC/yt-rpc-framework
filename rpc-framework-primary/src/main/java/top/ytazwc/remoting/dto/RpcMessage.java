package top.ytazwc.remoting.dto;

import lombok.*;

/**
 * @author 花木凋零成兰
 * @title RpcMessage
 * @date 2024/5/23 14:06
 * @package top.ytazwc.remoting.dto
 * @description 网络传输信息的实体
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class RpcMessage {

    /**
     * 信息类型
     */
    private byte messageType;
    /**
     * 序列化类型
     */
    private byte codec;
    /**
     * 压缩类型
     */
    private byte compress;
    /**
     * 请求id
     */
    private int requestId;
    /**
     * 请求数据
     */
    private Object data;

}
