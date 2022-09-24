package cn.sdadgz.web_springboot.Utils.SameCode.Page;

import cn.sdadgz.web_springboot.entity.Img;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Mapper<T> extends BaseMapper<T> {

    // 正常用户分页
    List<T> getPage(@Param("userId") Integer userId, @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

}
