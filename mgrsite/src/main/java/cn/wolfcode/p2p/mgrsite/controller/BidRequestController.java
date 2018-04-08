package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;
import cn.wolfcode.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/21.
 */
@Controller
public class BidRequestController {
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private IRealAuthService realAuthService;
    @RequestMapping("/bidrequest_publishaudit_list")
    public String publisAuditPage(@ModelAttribute("qo") BidRequestQueryObject qo, Model model){
        //只查询待发布的借款对象集合
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
        model.addAttribute("pageResult",bidRequestService.queryPage(qo));
        return "bidrequest/publish_audit";
    }
    @RequestMapping("/bidrequest_publishaudit")
    @ResponseBody
    public JSONResult publishAudit(Long id,int state,String remark){
        JSONResult result = null;
        try{
            bidRequestService.publishAudit(id,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/borrow_info")
    public String borrowInfoPage(Long id,Model model){
        BidRequest bidRequest = bidRequestService.get(id);
        if(bidRequest!=null){
            //bidRequest
            model.addAttribute("bidRequest", bidRequest);
            //userInfo
            Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo",createUserUserinfo);
            //realAuth
            model.addAttribute("realAuth",realAuthService.get(createUserUserinfo.getRealAuthId()));
            //audits
            model.addAttribute("audits",bidRequestAuditHistoryService.queryLisByBidRequestId(id));
            //userFiles
            model.addAttribute("userFiles",userFileService.queryListByLogininfoId(bidRequest.getCreateUser().getId()));
        }
        return "bidrequest/borrow_info";
    }
    @RequestMapping("/bidrequest_audit1_list")
    public String fullAudit1Page(@ModelAttribute("qo") BidRequestQueryObject qo, Model model){
        //只查询待发布的借款对象集合
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
        model.addAttribute("pageResult",bidRequestService.queryPage(qo));
        return "bidrequest/audit1";
    }
    @RequestMapping("/bidrequest_audit1")
    @ResponseBody
    public JSONResult fullAudit1(Long id,int state,String remark){
        JSONResult result = null;
        try{
            bidRequestService.audit1(id,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
    @RequestMapping("/bidrequest_audit2_list")
    public String fullAudit2Page(@ModelAttribute("qo") BidRequestQueryObject qo, Model model){
        //只查询满标二审的借款对象集合
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
        model.addAttribute("pageResult",bidRequestService.queryPage(qo));
        return "bidrequest/audit2";
    }
    @RequestMapping("/bidrequest_audit2")
    @ResponseBody
    public JSONResult fullAudit2(Long id,int state,String remark){
        JSONResult result = null;
        try{
            bidRequestService.audit2(id,state,remark);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
