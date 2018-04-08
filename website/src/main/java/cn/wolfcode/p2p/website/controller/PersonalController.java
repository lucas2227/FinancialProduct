package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.website.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/14.
 */
@Controller
public class PersonalController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserinfoService userinfoService;
    @RequestMapping("/personal")
    @RequireLogin
    public String personalPage(Model model){
        model.addAttribute("account",accountService.getCurrent());
        model.addAttribute("userinfo",userinfoService.getCurrent());
        return "personal";
    }
    @RequestMapping("/bindPhone")
    @ResponseBody
    public JSONResult bindPhone(String phoneNumber,String verifyCode){
        JSONResult result = null;
        try{
            userinfoService.bindPhone(phoneNumber,verifyCode);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/bindEmail")
    public String bindEmail(String key,Model model){
        try{
            userinfoService.bindEmail(key);
            model.addAttribute("success",true);
        }catch (Exception e){
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
        }
        return "checkmail_result";
    }
}
