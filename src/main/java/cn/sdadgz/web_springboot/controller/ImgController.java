package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileU;
import cn.sdadgz.web_springboot.Utils.JwtU;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    @PostMapping("/upload")
    public Result upload(@RequestPart("files") MultipartFile files,
                         HttpServletRequest request) throws NoSuchAlgorithmException {

        return upload(files, "undefined", request);
    }

    @PostMapping("/upload")
    public Result upload(@RequestPart("files") MultipartFile files,
                         @RequestParam("field") String field,
                         HttpServletRequest request) throws NoSuchAlgorithmException {
        // 获取用户名
        String token = request.getHeader("token");
        int userid = Integer.parseInt(JwtU.getAudience(token));

        // 上传到服务器
        FileU fileU = new FileU(imgMapper, uploadPath, downloadPath);
        Map<String, Object> map = fileU.uploadF(files, userid, field);

        return Result.success(map);
    }

    @PostMapping("/uploads")
    public Result uploads(@RequestPart("files") MultipartFile[] files,
                          @RequestParam("field") String field,
                          HttpServletRequest request) throws NoSuchAlgorithmException {
        // 获取用户名
        String token = request.getHeader("token");
        int userid = Integer.parseInt(JwtU.getAudience(token));

        Map<String, Object> map = new HashMap<>();

        // 上传到服务器
        for (int i = 0; i < files.length; i++) {
            FileU u = new FileU(imgMapper, uploadPath, downloadPath);
            Map<String, Object> tempMap = u.uploadF(files[i], userid, field);
            map.put(String.valueOf(i), tempMap);
        }

        return Result.success(map);
    }

    @PostMapping("/uploads")
    public Result uploads(@RequestPart("files") MultipartFile[] files,
                          HttpServletRequest request) throws NoSuchAlgorithmException {

        return uploads(files, "undefined", request);
    }

}
