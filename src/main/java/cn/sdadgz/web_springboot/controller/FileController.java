package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.utils.FileUtil;
import cn.sdadgz.web_springboot.utils.IdUtil;
import cn.sdadgz.web_springboot.utils.SameCode.Page.Page;
import cn.sdadgz.web_springboot.utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.common.Result;
import cn.sdadgz.web_springboot.config.BusinessException;
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
        UserBan.getTheFuckOut(fileService.getFileById(file.getId()).getUserId(), request);

        int i = fileService.updateFileById(file);

        return Result.success(i);
    }

    // 删除
    @DeleteMapping
    public Result delete(@RequestParam("id") int id,
                         HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        // 获取请求中的用户id
        int userId = IdUtil.getUserId(request);
        File file = fileService.getFileById(id);

        // 阻止跨权限
        if (userId > 0 && userId != file.getUserId()) {
            throw new BusinessException("498", "权限不足");
        }

        // 删除
        Long aLong = fileService.virtualDelete(Collections.singletonList(file));
        map.put("id", aLong);

        // 哪个傻逼写的api，这么占用资源不怕宕机
//        // 获取同md5文件
//        List<File> files = fileService.getFilesByMd5(file.getMd5());
//        boolean realDelete = true;
//        for (File dFile : files) {
//            if (!dFile.getIsDelete()) {
//                realDelete = false;
//                break;
//            }
//        }
//
//        // 确实需要删除了
//        if (realDelete) {
//            String path = file.getUrl();
//            if (path.contains(downloadPath)) { // 是上传的图片，不是网图
//                path = path.substring(downloadPath.length());
//                java.io.File jFile = new java.io.File(uploadPath + path);
//                boolean delete = jFile.delete();
//                map.put("realDelete", delete);
//            }
//
//            // 数据库删除
//            for (File deleteFile : files) {
//                int deleteById = fileMapper.deleteById(deleteFile.getId());
//                map.put("deleteFile" + deleteFile.getId(), deleteById);
//            }
//        }

        return Result.success(map);
    }

    // 分页
    @GetMapping("/{username}/page")
    public Result page(@RequestParam("currentPage") int currentPage,
                       @RequestParam("pageSize") int pageSize,
                       @PathVariable("username") String username) {

        // 以前的屎山，他妈的哪个傻逼写的
//        Page<FileMapper, File> page = new Page<>();
//        Map<String, Object> map = page.getPage(currentPage, pageSize, userService.getUserIdByName(username), fileMapper);

        Map<String, Object> page = fileService.getPage(userService.getUserIdByName(username), currentPage, pageSize);

        return Result.success(page);
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
