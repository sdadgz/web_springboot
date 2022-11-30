package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.dao.ImgUpdateDao;
import cn.sdadgz.web_springboot.utils.FileUtil;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IImgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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

    @Resource
    private IImgService imgService;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    private final String banner = "首页横幅";

    private final String background = "全局背景图片";

    // 获取首页横幅
    @GetMapping("/{username}/banner")
    public Result banner(@PathVariable String username) {

        List<Img> imgs = imgService.getImgs(banner, username);

        return Result.success(imgs);
    }

    // 获取背景图片
    @GetMapping("/{username}/background")
    public Result background(@PathVariable String username) {

        List<Img> imgs = imgService.getImgs(background, username);

        return Result.success(imgs);
    }

    // 修改博客
    @PutMapping("/update")
    public Result update(@RequestBody ImgUpdateDao paramMap,
                         HttpServletRequest request) {

        // 返回集
        Map<String, Object> map = new HashMap<>();

        // 获取需要修改的数据
        String field = paramMap.getField();
        List<Integer> idList = paramMap.getIdList();

        // 使用多个参数
        for (Integer i : idList) {
            map.put("multiple:" + i, updateF(i, field, request));
        }

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

        Page<ImgMapper, Img> page = new Page<>();
        Map<String, Object> map = page.getPage(currentPage, pageSize, request, imgMapper);

        return Result.success(map);
    }

    // 删除
    @DeleteMapping
    public Result delete(@RequestBody Map<String, Integer> map,
                         HttpServletRequest request) {

        // 返回集
        Map<String, Object> resultMap = new HashMap<>();

        // 获取图片id
        int id = map.get("id");

        // 获取用户id
        int userId = IdUtil.getUserId(request);

        // 获取图片
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getId, id);
        Img img = imgMapper.selectById(id);

        // 不是海克斯科技用户还想删别人东西
        if (img.getUserId() != userId && userId > 0) {
            throw new BusinessException("498", "权限不足");
        }

        // 虚拟删除
        img.setIsDelete(true);
        int i = imgMapper.updateById(img);

        resultMap.put("mapperDelete", i);

        return Result.success(resultMap);
    }

    // 上传
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         @RequestParam("field") String field,
                         HttpServletRequest request) throws NoSuchAlgorithmException, IOException {
        // 获取用户名
        int userid = IdUtil.getUserId(request);

        // 上传到服务器
        FileUtil fileU = new FileUtil();
        Map<String, Object> map = fileU.uploadImg(file, userid, field);

        return Result.success(map);
    }

    // 批量上传
    @PostMapping("/uploads")
    public Result uploads(@RequestPart("files") MultipartFile[] files,
                          @RequestParam("field") String field,
                          HttpServletRequest request) throws NoSuchAlgorithmException, IOException {
        // 获取用户名
        int userid = IdUtil.getUserId(request);

        Map<String, Object> map = new HashMap<>();

        // 上传到服务器
        for (int i = 0; i < files.length; i++) {
            FileUtil u = new FileUtil();
            Map<String, Object> tempMap = u.uploadImg(files[i], userid, field);
            map.put(String.valueOf(i), tempMap);
        }

        return Result.success(map);
    }

    // 带验证的修改
    private Map<String, Object> updateF(int id, String field, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        // 获取用户id
        int userId = IdUtil.getUserId(request);

        // 获取图片信息并验证
        Img img = imgMapper.selectById(id);
        if (userId > 0) { // 正常用户
            if (img.getUserId() != userId) { // 尝试跨权限
                // 拒绝他
                throw new BusinessException("498", "权限不足");
            }
        }

        if (img == null) {
            throw new BusinessException("433", "图片id不存在");
        }
        img.setField(field);
        int i = imgMapper.updateById(img);
        map.put("id:" + id, i);

        return map;
    }

}
