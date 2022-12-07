package cn.sdadgz.web_springboot.entity;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import cn.sdadgz.web_springboot.utils.GeneralUtil;
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
        return Md5Util.md5(GeneralUtil.thisOrDefault(password, "这里写的东西，嗯，我说的是这个方法和这个字符串，" +
                "并没有什么用，这是真真正正的可以手欠删掉的东西，不像有些东西觉得没用删了之后直接报错，妈的更气人的是有的东西他当时不报" +
                "错，等过两天你无意间测试到这个api的时候妈的报错，离谱，找了一整天问题在哪里最后发现是自己几天前埋下的坑，这里替代了序" +
                "列化使用的password为虚拟的，假的，乱人耳目的东西，2022-12-07T12:54:23测试redis反序列化和序列化时发现得给他也赋" +
                "一个默认值，不然序列化的时候get他会出现MD5传个null过去妈的"));
    }

}
