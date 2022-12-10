package cn.sdadgz.web_springboot.utils;

import cn.sdadgz.web_springboot.config.BusinessException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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
    public static String getIp(@NonNull HttpServletRequest request) {
        try {
            return getIpThrows(request);
        } catch (BusinessException e) {
            log.warn("没得到你的ip");
            return null;
        }
    }

    public static String getIpThrows(@NonNull HttpServletRequest request) throws BusinessException {
        // nginx
        String ip = request.getHeader("x-real-ip");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            log.info("x-real-ip 获取到了ip：{}，tcp连接ip：{}", ip, request.getRemoteAddr());
            return ip;
        }
        // 代理
        ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            log.info("x-forwarded-ip 获取到了ip：{}，tcp连接ip：{}", ip, request.getRemoteAddr());
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            // 连接的ip
            ip = request.getRemoteAddr();
            if (GeneralUtil.isNull(ip)) {
                throw new BusinessException("466", "没得到ip");
            }
            return ip;
        }
    }
}
