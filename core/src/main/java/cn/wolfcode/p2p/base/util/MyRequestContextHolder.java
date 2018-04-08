package cn.wolfcode.p2p.base.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by seemygo on 2017/12/12.
 */
public class MyRequestContextHolder {
    private static ThreadLocal<HttpServletRequest> local = new ThreadLocal<HttpServletRequest>();
    public static void set(HttpServletRequest request){
        local.set(request);
    }
    public static HttpServletRequest get(){
        return local.get();
    }
}
