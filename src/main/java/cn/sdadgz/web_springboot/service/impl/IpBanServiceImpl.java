package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.Utils.StrUtil;
import cn.sdadgz.web_springboot.common.Constants;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.IpBanMapper;
import cn.sdadgz.web_springboot.service.IIpBanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sdadgz
 * @since 2022-11-09
 */
@Service
public class IpBanServiceImpl extends ServiceImpl<IpBanMapper, IpBan> implements IIpBanService {

    @Resource
    private IpBanMapper ipBanMapper;

    @Override
    public boolean blacklist(HttpServletRequest request) {
        String ip = IdUtil.getIp(request);

        LambdaQueryWrapper<IpBan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IpBan::getIp, ip);
        Long aLong = ipBanMapper.selectCount(wrapper);

        if (aLong > StrUtil.BLACKLIST_BAN) {
            throw new BusinessException(Constants.CODE_498, "用户已被拉入黑名单");
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
}
