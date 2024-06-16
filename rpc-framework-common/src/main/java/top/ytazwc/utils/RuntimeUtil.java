package top.ytazwc.utils;

/**
 * @author 花木凋零成兰
 * @title RuntimeUtil
 * @date 2024/6/15 22:16
 * @package top.ytazwc.utils
 * @description 运行工具类
 */
public class RuntimeUtil {

    /**
     * 获取CPU的核心数
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }

}
