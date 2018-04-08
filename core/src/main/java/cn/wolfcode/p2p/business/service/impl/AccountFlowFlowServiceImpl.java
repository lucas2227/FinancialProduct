package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.domain.AccountFlow;
import cn.wolfcode.p2p.business.mapper.AccountFlowMapper;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by seemygo on 2017/12/22.
 */
@Service@Transactional
public class AccountFlowFlowServiceImpl implements IAccountFlowService {
    @Autowired
    private AccountFlowMapper accountFlowMapper;
    @Override
    public int save(AccountFlow accountFlow) {
        return accountFlowMapper.insert(accountFlow);
    }
    private void createFlow(Account account, BigDecimal amount,int actionType,String remark){
        AccountFlow flow = new AccountFlow();
        flow.setAccountId(account.getId());
        flow.setUsableAmount(account.getUsableAmount());
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setTradeTime(new Date());
        flow.setRemark(remark);
        flow.setAmount(amount);
        flow.setActionType(actionType);
        this.save(flow);
    }
    @Override
    public void createRechargeOfflineFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE,"线下充值:"+amount+"元");
    }

    @Override
    public void createBidFreezedFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED,"投标冻结:"+amount+"元");
    }

    @Override
    public void createBidFailedFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED,"投标失败,取消冻结:"+amount+"元");
    }

    @Override
    public void createBorrowSuccessFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL,"借款成功:"+amount+"元");
    }

    @Override
    public void createPayAccountManagementChargeFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_CHARGE,"支付平台借款手续费:"+amount+"元");
    }

    @Override
    public void createBidSuccessFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL,"投标流水,冻结减少:"+amount+"元");
    }

    @Override
    public void createReturnMoneyFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY,"还款成功:"+amount+"元");
    }

    @Override
    public void createGainReturnMoneyFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY,"回款成功:"+amount+"元");
    }

    @Override
    public void createPayInterestManagerChargeFlow(Account account, BigDecimal amount) {
        createFlow(account,amount,BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE,"支付利息管理费:"+amount+"元");
    }
}
