package cn.sdadgz.web_springboot.Utils.SameCode.Page;

import cn.sdadgz.web_springboot.Utils.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<MP extends Mapper<T>, T> {
    public Map<String, Object> getPage(int currentPage,
                                       int pageSize,
                                       HttpServletRequest request,
                                       MP mapper) {

        // 设置开始页
        int startPage = (currentPage - 1) * pageSize;

        // 初始化
        Map<String, Object> map = new HashMap<>();
        List<T> lists;
        Long total;

        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // 获取用户id
        int id = IdUtil.getId(request);
        if (id > 0) { // 正常用户
            lists = mapper.getPage(id, startPage, pageSize);
            wrapper.eq("user_id", id);
        } else { // 海克斯科技用户
            lists = mapper.getPage(null, startPage, pageSize);
        }

        total = mapper.selectCount(wrapper);

        map.put("lists", lists);
        map.put("total", total);

        return map;
    }
}
