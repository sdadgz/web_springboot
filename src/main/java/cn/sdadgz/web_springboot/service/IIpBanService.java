package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.entity.IpBan;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sdadgz
 * @since 2022-11-09
 */
public interface IIpBanService extends IService<IpBan> {

    // 是否是黑名单ip
    boolean blacklist(HttpServletRequest request);

}
