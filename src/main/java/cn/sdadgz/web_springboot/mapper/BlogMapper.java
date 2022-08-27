package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author eula
 * @since 2022-08-26
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    // 根据userid获取blogs
    List<Blog> getBlogsByUserId(int userid);
}
