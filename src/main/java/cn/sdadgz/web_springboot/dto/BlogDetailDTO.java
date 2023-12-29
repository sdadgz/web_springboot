package cn.sdadgz.web_springboot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 博客详情
 *
 * <p>
 * 废物本物
 * </p>
 *
 * @author sdadgz
 * @since 2023/12/29 12:43:57
 */
@Getter
@Setter
@Builder
@Accessors(chain = true)
public class BlogDetailDTO {

    private String text;

    private String imgUrl;

    private String title;

}
