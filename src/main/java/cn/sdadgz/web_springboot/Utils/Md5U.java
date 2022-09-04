package cn.sdadgz.web_springboot.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5U {

    public static String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] strByte = str.getBytes();
        md.update(strByte);
        BigInteger b = new BigInteger(1,md.digest());

        return b.toString();
    }

    public static String md5(File file){
        // 大文件报错byte是int设置长度，int超范围，哈哈我又偷懒去cv的
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
//            File f = new File(path);
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        assert bi != null;
        return bi.toString(16);
    }
}
