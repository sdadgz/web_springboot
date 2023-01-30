package cn.sdadgz.web_springboot.redisController;

import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.toy.Toy;
import cn.sdadgz.web_springboot.utils.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/toy")
public class ToyController {

    @Resource
    private RedisUtil redisUtil;

    // 小玩具咯
    public static final String TOY_114514 = "toy:114514";

    public static final int MAX_114514 = 1000100100;

    @GetMapping("/114514")
    public Result get114514(@RequestParam(value = "target", required = false) Long target, @RequestParam("src") int src) {
        Boolean lock = redisUtil.lock(TOY_114514);
        src = Math.abs(src); // 化绝对值
        // 这东西真吃cpu呢，可得上个锁
        if (lock) {
            if (src > MAX_114514){
                return Result.success("让我算会啊。。。");
            }
            Toy toy = new Toy(target);
            String res = toy.start(src);
            redisUtil.unlock(TOY_114514);
            return Result.success(res);
        }
        return Result.success("正在暴力中。。。");
    }

}
