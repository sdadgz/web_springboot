package cn.sdadgz.web_springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class UserAdvice {

    // 遣返坏东西
//    @Before(value = "execution(cn..Result cn..controller.*.*(..)) && args(..,username,request))",
//            argNames = "joinPoint,username,request")
//    public void userBan(JoinPoint joinPoint, String username, HttpServletRequest request) {
//        log.info("进入");
//    }
}
