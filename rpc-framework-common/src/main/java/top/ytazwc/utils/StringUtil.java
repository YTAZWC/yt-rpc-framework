package top.ytazwc.utils;

/**
 * @author 花木凋零成兰
 * @title StringUtil
 * @date 2024/6/16 16:40
 * @package top.ytazwc.utils
 * @description 字符串工具类
 */
public class StringUtil {

    /**
     * 用于判断字符串 是否为空 或只包含空格
     * @param s 待判断字符串
     * @return 判断结果
     */
    public static boolean isBlank(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
