package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.Img;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
public interface IImgService extends IService<Img> {

    // 新增图片
    void addImg(Img img);

    // 批量虚拟删除图片
    Long virtualDeleteBatch(List<Img> imgs);

    // 物理删除冗余图片
    void realDeleteBatch();

    // 更新
    int updateImgById(Img img);

    // 获取分页
    Map<String, Object> getPage(Integer userId, Integer currentPage, Integer pageSize);

    // userId -> total
    Long getTotalByUserId(Integer userId);

    // field, username -> imgs
    List<Img> getImgsByFieldAndUsername(String field, String username);

    // field, username --随机-> imgs
    List<Img> getImgsRandByFieldAndUsername(String field, String username, Integer count);

    // id -> img
    Img getImgById(Integer imgId);

    // md5 -> imgs
    List<Img> getImgsByMD5(String md5);

    // 获取未使用图片
    List<Img> getNeverUseImgs(String field, Integer userId);

    // 获取空引用图片
    List<Img> getGC();

}
