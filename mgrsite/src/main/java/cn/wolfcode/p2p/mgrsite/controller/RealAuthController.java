package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/18.
 */
@Controller
public class RealAuthController {
    @Autowired
    private IRealAuthService realAuthService;
    @RequestMapping("/realAuth")
    public String realAuthPage(@ModelAttribute("qo") RealAuthQueryObject qo, Model model){
        model.addAttribute("pageResult",realAuthService.queryPage(qo));
        return "realAuth/list";
    }
    @RequestMapping("/realAuth_audit")
    @ResponseBody
    public JSONResult audit(Long id,int state,String remark){
        JSONResult result = null;
        try{
            realAuthService.audit(id,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
