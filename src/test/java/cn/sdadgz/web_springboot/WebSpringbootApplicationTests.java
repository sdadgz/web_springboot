package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Resource
    ImgMapper imgMapper;

    @Resource
    private IImgService imgService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private IFileService fileService;

    @Resource
    private IUserService userService;

    @Resource
    private IIpBanService ipBanService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void random() {
        int anInt = RandomUtil.getInt(60);
        System.out.println(anInt);
        anInt = RandomUtil.getInt(60);
        System.out.println(anInt);
        anInt = RandomUtil.getInt(24);
        System.out.println(anInt);
        anInt = RandomUtil.getInt(7);
        System.out.println(anInt);
    }

    @Test
    void cache() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            fileService.getPage(userService.getUserIdByName("sdadgz"), RandomUtil.getInt(3) + 1, 10);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    void redis() throws JsonProcessingException {
        User pojo = new User().setName("sdadgz");
        redisTemplate.opsForValue().set("user", pojo);

        Object user1 = redisTemplate.opsForValue().get("user");
        System.out.println(user1);

        // 字符串测试
        redisTemplate.opsForValue().set("string","这是一个中文字符串");
        Object string = redisTemplate.opsForValue().get("string");
        System.out.println(string);
    }

}
