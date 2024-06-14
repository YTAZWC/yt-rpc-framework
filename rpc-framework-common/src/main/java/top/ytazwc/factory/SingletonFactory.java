package top.ytazwc.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yt
 * 2024/6/14
 * 单例对象工厂类
 */
public class SingletonFactory {
    private static final Map<String, Object> OBJECTS_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> tClass) {
        if (tClass == null) {
            throw new IllegalArgumentException();
        }
        String key = tClass.toString();
        // 判断缓存中是否存在该实例
        if (OBJECTS_MAP.containsKey(key)) {
            // 存在则返回
            return tClass.cast(OBJECTS_MAP.get(key));
        } else {
            // 不存在则 创建实例 并存入缓存
            return tClass.cast(OBJECTS_MAP.computeIfAbsent(key, k -> {
                try {
                    return tClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }));
        }
    }

}
