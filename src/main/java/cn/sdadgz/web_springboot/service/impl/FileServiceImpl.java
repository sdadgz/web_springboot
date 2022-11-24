package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.config.FileConfig;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.utils.GeneralUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public File deleteFile(Integer fileId, Integer userId) {
        File file = fileMapper.selectById(fileId);

        // 遣返跨权
        if (userId > 0 && !userId.equals(file.getUserId())) {
            throw new BusinessException("498", "权限不足");
        }

        file.setIsDelete(true);
        fileMapper.updateById(file);
        return file;
    }

    @Override
    public File realDelete(File file) {

        // 确实需要删除了
        String path = file.getUrl();
        // 是上传的图片，不是网图
        if (path.contains(fileConfig.getDownloadPath())) {
            path = path.substring(fileConfig.getDownloadPath().length());
            java.io.File jFile = new java.io.File(fileConfig.getUploadPath() + path);
            boolean delete = jFile.delete();
            log.info("删除文件{}，{}", jFile.getName(), GeneralUtil.tf(delete));
        }

        // 数据库删除
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getMd5, file.getMd5());
        fileMapper.delete(wrapper);

        return file;
    }
}
