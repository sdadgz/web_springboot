package cn.sdadgz.web_springboot.scheduled;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class BlogScheduled {

    @Resource
    private IBlogService blogService;

    @Scheduled(cron = "0 0 7 ? * 1")
    public void deleteBlog() {
        List<Blog> gc = blogService.getGC();
        log.info("blog垃圾回收获取到了{}条垃圾", gc.size());
        blogService.removeBatchByIds(gc);
    }

}
