package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.*;
import cn.wolfcode.p2p.business.mapper.BidRequestMapper;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import cn.wolfcode.p2p.business.service.*;
import cn.wolfcode.p2p.business.util.CalculatetUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by seemygo on 2017/12/19.
 */
@Service@Transactional
public class BidRequestServiceImpl implements IBidRequestService {
    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestAuditHistoryService bidRequestAuditHistoryService;
    @Autowired
    private IBidService bidService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Override
    public int save(BidRequest bidRequest) {
        return bidRequestMapper.insert(bidRequest);
    }

    @Override
    public int update(BidRequest bidRequest) {
        int count = bidRequestMapper.updateByPrimaryKey(bidRequest);
        if(count==0){
            throw new RuntimeException("乐观锁异常,bidREquestId:"+bidRequest.getId());
        }
        return count;
    }

    @Override
    public BidRequest get(Long id) {
        return bidRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean canApply(Userinfo userinfo) {
        if(userinfo.getIsBasicInfo()&&
                userinfo.getIsRealAuth()&&
                userinfo.getIsVedioAuth()&&
                userinfo.getScore()>= BidConst.CREDIT_BORROW_SCORE){
            return true;
        }
        return false;
    }

    @Override
    public void apply(BidRequest bidRequest) {
        //有哪些判断?
        Userinfo userinfo = userinfoService.getCurrent();
        Account account = accountService.getCurrent();

        if(this.canApply(userinfo)&& //判断用户是否有借款的资格?
                bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0&&//借款金额>=系统最小借款金额
                bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())<=0&&//借款金额<=当前用户的剩余授信额度
                bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0&&//最小投标>=系统最小投标
                bidRequest.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE)>=0&&//借款利息>=系统最小利息
                bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE)<=0&&//借款利息<=系统最大利息
                !userinfo.getHasBidRquestProcess()//没有借款的申请
                ){
            //创建哪些对象
            BidRequest br = new BidRequest();
            br.setApplyTime(new Date());//借款申请时间
            br.setBidRequestAmount(bidRequest.getBidRequestAmount());//借款的金额
            br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//借款的状态(待发布)
            br.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);//标的类型(信用标)
            br.setCreateUser(UserContext.getCurrent());//借款人(当前登录用户)
            br.setCurrentRate(bidRequest.getCurrentRate());//借款利息(存的是百分比)
            br.setDescription(bidRequest.getDescription());//借款的描述
            br.setDisableDays(bidRequest.getDisableDays());//招标天数
            br.setMinBidAmount(bidRequest.getMinBidAmount());//设置的最小投标
            br.setMonthes2Return(bidRequest.getMonthes2Return());//还款的期数
            br.setReturnType(BidConst.RETURN_TYPE_MONTH_INTEREST_PRINCIPAL);//还款方式(按月分期)
            br.setTitle(bidRequest.getTitle());//借款的标题
            //借款总共的利息
            br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(br.getReturnType(),br.getBidRequestAmount(),
                    br.getCurrentRate(),br.getMonthes2Return()));
            this.save(br);
            //当前用户的信息有哪些变化
            userinfo.addState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
            userinfoService.update(userinfo);
        }
    }

    @Override
    public PageInfo queryPage(BidRequestQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = this.bidRequestMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void publishAudit(Long id, int state, String remark) {
        //做审核判断?
        BidRequest bidRequest = this.get(id);
        //根据id获取借款对象,判断是否处于待发布状态
        if(bidRequest!=null && bidRequest.getBidRequestState()==BidConst.BIDREQUEST_STATE_PUBLISH_PENDING){
            //需要创建哪些对象呢?
            //创建审核历史对象,设置相关的属性
            BidRequestAuditHistory brah = new BidRequestAuditHistory();
            brah.setApplyTime(bidRequest.getApplyTime());
            brah.setRemark(remark);
            brah.setApplier(bidRequest.getCreateUser());
            brah.setAuditor(UserContext.getCurrent());
            brah.setAuditTime(new Date());
            brah.setBidRequestId(bidRequest.getId());
            brah.setAuditType(BidRequestAuditHistory.AUDITTYPE_PUBLISH_AUDIT);
            if(state==BidRequestAuditHistory.STATE_PASS){
                //如果审核通过?
                brah.setState(BidRequestAuditHistory.STATE_PASS);
                //  哪些属性设置
                //  审核对象设置相关的属性
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                //  借款对象的发布时间,截止时间,风控意见,借款状态
                bidRequest.setPublishTime(new Date());
                bidRequest.setNote(remark);
                bidRequest.setDisableDate(DateUtils.addDays(bidRequest.getPublishTime(),bidRequest.getDisableDays()));
            }else{
                //如果审核失败
                brah.setState(BidRequestAuditHistory.STATE_REJECT);
                //  哪些属性需要设置
                //  借款对象的属性设置
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                //申请人的信息有审核变化.
                //  移除用户的状态码
                Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
                userinfoService.update(createUserUserinfo);
            }
            bidRequestAuditHistoryService.save(brah);
            this.update(bidRequest);

        }

    }

    @Override
    public List<BidRequest> queryIndexList(BidRequestQueryObject qo) {
        PageHelper.startPage(1,5);
        return this.bidRequestMapper.queryPage(qo);
    }

    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {

        //1.需要有哪些的判断条件,标的状态,投标的钱是否合理.
            BidRequest bidRequest = this.get(bidRequestId);
            Account account = accountService.getCurrent();

            if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING&&
                    amount.compareTo(bidRequest.getMinBidAmount())>=0&&//投标金额>=最小投标
                    amount.compareTo(account.getUsableAmount().min(bidRequest.getRemainAmount()))<=0&&//投标金额<=MIN(账户可用余额,该标剩余的金额)
                    !UserContext.getCurrent().getId().equals(bidRequest.getCreateUser().getId())//投资人不是借款人
                    ){
                //2.对应借款对象有哪些属性需要设置
                bidRequest.setBidCount(bidRequest.getBidCount()+1);
                bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
                //3.创建投标对象
                Bid bid = new Bid();
                bid.setActualRate(bidRequest.getCurrentRate());//该标利率
                bid.setAvailableAmount(amount);//投标金额
                bid.setBidRequestId(bidRequestId);//关联借款对象
                bid.setBidRequestTitle(bidRequest.getTitle());//借款标题
                bid.setBidUser(UserContext.getCurrent());//投资人
                bid.setBidTime(new Date());//投标时间
                bid.setBidRequestState(bidRequest.getBidRequestState());//标的状态(招标中)
                bidService.save(bid);
                //投资人的账户
                //  可用金额减少,冻结金额增加
                account.setUsableAmount(account.getUsableAmount().subtract(amount));
                account.setFreezedAmount(account.getFreezedAmount().add(amount));
                accountService.update(account);
                //  生成投标冻结流水
                accountFlowService.createBidFreezedFlow(account,amount);
                //4.怎么判断已经投满了呢?
                if(bidRequest.getCurrentSum().compareTo(bidRequest.getBidRequestAmount())==0){
                    //5.投满之后借款对象和投标对象的属性有审核变化?
                    //借款对象设置状态---->满标一审
                    bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                    //投标对象设置状态---->满标一审
                    bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                }
                this.update(bidRequest);
        }
    }

    @Override
    public void audit1(Long id, int state, String remark) {
        //1.需要哪些判断条件,
        BidRequest bidRequest = this.get(id);
        //  判断借款状态是否处于满标一审
        if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1){
            //2.创建审核度对象
            createBidRequestAuditHistory(bidRequest,remark,state,BidRequestAuditHistory.AUDITTYPE_FULL_AUDIT1);
            if(state==BidRequestAuditHistory.STATE_PASS){
                //3.如果审核通过
                //      借款对象和投标对象有什么变化---->满标二审
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            }else{
                //4.如果审核失败
                auditReject(bidRequest);
            }
            this.update(bidRequest);
        }

    }

    @Override
    public void audit2(Long id, int state, String remark) {
        //1,需要有哪些判断条件.
        //有这个借款对象,借款对象处于满标二审
        BidRequest bidRequest = this.get(id);
        if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2){
            //2.需要创建什么对象呢?
            //创建审核历史对象,并设置相关的属性
            createBidRequestAuditHistory(bidRequest,remark,state,BidRequestAuditHistory.AUDITTYPE_FULL_AUDIT2);
            if(state==BidRequestAuditHistory.STATE_PASS){
                //3.如果审核通过
                //      3.1 对于借款对象和投标对象有什么变化?
                //          借款对象和投标对象的状态变更还款中
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
                bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //      3.2 对于借款人有哪些变化?
                Account createUserAccount = accountService.get(bidRequest.getCreateUser().getId());
                //          借款人的可用金额增加,待还本息增加,剩余授信额度减少.
                //借款人的可用金额=借款人的原可用金额+该次借款的金额
                createUserAccount.setUsableAmount(createUserAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
                //借款人的待还本息=借款人的原待还本息+借款金额+借款利息
                createUserAccount.setUnReturnAmount(createUserAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount()).add(bidRequest.getTotalRewardAmount()));
                //借款人的剩余授信额度=借款人的原剩余授信额度-该次借款的金额
                createUserAccount.setRemainBorrowLimit(createUserAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                //          生成借款成功的流水
                accountFlowService.createBorrowSuccessFlow(createUserAccount,bidRequest.getBidRequestAmount());
                //          借款人支付平台的借款手续费(5%比例收取),可用金额减少
                //借款手续费=借款金额*手续费比例(5%)
                BigDecimal accountManagementCharge = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                createUserAccount.setUsableAmount(createUserAccount.getUsableAmount().subtract(accountManagementCharge));
                //          生成支付平台借款手续费的流水
                accountFlowService.createPayAccountManagementChargeFlow(createUserAccount,accountManagementCharge);
                accountService.update(createUserAccount);
                //          移除借款人的借款申请状态码
                Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
                createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
                userinfoService.update(createUserUserinfo);
                //          系统账户收取借款手续费,系统账户的可用金额增加(系统账户TODO)
                SystemAccount systemAccount = systemAccountService.getCurrent();
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(accountManagementCharge));
                systemAccountService.update(systemAccount);
                //          生成系统账户收取借款手续费的流水.
                systemAccountFlowService.createGainAccountManagementChargeFlow(systemAccount,accountManagementCharge);
                //      3.3 对于投资人有哪些变化?
                //          遍历投标集合,找到每一个投标人,投标人的冻结金额减少, (待收本金,待收利息增加 TODO)
                Map<Long,Account> accountMap = new HashMap<Long,Account>();
                Account bidUserAccount;
                Long bidUserId;
                for(Bid bid:bidRequest.getBids()){
                    bidUserId = bid.getBidUser().getId();
                    bidUserAccount = accountMap.get(bidUserId);
                    if(bidUserAccount==null){
                        bidUserAccount = accountService.get(bidUserId);
                        accountMap.put(bidUserId,bidUserAccount);
                    }
                    //投资人的冻结金额=投资人的原冻结金额-该次投标的金额
                    bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    //          生成投标成功的流水.
                    accountFlowService.createBidSuccessFlow(bidUserAccount,bid.getAvailableAmount());
                }
                //      3.4 创建还款对象和还款明细对象.
                List<PaymentSchedule> paymentScheduleList = createPaymentScheduleList(bidRequest);
                //      3.5 计算投资人的待收本金和代收利息
                for(PaymentSchedule ps:paymentScheduleList){
                    for (PaymentScheduleDetail psd:ps.getDetails()){
                        bidUserAccount = accountMap.get(psd.getToLogininfo());
                        bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().add(psd.getInterest()));
                        bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                    }
                }
                //      3.6 对投资人的账户进行统一的更新
                for(Account account:accountMap.values()){
                    accountService.update(account);
                }
            }else{
                //4.如果审核失败
                //      逻辑和满标一审的审核失败逻辑一致.
                auditReject(bidRequest);
            }
            this.update(bidRequest);
        }
    }

    private List<PaymentSchedule> createPaymentScheduleList(BidRequest bidRequest) {
        List<PaymentSchedule> paymentScheduleList = new ArrayList<PaymentSchedule>();
        //有几个还款对象由分期数(借款的月数)决定的
        PaymentSchedule ps;
        BigDecimal principalTemp = BidConst.ZERO;
        BigDecimal interestTemp = BidConst.ZERO;
        for(int i=0;i<bidRequest.getMonthes2Return();i++){
            ps = new PaymentSchedule();
            //关联借款对象id
            ps.setBidRequestId(bidRequest.getId());
            //借款标题
            ps.setBidRequestTitle(bidRequest.getTitle());
            //借款类型(信用标)
            ps.setBidRequestType(bidRequest.getBidRequestType());
            //关联借款人
            ps.setBorrowUser(bidRequest.getCreateUser());
            //还款期数
            ps.setMonthIndex(i+1);
            //还款类型(按月分期)
            ps.setReturnType(bidRequest.getReturnType());
            //截止时间=发标时间+对应月数
            ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(),i+1));
            //判断是否最后一期还款
            if(i<bidRequest.getMonthes2Return()-1){
                //不是最后一期
                ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(),//还款类型(按月分期)
                        bidRequest.getBidRequestAmount(),//借款的总金额
                        bidRequest.getCurrentRate(),//借款的年华利率
                        i+1,//是第几期的还款
                        bidRequest.getMonthes2Return()));//借款的期数
                ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(),//还款类型(按月分期)
                        bidRequest.getBidRequestAmount(),//借款的总金额
                        bidRequest.getCurrentRate(),//借款的年华利率
                        i+1,//是第几期的还款
                        bidRequest.getMonthes2Return()));//借款的期数
                //该期还款本金=该期还款金额-该期的利息
                ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
                principalTemp = principalTemp.add(ps.getPrincipal());
                interestTemp = interestTemp.add(ps.getInterest());
            }else{
                //最后一期
                //该期还款本金=总的还款金额-(N-1期的本金之和)
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(principalTemp));
                //该期还款利息=总的还款利息-(N-1期的利息之和)
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(interestTemp));
                //该期还款金额 = 该期还款本金+该期还款利息
                ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
            }

            paymentScheduleService.save(ps);
            //创建还款明细对象
            createPaymentScheduleDetail(ps,bidRequest);
            paymentScheduleList.add(ps);
        }
        return paymentScheduleList;
    }

    private void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {
        PaymentScheduleDetail psd;
        //有几个还款明细对象是由投资人个数决定
        Bid bid;
        BigDecimal principalTemp = BidConst.ZERO;
        BigDecimal interestTemp = BidConst.ZERO;
        for(int i=0;i<bidRequest.getBids().size();i++){
            bid = bidRequest.getBids().get(i);
            psd = new PaymentScheduleDetail();
            //截止时间
            psd.setDeadLine(ps.getDeadLine());
            //还款方式
            psd.setReturnType(ps.getReturnType());
            //投资的金额
            psd.setBidAmount(bid.getAvailableAmount());
            //关联投标度夏凝
            psd.setBidId(bid.getId());
            //关联借款对象
            psd.setBidRequestId(bidRequest.getId());
            //关联还款人(借款人)
            psd.setFromLogininfo(bidRequest.getCreateUser());
            //第几期的还款明细对象
            psd.setMonthIndex(ps.getMonthIndex());
            //关联还款对象
            psd.setPaymentScheduleId(ps.getId());
            //关联投资人(汇款人)
            psd.setToLogininfo(bid.getBidUser().getId());
            //判断是否最后一个投资人
            if(i<bidRequest.getBids().size()-1){
                //不是最后一个投资人
                BigDecimal rate = bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),BidConst.CAL_SCALE, RoundingMode.HALF_UP);
                //投资人的该还款明细的本金=(该投资的金额/总的借款金额)*该期还款对象中的还款本金
                psd.setPrincipal(rate.multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
                //投资人的该还款明细的利息=(该投资的金额/总的借款金额)*该期还款对象中的还款利息
                psd.setInterest(rate.multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE,RoundingMode.HALF_UP));
                //投资人的该还款明细的金额=投资人的该还款明细的本金+投资人的该还款明细的利息
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
                principalTemp = principalTemp.add(psd.getPrincipal());
                interestTemp = interestTemp.add(psd.getInterest());
            }else{
                //是最后一个投资人
                //投资人的该还款明细的本金=该期还款对象中的还款本金-(N-1)个投资人的本金之和
                psd.setPrincipal(ps.getPrincipal().subtract(principalTemp));
                //投资人的该还款明细的利息=该期还款对象中的还款利息-(N-1)个投资人的利息之和
                psd.setInterest(ps.getInterest().subtract(interestTemp));
                //投资人的该还款明细的金额=投资人的该还款明细的本金+投资人的该还款明细的利息
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));
            }
            paymentScheduleDetailService.save(psd);
            ps.getDetails().add(psd);
        }
    }

    private void createBidRequestAuditHistory(BidRequest bidRequest,String remark,int state,int auditType){
        BidRequestAuditHistory brah = new BidRequestAuditHistory();
        brah.setAuditType(auditType);//审核类型
        brah.setBidRequestId(bidRequest.getId());//关联借款度夏凝
        brah.setRemark(remark);//审核备注
        brah.setApplyTime(new Date());//申请时间(准确的应该是最后一个投标的时间)
        brah.setApplier(bidRequest.getCreateUser());//申请人(借款人)
        brah.setAuditor(UserContext.getCurrent());//审核人
        brah.setAuditTime(new Date());//审核时间
        if(state==BidRequestAuditHistory.STATE_PASS){
            //3.如果审核通过
            brah.setState(BidRequestAuditHistory.STATE_PASS);
        }else{
            //4.如果审核失败
            //      借款对象和投标对象有什么变化--->满标拒绝
            brah.setState(BidRequestAuditHistory.STATE_REJECT);
        }
        bidRequestAuditHistoryService.save(brah);
    }
    private void auditReject(BidRequest bidRequest){
        //4.如果审核失败
        //      借款对象和投标对象有什么变化--->满标拒绝
        bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
        bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_REJECTED);
        //      借款人移除借款状态码
        Userinfo createUserUserinfo = userinfoService.get(bidRequest.getCreateUser().getId());
        createUserUserinfo.removeState(BitStatesUtils.HAS_BIDREQUEST_PROCESS);
        userinfoService.update(createUserUserinfo);

        //      钱怎么处理,投资人的账户有审核变化
        //      遍历投标的集合bidRequest.getBids();
        Map<Long,Account> accountMap = new HashMap<Long,Account>();
        Account bidUserAccount;
        Long bidUserId;
        for(Bid bid:bidRequest.getBids()){
            bidUserId = bid.getBidUser().getId();
            bidUserAccount = accountMap.get(bidUserId);
            if(bidUserAccount==null){
                bidUserAccount = accountService.get(bid.getBidUser().getId());
                accountMap.put(bidUserId,bidUserAccount);
            }
            //      投资人的可用金额增加,冻结金额减少
            bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(bid.getAvailableAmount()));
            bidUserAccount.setFreezedAmount(bidUserAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
            //      生成投标失败流水
            accountFlowService.createBidFailedFlow(bidUserAccount,bid.getAvailableAmount());
        }
        //对账户进行统一的同学
        for(Account account:accountMap.values()){
            accountService.update(account);
        }
    }
}
