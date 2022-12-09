package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.utils.TimeUtil;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.IpBan;
import cn.sdadgz.web_springboot.mapper.IpBanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @Resource
    private IIpBanService ipBanService;

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

        ipBanService.addExcept(e);

        return Result.error(e.getCode(), e.getMessage());
    }
}
