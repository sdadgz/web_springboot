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
    public void deleteFile() {
        List<File> gc = fileService.getGC();
        log.info("file垃圾回收获取到了{}条垃圾", gc.size());
        fileService.removeBatchByIds(gc);
    }

}
