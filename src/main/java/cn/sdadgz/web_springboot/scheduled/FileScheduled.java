package cn.sdadgz.web_springboot.scheduled;

import cn.sdadgz.web_springboot.config.FileConfig;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import cn.sdadgz.web_springboot.utils.Md5Util;
import cn.sdadgz.web_springboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class FileScheduled {

    private final IFileService fileService;
    private final FileConfig fileConfig;

    @Scheduled(cron = "23 9 9 17 2 ?")
    @PostConstruct
    public void deleteFile() {
        // 删除注销账号的文件
        List<File> gc = fileService.getGC();
        Long aLong = fileService.virtualDelete(gc);
        log.info("file垃圾回收获取到了{}条垃圾，虚拟删除了{}条数据", gc.size(), aLong);
    }

    @Scheduled(cron = "23 9 9 ? * 5")
    public void realDelete() {
        log.info("清理file回收站");
        fileService.realDelete();
    }

    /**
     * 清理不在数据库中的实体文件
     */
    @Scheduled(cron = "23 9 10 ? * 1")
    public void cleanFileNotInDatabase() {
        log.info("开始清理数据库中不存在的实体文件！");
        // 获取仓库的根路径
        String uploadPath = fileConfig.getUploadPath() + StringUtil.REPOSITORY;
        // 获取跟文件和子文件
        java.io.File rootFile = new java.io.File(uploadPath);
        java.io.File[] fileList = rootFile.listFiles();
        // 遍历文件，找不到的就删了
        if (Objects.nonNull(fileList)) {
            Arrays.stream(fileList).forEach(file -> {
                String fileName = file.getName();
                String md5 = Md5Util.md5(file);
                if (!fileService
                        .lambdaQuery()
                        .like(File::getUrl, fileName)
                        .eq(File::getMd5, md5)
                        .exists()) {
                    // 该文件不在数据库中，删除
                    log.info("删除文件：{}", fileName);
                    FileUtil.deleteDir(file);
                }
            });
        }
    }
}
