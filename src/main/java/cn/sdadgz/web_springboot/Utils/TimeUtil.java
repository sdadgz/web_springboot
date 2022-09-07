package cn.sdadgz.web_springboot.Utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtil {

    // 获取当前时间
    public static LocalDateTime now(){
        return LocalDateTime.now();
    }

    // date转当前类型
    public static LocalDateTime translate(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDateTime();
    }
}
