package cn.sdadgz.web_springboot.utils.SameCode.User;

import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserBan {

    // 他自己
    public static UserBan userBan;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        userBan = this;
    }

    // 根据url和request遣返坏东西
    public static void getTheFuckOut(String username,
                                     HttpServletRequest request) {

        // 根据名字获取id
        int userId = userBan.userService.getUserIdByName(username);

        getTheFuckOut(userId, request);
    }

    // 根据userId和request遣返坏东西
    public static void getTheFuckOut(int userId,
                                     HttpServletRequest request) {

        // 获取请求中的用户id
        int requestId = IdUtil.getUserId(request);

        if (requestId > 0) {
            if (requestId != userId) {
                throw new BusinessException("498", "权限不足");
            }
        }
    }

}
