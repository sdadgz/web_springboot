package cn.sdadgz.web_springboot.Utils;

import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
public class FileUtil {

    private String uploadPath;
    private String downloadPath;
    private ImgMapper imgMapper;
    private final String blogField = "博客内图片";

//    @Value("${my.file-config.uploadPath}")
//    private String uploadPath;
//
//    @Value("${my.file-config.downloadPath}")
//    private String downloadPath;

    // 构造器
    public FileUtil(String uploadPath, String downloadPath, ImgMapper imgMapper) {
        this.uploadPath = uploadPath;
        this.downloadPath = downloadPath;
        this.imgMapper = imgMapper;
    }

    // 笔记上传
    public Blog mdUpload(MultipartFile file, String title, HttpServletRequest request,
                         BlogMapper blogMapper, int imgId, String detail) throws IOException {
        // 文件原始名
        String originalFilename = file.getOriginalFilename();

        // 没标题就用文件名
        if (title.length() < 1) {
            assert originalFilename != null;
            title = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        }

        // 获取用户id
        int userid = IdUtil.getId(request);

        // 防止重复标题
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("user_id", userid);
        List<Blog> blogs = blogMapper.selectList(wrapper);
        if (blogs.size() > 0) {
            throw new BusinessException("465", "重复的标题");
        }

        // 初始化
        FileUtil fileUtil = new FileUtil(uploadPath, downloadPath, null);

        // 获取创建时间和处理后的博客内容
//        LocalDateTime createTime = fileUtil.getCreateTime(file); // 被阉割
        LocalDateTime createTime = TimeUtil.now();
//        String text = fileUtil.md((File) file);
        String path = uploadPath + "blog/temp.md";
        fileUtil.uploadToServer(file, path);
        File jFile = new File(path);
        String text = fileUtil.md(jFile);

        Blog blog = new Blog();
        blog.setUserId(userid);
        blog.setText(text);
        blog.setImgId(imgId);
        blog.setTitle(title);
        blog.setDetail(detail);
        blog.setCreatetime(createTime);
        blogMapper.insert(blog);

        return blog;
    }

    // 笔记转字符
    public String md(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String line;
        while ((line = br.readLine()) != null) { // 将图片地址修改
            if (line.startsWith("![")) {
                boolean isImg = true;
                int index = 2;
                String[] exists = {"](", ")"};
                for (String c : exists) {
                    index = line.indexOf(c, index);
                    if (index == -1) {
                        isImg = false;
                        break;
                    }
                }
                if (isImg) { // 是图片
                    if (!(line.contains("(http://") || line.contains("(https://"))) { // 都不包括链接
                        String start = line.substring(0, line.indexOf('(')); // 去掉右边括号
                        String originName = line.substring(line.lastIndexOf('\\') + 1, line.length() - 1);
                        line = start + '(' + downloadPath + "blog/" + originName + ')';
                    }
                }
            }
            // 添加到sb
            sb.append(line).append('\n');
        }
        br.close();
        return sb.toString();
    }

    // 获取文件创建时间
    public LocalDateTime getCreateTime(MultipartFile file) {
        String path = uploadPath + "temp/temp.md";
        uploadToServer(file, path);
        Date date = getCreateTime(path);
        return TimeUtil.translate(date);
    }

    // 获取文件创建时间
    private Date getCreateTime(String fullFileName) {
        Path path = Paths.get(fullFileName);
        BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr;
        try {
            attr = basicview.readAttributes();
            return new Date(attr.creationTime().toMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
        return cal.getTime();
    }

    // 上传到服务器
    public Map<String, Object> uploadImg(MultipartFile file, int userid, String field) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();

        // 上传到数据库对象
        Img img = new Img();
        img.setCreatetime(TimeUtil.now());
        img.setUserId(userid);
        img.setField(field);

        // 基本属性
        String uuid = IdUtil.uuid();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileName = getType(originalFilename); // 结尾加上类型
        String path;
        String url;
        // blog不增加uuid
        if (field.equals(blogField)) {
            path = uploadPath + "blog/" + originalFilename;
            url = downloadPath + "blog/" + originalFilename;
        } else {
            path = uploadPath + uuid + fileName;
            url = downloadPath + uuid + fileName;
        }

        // 上传到服务器
        uploadToServer(file, path);
        File jFile = new File(path);
        // 追加md5
        String md5 = Md5Util.md5(jFile);

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
    public void uploadToServer(MultipartFile file, String path) {
        if (!file.isEmpty()) {
            try {
                file.transferTo(new File(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 根据文件名获取文件类型
    private String getType(String fileName) {
        if (!fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}
