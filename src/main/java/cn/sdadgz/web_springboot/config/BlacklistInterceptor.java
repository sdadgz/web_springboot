package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.service.IIpBanService;
import org.jetbrains.annotations.NotNull;
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
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        return ipBanService.blacklist(request);
    }
}
