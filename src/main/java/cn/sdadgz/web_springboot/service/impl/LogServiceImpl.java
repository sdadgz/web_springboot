package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.UnificationConfig;
import cn.sdadgz.web_springboot.entity.Log;
import cn.sdadgz.web_springboot.mapper.LogMapper;
import cn.sdadgz.web_springboot.service.ILogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sdadgz
 * @since 2022-12-13
 */
@Service
@CacheConfig(cacheNames = "logCache")
@Slf4j
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Resource
    private LogMapper logMapper;

    @Resource
    private ILogService logService;

    // 不要手欠删了
    @Resource
    public UnificationConfig unificationConfig;

    @CacheEvict(allEntries = true)
    @Override
    public int addLog(Log l) {
        return logMapper.insert(l);
    }

    @Cacheable(unless = "#result.get(#root.target.unificationConfig.getResponseLists()).size() == 0")
    @Override
    public Map<String, Object> getPage(Integer userId, Integer currentPage, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        int startPage = (currentPage - 1) * pageSize;

        List<Log> page = logMapper.getPage(userId, startPage, pageSize);
        Long total = logService.getTotal(userId);

        map.put(unificationConfig.getResponseLists(), page);
        map.put(unificationConfig.getResponseTotal(), total);

        return map;
    }

    @Cacheable(unless = "#result == 0")
    @Override
    public Long getTotal(Integer userId) {
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Log::getUserId, userId);
        return logMapper.selectCount(wrapper);
    }
}
