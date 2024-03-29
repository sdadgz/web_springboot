package cn.sdadgz.web_springboot.aop;

import cn.sdadgz.web_springboot.config.ServerConfig;
import cn.sdadgz.web_springboot.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class AllAdvice {

    // 默认过期时间
    public static final int TIMEOUT = TimeUtil.SECOND_PER_DAY * 7;

    // server配置
    @Resource
    private ServerConfig serverConfig;

    // 总ip存放地
    public static final String IP_LIST = "ip_list";

    @Resource
    private RedisUtil redisUtil;

    @Before(value = "execution(boolean cn..config.BlacklistInterceptor.*(javax.servlet.http.HttpServletRequest,..)) && args(request,..)")
    @Async
    public void visit(JoinPoint joinPoint, HttpServletRequest request) {
        String ip = IdUtil.getIp(request);
        if (!GeneralUtil.isNull(ip)) {
            // ip访问信息储存到redis里
            addIp(ip);
        }
    }

    @Before(value = "execution(boolean cn..config.BlacklistInterceptor.*(javax.servlet.http.HttpServletRequest,..)) && args(request,..)")
    public void localhostOnly(JoinPoint joinPoint, HttpServletRequest request) {
//        // 放行options
//        if (WebUtil.passOptions(request)) {
//            return;
//        }

        // 仅允许localhost访问
//        String ip = request.getRemoteAddr();
//        if (!serverConfig.getLocalhostIp().equals(ip)) {
//            throw new BusinessException("471", "ip:" + ip + "被拒绝访问");
//        }
    }

    // 将ip添加到redis里
    public void addIp(String ip) {
        // 年-月-日:ip 作为key
        String key = TimeUtil.nowDay() + StringUtil.COLON + ip;

        // 计数
        redisUtil.setIncrExp(key, TIMEOUT, k -> log.info("ip：{}今日首次访问，统计过期时间{}s", k, TIMEOUT));

        // 总记
        redisUtil.setSet(IP_LIST, ip);
    }

}
