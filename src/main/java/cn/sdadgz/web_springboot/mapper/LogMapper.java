package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sdadgz
 * @since 2022-12-13
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

    // 获取分页
    List<Log> getPage(@Param("userId") Integer userId,
                      @Param("startPage") Integer startPage,
                      @Param("pageSize") Integer pageSize);

}
