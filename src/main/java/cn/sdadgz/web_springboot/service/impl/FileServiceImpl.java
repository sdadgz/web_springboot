package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.FileConfig;
import cn.sdadgz.web_springboot.config.UnificationConfig;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@CacheConfig(cacheNames = "fileCache")
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private IFileService fileService;

    @Resource
    private FileConfig fileConfig;

    // Cacheable注解不认识private
    @Resource
    public UnificationConfig unificationConfig;

    @Override
    @CacheEvict(allEntries = true)
    public int addFile(File file) {
        return fileMapper.insert(file);
    }

    @Override
    @Cacheable(unless = "#result.get(#root.target.unificationConfig.getResponseLists()).size() == 0")
    public Map<String, Object> getPage(Integer userId, Integer currentPage, Integer pageSize) {

        // 初始化
        Map<String, Object> map = new HashMap<>();
        int startPage = (currentPage - 1) * pageSize;

        List<File> page = fileMapper.getPage(userId, startPage, pageSize);
        Long totalByUserId = fileService.getTotalByUserId(userId);

        map.put(unificationConfig.getResponseLists(), page);
        map.put(unificationConfig.getResponseTotal(), totalByUserId);

        return map;
    }

    @Override
    @Cacheable(unless = "#result == 0L")
    public Long getTotalByUserId(Integer userId) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getUserId, userId);
        return fileMapper.selectCount(wrapper);
    }

    // 根据md5查询
    @Override
    @Cacheable(unless = "#result.size() == 0")
    public List<File> getFilesByMd5(String md5) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getMd5, md5);
        return fileMapper.selectList(wrapper);
    }

    @Override
    @Cacheable(unless = "#result == null")
    public File getFileByFilename(String filename) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getOriginalFilename, filename);
        return fileMapper.selectOne(wrapper);
    }

    @Override
    @Cacheable(unless = "#result == null")
    public File getFileById(Integer id) {
        return fileMapper.selectById(id);
    }

    @Override
    @Cacheable(unless = "#result == null")
    public Integer updateFileById(File file) {
        return fileMapper.updateById(file);
    }

    // 垃圾回收，定期调用不需要缓存
    @Override
    public List<File> getGC() {
        return fileMapper.getGC();
    }

    @Override
    @CacheEvict(allEntries = true)
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
    @CacheEvict(allEntries = true)
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
    @CacheEvict(allEntries = true)
    public void deleteByMD5Batch(String... md5) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(File::getMd5, Arrays.asList(md5));
        int delete = fileMapper.delete(wrapper);
        log.info("file数据库根据md5s删除了{}条数据", delete);
    }
}
