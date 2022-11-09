package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.service.IIpBanService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BlacklistInterceptor implements HandlerInterceptor {

    @Resource
    private IIpBanService ipBanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ipBanService.blacklist(request);
    }
}
