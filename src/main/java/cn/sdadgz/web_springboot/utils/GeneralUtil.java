package cn.sdadgz.web_springboot.utils;

public class GeneralUtil {

    // 非空
    public static boolean notNull(Object obj) {
        // 空指针否
        if (obj == null) {
            return true;
        }
        // 空字符串否
        if (obj instanceof String) {
            String str = (String) obj;
            return str.equals(StrUtil.EMPTY_STRING);
        }
        return false;
    }

    // 成功失败
    public static String tf(boolean b) {
        return b ? "成功" : "失败";
    }
}
