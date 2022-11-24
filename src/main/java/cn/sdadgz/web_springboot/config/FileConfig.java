package cn.sdadgz.web_springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my.file-config")
@Getter
@Setter
public class FileConfig {

    private String uploadPath;
    private String staticPath;
    private String downloadPath;

}
