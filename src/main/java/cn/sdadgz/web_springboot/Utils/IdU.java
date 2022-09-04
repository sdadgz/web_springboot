package cn.sdadgz.web_springboot.Utils;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class IdU {

    public static String uuid() throws NoSuchAlgorithmException {
        // 时间加伪随机数
        String time = String.valueOf(System.currentTimeMillis());
        Random rand = new Random();
        String randStr = String.valueOf(rand.nextInt((int) (1e6)));
        // 转36进制
        long num = Long.parseLong(time + randStr);
        return Md5U.md5(Long.toString(num,36));
    }
}
