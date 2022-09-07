package cn.sdadgz.web_springboot.Utils;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class IdUtil {

    public static String uuid() throws NoSuchAlgorithmException {
        // 时间加伪随机数
        String time = String.valueOf(System.currentTimeMillis());
        Random rand = new Random();
        String randStr = String.valueOf(rand.nextInt((int) (1e6)));
        // 转36进制
        long num = Long.parseLong(time + randStr);
        return Md5Util.md5(Long.toString(num, 36));
    }

    public static int getId(HttpServletRequest request) {
        return Integer.parseInt(JwtUtil.getAudience(request.getHeader("token")));
    }

    public static String getName(HttpServletRequest request) {
        return JwtUtil.getClaimByName(request.getHeader("token"), "username").asString();
    }
}
