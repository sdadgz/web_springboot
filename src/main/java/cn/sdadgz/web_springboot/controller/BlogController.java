package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
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
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    BlogMapper blogMapper;

    @Resource
    UserMapper userMapper;

    // 获取blogs空手套白狼
    @GetMapping("/blogs")
    public Result getBlogs() {
        return getBlogsByNameFun("sdadgz");
    }

    // 提供用户名获取blogs
    @GetMapping("/blogs/{username}")
    public Result getBlogsByUserName(@PathVariable String username) {
        return getBlogsByNameFun(username);
    }

    // 根据用户名获取blogs
    private Result getBlogsByNameFun(String username) {

        User user = getUserByNameFun(username);
        if (user != null) {
            // 获取id
            int id = user.getId();

            QueryWrapper<Blog> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", id);
            List<Blog> blogs = blogMapper.selectList(wrapper);

            return Result.success(blogs);
        }
        return Result.error("454", "用户名错误，无法通过username查到该用户");
    }

    // 根据用户名获取用户id
    private User getUserByNameFun(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", username);
        List<User> userList = userMapper.selectList(wrapper);
        return userList.size() == 1 ? userList.get(0) : null;
    }
}
