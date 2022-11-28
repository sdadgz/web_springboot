package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author eula
 * @since 2022-08-27
 */
@Service
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements IImgService {

    @Resource
    private IUserService userService;

    @Resource
    private ImgMapper imgMapper;

    @Override
    public List<Img> getImgs(String field, String username) {
        // 根据username获取userId
        int userId = userService.getUserIdByName(username);

        // 不拦截，共享单车
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Img::getField, field);
        wrapper.eq(Img::getUserId, userId);

        return imgMapper.selectList(wrapper);
    }

    @Override
    public List<Img> getNeverUseImg(String field, Integer userId) {
        return imgMapper.getNeverUseImgs(field, userId);
    }

    @Override
    public List<Img> getGC() {
        return imgMapper.getGC();
    }

    @Override
    public Long virtualDeleteBatch(List<Img> imgs) {
        return imgMapper.virtualDeleteBatch(imgs);
    }

    @Override
    public Long realDeleteBatch() {
        // TODO 获取需要被删除的md5值及url
        List<Img> deleteImgs = imgMapper.getDeleteImgs();
        return null;
    }
}
