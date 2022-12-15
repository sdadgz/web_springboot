package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.Log;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sdadgz
 * @since 2022-12-13
 */
public interface ILogService extends IService<Log> {

    // 上传
    int addLog(Log log);

    // 获取分页
    Map<String, Object> getPage(Integer userId, Integer currentPage, Integer pageSize);

    // 获取总数
    Long getTotal(Integer userId);

}
