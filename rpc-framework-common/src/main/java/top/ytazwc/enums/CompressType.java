package top.ytazwc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花木凋零成兰
 * @title CompressType
 * @date 2024/6/15 20:16
 * @package top.ytazwc.enums
 * @description 消息压缩类型
 */
@AllArgsConstructor
@Getter
public enum CompressType {

    GZIP((byte)0x01, "gzip");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (CompressType type : CompressType.values()) {
            if (type.getCode() == code) {
                return type.name;
            }
        }
        return null;
    }

}
