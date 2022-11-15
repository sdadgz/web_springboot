package cn.sdadgz.web_springboot.entity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import cn.sdadgz.web_springboot.utils.Md5Util;
import cn.sdadgz.web_springboot.utils.RandomUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author eula
 * @since 2022-08-25
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键自增
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String password;

    private String avatar;

    private LocalDateTime createTime;

    // 露马脚，虚假的密码
    @JsonProperty("password")
    public String getInterferencePassword() throws NoSuchAlgorithmException {
        return Md5Util.md5(password);
    }

}
