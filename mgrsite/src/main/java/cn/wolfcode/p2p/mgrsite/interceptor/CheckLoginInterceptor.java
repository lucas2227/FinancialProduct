package cn.wolfcode.p2p.mgrsite.interceptor;

import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by seemygo on 2017/12/15.
 */

public class CheckLoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
           //handler当期访问的方法对象
            System.out.println(handler);
            HandlerMethod method = (HandlerMethod) handler;
            //判断用户是否有登录
            if(UserContext.getCurrent()==null){
                //跳转到登录页面
                response.sendRedirect("/login.html");
                return false;
            }
        }
        return true;
    }
}
