package top.ytazwc.serializer;

import top.ytazwc.extension.SPI;

/**
 * @author 花木凋零成兰
 * @title Serializer
 * @date 2024/6/16 16:37
 * @package top.ytazwc.serializer
 * @description 序列化接口；所有序列化类都要实现该接口
 */
@SPI
public interface Serializer {
    /**
     * 序列化
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

}
