package cn.sdadgz.web_springboot.utils;

import cn.sdadgz.web_springboot.config.BusinessException;

/**
 * 异常工具
 *
 * <p>
 * 废物本物
 * </p>
 *
 * @author sdadgz
 * @since 2024/3/19 22:09:50
 */
public class ExceptionUtil {
    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, String errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
