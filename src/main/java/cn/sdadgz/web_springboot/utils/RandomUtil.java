package cn.sdadgz.web_springboot.utils;

import java.util.Random;

public class RandomUtil {

    // 获取随机数
    public static int getInt(int max) {
        return getInt(max, System.currentTimeMillis());
    }

    public static int getInt(int max, long seed) {
        Random random = new Random(seed);
        return random.nextInt(max);
    }

    public static int getInt(int max, int seed) {
        return getInt(max, System.currentTimeMillis() + seed);
    }
}
