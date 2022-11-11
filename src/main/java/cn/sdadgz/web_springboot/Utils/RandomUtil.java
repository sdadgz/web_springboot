package cn.sdadgz.web_springboot.Utils;

import java.util.Random;

public class RandomUtil {
    public static int getInt(int max) {
        return getInt(max, System.currentTimeMillis());
    }

    public static int getInt(int max, long seed) {
        Random random = new Random(seed);
        return random.nextInt(max);
    }
}
