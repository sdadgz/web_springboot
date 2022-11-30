package cn.sdadgz.web_springboot.scheduled;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.utils.GeneralUtil;
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

    @Scheduled(cron = "41 41 5 ? * 4")
    public void deleteBlog() {
        List<Blog> gc = blogService.getGC();
        boolean b = blogService.removeBatchByIds(gc);
        log.info("blog垃圾回收获取到了{}条垃圾，删除{}", gc.size(), GeneralUtil.tf(b));
    }

}
