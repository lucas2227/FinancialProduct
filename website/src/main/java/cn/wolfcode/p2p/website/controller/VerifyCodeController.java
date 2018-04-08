package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/15.
 */
@Controller
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    public JSONResult sendVerifyCode(String phoneNumber){
        JSONResult result = null;
        try{
            verifyCodeService.sendVerifyCode(phoneNumber);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
