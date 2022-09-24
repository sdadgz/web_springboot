package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sdadgz
 * @since 2022-09-23
 */
@Mapper
public interface FileMapper extends BaseMapper<File>, cn.sdadgz.web_springboot.Utils.SameCode.Page.Mapper<File> {

    // 正常用户分页
    List<File> getPage(@Param("userId") Integer userId, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

}
