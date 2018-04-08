package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.IpLog;
import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.LogininfoMapper;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IIpLogService;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.MD5;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/12.
 */
@Service@Transactional
public class LogininfoServiceImpl implements ILogininfoService {
    @Autowired
    private LogininfoMapper logininfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IIpLogService ipLogService;
    @Override
    public void register(String username, String password) {
        //1.判断用户名在数据库中是否已经存在
        int count = logininfoMapper.selectCountByUsername(username);
        if(count>0){
            throw new RuntimeException("用户名已存在!");
        }
        //2.如果不存在,插入数据
        Logininfo logininfo = new Logininfo();
        logininfo.setUsername(username);
        logininfo.setPassword(MD5.encode(password));
        logininfo.setUserType(Logininfo.USERTYPE_USER);
        logininfoMapper.insert(logininfo);

        //创建账户对象和基本信息对象
        Account account = new Account();
        account.setId(logininfo.getId());
        accountService.save(account);

        Userinfo userinfo = new Userinfo();
        userinfo.setId(logininfo.getId());
        userinfoService.save(userinfo);


    }

    @Override
    public boolean checkUsername(String username) {
       /* //去数据库中根据用户名查总数
        int count = logininfoMapper.selectCountByUsername(username);
        //总数>0,说明已存在,返回false
        if(count>0){
            return false;
        }else{
            return true;
        }*/
        //总数<=0,说明不存在,返回true
        return logininfoMapper.selectCountByUsername(username)<=0;
    }

    @Override
    public Logininfo login(String username, String password,int userType) {
        //1.根据用户和密码查询logininfo对象
        Logininfo logininfo = logininfoMapper.login(username,MD5.encode(password),userType);
        //如果logininfo对象为null,数据库找不到对应的账户和密码,提示账户密码有误
        IpLog ipLog = new IpLog();
        ipLog.setUsername(username);
        ipLog.setLoginTime(new Date());
        ipLog.setIp(UserContext.getIp());
        ipLog.setUserType(userType);
        if(logininfo==null){
            ipLog.setState(IpLog.LOGIN_FAILED);
        }else{
            UserContext.setCurrent(logininfo);
            ipLog.setState(IpLog.LOGIN_SUCCESS);
            //如果不为null,把logininfo对象设置到session中.TODO
        }
        ipLogService.save(ipLog);
        return logininfo;
    }

    @Override
    public void initAdmin() {
        //1.根据类型判断数据库中是否有管理员
        int count = logininfoMapper.selectCountByUserType(Logininfo.USERTYPE_MANAGER);
        //2.如果没有创建一个
        if(count==0){
            Logininfo logininfo = new Logininfo();
            logininfo.setUsername(BidConst.ADMIN_DEFAULT_ACCOUNT);
            logininfo.setPassword(MD5.encode(BidConst.ADMIN_DEFAULT_PASSWORD));
            logininfo.setState(Logininfo.STATE_NORMAL);
            logininfo.setUserType(Logininfo.USERTYPE_MANAGER);
            logininfoMapper.insert(logininfo);
        }
    }

    @Override
    public List<Map<String, Object>> autoComplate(String keyword) {
        return logininfoMapper.autoComplate(keyword);
    }
}
