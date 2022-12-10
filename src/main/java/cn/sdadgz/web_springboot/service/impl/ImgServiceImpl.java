package cn.sdadgz.web_springboot.service.impl;

import cn.sdadgz.web_springboot.config.UnificationConfig;
import cn.sdadgz.web_springboot.entity.Img;
import cn.sdadgz.web_springboot.mapper.ImgMapper;
import cn.sdadgz.web_springboot.service.IImgService;
import cn.sdadgz.web_springboot.service.IUserService;
import cn.sdadgz.web_springboot.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@CacheConfig(cacheNames = "imgCache")
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements IImgService {

    @Resource
    private IUserService userService;

    @Resource
    private ImgMapper imgMapper;

    @Resource
    private IImgService imgService;

    @Resource
    public UnificationConfig unificationConfig;

    // 新增图片
    @Override
    @CacheEvict(allEntries = true)
    public void addImg(Img img) {
        int insert = imgMapper.insert(img);
        log.info("img新增：{} 条数据", insert);
    }

    // 分页
    @Override
    @Cacheable(unless = "#result.get(#root.target.unificationConfig.getResponseLists()).size() == 0")
    public Map<String, Object> getPage(Integer userId, Integer currentPage, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        int startPage = (currentPage - 1) * pageSize;

        List<Img> page = imgMapper.getPage(userId, startPage, pageSize);
        Long totalByUserId = imgService.getTotalByUserId(userId);

        map.put(unificationConfig.getResponseLists(), page);
        map.put(unificationConfig.getResponseTotal(), totalByUserId);

        return map;
    }

    // 用户总图片数
    @Override
    @Cacheable(unless = "#result == 0L")
    public Long getTotalByUserId(Integer userId) {
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getUserId, userId);
        return imgMapper.selectCount(wrapper);
    }

    // username, field -> imgs
    @Override
    @Cacheable(unless = "#result.size() == 0")
    public List<Img> getImgsByFieldAndUsername(String field, String username) {
        // username -> userId
        int userId = userService.getUserIdByName(username);

        // 不拦截，共享单车
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Img::getField, field);
        wrapper.eq(Img::getUserId, userId);

        return imgMapper.selectList(wrapper);
    }

    // 随机 field 图片
    @Override
    @Cacheable(unless = "#result.size() == 0")
    public List<Img> getImgsRandByFieldAndUsername(String field, String username, Integer count) {
        // username -> userId
        int userId = userService.getUserIdByName(username);
        return imgMapper.getImgsRandByFieldAndUserId(field, userId, count);
    }

    // 获取未使用的图片
    @Override
    public List<Img> getNeverUseImgs(String field, Integer userId) {
        // 缓存清理问题，解决了，不缓存就行了
        return imgMapper.getNeverUseImgs(field, userId);
    }

    // 获取空引用垃圾，定时获取，无需缓存
    @Override
    public List<Img> getGC() {
        return imgMapper.getGC();
    }

    @Override
    @CacheEvict(allEntries = true)
    public Long virtualDeleteBatch(List<Img> imgs) {
        if (imgs.size() < 1) {
            return 0L;
        }
        return imgMapper.virtualDeleteBatch(imgs);
    }

    @Override
    @CacheEvict(allEntries = true)
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

    @Override
    @Cacheable(unless = "#result == null")
    public Img getImgById(Integer imgId) {
        return imgMapper.selectById(imgId);
    }

    @Override
    @Cacheable(unless = "#result.size() == 0")
    public List<Img> getImgsByMD5(String md5) {
        LambdaQueryWrapper<Img> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Img::getMd5, md5);
        return imgMapper.selectList(wrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateImgById(Img img) {
        return imgMapper.updateById(img);
    }

}
