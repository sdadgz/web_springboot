package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author eula
 * @since 2022-08-25
 */
public interface IUserService extends IService<User> {

    // 根据用户名获取用户id
    int getUserIdByName(String username);

    // 根据用户名获取用户
    User getUserByName(String username);

    // 用户名已被占用
    boolean nameExists(String username);
}
