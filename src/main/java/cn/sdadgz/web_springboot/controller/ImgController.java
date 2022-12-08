package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.dao.ImgOnlyUrlDao;
import cn.sdadgz.web_springboot.dao.ImgUpdateDao;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import cn.sdadgz.web_springboot.utils.GeneralUtil;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IImgService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private IUserService userService;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    private static final String BANNER = "首页横幅";
    private static final String BACKGROUND = "全局背景图片";

    // 默认banner数量
    public static final int DEFAULT_BANNER_COUNT = 6;
    // 默认background数量
    public static final int DEFAULT_BACKGROUND_COUNT = 6;

    /**
     * 获取首页横幅，随机
     *
     * @param username 用户名
     * @param count    获取几张图片，可选
     * @return 图片数组 ImgOnlyUrlDao
     */
    @GetMapping("/{username}/banner")
    public Result banner(@PathVariable String username,
                         @RequestParam(value = "count", required = false) Integer count) {

        List<Img> imgs = imgService.getImgsRandByFieldAndUsername(BANNER, username, GeneralUtil.thisOrDefault(count, DEFAULT_BANNER_COUNT));

        // 转传输格式
        List<ImgOnlyUrlDao> res = ImgOnlyUrlDao.toThis(imgs);

        return Result.success(res);
    }

    /**
     * 获取背景图片，随机
     *
     * @param username 用户名
     * @param count    获取几张图片，可选
     * @return 图片数组 ImgOnlyUrlDao
     */
    @GetMapping("/{username}/background")
    public Result background(@PathVariable String username,
                             @RequestParam(value = "count", required = false) Integer count) {

        List<Img> imgs = imgService.getImgsRandByFieldAndUsername(BACKGROUND, username, GeneralUtil.thisOrDefault(count, DEFAULT_BACKGROUND_COUNT));

        // 转传输格式
        List<ImgOnlyUrlDao> res = ImgOnlyUrlDao.toThis(imgs);

        return Result.success(res);
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

        Map<String, Object> page = imgService.getPage(userService.getUserIdByName(username), currentPage, pageSize);

        return Result.success(page);
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
        Img img = imgService.getImgById(id);

        // 不是海克斯科技用户还想删别人东西
        if (img.getUserId() != userId && userId > 0) {
            throw new BusinessException("498", "权限不足");
        }

        // 虚拟删除
        Long aLong = imgService.virtualDeleteBatch(Collections.singletonList(img));

        resultMap.put("updateCount", aLong);

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
    private Map<String, Object> updateF(int imgId, String field, HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        // 获取用户id
        int userId = IdUtil.getUserId(request);

        // 获取图片信息并验证
        Img img = imgService.getImgById(imgId);
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
        int i = imgService.updateImgById(img);
        map.put("imgId:" + imgId, i);

        return map;
    }

}
