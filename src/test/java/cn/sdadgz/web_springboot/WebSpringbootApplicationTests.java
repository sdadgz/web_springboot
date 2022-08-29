package cn.sdadgz.web_springboot;

import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
class WebSpringbootApplicationTests {

    @Resource
    BlogMapper blogMapper;

    @Test
    void contextLoads() throws IOException {
//        String filePath = "D:/下载/编程/笔记/docker.md";
//        File file = new File(filePath);
//        FileReader reader = new FileReader(file);
//        BufferedReader br = new BufferedReader(reader);
//        String line;
//        while ((line = br.readLine()) != null){
//            System.out.println(line);
//        }

        List<Blog> blogs = blogMapper.getBlogsByName("sdadgz");
        for (Blog blog : blogs) {
            System.out.println(blog.getText());
            System.out.println("======================================");
        }
    }

}
