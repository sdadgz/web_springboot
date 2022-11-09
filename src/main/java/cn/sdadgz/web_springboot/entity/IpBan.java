package cn.sdadgz.web_springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author sdadgz
 * @since 2022-11-09
 */
@Getter
@Setter
@TableName("ip_ban")
@ApiModel(value = "IpBan对象", description = "")
public class IpBan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ip;

    private Integer userId;

    private String msg;

    private LocalDateTime createTime;
}
