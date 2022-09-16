package cn.sdadgz.web_springboot.Utils.SameCode.Img;

import cn.sdadgz.web_springboot.Utils.SameCode.User.UserBan;
import cn.sdadgz.web_springboot.Utils.SameCode.User.UserSame;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ImgSame {

    private static ImgSame imgSame;

    @Resource
    private ImgMapper imgMapper;

    @PostConstruct
    public void init(){
        imgSame = this;
    }

    public static List<Img> getImgs(String field, String username, HttpServletRequest request){
        // 拦截
//        new UserBan().getTheFuckOut(username, request);

        // 获取用户id
        int userId = UserSame.getUserIdByName(username);

        // 不拦截，共享单车
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getField, field);
        wrapper.eq(Img::getUserId, userId);

        return imgSame.imgMapper.selectList(wrapper);
    }
}
