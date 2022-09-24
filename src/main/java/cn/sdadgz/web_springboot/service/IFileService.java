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

}
