package cn.wolfcode.p2p;

import cn.wolfcode.p2p.mgrsite.interceptor.CheckLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by seemygo on 2017/12/15.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor()).addPathPatterns("/*").excludePathPatterns("/mgrLogin");
    }
    @Bean
    public CheckLoginInterceptor checkLoginInterceptor(){
        return new CheckLoginInterceptor();
    }
}
