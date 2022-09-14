package cn.sdadgz.web_springboot.Utils;

import java.util.Random;

public class RandomUtil {
    public static int getInt(int max){
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(max);
    }
}
