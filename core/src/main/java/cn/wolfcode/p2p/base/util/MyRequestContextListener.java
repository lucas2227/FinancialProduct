package cn.wolfcode.p2p.base.util;

import javax.servlet.ServletRequestEvent;

import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by seemygo on 2017/12/12.
 */
public class MyRequestContextListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        MyRequestContextHolder.set(request);
        System.out.println("MyRequestContextListener中的request:"+request);

    }
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

}
