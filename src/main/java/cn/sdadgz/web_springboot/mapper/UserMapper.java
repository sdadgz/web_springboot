package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author eula
 * @since 2022-08-25
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
