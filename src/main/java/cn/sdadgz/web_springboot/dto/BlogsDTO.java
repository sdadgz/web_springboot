package cn.sdadgz.web_springboot.dto;

import cn.sdadgz.web_springboot.entity.Blog;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 博客列表dto
 *
 * <p>
 * 废物本物
 * </p>
 *
 * @author sdadgz
 * @since 2023/12/29 12:57:07
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class BlogsDTO {

    private String imgUrl;

    private String imgReduceUrl;

    private String userAvatar;

    private String title;

    private String detail;

    private String userName;

    private LocalDateTime createTime;

    private Integer imgId;

    public static BlogsDTO of(Blog blog) {
        return builder()
                .imgUrl(blog.getImg().getUrl())
                .imgReduceUrl(blog.getImg().getReduceUrl())
                .userAvatar(blog.getUser().getAvatar())
                .title(blog.getTitle())
                .detail(blog.getDetail())
                .userName(blog.getUser().getName())
                .createTime(blog.getCreateTime())
                .imgId(blog.getImgId())
                .build();
    }
}
