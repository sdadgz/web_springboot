package cn.sdadgz.web_springboot.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeUtil {

    // 一天有多少秒呢
    public static final int SECOND_PER_DAY = 24 * 60 * 60;

    // 获取当前时间
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    // 精确到天 - 获取当前时间
    public static LocalDate nowDay() {
        return LocalDate.now();
    }
}
