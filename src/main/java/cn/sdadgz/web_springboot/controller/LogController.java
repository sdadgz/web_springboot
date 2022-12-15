package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.Log;
import cn.sdadgz.web_springboot.service.ILogService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sdadgz
 * @since 2022-12-13
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    public static final String TITLE = "title";
    public static final String TEXT = "text";

    @Resource
    private ILogService logService;

    @PostMapping
    public Result upload(@RequestBody Map<String, String> requestMap,
                         HttpServletRequest request) {

        // 获取信息
        int userId = IdUtil.getUserId(request);
        String username = IdUtil.getUsername(request);
        String title = requestMap.get(TITLE);
        String text = requestMap.get(TEXT);

        // 创建上传的对象
        Log l = new Log();
        l.setTitle(title);
        l.setText(text);
        l.setUserId(userId);
        l.setCreateTime(TimeUtil.now());

        // 上传
        int i = logService.addLog(l);

        log.info("{}上传{}个log", username, i);

        return Result.success();
    }

    @GetMapping("/page")
    public Result getLog(@RequestParam("currentPage") Integer currentPage,
                         @RequestParam("pageSize") Integer pageSize,
                         HttpServletRequest request) {

        int userId = IdUtil.getUserId(request);
        Map<String, Object> page = logService.getPage(userId, currentPage, pageSize);

        return Result.success(page);
    }

}
