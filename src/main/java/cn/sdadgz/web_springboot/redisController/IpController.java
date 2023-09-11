package cn.sdadgz.web_springboot.redisController;

import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.RedisUtil;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.utils.StringUtil;
import cn.sdadgz.web_springboot.utils.TimeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/ip")
public class IpController {

    // 允许唯一非管理员查看，嗯，也就是我自己
    public static final String ALLOW_USER = "sdadgz";

    @Resource
    private RedisUtil redisUtil;

    @GetMapping
    public Result getIp(HttpServletRequest request) {
        String ip = IdUtil.getIp(request);
        return Result.success(ip);
    }

    @GetMapping("/today")
    public Result getTodayIpList(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 允许管理员及我自己
        UserBan.getTheFuckOut(ALLOW_USER, request);

        // todo 要不试试上redis事务？
        // 根据前缀获取今日来访者
        Set<String> keys = redisUtil.getKeys(TimeUtil.nowDay() + StringUtil.COLON);

        keys.forEach(key -> map.put(key, redisUtil.get(key)));

        return Result.success(map);
    }

}
