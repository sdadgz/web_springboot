package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author eula
 * @since 2022-08-26
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog>, cn.sdadgz.web_springboot.Utils.SameCode.Page.Mapper<Blog> {

    // 根据name和title获取blog
    Blog getBlog(@Param("username") String username, @Param("title") String title);

    // 根据name获取blogs
    List<Blog> getBlogsByName(@Param("username") String username);

    // 根据userId获取blogs
    List<Blog> getBlogsByUserId(@Param("userId") Integer userId);

    // 正常用户分页
    List<Blog> getPage(@Param("userId") Integer userId, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

}
