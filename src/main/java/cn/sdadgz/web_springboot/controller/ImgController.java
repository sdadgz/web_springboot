package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
@RestController
@RequestMapping("/img")
public class ImgController {

    @Resource
    private ImgMapper imgMapper;

    @Resource
    private UserMapper userMapper;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    // 分页
    @GetMapping("/{username}/page")
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       @PathVariable("username") String username,
                       HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Page<Img> page = new Page<>(currentPage, pageSize);
        Long total;
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Img::getCreatetime);

        int userId = IdUtil.getId(request);

        if (userId > 0) { // 不是root用户
            if (!IdUtil.getName(request).equals(username)) { // 权限不足
                throw new BusinessException("498", "权限不足");
            } else { // 正常用户正常查询
                wrapper.eq(Img::getUserId, userId);
                imgMapper.selectPage(page, wrapper);
                // 返回总数
                total = imgMapper.selectCount(wrapper);
            }
        } else { // root用户全部查询
            imgMapper.selectPage(page, wrapper); // 分页
            total = imgMapper.selectCount(wrapper); // 总数
        }

        for (Img img : page.getRecords()) {
            // 设置用户
            Integer id = img.getUserId();
            User user = userMapper.selectById(id);
            if (user == null) {
                user = new User();
                user.setName("用户已注销");
            }
            img.setUser(user);
        }

        map.put("imgs", page.getRecords());
        map.put("total", total);

        return Result.success(map);
    }

    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         @RequestParam("field") String field,
                         HttpServletRequest request) throws NoSuchAlgorithmException {
        // 获取用户名
        int userid = IdUtil.getId(request);

        // 上传到服务器
        FileUtil fileU = new FileUtil(uploadPath, downloadPath, imgMapper);
        Map<String, Object> map = fileU.uploadImg(file, userid, field);

        return Result.success(map);
    }

    @PostMapping("/uploads")
    public Result uploads(@RequestPart("files") MultipartFile[] files,
                          @RequestParam("field") String field,
                          HttpServletRequest request) throws NoSuchAlgorithmException {
        // 获取用户名
        int userid = IdUtil.getId(request);

        Map<String, Object> map = new HashMap<>();

        // 上传到服务器
        for (int i = 0; i < files.length; i++) {
            FileUtil u = new FileUtil(uploadPath, downloadPath, imgMapper);
            Map<String, Object> tempMap = u.uploadImg(files[i], userid, field);
            map.put(String.valueOf(i), tempMap);
        }

        return Result.success(map);
    }
}
