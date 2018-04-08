package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.IPaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/24.
 */
@Controller
public class ReturnMoneyController {
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IAccountService accountService;

    @RequestMapping("/borrowBidReturn_list")
    public String returnMoneyPage(@ModelAttribute("qo") PaymentScheduleQueryObject qo, Model model){
        qo.setCurrentUserId(UserContext.getCurrent().getId());
        model.addAttribute("account",accountService.getCurrent());
        model.addAttribute("pageResult",paymentScheduleService.queryPage(qo));
        return "returnmoney_list";
    }
    @RequestMapping("/returnMoney")
    @ResponseBody
    public JSONResult returnMoney(Long id){
        JSONResult result = null;
        try{
            paymentScheduleService.returnMoney(id);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
