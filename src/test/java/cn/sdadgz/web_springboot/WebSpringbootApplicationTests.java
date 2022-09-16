package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.SameCode.User.UserSame;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Resource
    ImgMapper imgMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
        int sdadgz = UserSame.getUserIdByName("sdadgz");
        System.out.println(sdadgz);
    }
}
