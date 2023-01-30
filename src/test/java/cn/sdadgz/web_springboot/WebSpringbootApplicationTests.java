package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.*;
import cn.sdadgz.web_springboot.toy.Toy;
import cn.sdadgz.web_springboot.utils.RandomUtil;
import cn.sdadgz.web_springboot.utils.RedisUtil;
import cn.sdadgz.web_springboot.utils.StrUtil;
import cn.sdadgz.web_springboot.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private IBlogService blogService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Test
    void toy() {
        Toy toy = new Toy();
        String start = toy.start(100);
        System.out.println(start);
    }

    //    @Test
    void cache() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            fileService.getPage(userService.getUserIdByName("sdadgz"), RandomUtil.getInt(3) + 1, 10);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    //    @Test
    void redis() {
        // 测试工具类
        LocalDate localDate = TimeUtil.nowDay();
        String ip1 = "0.0.0.0";
        String ip2 = "0.0.0.1";
        String ip3 = "0.0.0.2";
        redisUtil.setSet("ip_list", ip1, ip2, ip3);
        redisUtil.setIncrExp(localDate + StrUtil.COLON + ip1, TimeUtil.SECOND_PER_DAY * 7);
        redisUtil.setIncrExp(localDate + StrUtil.COLON + ip2, TimeUtil.SECOND_PER_DAY * 7);
        redisUtil.setIncrExp(localDate + StrUtil.COLON + ip3, TimeUtil.SECOND_PER_DAY * 7);
    }

    //    @Test
    void imgCacheBlogCache() {
        List<Img> imgs = imgService.getNeverUseImgs("博客首页", 1);
        Blog blog = blogService.getBlogByUsernameAndTitle("sdadgz", "javaweb");

        blog.setImgId(imgs.get(RandomUtil.getInt(imgs.size())).getId());
        blogService.updateBlogById(blog);
        List<Img> i2 = imgService.getNeverUseImgs("博客首页", 1);

        int flat = 0;
        for (Img img : imgs) {
            boolean flag = false;
            for (Img img1 : i2) {
                if (img1.getId().equals(img.getId())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                flat++;
            }
        }

        System.out.println(flat);
    }

}
