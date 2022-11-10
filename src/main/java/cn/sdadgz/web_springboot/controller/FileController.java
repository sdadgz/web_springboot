package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.Utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.Utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.config.DangerousException;
import cn.sdadgz.web_springboot.entity.File;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Resource
    private IFileService fileService;

    @Resource
    private IUserService userService;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    // 修改
    @PutMapping("/update")
    public Result update(@RequestBody File file,
                         HttpServletRequest request) {

        // 遣返
        UserBan.getTheFuckOut(fileMapper.selectById(file.getId()).getUserId(), request);

        int i = fileMapper.updateById(file);

        return Result.success(i);
    }

    // 删除
    @DeleteMapping
    public Result delete(@RequestParam("id") int id,
                         HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        // 获取请求中的用户id
        int userId = IdUtil.getId(request);
        File file = fileMapper.selectById(id);

        // 阻止跨权限
        if (userId > 0 && userId != file.getUserId()) {
            throw new DangerousException("498", "权限不足",request, userId);
        }

        // 删除
        file.setIsDelete(true);
        int i = fileMapper.updateById(file);
        map.put("id", i);

        // 获取同md5文件
        List<File> files = fileService.getFilesByMd5(file.getMd5());
        boolean realDelete = true;
        for (File dFile : files) {
            if (!dFile.getIsDelete()) {
                realDelete = false;
                break;
            }
        }

        // 确实需要删除了
        if (realDelete) {
            String path = file.getUrl();
            if (path.contains(downloadPath)) { // 是上传的图片，不是网图
                path = path.substring(downloadPath.length());
                java.io.File jFile = new java.io.File(uploadPath + path);
                boolean delete = jFile.delete();
                map.put("realDelete", delete);
            }

            // 数据库删除
            for (File deleteFile : files) {
                int deleteById = fileMapper.deleteById(deleteFile.getId());
                map.put("deleteFile" + deleteFile.getId(), deleteById);
            }
        }

        return Result.success(map);
    }

    // 分页
    @GetMapping("/{username}/page")
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       HttpServletRequest request,
                       @PathVariable("username") String username) {

        Page<FileMapper, File> page = new Page<>();
        Map<String, Object> map = page.getPage(currentPage, pageSize, userService.getUserIdByName(username), fileMapper);

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
