package cn.sdadgz.web_springboot.Utils.SameCode.User;

import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserBan {

    // 他自己
    static UserBan userBan;

    @Resource
    UserMapper userMapper;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        userBan = this;
    }

    // 根据url和request遣返坏东西
    public void getTheFuckOut(String username,
                              HttpServletRequest request) {
        // 遣返跨权人员
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, username);
        List<User> users = userBan.userMapper.selectList(wrapper);
        if (users.size() != 1) {
            throw new BusinessException("404", "用户歧义");
        }
        int requestId = IdUtil.getId(request);
        if (requestId > 0) {
            if (requestId != users.get(0).getId()) {
                throw new BusinessException("498", "权限不足");
            }
        }
    }

}
