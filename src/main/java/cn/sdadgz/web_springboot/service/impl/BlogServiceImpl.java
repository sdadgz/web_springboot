package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.utils.SameCode.Page.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author eula
 * @since 2022-08-26
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Resource
    private BlogMapper blogMapper;


    @Override
    public Map<String, Object> getPage(int userId, int currentPage, int pageSize) {
        Page<BlogMapper, Blog> page = new Page<>();
        return page.getPage(currentPage, pageSize, userId, blogMapper);
    }
}
