package cn.sdadgz.web_springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my.server")
@Getter
@Setter
public class ServerConfig {

    private String name;

    private String uploadPath;

}
