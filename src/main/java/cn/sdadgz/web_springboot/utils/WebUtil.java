package cn.sdadgz.web_springboot.utils;

import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WebUtil {

    public static boolean passOptions(HttpServletRequest request) {
        return "OPTIONS".equals(request.getMethod().toUpperCase());
    }

    /**
     * 将url转义，妈的有的逼东西不转义不让用
     *
     * @param url 原url
     * @return 转义之后的url
     */
    public static String encodeURL(String url) {
        url = url.replaceAll("%", "%25");
//        try {
//            url = URLEncoder.encode(url, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        return url;
    }

}
