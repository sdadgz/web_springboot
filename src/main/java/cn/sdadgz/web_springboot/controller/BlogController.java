package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.dto.BlogDetailDTO;
import cn.sdadgz.web_springboot.dto.BlogsDTO;
import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.*;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.service.IImgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private RedisUtil redisUtil;

    private static final int DEFAULT_IMG_ID = 0; // 默认图片id
    private static final String BLOG_IMG = "博客首页";
    public static final String UPLOAD_LOCK = "lock:upload";
    public static final int UPLOAD_LOCK_MS = 1000;

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

        return Result.success(blog);
    }

    // 批量上传博客
    @PostMapping("/uploads")
    public Result upload(@RequestPart("files") MultipartFile files,
                         @RequestParam(value = "createTime", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createTime,
                         HttpServletRequest request) throws IOException {

        // 加锁
        redisUtil.waitLock(UPLOAD_LOCK, UPLOAD_LOCK_MS);

        // 初始化
        Map<String, Object> map = new HashMap<>();
        FileUtil fileUtil = new FileUtil();
        int userId = IdUtil.getUserId(request);

        // 时间不存在使用当前时间
        if (GeneralUtil.isNull(createTime)) {
            createTime = TimeUtil.now();
        }

        // 获取本人博客首页图片
        List<Img> neverUseImg = imgService.getNeverUseImgs(BLOG_IMG, userId);

        if (neverUseImg.size() < 1) {
            // 他没存图片
            fileUtil.mdUpload(files, "", request, DEFAULT_IMG_ID, "", createTime);
        } else {
            // 他存了图片
            int seed = files.hashCode() + createTime.hashCode() + request.hashCode() + map.hashCode() +
                    fileUtil.hashCode() + userId + neverUseImg.hashCode();
            int rand = RandomUtil.getInt(neverUseImg.size(), seed);
            Integer imgId = neverUseImg.get(rand).getId();
            fileUtil.mdUpload(files, StringUtil.EMPTY_STRING, request, imgId, StringUtil.EMPTY_STRING, createTime);
        }

        // 释放锁
        redisUtil.unlock(UPLOAD_LOCK);

        return Result.success(map);
    }

    // 获取某个博客
    @GetMapping("/{username}/blog/{title}")
    public Result getBlog(@PathVariable("username") String username,
                          @PathVariable("title") String title) {

        Blog blog = blogService.getBlogByUsernameAndTitle(username, title);
        BlogDetailDTO dto = BlogDetailDTO.builder()
                .text(blog.getText())
                .imgUrl(blog.getImg().getUrl())
                .title(blog.getTitle())
                .build();
        return Result.success(dto);
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

        // 去掉冗余字段
        Object lists = map.get("lists");
        List<Blog> blogs = (List<Blog>) lists;
        List<BlogsDTO> blogsDTOS = blogs.stream().map(BlogsDTO::of).collect(Collectors.toList());
        map.put("lists", blogsDTOS);

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
    /** Deprecated **/
    @GetMapping("/{username}/page")
    @Deprecated
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       @PathVariable("username") String username,
                       HttpServletRequest request) {

        // 遣返
        UserBan.getTheFuckOut(username, request);

        Map<String, Object> map = blogService.getPage(userService.getUserIdByName(username), currentPage, pageSize);

        return Result.success(map);
    }
}
