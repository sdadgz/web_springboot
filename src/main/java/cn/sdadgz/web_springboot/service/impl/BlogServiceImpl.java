package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.UnificationConfig;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.utils.SameCode.Page.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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
@CacheConfig(cacheNames = "blogCache")
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    public UnificationConfig unificationConfig;

    @Override
    @CacheEvict(allEntries = true)
    public void addBlog(Blog blog) {
        int insert = blogMapper.insert(blog);
        log.info("新增blog：{} 条", insert);
    }

    @Override
    @Cacheable(unless = "#result.get(#root.target.unificationConfig.getResponseLists()).size() == 0")
    public Map<String, Object> getPage(int userId, int currentPage, int pageSize) {
        Page<BlogMapper, Blog> page = new Page<>();
        return page.getPage(currentPage, pageSize, userId, blogMapper);
    }

    // 空引用垃圾回收，定时的，不需要缓存
    @Override
    public List<Blog> getGC() {
        return blogMapper.getGC();
    }

    @Override
    @Cacheable(unless = "#result == null")
    public Blog getBlogById(Integer id) {
        return blogMapper.selectById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateBlogById(Blog blog) {
        return blogMapper.updateById(blog);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Blog getBlogByUsernameAndTitle(String username, String title) {
        return baseMapper.getBlog(username, title);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(Integer id) {
        return blogMapper.deleteById(id);
    }

}
