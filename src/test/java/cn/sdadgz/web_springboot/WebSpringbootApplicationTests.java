package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.JwtU;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
//        String token = JwtU.CreateToken(String.valueOf(0), "sdadgz", "123456");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwIiwiZXhwIjoxNjYyMDUwOTMzLCJpYXQiOjE2NjIwNDM3NTksInVzZXJuYW1lIjoic2RhZGd6In0.kR_DtqsYnvbqCHsje6MJqIRsTzaf4901rYY_Cwr5PlM";
        System.out.println(token);
        System.out.println(JwtU.getAudience(token));
        System.out.println(JwtU.checkDate(token));
        System.out.println(JwtU.getClaimByName(token, "username"));
        System.out.println(JwtU.vertifyToken(token, "0", "123456"));
    }
}
