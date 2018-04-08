package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;
import cn.wolfcode.p2p.base.service.ILogininfoService;
import cn.wolfcode.p2p.base.service.IVedioAuthService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/19.
 */
@Controller
public class VedioAuthController {
    @Autowired
    private IVedioAuthService vedioAuthService;
    @Autowired
    private ILogininfoService logininfoService;
    @RequestMapping("/vedioAuth")
    public String vedioAuthPage(@ModelAttribute("qo") VedioAuthQueryObject qo, Model model){
        model.addAttribute("pageResult",vedioAuthService.queryPage(qo));
        return "vedioAuth/list";
    }
    @RequestMapping("/vedioAuth_audit")
    @ResponseBody
    public JSONResult audit(Long loginInfoValue,int state,String remark){
        JSONResult result = null;
        try{
            vedioAuthService.aduit(loginInfoValue,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }


    @RequestMapping("/autoComplate")
    @ResponseBody
    public List<Map<String,Object>> autoComplate(String keyword){

        return logininfoService.autoComplate(keyword);
    }

    public static void main(String[] args) throws Exception {
        List<String> xxx = new ArrayList<String>();

        Class clazz = ArrayList.class;
        Method addMethod = clazz.getMethod("add", Object.class);
        addMethod.invoke(xxx,new Logininfo());
        addMethod.invoke(xxx,1);


        System.out.println(xxx);
    }
}
