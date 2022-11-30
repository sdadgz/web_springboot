package cn.sdadgz.web_springboot.mapper;

import cn.sdadgz.web_springboot.entity.IpBan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sdadgz
 * @since 2022-11-09
 */
@Mapper
public interface IpBanMapper extends BaseMapper<IpBan> {

    // 获取GC
    List<IpBan> getGC();

}
