package org.idea.spring.framework.common.util;

/**
 * @author linhao
 * @date created in 6:57 下午 2020/10/9
 */
public class StringUtils {

    public static String EMPTY_STR = "";

    public static boolean isEmpty(String str){
        return str==null || str.length()==0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean isStringArrEmpty(String[] arr){
        return arr==null || arr.length==0;
    }

    public static boolean isNotStringArrEmpty(String[] arr){
        return !isStringArrEmpty(arr);
    }

    /**
     * 处理字符串首字母小写问题
     *
     * @param className
     * @return
     */
    public static String toLowerFirstName(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
