package cn.sdadgz.web_springboot.utils.SameCode.User;

import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.Md5Util;
import cn.sdadgz.web_springboot.config.DangerousException;
import cn.sdadgz.web_springboot.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@Component
public class UserUtil {

    private static final String SALT = "就这技术别说当程序员了，当个低级码农都费劲，他妈的";

    // 原密码 -> 加密密码
    public static String encryptPassword(String password) throws NoSuchAlgorithmException {
//        return Md5Util.md5(password + SALT);
        return password;
    }

    // 原user, 加密user -> 不对就throw
    public static void verify(User user, User databaseUser, HttpServletRequest request) throws NoSuchAlgorithmException {
        if (!encryptPassword(user.getPassword()).equals(databaseUser.getPassword())) {
            throw new DangerousException("498", "用户名或密码错误", request, databaseUser.getId());
        }
    }
}
