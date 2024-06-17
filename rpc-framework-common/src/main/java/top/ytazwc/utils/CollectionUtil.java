package top.ytazwc.utils;

import java.util.Collection;

/**
 * @author 花木凋零成兰
 * @title CollectionUtil
 * @date 2024/6/16 17:27
 * @package top.ytazwc.utils
 * @description 集合工具类
 */
public class CollectionUtil {
    /*
     判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
