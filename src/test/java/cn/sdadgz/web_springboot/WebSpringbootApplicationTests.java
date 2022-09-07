package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.TimeUtil;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
        String pathStr = "D:/下载/编程/笔记/docker.md";
//        FileUtil u = new FileUtil();
//        File file = new File(pathStr);
//        String md = u.md(file);
//        System.out.println(md);
        Date date = FileUtil.getCreateTime(pathStr);
        System.out.println(TimeUtil.translate(date));
    }
}
