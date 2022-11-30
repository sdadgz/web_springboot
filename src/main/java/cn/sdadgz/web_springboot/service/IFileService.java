package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sdadgz
 * @since 2022-09-23
 */
public interface IFileService extends IService<File> {

    // 根据md5查询
    List<File> getFilesByMd5(String md5);

    // 垃圾回收
    List<File> getGC();

    // 删除文件
    Long virtualDelete(List<File> files);

    // 物理删除
    void realDelete();

    // 根据md5删除数据库
    void deleteByMD5Batch(String... md5);
}
