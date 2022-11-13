package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.utils.IdUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class DangerousException extends RuntimeException {
    private String code;

    private String ip;

    private Integer userId;

    public DangerousException(String code, String message, String ip, Integer userId) {
        super(message);
        this.code = code;
        this.ip = ip;
        this.userId = userId;
    }

    public DangerousException(String code, String message, HttpServletRequest request, Integer userId) {
        super(message);
        this.code = code;
        this.ip = IdUtil.getIp(request);
        this.userId = userId;
    }
}
