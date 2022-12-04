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

    // 新增用户
    void addUser(User user);

    // 根据id修改
    void updateUserById(User user);

    // username -> userId
    int getUserIdByName(String username);

    // username -> user
    User getUserByName(String username);

    // userId -> user
    User getUserById(Integer userId);

    // username -> 被占用
    boolean nameExists(String username);

}
