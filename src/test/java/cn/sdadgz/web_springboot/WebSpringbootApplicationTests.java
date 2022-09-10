package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.TimeUtil;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
        List<Blog> blogs = blogMapper.getBlogsByName("sdadgz");
        System.out.println(1);
    }
}
