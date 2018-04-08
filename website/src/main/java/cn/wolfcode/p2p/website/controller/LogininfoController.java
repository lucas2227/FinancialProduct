package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/12.
 */
@Controller
public class LogininfoController {
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("/userRegister")
    @ResponseBody
    public JSONResult userRegister(String username, String password){
        JSONResult result = null;
        try{
            logininfoService.register(username,password);
            result = new JSONResult("注册成功");
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(String username){
        return logininfoService.checkUsername(username);
    }
    @RequestMapping("/userLogin")
    @ResponseBody
    public JSONResult login(String username,String password){
        JSONResult result = null;
        Logininfo logininfo = logininfoService.login(username,password,Logininfo.USERTYPE_USER);
        if(logininfo==null){
            result = new JSONResult(false,"账户密码有误");
        }else{
            result = new JSONResult("登录成功");
        }
        return result;
    }
}
