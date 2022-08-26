package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/name/{username}")
    public Result getUserByName(@PathVariable String username) {

        return Result.success();
    }
}
