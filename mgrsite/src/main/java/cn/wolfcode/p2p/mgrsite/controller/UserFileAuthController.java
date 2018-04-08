package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/19.
 */
@Controller
public class UserFileAuthController {
    @Autowired
    private IUserFileService userFileService;
    @RequestMapping("/userFileAuth")
    public String userFileAuthPage(@ModelAttribute("qo") UserFileQueryObject qo, Model model){
        model.addAttribute("pageResult",userFileService.queryPage(qo));
        return "userFileAuth/list";
    }
    @RequestMapping("/userFile_audit")
    @ResponseBody
    public JSONResult audit(Long id,int score,int state,String remark){
        JSONResult result = null;
        try{
            userFileService.aduit(id,score,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
