package cn.sdadgz.web_springboot.scheduled;

import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@EnableScheduling
@Configuration
public class ImgScheduled {

    @Resource
    private IImgService imgService;

    // 垃圾回收
    @Scheduled(cron = "22 37 2 ? * 7")
    void delete() {
        List<Img> gc = imgService.getGC();
        Long aLong = imgService.virtualDeleteBatch(gc);
        log.info("img垃圾回收获取到{}条垃圾，成功删除{}条垃圾", gc.size(), aLong);
    }

    // 定期物理删除
    @Scheduled(cron = "11 0 8 ? * 1")
    void realDelete() {
        imgService.realDeleteBatch();
    }

}
