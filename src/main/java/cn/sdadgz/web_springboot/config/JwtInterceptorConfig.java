package cn.sdadgz.web_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class JwtInterceptorConfig implements WebMvcConfigurer {

    @Resource
    JwtAuthenticationInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login", // 用户登录
                        "/user", // 用户注册
                        "/blog/*/blogs", // 博客s
                        "/blog/*/blog/*", // 博客
                        "/static/**");
    }
}
