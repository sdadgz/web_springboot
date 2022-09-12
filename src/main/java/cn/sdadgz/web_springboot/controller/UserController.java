package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.JwtUtil;
import cn.sdadgz.web_springboot.Utils.TimeUtil;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
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
    UserMapper userMapper;

    // 新建用户
    @PostMapping("")
    public Result setUser(@RequestBody User user) throws NoSuchAlgorithmException {
        user.setCreatetime(TimeUtil.now());
        int insert = userMapper.insert(user);
        return Result.success(loginF(user));
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) throws NoSuchAlgorithmException {
        Map<String, Object> map = loginF(user);
        return Result.success(map);
    }

    // 返回token及基本信息
    private Map<String, Object> loginF(User user) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();
        String username = user.getName();
        String password = user.getPassword();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", username);
        List<User> list = userMapper.selectList(wrapper);

        if (list.size() > 0) {
            boolean b = list.get(0).getPassword().equals(password);
            if (b) {
                User trueUser = list.get(0);
                String token = JwtUtil.CreateToken(trueUser.getId().toString(), trueUser.getName(), trueUser.getPassword());
                map.put("user", trueUser);
                map.put("token", token);
                return map;
            }
        }
        throw new BusinessException("485", "用户名或密码错误");
    }

    // 用户名已被占用
    @GetMapping("")
    public Result exists(@RequestParam("username") String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", username);
        boolean exists = userMapper.exists(wrapper);
        return Result.success(exists);
    }
}
