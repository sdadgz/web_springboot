package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author eula
 * @since 2022-08-25
 */
@Service
@CacheConfig(cacheNames = "userCache")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    // 根据name获取id，上位缓存
    @Override
    public int getUserIdByName(String username) {
        return userService.getUserByName(username).getId();
    }

    // 根据name获取用户
    @Override
    @Cacheable(key = "#username")
    public User getUserByName(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getName, username);
        List<User> users = userMapper.selectList(userLambdaQueryWrapper);

        // 非法的用户名
        if (users.size() != 1) {
            throw new BusinessException("499", "用户名或密码错误");
        }
        return users.get(0);
    }

    @Override
    @Cacheable(unless = "#result == null")
    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }

    // 用户名已存在
    @Override
    @Cacheable(key = "#username")
    public boolean nameExists(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, username);
        return userMapper.exists(wrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void updateUserById(User user) {
        int i = userMapper.updateById(user);
        log.info("user修改：{} 条", i);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void addUser(User user) {
        userMapper.insert(user);
    }

}
