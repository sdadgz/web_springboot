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

    @Scheduled(cron = "9 9 6 ? * 2")
    private void deleteFile() {
        List<File> gc = fileService.getGC();
        log.info("file垃圾回收获取到了{}条垃圾", gc.size());

        Long aLong = fileService.virtualDelete(gc);
        log.info("file垃圾回收修改数据库{}条数据", aLong);
    }

    @Scheduled(cron = "7 1 22 ? * 6")
    private void realDelete(){
        fileService.realDelete();
    }

}
