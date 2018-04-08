package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IEmailService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/16.
 */
@Controller
public class EmailController {
    @Autowired
    private IEmailService emailService;
    @RequestMapping("/sendEmail")
    @ResponseBody
    public JSONResult sendEmail(String email){
        JSONResult result = null;
        try{
            emailService.sendEmail(email);
            result = new JSONResult("发送成功");
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
