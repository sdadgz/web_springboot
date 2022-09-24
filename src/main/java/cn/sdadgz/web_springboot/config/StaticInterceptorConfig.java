package cn.sdadgz.web_springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class StaticInterceptorConfig implements WebMvcConfigurer {

    @Resource
    StaticInterceptor staticInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(staticInterceptor).addPathPatterns("/static/repository/*");
    }
}
