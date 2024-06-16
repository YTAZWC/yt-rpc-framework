package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title SerializationType
 * @date 2024/6/15 20:05
 * @package top.ytazwc.enums
 * @description 序列化类型-枚举
 */
@AllArgsConstructor
@Getter
public enum SerializationType {

    KRYO((byte)0x01, "kryo"),
    PROTOSTUFF((byte)0x02, "protostuff"),
    HESSIAN((byte)0x03, "hessian");

    // 序列化类型 编码
    private final byte code;
    // 序列化类型 编码对应的名字
    private final String name;

    public static String getName(byte code) {
        for (SerializationType type : SerializationType.values()) {
            if (type.getCode() == code) {
                return type.name;
            }
        }
        return null;
    }

}
