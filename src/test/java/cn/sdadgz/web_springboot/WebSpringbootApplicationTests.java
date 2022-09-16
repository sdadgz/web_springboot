package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.SameCode.User.UserSame;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Resource
    ImgMapper imgMapper;

    @Test
    void contextLoads() throws IOException, NoSuchAlgorithmException {
        String path = "D:\\下载\\不用思考，删这个文件夹\\258969461563168455685717945467846336308illust_91452046_20210724_134755.jpg";
        String reducePath = "D:\\下载\\不用思考，删这个文件夹\\reduce.jpg";

//        Thumbnails.of(path).scale(1).outputQuality(.8).toFile(path + ".jpg");
    }
}
