package cn.sdadgz.web_springboot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author eula
 * @since 2022-08-26
 */
@Getter
@Setter
@ApiModel(value = "Blog对象", description = "")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键自增
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String text;

    private Integer imgId;

    private String title;

    private String detail;

    private LocalDateTime createtime;

    // 用户
    @TableField(exist = false)
    private User user;

    // 首页图片
    @TableField(exist = false)
    private Img img;
}
