package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sdadgz
 * @since 2022-09-23
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
