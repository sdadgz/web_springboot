package cn.sdadgz.web_springboot.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 前端传数据使用，修改博客
 * */
@Getter
@Setter
public class ImgUpdateDao {

    List<Integer> idList;

    String field;

}
