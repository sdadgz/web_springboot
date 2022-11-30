package cn.sdadgz.web_springboot.aop;

import cn.sdadgz.web_springboot.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class AllAdvice {

    @Before(value = "execution(cn..Result cn..controller.*.*(..,javax.servlet.http.HttpServletRequest)) && args(..,request)")
    public void visit(JoinPoint joinPoint, HttpServletRequest request) {
        String ip = IdUtil.getIp(request);
        log.info("访问者ip：{}", ip);
    }

}
