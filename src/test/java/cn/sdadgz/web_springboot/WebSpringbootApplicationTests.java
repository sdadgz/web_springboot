package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.scheduled.ImgScheduled;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    UserMapper userMapper;

    @Resource
    private FileMapper fileMapper;

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

}
