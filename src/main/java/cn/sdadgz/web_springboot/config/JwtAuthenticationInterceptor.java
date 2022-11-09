package cn.sdadgz.web_springboot.config;

import cn.sdadgz.web_springboot.Utils.IdUtil;
import cn.sdadgz.web_springboot.Utils.JwtUtil;
import cn.sdadgz.web_springboot.Utils.StrUtil;
import cn.sdadgz.web_springboot.entity.User;
import cn.sdadgz.web_springboot.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //放行OPTIONS请求
        String method = request.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }
        //获取token
        String token = request.getHeader("token");
        System.out.println("Token已拦截请求");
        //token不存在直接报错
        if (token == null) {
            throw new BusinessException("499", "Token不存在");
        }
        //获取签发对象id，不存在直接报错
        String userid = null; //获取签发对象的Userid
        try {
            userid = JwtUtil.getAudience(token);//获取token中的签发对象
        } catch (Exception e) {
            throw new DangerousException("499", "数据校验失败", IdUtil.getIp(request), StrUtil.NO_USER);
        }
        //带着token验证载荷username是否正确
        User realUser = null;
        realUser = userMapper.selectById(userid);
        if (realUser == null) { // 用户不存在
            throw new DangerousException("499", "认证错误", request, Integer.parseInt(userid));
        }
        //姓名不匹配返回
        if (!Objects.equals(realUser.getName(), JwtUtil.getClaimByName(token, "username").asString())) {
            throw new DangerousException("499", "认证错误", request, Integer.parseInt(userid));
        }
        //检查是否过期
        if (JwtUtil.checkDate(token)) {
            throw new BusinessException("499", "token过期");
        }
        log.info("tocken验证成功");
        //布尔值验证
        return JwtUtil.vertifyToken(token, userid, realUser.getPassword());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
