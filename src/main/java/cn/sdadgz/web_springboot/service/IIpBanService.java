package cn.sdadgz.web_springboot.service;

import cn.sdadgz.web_springboot.config.DangerousException;
import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    boolean blacklist(String ip);

    // 防止用户密码被暴力破解
    void protect(User user);

    // 垃圾回收
    List<IpBan> getGC();

    // 异常记录
    void addExcept(DangerousException e);

}
