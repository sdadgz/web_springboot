package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.*;
import cn.sdadgz.web_springboot.utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@Slf4j
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;

    @Resource
    private IUserService userService;

    @Resource
    private IImgService imgService;

    private static final int DEFAULT_IMG_ID = 0; // 默认图片id
    private static final String BLOG_IMG = "博客首页";

    // 上传并新建一个博客
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         @RequestParam("imgId") int imgId,
                         @RequestParam("title") String title,
                         @RequestParam("detail") String detail,
                         @RequestParam(value = "createTime", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createTime,
                         HttpServletRequest request) throws IOException {

        FileUtil fileUtil = new FileUtil();
        Blog blog = fileUtil.mdUpload(file, title, request, imgId, detail, createTime);

        // 使用前端传来的时间作为创建时间
//        blog.setCreateTime(createTime);
//        blogService.updateBlogById(blog);

        return Result.success(blog);
    }

    // 批量上传博客
    @PostMapping("/uploads")
    public Result upload(@RequestPart("files") MultipartFile files,
                         @RequestParam(value = "createTime", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createTime,
                         HttpServletRequest request) throws IOException {

        // 初始化
        Map<String, Object> map = new HashMap<>();
        FileUtil fileUtil = new FileUtil();
        int userId = IdUtil.getUserId(request);

        // 时间不存在使用当前时间
        if (GeneralUtil.notNull(createTime)) {
            createTime = TimeUtil.now();
        }

        // 获取本人博客首页图片
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getUserId, userId); // 用户id
        wrapper.like(Img::getField, BLOG_IMG);

        List<Img> neverUseImg = imgService.getNeverUseImg(BLOG_IMG, userId);
        if (neverUseImg.size() < 1) {
            fileUtil.mdUpload(files, "", request, DEFAULT_IMG_ID, "", createTime);
        } else {
            int seed = files.hashCode() + request.hashCode() + map.hashCode() + fileUtil.hashCode() + userId
                    + wrapper.hashCode() + neverUseImg.hashCode();
            int rand = RandomUtil.getInt(neverUseImg.size(), seed);
            Integer imgId = neverUseImg.get(rand).getId();
            fileUtil.mdUpload(files, StrUtil.EMPTY_STRING, request, imgId, StrUtil.EMPTY_STRING, createTime);
        }

        return Result.success(map);
    }

    // 获取某个博客
    @GetMapping("/{username}/blog/{title}")
    public Result getBlog(@PathVariable("username") String username,
                          @PathVariable("title") String title) {

        Blog blog = blogService.getBlogByUsernameAndTitle(username, title);
        return Result.success(blog);
    }

    // 提供用户名获取blogs
    @GetMapping("/{username}/blogs")
    public Result getBlogsByUserName(@PathVariable("username") String username,
                                     @RequestParam("currentPage") int currentPage,
                                     @RequestParam("pageSize") int pageSize) {

        // 用户id
        int userId = userService.getUserIdByName(username);

        // 分页数据
        Map<String, Object> map = blogService.getPage(userId, currentPage, pageSize);

        return Result.success(map);
    }

    // 修改博客
    @PostMapping("/update")
    public Result updateBlog(@RequestBody Blog blog,
                             HttpServletRequest request) {

        int userId = IdUtil.getUserId(request);
        int id = blog.getId();
        if (userId > 0) {
            Blog dbBlog = blogService.getBlogById(id);
            if (dbBlog.getUserId() != userId) {
                throw new BusinessException("498", "权限不足");
            }
        }
        int i = blogService.updateBlogById(blog);

        return Result.success(i);
    }

    // 删除博客
    @DeleteMapping
    public Result deleteBlog(@RequestBody Map<String, List<Integer>> objectMap,
                             HttpServletRequest request) {

        // 返回集
        Map<String, Object> map = new HashMap<>();
        int count = 0;

        int userId = IdUtil.getUserId(request);

        List<Integer> idList = objectMap.get("idList");

        for (Integer integer : idList) {
            Blog blog = blogService.getBlogById(integer);
            // 阻止用户跨权限
            if (userId > 0 && blog.getUserId() != userId) {
                throw new BusinessException("498", "权限不足");
            }

            count += blogService.deleteById(integer);
        }

        // 返回数据
        map.put("delete", count);

        return Result.success(map);
    }

    // 分页
    @GetMapping("/{username}/page")
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       @PathVariable("username") String username,
                       HttpServletRequest request) {

        // 遣返
        UserBan.getTheFuckOut(username, request);

        Map<String, Object> map = blogService.getPage(userService.getUserIdByName(username), currentPage, pageSize);

        // 真是个废物码农，完全不会解耦
//        Page<BlogMapper, Blog> page = new Page<>();
//        Map<String, Object> map = page.getPage(currentPage, pageSize, request, blogMapper);

        return Result.success(map);
    }
}
