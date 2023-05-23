package cn.sdadgz.web_springboot.scheduled;

import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@EnableScheduling
@Configuration
public class FileScheduled {

    @Resource
    private IFileService fileService;

    @Scheduled(cron = "14 21 8 ? * 1")
    private void deleteFile() {
        List<File> gc = fileService.getGC();
        Long aLong = fileService.virtualDelete(gc);
        log.info("file垃圾回收获取到了{}条垃圾，虚拟删除了{}条数据", gc.size(), aLong);
    }

    @Scheduled(cron = "55 0 23 ? * 1")
    private void realDelete() {
        log.info("清理file回收站");
        fileService.realDelete();
    }
}
