package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.Img;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
public interface IImgService extends IService<Img> {

    // 获取图片
    List<Img> getImgs(String field, String username);
}
