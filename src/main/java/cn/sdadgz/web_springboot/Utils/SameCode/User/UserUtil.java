package cn.sdadgz.web_springboot.Utils.SameCode.User;

import cn.sdadgz.web_springboot.Utils.Md5Util;
import cn.sdadgz.web_springboot.config.DangerousException;
import cn.sdadgz.web_springboot.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

public class UserUtil {

    private static final String SALT = "就这技术别说当程序员了，当个低级码农都费劲，他妈的";

    // 设置加密密码
    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
        return Md5Util.md5(password + SALT);
    }

    // 验证密码是否正确
    public static void verify(User user, User databaseUser, HttpServletRequest request) throws NoSuchAlgorithmException {
        if (!encryptPassword(user.getPassword()).equals(databaseUser.getPassword())) {
            throw new DangerousException("498", "用户名或密码错误", request, databaseUser.getId());
        }
    }
}
