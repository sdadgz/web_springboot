package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IIpBanService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.GeneralUtil;
import cn.sdadgz.web_springboot.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @Test
    void cache() {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            fileService.getPage(userService.getUserIdByName("sdadgz"), RandomUtil.getInt(3) + 1, 10);
        }
        System.out.println(System.currentTimeMillis() - l);
    }

}
