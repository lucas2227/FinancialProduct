package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by seemygo on 2017/12/21.
 */
@Controller
public class InvestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @RequestMapping("/invest")
    public String investPage(){
        return "invest";
    }
    @RequestMapping("/invest_list")
    public String investList(BidRequestQueryObject qo,Model model){
        if(qo.getBidRequestState()==-1){
            qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        }
        model.addAttribute("pageResult",bidRequestService.queryPage(qo));
        return "invest_list";
    }
}
