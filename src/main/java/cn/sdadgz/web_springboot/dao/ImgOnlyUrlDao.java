package cn.sdadgz.web_springboot.dao;

import cn.sdadgz.web_springboot.entity.Img;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 只使用url的图片
 */
@Getter
@Setter
@Accessors(chain = true)
public class ImgOnlyUrlDao {

    private String url;

    // 转换到这个类
    public static List<ImgOnlyUrlDao> toThis(List<? extends Img> imgs) {
        List<ImgOnlyUrlDao> res = new ArrayList<>();
        imgs.forEach(img -> res.add(new ImgOnlyUrlDao().setUrl(img.getUrl())));
        return res;
    }

}
