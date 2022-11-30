package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.Img;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
public interface IImgService extends IService<Img> {

    // 获取图片
    List<Img> getImgs(String field, String username);

    // 获取没有被使用过的图片
    List<Img> getNeverUseImg(String field, Integer userId);

    // 空引用图片
    List<Img> getGC();

    // 批量虚拟删除图片
    Long virtualDeleteBatch(List<Img> imgs);

    // 物理删除冗余图片
    void realDeleteBatch();

}
