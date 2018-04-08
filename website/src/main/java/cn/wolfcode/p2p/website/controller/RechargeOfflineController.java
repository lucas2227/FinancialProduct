package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.service.IPlatformBankinfoService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/22.
 */
@Controller
public class RechargeOfflineController {
    @Autowired
    private IPlatformBankinfoService platformBankinfoService;
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;
    @RequestMapping("/recharge")
    public String rechargeOfflinePage(Model model){
        model.addAttribute("banks",platformBankinfoService.selectAll());
        return "recharge";
    }
    @RequestMapping("/recharge_save")
    @ResponseBody
    public JSONResult rechargeSave(RechargeOffline rechargeOffline){
        JSONResult result = null;
        try{
            rechargeOfflineService.rechargeSave(rechargeOffline);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
