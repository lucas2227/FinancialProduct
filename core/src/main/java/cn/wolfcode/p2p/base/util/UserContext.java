package cn.wolfcode.p2p.base.util;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.vo.VerifyCodeVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by seemygo on 2017/12/12.
 */
public class UserContext {
    public static final String USER_IN_SESSION = "logininfo";
    public static final String VERIFYCODE_IN_SESSION = "verifyCode";
    public static HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(request);
      /*  HttpServletRequest request = MyRequestContextHolder.get();
        System.out.println("UserContext中的request:"+request);*/
        return request;
    }
    public static void setCurrent(Logininfo current) {
        getRequest().getSession().setAttribute(USER_IN_SESSION,current);
    }
    public static Logininfo getCurrent(){
        return (Logininfo) getRequest().getSession().getAttribute(USER_IN_SESSION);
    }

    public static String getIp() {
        return getRequest().getRemoteAddr();
    }

    public static void setVerifyCodeVo(VerifyCodeVo verifyCodeVo) {
        getRequest().getSession().setAttribute(VERIFYCODE_IN_SESSION,verifyCodeVo);
    }
    public static VerifyCodeVo getVerifyCodeVo(){
        return (VerifyCodeVo) getRequest().getSession().getAttribute(VERIFYCODE_IN_SESSION);
    }
}
