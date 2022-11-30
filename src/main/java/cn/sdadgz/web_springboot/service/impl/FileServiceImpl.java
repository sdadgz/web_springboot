package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.FileConfig;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sdadgz
 * @since 2022-09-23
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileConfig fileConfig;

    // 根据md5查询
    @Override
    public List<File> getFilesByMd5(String md5) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getMd5, md5);
        return fileMapper.selectList(wrapper);
    }

    @Override
    public List<File> getGC() {
        return fileMapper.getGC();
    }

    @Override
    public Long virtualDelete(List<File> files) {
        // 去掉空
        if (files.size() < 1) {
            log.info("file虚拟删除传参为空");
            return 0L;
        }

        // 虚拟删除
        return fileMapper.virtualDeleteBatch(files);
    }

    @Override
    public void realDelete() {
        // 真实删除图片
        FileUtil fileUtil = new FileUtil();
        List<File> deleteFiles = fileMapper.getDeleteFiles();

        for (File deleteFile : deleteFiles) {
            // 文件删除
            fileUtil.deleteByUrl(deleteFile.getUrl());

            // 数据库删除
            deleteByMD5Batch(deleteFile.getMd5());
        }
    }

    @Override
    public void deleteByMD5Batch(String... md5) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(File::getMd5, Arrays.asList(md5));
        int delete = fileMapper.delete(wrapper);
        log.info("file数据库根据md5s删除了{}条数据", delete);
    }
}
