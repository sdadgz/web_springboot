package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.Utils.TimeUtil;
import cn.sdadgz.web_springboot.common.Constants;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.mapper.IpBanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @Resource
    private IpBanMapper ipBanMapper;

    // 常规异常
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public Result handleBussinessException(BusinessException e) {
        return Result.error(e.getCode(), "异常：" + e.getMessage());
    }

    // 危险异常
    @ResponseBody
    @ExceptionHandler(DangerousException.class)
    public Result dangerousException(DangerousException e) {
        log.error("出现异常ip：{}，异常行为：{}", e.getIp(), e.getMessage());

        IpBan ipBan = new IpBan();
        ipBan.setCreateTime(TimeUtil.now());
        ipBan.setMsg(e.getMessage());
        ipBan.setUserId(e.getUserId());
        ipBan.setIp(e.getIp());

        ipBanMapper.insert(ipBan);

        return Result.error(e.getCode(), e.getMessage());
    }
}
