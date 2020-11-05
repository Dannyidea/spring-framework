package org.idea.spring.framework.common;

/**
 * @author linhao
 * @date created in 8:13 下午 2020/11/2
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 首字母小写处理
     *
     * @param name
     * @return
     */
    public static String firstCharLower(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return chars.toString();
    }
}
