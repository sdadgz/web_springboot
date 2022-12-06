package cn.sdadgz.web_springboot.utils;

import cn.sdadgz.web_springboot.config.BusinessException;
import cn.sdadgz.web_springboot.config.DangerousException;
import cn.sdadgz.web_springboot.entity.Blog;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.BlogMapper;
import cn.sdadgz.web_springboot.mapper.FileMapper;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import cn.sdadgz.web_springboot.service.IBlogService;
import cn.sdadgz.web_springboot.service.IFileService;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class FileUtil {
    private static FileUtil fileUtil;

    @Value("${my.file-config.uploadPath}")
    private String uploadPath;

    @Value("${my.file-config.downloadPath}")
    private String downloadPath;

    @Resource
    private ImgMapper imgMapper;

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private IBlogService blogService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private IFileService fileService;

    @Resource
    private IImgService imgService;

    private static final String BLOG_DIR = "blog";

    private static final int SCREEN_WIDTH = 1920; // 屏幕宽度
    private static final int BLOG_COLUMNS = 2; // 博客分几栏

    private final String[] SUPPORT_TYPE = {".jpg", ".png", ".jpeg"}; // 支持压缩的类型

    private final String BLOG_FIELD = "博客内图片";
    private final String BLOG_BANNER = "博客首页";

    @PostConstruct
    public void init() {
        fileUtil = this;
    }

    // 文件上传
    public Map<String, Object> fileUpload(MultipartFile file, HttpServletRequest request) {
        // 初始化
        HashMap<String, Object> map = new HashMap<>();
        cn.sdadgz.web_springboot.entity.File uploadFile = new cn.sdadgz.web_springboot.entity.File();
        uploadFile.setCreateTime(TimeUtil.now());

        // 获取用户
        int userId = IdUtil.getUserId(request);
        User user = fileUtil.userService.getUserById(userId);
        uploadFile.setUserId(userId);

        // 去重复文件名
        String originalFilename = user.getName() + StrUtil.UNDERSCORE + file.getOriginalFilename();
        while (filenameExists(originalFilename)) {
            // 获取类型
            String type = getType(originalFilename);
            // 除类型以外的
            originalFilename = getOriginalFilename(originalFilename) + StrUtil.COPY + type;
        }
        uploadFile.setOriginalFilename(originalFilename);

        // 上传
        String url = fileUtil.downloadPath + StrUtil.REPOSITORY + StrUtil.LEVER + originalFilename;
        String path = fileUtil.uploadPath + StrUtil.REPOSITORY + StrUtil.LEVER + originalFilename;
        uploadToServer(file, path);
        uploadFile.setUrl(url);

        // md5
        File jFile = new File(path);
        String md5 = Md5Util.md5(jFile);
        uploadFile.setMd5(md5);

        // 去重
        cn.sdadgz.web_springboot.entity.File exists = fileExists(md5);
        if (exists != null) {
            // 重复了，都多余了，爱谁谁
            uploadFile.setUrl(exists.getUrl());
            // 删除文件
            if (jFile.delete()) {
                map.put("fileInfo", "重复文件，删除了，用旧的");
            } else {
                throw new DangerousException("557", "上传文件异常", IdUtil.getIp(request), userId);
            }
        }

        // 数据库
        int i = fileUtil.fileService.addFile(uploadFile);
        log.info("file新增{}条数据", i);

        // 返回id
        map.put("id", uploadFile.getId());
        map.put("add", i);

        return map;
    }

    // 笔记上传
    public Blog mdUpload(MultipartFile file,
                         String title,
                         HttpServletRequest request,
                         int imgId,
                         String detail,
                         LocalDateTime createTime) throws IOException {

        // 初始化
        Blog blog = new Blog();

        // 创建时间
        if (createTime == null) {
            createTime = TimeUtil.now();
        }

        // 文件原始名
        String originalFilename = file.getOriginalFilename();

        // 没标题就用文件名
        if (title.length() < 1) {
            assert originalFilename != null;
            title = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        }

        // 获取用户id和名字
        int userid = IdUtil.getUserId(request);
        String username = IdUtil.getUsername(request);

        // 防止重复标题
        Blog blogs = fileUtil.blogService.getBlogByUsernameAndTitle(username, title);
        boolean save = blogs == null;

        // text更新
        String text = fileToString(file);
        blog.setText(text);

        if (save) {
            // 处理后的博客内容
            blog.setTitle(title);
            blog.setDetail(detail);
            blog.setUserId(userid);
            blog.setImgId(imgId);
            blog.setCreateTime(createTime);

            // 新增
            fileUtil.blogService.addBlog(blog);
        } else {
            blog.setId(blogs.getId());
            fileUtil.blogService.updateBlogById(blog);
        }

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
                        line = start + '(' + fileUtil.downloadPath + "blog/" + originName + ')';
                    }
                }
            }
            // 添加到sb
            sb.append(line).append('\n');
        }
        br.close();
        return sb.toString();
    }

    // 上传到服务器
    public Map<String, Object> uploadImg(MultipartFile file, int userid, String field) throws NoSuchAlgorithmException, IOException {
        Map<String, Object> map = new HashMap<>();

        // 上传到数据库对象
        Img img = new Img();
        img.setCreateTime(TimeUtil.now());
        img.setUserId(userid);
        img.setField(field);

        // 基本属性
        String uuid = IdUtil.uuid();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String type = getType(originalFilename); // 结尾加上类型
        String path;
        String url;
        String reduceUrl;
        // blog不增加uuid
        if (field.equals(fileUtil.BLOG_FIELD)) {
            path = fileUtil.uploadPath + "blog/" + originalFilename;
            url = fileUtil.downloadPath + "blog/" + originalFilename;
        } else {
            path = fileUtil.uploadPath + uuid + type;
            url = fileUtil.downloadPath + uuid + type;
        }

        // 上传到服务器
        uploadToServer(file, path);
        File jFile = new File(path);
        // 追加md5
        String md5 = Md5Util.md5(jFile);

        // 文件去重
        Img md5Exists = imgExists(md5);
        if (md5Exists != null) {
            // 重复图片
            url = md5Exists.getUrl();
            reduceUrl = md5Exists.getReduceUrl();
            if (jFile.delete()) {
                map.put("fileInfo", "重复文件，删除了，用旧的");
            } else {
                log.error("文件{}删除失败", path);
                throw new BusinessException("557", "上传文件异常");
            }
        } else {
            // 设置浓缩图
            reduceUrl = setReduceImg(jFile);
        }

        // 上传到数据库
        img.setMd5(md5);
        img.setUrl(url);
        img.setReduceUrl(reduceUrl);
        fileUtil.imgService.addImg(img);

        // 返回图片id
        map.put("id", img.getId());

        return map;
    }

    // 设置浓缩图 返回url
    public String setReduceImg(File file) throws IOException {

        // 浓缩图路径
        String reducePath = file.getPath() + ".jpg";

        // 获取类型
        String name = reducePath.substring(fileUtil.uploadPath.length());
        String fileName = file.getName();
        String type = getType(fileName);
        if (containsType(type)) {
            // 图片高度
            BufferedImage image = ImageIO.read(file);
            int height = image.getHeight();

            // 压缩
            Thumbnails.of(file).size(SCREEN_WIDTH / BLOG_COLUMNS, height).toFile(reducePath);

            // 返回路径
            return fileUtil.downloadPath + name;
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

    // 根据url删除本地文件
    public void deleteByUrl(String... url) {
        for (String s : url) {
            String path = urlToPath(s);
            File file = new File(path);
            boolean delete = file.delete();
            if (delete) {
                log.info("删除文件：\"{}\"成功", path);
            } else {
                log.error("删除文件：\"{}\"失败", path);
            }
        }
    }

    // url转path
    private String urlToPath(String url) {
        if (url.startsWith(fileUtil.downloadPath)) {
            url = fileUtil.uploadPath + url.substring(fileUtil.downloadPath.length());
        }
        return url;
    }

    // multipartFile -> 数据库blog中text字段
    private String fileToString(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String path = fileUtil.uploadPath + BLOG_DIR + StrUtil.LEVER + originalFilename;
        uploadToServer(file, path);
        File jFile = new File(path);
        String text = md(jFile);
        if (!jFile.delete()) {
            throw new BusinessException("588", "文件删除失败");
        }
        return text;
    }

    // 文件名已经存在
    private boolean filenameExists(String filename) {
        return fileUtil.fileService.getFileByFilename(filename) != null;
    }

    // 包含可处理图片
    private boolean containsType(String type) {
        for (String s : SUPPORT_TYPE) {
            if (type.equals(s)) {
                return true;
            }
        }
        return false;
    }

    // md5是否已经存在
    private Img imgExists(String md5) {
        List<Img> files = fileUtil.imgService.getImgsByMD5(md5);
        if (files.size() > 0) {
            return files.get(0);
        }
        return null;
    }

    // md5是否已经存在
    private cn.sdadgz.web_springboot.entity.File fileExists(String md5) {
        List<cn.sdadgz.web_springboot.entity.File> files = fileUtil.fileService.getFilesByMd5(md5);
        if (files.size() > 0) {
            return files.get(0);
        }
        return null;
    }

    // 根据文件名获取文件类型
    private String getType(String fileName) {
        if (!fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    // 获取文件名
    private String getOriginalFilename(String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
