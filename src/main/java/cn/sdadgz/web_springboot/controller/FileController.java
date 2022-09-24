package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.Utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sdadgz
 * @since 2022-09-23
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileMapper fileMapper;

    // 删除
    @DeleteMapping("")
    public Result delete(@RequestParam("id") int id,
                         HttpServletRequest request) {

        return Result.success();
    }

    // 分页
    @GetMapping("/{username}/page")
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       HttpServletRequest request,
                       @PathVariable("username") String username) {

        UserBan.getTheFuckOut(username, request);

        Page<FileMapper, File> page = new Page<>();
        Map<String, Object> map = page.getPage(currentPage, pageSize, request, fileMapper);

        return Result.success(map);
    }

    // 上传文件
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         HttpServletRequest request) {

        FileUtil fileUtil = new FileUtil();
        Map<String, Object> map = fileUtil.fileUpload(file, request);

        return Result.success(map);
    }

}
