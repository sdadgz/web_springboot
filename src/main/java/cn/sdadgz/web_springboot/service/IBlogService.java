package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

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

    // 获取用户blog分页
    Map<String, Object> getPage(int userId, int currentPage, int pageSize);
}
