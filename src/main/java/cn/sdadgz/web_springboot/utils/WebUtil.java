package cn.sdadgz.web_springboot.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {

    public static boolean passOptions(HttpServletRequest request){
        return request.getMethod().toUpperCase().equals("OPTIONS");
    }

}
