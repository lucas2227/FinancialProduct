package cn.wolfcode.p2p.mgrsite.listener;

import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by seemygo on 2017/12/15.
 */
@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ILogininfoService logininfoService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logininfoService.initAdmin();
        systemAccountService.initSystemAccount();
    }
}
