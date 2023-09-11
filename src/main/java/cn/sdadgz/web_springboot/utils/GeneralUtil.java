package cn.sdadgz.web_springboot.utils;

public class GeneralUtil {

    /**
     * 如果没有值采用默认值，0和空字符串为空
     *
     * @param value 判断是否为空的值
     * @param defaultValue 为空时采用的默认值
     * @return js语法中的 value || defaultValue
     * */
    public static <VALUE> VALUE thisOrDefault(VALUE value, VALUE defaultValue) {
        return !isNull(value) ? value : defaultValue;
    }

    // 非空
    public static boolean isNull(Object obj) {
        // 空指针否
        if (obj == null) {
            return true;
        }
        // 空字符串否
        if (obj instanceof String) {
            String str = (String) obj;
            return str.equals(StringUtil.EMPTY_STRING);
        }
        // 为0的数字
        if (obj instanceof Integer) {
            Integer in = (Integer) obj;
            return in.equals(0);
        }

        return false;
    }

    // 成功失败
    public static String tf(boolean b) {
        return b ? "成功" : "失败";
    }
}
