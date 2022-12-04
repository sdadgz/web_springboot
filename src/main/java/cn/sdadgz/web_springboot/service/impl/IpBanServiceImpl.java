package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.StrUtil;
import cn.sdadgz.web_springboot.common.Constants;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.IpBanMapper;
import cn.sdadgz.web_springboot.service.IIpBanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
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

    @Override
    @Cacheable(key = "#request.getHeader(\"X-FORWARDED-FOR\") == null ? #request.remoteAddr : #request.getHeader(\"X-FORWARDED-FOR\")")
    public boolean blacklist(HttpServletRequest request) {
        String ip = IdUtil.getIp(request);

        LambdaQueryWrapper<IpBan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IpBan::getIp, ip);
        Long aLong = ipBanMapper.selectCount(wrapper);

        if (aLong > StrUtil.BLACKLIST_BAN) {
            throw new BusinessException(Constants.CODE_498, "加载失败，联系站长。");
        }

        return true;
    }

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
}
