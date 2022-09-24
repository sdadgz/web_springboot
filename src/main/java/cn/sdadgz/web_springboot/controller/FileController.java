package cn.sdadgz.web_springboot.controller;

import cn.sdadgz.web_springboot.Utils.FileUtil;
import cn.sdadgz.web_springboot.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    // 上传文件
    @PostMapping("/upload")
    public Result upload(@RequestPart("file") MultipartFile file,
                         HttpServletRequest request) {

        FileUtil fileUtil = new FileUtil();
        Map<String, Object> map = fileUtil.fileUpload(file, request);

        return Result.success(map);
    }

}
