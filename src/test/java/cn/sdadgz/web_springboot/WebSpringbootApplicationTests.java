package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@SpringBootTest
@Slf4j
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Resource
    ImgMapper imgMapper;

    @Resource
    UserMapper userMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
        User user = new User();
        user.setPassword("36df389cf36bd9a12114939a021db844");
        userMapper.update(user, null);
    }

    @Test
    void getImgs(){
        long l = System.currentTimeMillis();
        imgMapper.getNeverUseImgs("首页",1);
        long l1 = System.currentTimeMillis() - l;
        imgMapper.getNeverUseImgs("首页",1);
        long l2 = System.currentTimeMillis() - l;
        imgMapper.getNeverUseImgs("首页",1);
        long l3 = System.currentTimeMillis() - l;
        imgMapper.getNeverUseImgs("首页",1);
        long l4 = System.currentTimeMillis() - l;
        System.out.println(l);
    }

    @Test
    void random(){
        Random random = new Random(1);
        int i = random.nextInt();
        System.out.println(random.hashCode());
        System.out.println(i);
    }
}
