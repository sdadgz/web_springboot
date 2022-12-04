package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sdadgz
 * @since 2022-09-23
 */
public interface IFileService extends IService<File> {

    // 新增文件
    int addFile(File file);

    // 根据id修改
    Integer updateFileById(File file);

    // 虚拟删除
    Long virtualDelete(List<File> files);

    // 物理删除（固定只删除不需要的）
    void realDelete();

    // 根据md5删除数据库
    void deleteByMD5Batch(String... md5);

    // 获取分页
    Map<String, Object> getPage(Integer userId, Integer currentPage, Integer pageSize);

    // id -> file
    File getFileById(Integer id);

    // userId -> file总数
    Long getTotalByUserId(Integer userId);

    // md5 -> files
    List<File> getFilesByMd5(String md5);

    // filename -> file
    File getFileByFilename(String filename);

    // 垃圾回收
    List<File> getGC();

}
