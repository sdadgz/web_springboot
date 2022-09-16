package cn.sdadgz.web_springboot.Utils.SameCode.User;

import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class UserSame {
    private static UserSame userSame;

    @Resource
    private UserMapper userMapper;

    @PostConstruct
    public void init() {
        userSame = this;
    }

    public static int getUserIdByName(String username) {
        // 获取用户id
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getName, username);
        List<User> users = userSame.userMapper.selectList(userLambdaQueryWrapper);
        if (users.size() != 1) {
            throw new BusinessException("404", "错误的用户名");
        }
        return users.get(0).getId();
    }
}
