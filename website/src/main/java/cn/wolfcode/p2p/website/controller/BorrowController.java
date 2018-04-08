package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by seemygo on 2017/12/16.
 */
@Controller
public class BorrowController {
    @Autowired
    private IAccountService accountServiec;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;
    @RequestMapping("/borrow")
    public String borrowPage(Model model){
        Logininfo logininfo = UserContext.getCurrent();
        if(logininfo==null){
            return "redirect:/borrowPage.html";
        }else{
            //account
            model.addAttribute("account",accountServiec.getCurrent());
            //userinfo
            model.addAttribute("userinfo",userinfoService.getCurrent());
            //creditBorrowScore
            model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);
            return "borrow";
        }
    }
    @RequestMapping("/borrowInfo")
    public String borrowInfo(Model model){
        //进入借款页面之前需要判断用户是否已经发起借款申请了,
        Userinfo userinfo = userinfoService.getCurrent();
        //判断用户是否有借款资格
        if(!bidRequestService.canApply(userinfo)){
            return "redirect:/borrow";
        }
        //如果已经发起,跳转等待页面
        if(userinfo.getHasBidRquestProcess()){
            return "borrow_apply_result";
        }
        //如果没有发起,跳转到申请页面
        //minBidRequestAmount最小的借款金额
        model.addAttribute("minBidRequestAmount",BidConst.SMALLEST_BIDREQUEST_AMOUNT);
        //account当前账户信息
        model.addAttribute("account",accountServiec.getCurrent());
        //minBidAmount最小投标金额
        model.addAttribute("minBidAmount",BidConst.SMALLEST_BID_AMOUNT);
        return "borrow_apply";
    }
    @RequestMapping("/borrow_apply")
    public String borrowApply(BidRequest bidRequest){
        bidRequestService.apply(bidRequest);
        return "redirect:/borrowInfo";
    }
    @RequestMapping("/borrow_info")
    public String borrowInfo(Long id,Model model){
        BidRequest bidRequest = bidRequestService.get(id);
        if(bidRequest!=null){
            //bidRequest
            model.addAttribute("bidRequest", bidRequest);
            //userInfo
            Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo",createUserUserinfo);
            //realAuth
            model.addAttribute("realAuth",realAuthService.get(createUserUserinfo.getRealAuthId()));
            //userFiles
            model.addAttribute("userFiles",userFileService.queryListByLogininfoId(bidRequest.getCreateUser().getId()));
            //判断是否有登录
            Logininfo current = UserContext.getCurrent();
            if(current !=null){
                //已经登录,判断当前登录的用户是否借款人
                if(current.getId().equals(bidRequest.getCreateUser().getId())){
                    model.addAttribute("self",true);
                }else{
                    model.addAttribute("self",false);
                    model.addAttribute("account",accountServiec.getCurrent());
                }
            }else{
                //没有登录
                model.addAttribute("self",false);
            }
        }
        return "borrow_info";
    }
    @RequestMapping("/borrow_bid")
    @ResponseBody
    public JSONResult bid(Long bidRequestId, BigDecimal amount){
        JSONResult result = null;
        try{
            bidRequestService.bid(bidRequestId,amount);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
