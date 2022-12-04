package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author eula
 * @since 2022-08-26
 */
public interface IBlogService extends IService<Blog> {

    // 新增博客
    void addBlog(Blog blog);

    // 获取用户blog分页
    Map<String, Object> getPage(int userId, int currentPage, int pageSize);

    // 获取需要删除的blog
    List<Blog> getGC();

    // 根据id获取
    Blog getBlogById(Integer id);

    // 根据id修改
    int updateBlogById(Blog blog);

    // 根据用户和标题获取
    Blog getBlogByUsernameAndTitle(String username, String title);

    // 根据id删除
    int deleteById(Integer id);
}
