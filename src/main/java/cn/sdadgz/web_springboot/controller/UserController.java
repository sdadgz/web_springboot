package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.utils.*;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserUtil;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.service.IUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author eula
 * @since 2022-08-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IImgService imgService;

    @Resource
    private IIpBanService ipBanService;

    private static final String USERNAME = "username";
    private static final String OLD_PASSWORD = "oldPassword";
    private static final String NEW_PASSWORD = "newPassword";


    /**
     * 测试接口，放行的
     *
     * @return 成功
     */
    @GetMapping("/test")
    public Result test() {
        return Result.success();
    }


    /**
     * 关机接口
     */
    @GetMapping("/shutdown")
    public void shutdown() {
        SpringApplication.exit(SpringUtil.getApplicationContext(), () -> 0);
    }

    /**
     * 重置token时间
     *
     * @param token 旧token
     * @return token
     */
    @GetMapping("/token")
    public Result refreshToken(@RequestHeader(StringUtil.TOKEN) String token) throws NoSuchAlgorithmException {

        String s = JwtUtil.reFlushToken(token);

        return Result.success(s);
    }

    /**
     * 上传头像
     *
     * @param imgId 头像的图片id
     * @return null
     */
    @PutMapping("/avatar")
    public Result uploadAvatar(@RequestParam("imgId") int imgId, HttpServletRequest request) {

        // 获取userId
        int userId = IdUtil.getUserId(request);
        // 选择图片
        Img img = imgService.getImgById(imgId);

        // 修改user
        User user = new User();
        user.setId(userId);
        user.setAvatar(img.getReduceUrl() != null ? img.getReduceUrl() : img.getUrl());
        userService.updateUserById(user);

        return Result.success();
    }

    // 新建用户
    @PostMapping
    public Result setUser(@RequestBody User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        user.setCreateTime(TimeUtil.now());
        String password = user.getPassword();
        user.setPassword(UserUtil.encryptPassword(password));
        userService.addUser(user);
        user.setPassword(password);
        return Result.success(loginF(user, request));
    }

    // 修改密码
    @PutMapping("/password")
    public Result update(@RequestBody Map<String, String> map, HttpServletRequest request) throws NoSuchAlgorithmException {

        // 加密密码
        String username = map.get(USERNAME);
        String oldPassword = map.get(OLD_PASSWORD);
        String newPassword = map.get(NEW_PASSWORD);

        // 验证
        User userByName = userService.getUserByName(username);
        UserUtil.verify(new User().setPassword(oldPassword), userByName, request);

        userByName.setPassword(UserUtil.encryptPassword(newPassword));
        userService.updateUserById(userByName);

        return Result.success();
    }

    // 登录 放行
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, Object> map = loginF(user, request);
        return Result.success(map);
    }

    // 用户名已被占用
    @GetMapping
    public Result exists(@RequestParam("username") String username) {

        boolean exists = userService.nameExists(username);

        return Result.success(exists);
    }

    // 登录并返回token及基本信息
    private Map<String, Object> loginF(User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();
        String username = user.getName();

        // 获取用户
        User dbUser = userService.getUserByName(username);

        // 冻结用户，保护用户密码不被暴力破解
        ipBanService.protect(dbUser);

        // 验证密码
        UserUtil.verify(user, dbUser, request);

        String token = JwtUtil.createToken(dbUser);
        map.put("user", dbUser);
        map.put("token", token);
        return map;
    }

}
