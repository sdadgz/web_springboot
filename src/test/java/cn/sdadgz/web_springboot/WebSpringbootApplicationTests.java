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
        String path = "D:\\下载\\不用思考，删这个文件夹\\258969461563168455685717945467846336308illust_91452046_20210724_134755.jpg";
        String reducePath = "D:\\下载\\不用思考，删这个文件夹\\reduce.jpg";

        System.out.println(Md5Util.md5(path));

//        Thumbnails.of(path).scale(1).outputQuality(.8).toFile(path + ".jpg");
    }

    @Test
    void error(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName,"sdadgz");
        userMapper.selectCount(wrapper);
    }
}
