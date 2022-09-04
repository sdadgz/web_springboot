package cn.sdadgz.web_springboot.Utils;

import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.javaws.util.JfxHelper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileU {

    private ImgMapper imgMapper;
    private String uploadPath;
    private String downloadPath;

    public FileU(ImgMapper imgMapper, String uploadPath, String downloadPath) {
        this.imgMapper = imgMapper;
        this.uploadPath = uploadPath;
        this.downloadPath = downloadPath;
    }

    // 上传到服务器
    public Map<String, Object> uploadF(MultipartFile file, int userid, String field) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();

        // 上传到数据库对象
        Img img = new Img();
        img.setCreatetime(TimeU.now());
        img.setUserId(userid);

        // 基本属性
        String uuid = IdU.uuid();
        String fileName = file.getOriginalFilename();
        String path;
        String url;
        // blog不增加uuid
        if (field.equals("blog")) {
            path = uploadPath + "blog/" + fileName;
            url = downloadPath + "blog/" + fileName;
        } else {
            path = uploadPath + uuid + fileName;
            url = downloadPath + uuid + fileName;
        }

        // 上传到服务器
        uploadToServer(file, path);
        File jFile = new File(path);
        // 追加md5
        String md5 = Md5U.md5(jFile);

        // 文件去重
        String oldUrl = md5Exists(md5);
        if (oldUrl != null) { // 重复了
            url = oldUrl;
            if (jFile.delete()) {
                map.put("file", "重复文件，删除了，用旧的");
            } else {
                throw new BusinessException("457", "上传文件异常");
            }
        }

        // 上传到数据库
        img.setMd5(md5);
        img.setUrl(url);
        imgMapper.insert(img);

        // 返回图片id
        map.put("id", img.getId());

        return map;
    }

    // md5是否已经存在
    private String md5Exists(String md5) {
        QueryWrapper<Img> wrapper = new QueryWrapper<>();
        wrapper.eq("md5", md5);
        List<Img> files = imgMapper.selectList(wrapper);
        if (files.size() > 0) {
            return files.get(0).getUrl();
        }
        return null;
    }

    // 上传到服务器
    private void uploadToServer(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            try {
                file.transferTo(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
