package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/15.
 * 后台的登录控制器
 */
@Controller
public class LoginController {
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("/mgrLogin")
    @ResponseBody
    public JSONResult login(String username,String password){
        Logininfo logininfo = logininfoService.login(username, password, Logininfo.USERTYPE_MANAGER);
        if(logininfo==null){
            return new JSONResult(false,"账号密码有误!");
        }else{
            return new JSONResult("登录成功");
        }
    }
    @RequestMapping("/index")
    public String indexPage(){
        return "main";
    }
}
