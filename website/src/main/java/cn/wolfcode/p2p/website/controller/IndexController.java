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
public class IndexController {
    @Autowired
    private IBidRequestService bidRequestService;
    @RequestMapping("/index")
    public String indexPage(BidRequestQueryObject qo,Model model){
        qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,BidConst.BIDREQUEST_STATE_PAYING_BACK,BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        //按照指定的顺序排序
        qo.setOrderByCondition(" ORDER BY br.bidRequestState ASC ");
        model.addAttribute("bidRequests",bidRequestService.queryIndexList(qo));
        return "main";
    }
}
