package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.DangerousException;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.StrUtil;
import cn.sdadgz.web_springboot.common.Constants;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.IpBanMapper;
import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.utils.TimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sdadgz
 * @since 2022-11-09
 */
@Service
@CacheConfig(cacheNames = "ipBanCache")
public class IpBanServiceImpl extends ServiceImpl<IpBanMapper, IpBan> implements IIpBanService {

    @Resource
    private IpBanMapper ipBanMapper;

    @Resource
    private IIpBanService ipBanService;

    @Override
    public boolean blacklist(HttpServletRequest request) {
        String ip = IdUtil.getIp(request);
        return ipBanService.blacklist(ip);
    }

    // 黑名单，没有什么是加一层解决不了的问题，如果有，那就再加一层
    @Cacheable
    @Override
    public boolean blacklist(String ip) {
        LambdaQueryWrapper<IpBan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IpBan::getIp, ip);
        Long aLong = ipBanMapper.selectCount(wrapper);

        if (aLong > StrUtil.BLACKLIST_BAN) {
            throw new BusinessException(Constants.CODE_498, "加载失败，联系站长。");
        }

        return true;
    }

    // 他人尝试次数超过指定次数恒返回假信息
    @Override
    public void protect(User user) {
        LambdaQueryWrapper<IpBan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IpBan::getUserId, user.getId());
        Long aLong = ipBanMapper.selectCount(wrapper);

        if (aLong > StrUtil.BLACKLIST_BAN) {
            throw new BusinessException(Constants.CODE_499, "用户名或密码错误");
        }
    }

    @Override
    public List<IpBan> getGC() {
        return ipBanMapper.getGC();
    }

    @Override
    @CacheEvict(allEntries = true)
    public void addExcept(DangerousException e) {
        IpBan ipBan = new IpBan();
        ipBan.setCreateTime(TimeUtil.now());
        ipBan.setMsg(e.getMessage());
        ipBan.setUserId(e.getUserId());
        ipBan.setIp(e.getIp());

        ipBanMapper.insert(ipBan);
    }

    @Override
    @Cacheable(unless = "#result.size() == 0")
    public List<IpBan> getIpBanPage(int currentPage, int pageSize) {
        int startPage = (currentPage - 1) * pageSize;
        LambdaQueryWrapper<IpBan> wrapper = new LambdaQueryWrapper<>();
        wrapper.last("limit " + startPage + "," + pageSize);
        return ipBanMapper.selectList(wrapper);
    }
}
