package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.Utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

        Page<ImgMapper, Img> page = new Page<>();
        Map<String, Object> map = page.getPage(currentPage, pageSize, request, imgMapper);

        return Result.success(map);
    }

    // 删除
    @DeleteMapping("")
    public Result delete(@RequestBody Map<String, Object> map,
                         HttpServletRequest request) {

        // 返回集
        Map<String, Object> resultMap = new HashMap<>();

        // 获取图片id
        int id = (int) map.get("id");

        // 获取用户id
        int userId = IdUtil.getId(request);

        // 获取图片
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getId, id);
        Img img = imgMapper.selectById(id);

        if (img.getUserId() != userId && userId > 0) { // 不是海克斯科技用户还想删别人东西
            throw new BusinessException("498", "权限不足");
        }

        // 虚拟删除
        img.setIsDelete(true);
        int i = imgMapper.updateById(img);

        resultMap.put("mapperDelete", i);

        // 真实删除
        String md5 = img.getMd5();
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getMd5, md5);
        List<Img> imgs = imgMapper.selectList(wrapper);
        boolean readDelete = true;
        for (Img img1 : imgs) {
            if (!img1.getIsDelete()) {
                readDelete = false;
                break;
            }
        }
        // 确实需要删除了
        if (readDelete) {
            String path = img.getUrl();
            if (path.contains(downloadPath)) { // 是上传的图片，不是网图
                path = path.substring(downloadPath.length());
            }
            File file = new File(uploadPath + path);
            boolean delete = file.delete();
            resultMap.put("realDelete", delete);

            // 数据库删除
            for (Img img1 : imgs) {
                int deleteById = imgMapper.deleteById(img1.getId());
                resultMap.put("deleteSqlItem" + img1.getId(), deleteById);
            }
        }
        return Result.success(resultMap);
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
