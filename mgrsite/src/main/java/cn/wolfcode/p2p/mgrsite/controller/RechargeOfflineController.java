package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankinfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/22.
 */
@Controller
public class RechargeOfflineController {
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;
    @Autowired
    private IPlatformBankinfoService platformBankinfoService;
    @RequestMapping("/rechargeOffline")
    public String rechargeOfflinePage(@ModelAttribute("qo") RechargeOfflineQueryObject qo, Model model){
        model.addAttribute("pageResult",rechargeOfflineService.queryPage(qo));
        model.addAttribute("banks",platformBankinfoService.selectAll());
        return "rechargeoffline/list";
    }
    @RequestMapping("/rechargeOffline_audit")
    @ResponseBody
    public JSONResult audit(Long id,int state,String remark){
        JSONResult result = null;
        try{
            rechargeOfflineService.audit(id,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
