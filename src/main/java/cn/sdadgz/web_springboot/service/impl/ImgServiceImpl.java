package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        if (imgs.size() < 1) {
            log.info("imgService虚拟删除为空");
            return 0L;
        }
        return imgMapper.virtualDeleteBatch(imgs);
    }

    @Override
    public void realDeleteBatch() {
        // 获取需要被删除的md5值及url
        FileUtil fileUtil = new FileUtil();
        List<Img> deleteImgs = imgMapper.getDeleteImgs();
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        long res = 0;

        for (Img deleteImg : deleteImgs) {
            // 物理删除
            fileUtil.deleteByUrl(deleteImg.getUrl(), deleteImg.getReduceUrl());
            // 数据库删除
            wrapper.eq(Img::getMd5, deleteImg.getMd5());
            res += imgMapper.delete(wrapper);
        }

        log.info("从img数据库删除了{}条信息", res);
    }

}
