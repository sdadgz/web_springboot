package cn.sdadgz.web_springboot.utils;

import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {

    private static JwtUtil jwtUtil;

    @Resource
    private IUserService userService;

    @PostConstruct
    public void init() {
        jwtUtil = this;
    }

    // token过期时间
    public static final int EXPIRE_DAY = 7;

    // userId, username, 加密密码 -> token
    public static String createToken(String userid, String username, String password) throws NoSuchAlgorithmException {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, EXPIRE_DAY);
        Date time = now.getTime();
        return JWT.create().withAudience(userid) // 签发对象 aud
                .withIssuedAt(new Date()) // 创建日期 iat
                .withExpiresAt(time) // 过期日期 exp
                .withClaim("username", username) // 载荷
                .sign(Algorithm.HMAC256(Md5Util.md5(userid) + Md5Util.md5(password)));  // 加密算法
    }

    // 加密user -> token
    public static String createToken(User user) throws NoSuchAlgorithmException {
        return createToken(user.getId().toString(), user.getName(), user.getPassword());
    }

    // oldToken -> newToken
    public static String reFlushToken(String token) throws NoSuchAlgorithmException {
        String userId = getAudience(token);
        User user = jwtUtil.userService.getUserById(Integer.parseInt(userId));
        return createToken(user);
    }

    // 认证测试
    public static Boolean verifyToken(String token, String userid, String password) throws NoSuchAlgorithmException {
        DecodedJWT jwt = null;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Md5Util.md5(userid) + Md5Util.md5(password))).build();
        try {
            jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // 获取签发对象
    public static String getAudience(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            //这里是token解析失败
            throw new RuntimeException();
        }
        return audience;
    }

    // 获取载荷的值
    public static Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }

    // 检测是否过期
    public static Boolean checkDate(String token) {
        Claim claim = JWT.decode(token).getClaim("exp");
        Date date = claim.asDate();
        System.out.println("token日期" + date);
        return date.before(new Date());
    }
}
