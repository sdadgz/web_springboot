package cn.sdadgz.web_springboot.Utils.SameCode.User;

import cn.sdadgz.web_springboot.Utils.Md5Util;
import cn.sdadgz.web_springboot.entity.User;

import java.security.NoSuchAlgorithmException;

public class UserUtil {

    public static final String SALT = "就这技术别说当程序员了，当个低级码农都费劲，他妈的";

    // 设置加密密码
    public static String getPassword(String password) throws NoSuchAlgorithmException {
        return Md5Util.md5(password + SALT);
    }

    public static boolean verify(User user, User databaseUser) throws NoSuchAlgorithmException {
        return getPassword(user.getPassword()).equals(databaseUser.getPassword());
    }

}
