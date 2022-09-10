package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
@Mapper
public interface ImgMapper extends BaseMapper<Img>, cn.sdadgz.web_springboot.Utils.SameCode.Page.Mapper<Img> {

    // 正常用户分页
    List<Img> getPageByUserId(@Param("userId") Integer userId, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

    // 海克斯科技用户分页
    List<Img> getPage(@Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);
}
