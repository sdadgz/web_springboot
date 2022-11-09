package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.Md5Util;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@SpringBootTest
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
}
