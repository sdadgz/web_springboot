package cn.sdadgz.web_springboot.utils.SameCode.Page;

import cn.sdadgz.web_springboot.utils.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Page<MP extends Mapper<T>, T> {

    // 获取分页
    public Map<String, Object> getPage(int currentPage,
                                       int pageSize,
                                       HttpServletRequest request,
                                       MP mapper) {

        int id = IdUtil.getUserId(request);

        return getPage(currentPage, pageSize, id, mapper);
    }

    // 获取分页
    public Map<String, Object> getPage(int currentPage,
                                       int pageSize,
                                       int id,
                                       MP mapper) {

        // 设置开始页
        int startPage = (currentPage - 1) * pageSize;

        // 初始化
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        List<T> lists;
        Long total;

        lists = mapper.getPage(id > 0 ? id : null, startPage, pageSize);
        if (id > 0) {
            wrapper.eq("user_id", id);
        }

        total = mapper.selectCount(wrapper);

        map.put("lists", lists);
        map.put("total", total);

        return map;
    }

}
