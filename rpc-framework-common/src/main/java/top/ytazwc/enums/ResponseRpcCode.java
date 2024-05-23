package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 花木凋零成兰
 * @title ResponseRpcCode
 * @date 2024/5/23 14:35
 * @package top.ytazwc.enums
 * @description rpc调用响应结果 枚举类
 */
@AllArgsConstructor
@Getter
@ToString
public enum ResponseRpcCode {

    /**
     * 响应成功
     */
    SUCCESS(200, "Good! The remote call is successful."),
    /**
     * 响应失败
     */
    FAIL(500, "NO! The remote class is fail.");

    /**
     * 响应结果编码
     */
    private final int code;
    /**
     * 响应结果信息
     */
    private final String message;

}
