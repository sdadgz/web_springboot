package cn.sdadgz.web_springboot.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
public class IdUtil {

    // uuid
    public static String uuid() throws NoSuchAlgorithmException {
        // 时间加伪随机数
        String time = String.valueOf(System.currentTimeMillis());
        Random rand = new Random();
        String randStr = String.valueOf(rand.nextInt((int) (1e6)));
        // 转36进制
        long num = Long.parseLong(time + randStr);
        return Md5Util.md5(Long.toString(num, 36));
    }

    // 获取userId
    public static int getUserId(HttpServletRequest request) {
        return Integer.parseInt(JwtUtil.getAudience(request.getHeader("token")));
    }

    // 获取username
    public static String getUsername(HttpServletRequest request) {
        return JwtUtil.getClaimByName(request.getHeader("token"), "username").asString();
    }

    // 获取ip
    public static String getIp(HttpServletRequest request) {
        //目前则是网关ip
        String ip = "";
        if (request != null) {
            ip = request.getHeader("X-FORWARDED-FOR");
            if (ip == null || "".equals(ip)) {
                ip = request.getRemoteAddr();
            }
        }

        log.info("访问者ip：{}", ip);
        return ip;
    }
}
