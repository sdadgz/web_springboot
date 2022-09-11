package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.Utils.TimeUtil;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Resource
    ImgMapper imgMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
        Random random = new Random();
        int i = random.nextInt();
        System.out.println(i);
    }
}
