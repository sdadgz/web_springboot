package cn.sdadgz.web_springboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my.unification-config")
@Getter
@Setter
public class UnificationConfig {

    private String responseLists;

    private String responseTotal;

}
