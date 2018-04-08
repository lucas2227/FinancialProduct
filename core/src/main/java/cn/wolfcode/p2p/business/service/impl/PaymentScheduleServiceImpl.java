package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleMapper;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import cn.wolfcode.p2p.business.service.*;
import cn.wolfcode.p2p.business.util.CalculatetUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/24.
 */
@Service@Transactional
public class PaymentScheduleServiceImpl implements IPaymentScheduleService {
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private ISystemAccountFlowService systemAccountFlowService;
    @Autowired
    private IPaymentScheduleDetailService paymentScheduleDetailService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IBidService bidService;
    @Override
    public int save(PaymentSchedule paymentSchedule) {
        return paymentScheduleMapper.insert(paymentSchedule);
    }

    @Override
    public int update(PaymentSchedule paymentSchedule) {
        return paymentScheduleMapper.updateByPrimaryKey(paymentSchedule);
    }

    @Override
    public PaymentSchedule get(Long id) {
        return paymentScheduleMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(PaymentScheduleQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = paymentScheduleMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void returnMoney(Long id) {
        //1.有什么的限制呢?
        //      还款对象需要存在,状态处于待还
        //      当前登录用户是否还款人
        //      判断当前用户的可用金额>=该期的还款金额
        PaymentSchedule ps = this.get(id);
        Account account = accountService.getCurrent();
        if(ps!=null && ps.getState()== BidConst.PAYMENT_STATE_NORMAL&&
                UserContext.getCurrent().getId().equals(ps.getBorrowUser().getId())&&//      当前登录用户是否还款人
                account.getUsableAmount().compareTo(ps.getTotalAmount())>=0//判断当前用户的可用金额>=该期的还款金额
                ){
            //2.还款对象和还款明细的属性有什么变化?
            //      还款对象设置还款日期,还款状态
            ps.setPayDate(new Date());
            ps.setState(BidConst.PAYMENT_STATE_DONE);
            this.update(ps);
            //      还款明细对象设置还款日期
            paymentScheduleDetailService.updatePayDate(ps.getId(),ps.getPayDate());
            //3.对于还款人(借款人),有什么样变化
            //      可用金额减少,待还本息减少,剩余授信额度增加
            //借款人的可用金额=借款人的原可用金额-该期的还款金额
            account.setUsableAmount(account.getUsableAmount().subtract(ps.getTotalAmount()));
            //借款人的待还本息=借款人的原待还本息-该期的还款金额
            account.setUnReturnAmount(account.getUnReturnAmount().subtract(ps.getTotalAmount()));
            //借款人的剩余授信额度=借款人的原剩余授信额度+该期的还款本金
            account.setRemainBorrowLimit(account.getRemainBorrowLimit().add(ps.getPrincipal()));
            accountService.update(account);
            //      生成还款成功的流水
            accountFlowService.createReturnMoneyFlow(account,ps.getTotalAmount());

            //4.对于投资人,有哪些变化
            Map<Long,Account> accountMap = new HashMap<Long,Account>();
            Account bidUserAccount;
            Long bidUserId;
            BigDecimal interestManagerCharge;
            SystemAccount systemAccount = systemAccountService.getCurrent();
            //      遍历还款明细集合,找到投资人的账户
            for(PaymentScheduleDetail psd:ps.getDetails()){
                bidUserId = psd.getToLogininfo();//投资人的id
                bidUserAccount = accountMap.get(bidUserId);
                if(bidUserAccount==null){
                    bidUserAccount = accountService.get(bidUserId);
                    accountMap.put(bidUserId,bidUserAccount);
                }
                //      可用金额增加,待收本金,待收利息减少
                bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().add(psd.getTotalAmount()));
                //投资人的待收本金=投资人的原待收本金-该还款明细中的本金
                bidUserAccount.setUnReceivePrincipal(bidUserAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
                //投资人的待收利息=投资人的原待收利息-该还款明细中的利息
                bidUserAccount.setUnReceiveInterest(bidUserAccount.getUnReceiveInterest().subtract(psd.getInterest()));
                //      生成回款成功的流水
                accountFlowService.createGainReturnMoneyFlow(bidUserAccount,psd.getTotalAmount());
                //      支付利息管理费
                interestManagerCharge = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
                //      生成支付利息管理费流水
                bidUserAccount.setUsableAmount(bidUserAccount.getUsableAmount().subtract(interestManagerCharge));
                accountFlowService.createPayInterestManagerChargeFlow(bidUserAccount,interestManagerCharge);
                //      系统账户收取利息管理费
                systemAccount.setUsableAmount(systemAccount.getUsableAmount().add(interestManagerCharge));
                //      生成系统账户收取利息管理费的流水
                systemAccountFlowService.createGainInterestManagerChargeFlow(systemAccount,interestManagerCharge);
            }
            //统一的系统账户和普通用户账户进行更新操作
            systemAccountService.update(systemAccount);
            for(Account buAccount:accountMap.values()){
                accountService.update(buAccount);
            }

            //5.怎么判断该借款所有的还款都已经还清呢?
            List<PaymentSchedule> paymentScheduleList = this.queryPaymentScheduleByBidRequestId(ps.getBidRequestId());
            //      根据借款id,查询出该借款的所有还款对象,判断所有还款的状态是否变成已还.如果都是已还,该借款已经还清
            boolean flag = true;//默认所有的还款都是还清
            for(PaymentSchedule paymentSchedule:paymentScheduleList){
                if(paymentSchedule.getState()!=BidConst.PAYMENT_STATE_DONE){
                    flag = false;
                    return;
                }
            }
            if(flag){
                //6.所有的还款都已经还清之后,借款对象和投标对象有哪些变化?
                BidRequest bidRequest = bidRequestService.get(ps.getBidRequestId());
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                bidRequestService.update(bidRequest);
                //      借款对象和投标对象的状态变更成已完成.
                bidService.updateState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
            }

        }
    }

    @Override
    public List<PaymentSchedule> queryPaymentScheduleByBidRequestId(Long bidRequestId) {
        return this.paymentScheduleMapper.queryPaymentScheduleByBidRequestId(bidRequestId);
    }
}
