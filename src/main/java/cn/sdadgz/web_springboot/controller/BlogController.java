package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.Utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.Utils.TimeUtil;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author eula
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    BlogMapper blogMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    ImgMapper imgMapper;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;


    // 上传并新建一个博客
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         @RequestParam("imgId") int imgId,
                         @RequestParam("title") String title,
                         @RequestParam("detail") String detail,
                         HttpServletRequest request) throws IOException {
        // 文件原始名
        String originalFilename = file.getOriginalFilename();

        // 没标题就用文件名
        if (title.length() < 1) {
            assert originalFilename != null;
            title = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        }

        // 获取用户id
        int userid = IdUtil.getId(request);

        // 防止重复标题
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("user_id", userid);
        List<Blog> blogs = blogMapper.selectList(wrapper);
        if (blogs.size() > 0) {
            throw new BusinessException("465", "重复的标题");
        }

        // 初始化
        FileUtil fileUtil = new FileUtil(uploadPath, downloadPath, null);

        // 获取创建时间和处理后的博客内容
//        LocalDateTime createTime = fileUtil.getCreateTime(file); // 被阉割
        LocalDateTime createTime = TimeUtil.now();
//        String text = fileUtil.md((File) file);
        String path = uploadPath + "blog/temp.md";
        fileUtil.uploadToServer(file, path);
        File jFile = new File(path);
        String text = fileUtil.md(jFile);

        Blog blog = new Blog();
        blog.setUserId(userid);
        blog.setText(text);
        blog.setImgId(imgId);
        blog.setTitle(title);
        blog.setDetail(detail);
        blog.setCreatetime(createTime);
        blogMapper.insert(blog);

        return Result.success(blog);
    }

    // 获取某个博客
    @GetMapping("/{username}/blog/{title}")
    public Result getBlog(@PathVariable("username") String username,
                          @PathVariable("title") String title) {

        Blog blog = blogMapper.getBlog(username, title);
        return Result.success(blog);
    }

    // 提供用户名获取blogs
    @GetMapping("/{username}/blogs")
    public Result getBlogsByUserName(@PathVariable("username") String username) {
        List<Blog> blogs = blogMapper.getBlogsByName(username);
        return Result.success(blogs);
    }

    // 修改博客
    @PostMapping("/update")
    public Result updateBlog(@RequestBody Blog blog,
                             HttpServletRequest request) {

        int userId = IdUtil.getId(request);
        int id = blog.getId();
        if (userId > 0) {
            Blog dbBlog = blogMapper.selectById(id);
            if (dbBlog.getUserId() != userId) {
                throw new BusinessException("498", "权限不足");
            }
        }
        int i = blogMapper.updateById(blog);

        return Result.success(i);
    }

    // 删除博客
    @DeleteMapping("")
    public Result deleteBlog(@RequestBody Map<String, Integer[]> objectMap) {

        Integer[] idList = objectMap.get("idList");

        for (Integer integer : idList) {
            blogMapper.deleteById(integer);
        }

        return Result.success();
    }

    // 分页
    @GetMapping("/{username}/page")
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       @PathVariable("username") String username,
                       HttpServletRequest request) {

        Page<BlogMapper, Blog> page = new Page<>();
        Map<String, Object> map = page.getPage(currentPage, pageSize, request, blogMapper);

        return Result.success(map);
    }
}
