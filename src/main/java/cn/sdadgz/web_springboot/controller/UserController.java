package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.JwtUtil;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserUtil;
import cn.sdadgz.web_springboot.utils.TimeUtil;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.service.IUserService;
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
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    @Resource
    private ImgMapper imgMapper;

    @Resource
    private IIpBanService ipBanService;

    private static final String USERNAME = "username";
    private static final String OLD_PASSWORD = "oldPassword";
    private static final String NEW_PASSWORD = "newPassword";

    // 上传头像
    @PutMapping("/avatar")
    public Result uploadAvatar(@RequestParam("imgId") int imgId, HttpServletRequest request) {

        // 初始化
        int userId = IdUtil.getUserId(request);
        Img img = imgMapper.selectById(imgId);

        // 修改user
        User user = new User();
        user.setId(userId);
        user.setAvatar(img.getUrl() != null ? img.getUrl() : img.getReduceUrl());
        userMapper.updateById(user);

        return Result.success();
    }

    // 修改密码
    @PutMapping("/password")
    public Result update(HttpServletRequest request,
                         @RequestBody Map<String, String> map) throws NoSuchAlgorithmException {

        // 初始化
        String username = map.get(USERNAME);
        String oldPassword = map.get(OLD_PASSWORD);
        String newPassword = map.get(NEW_PASSWORD);

        // 验证
        User userByName = userService.getUserByName(username);
        UserUtil.verify(new User().setPassword(oldPassword), userByName, request);

        userByName.setPassword(UserUtil.encryptPassword(newPassword));
        userMapper.updateById(userByName);

        return Result.success();
    }

    // 新建用户
    @PostMapping
    public Result setUser(@RequestBody User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        user.setCreateTime(TimeUtil.now());
        String password = user.getPassword();
        user.setPassword(UserUtil.encryptPassword(password));
        userMapper.insert(user);
        user.setPassword(password);
        return Result.success(loginF(user, request));
    }

    // 登录 放行
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, Object> map = loginF(user, request);
        return Result.success(map);
    }

    // 登录并返回token及基本信息
    private Map<String, Object> loginF(User user, HttpServletRequest request) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();
        String username = user.getName();

        // 获取用户
        User userByName = userService.getUserByName(username);

        // 冻结用户，保护用户密码不被暴力破解
        ipBanService.protect(userByName);

        // 验证密码
        UserUtil.verify(user, userByName, request);

        String token = JwtUtil.CreateToken(userByName.getId().toString(), userByName.getName(), userByName.getPassword());
        map.put("user", userByName);
        map.put("token", token);
        return map;
    }

    // 用户名已被占用
    @GetMapping
    public Result exists(@RequestParam("username") String username) {

        boolean exists = userService.nameExists(username);

        return Result.success(exists);
    }

}
