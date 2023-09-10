package cn.sdadgz.web_springboot.entity;

import cn.sdadgz.web_springboot.utils.WebUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
@Getter
@Setter
@ApiModel(value = "Img对象", description = "")
@Accessors(chain = true)
public class Img implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String url;

    @JsonIgnore
    private String md5;

    private Boolean isDelete;

    private LocalDateTime createTime;

    private String field;

    private Integer userId;

    private String reduceUrl;

    @TableField(exist = false)
    private User user;


//    /**
//     * 设置url时转义百分号
//     *
//     * @param url url
//     */
//    public void setUrl(String url) {
//        // url需要转义
//        this.url = WebUtil.encodeURL(url);
//    }


//    /**
//     * 设置url时转义百分号
//     *
//     * @param reduceUrl 浓缩图地址
//     */
//    public void setReduceUrl(String reduceUrl) {
//        // url需要转义
//        this.reduceUrl = WebUtil.encodeURL(reduceUrl);
//    }
}
