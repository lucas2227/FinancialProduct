package cn.wolfcode.p2p;

import cn.wolfcode.p2p.base.util.MyRequestContextListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by seemygo on 2017/12/12.
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:application-core.properties"),
        @PropertySource("classpath:sms.properties"),
        @PropertySource("classpath:email.properties")
})
@MapperScan({"cn.wolfcode.p2p.base.mapper","cn.wolfcode.p2p.business.mapper"})
public class CoreConfig {
    /*@Bean
    public ServletListenerRegistrationBean registrationBean(){
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(myRequestContextListener());
        return bean;
    }
    @Bean
    public MyRequestContextListener myRequestContextListener(){
        return new MyRequestContextListener();
    }*/
}
