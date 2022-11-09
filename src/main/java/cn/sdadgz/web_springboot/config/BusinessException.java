package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.Utils.StrUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
