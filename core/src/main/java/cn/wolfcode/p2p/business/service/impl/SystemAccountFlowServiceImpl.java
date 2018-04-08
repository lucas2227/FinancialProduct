package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.domain.SystemAccountFlow;
import cn.wolfcode.p2p.business.mapper.SystemAccountFlowMapper;
import cn.wolfcode.p2p.business.service.ISystemAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by seemygo on 2017/12/24.
 */
@Service@Transactional
public class SystemAccountFlowServiceImpl implements ISystemAccountFlowService {
    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;
    @Override
    public int save(SystemAccountFlow systemAccountFlow) {
        return systemAccountFlowMapper.insert(systemAccountFlow);
    }
    private void createFlow(SystemAccount systemAccount, BigDecimal amount,int actionType,String remark){
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setUsableAmount(systemAccount.getUsableAmount());
        flow.setFreezedAmount(systemAccount.getFreezedAmount());
        flow.setActionTime(new Date());
        flow.setAmount(amount);
        flow.setRemark(remark);
        flow.setActionType(actionType);
        this.save(flow);
    }
    @Override
    public void createGainAccountManagementChargeFlow(SystemAccount systemAccount, BigDecimal amount) {
        createFlow(systemAccount,amount, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE,"收取用户的借款手续费:"+amount);
    }

    @Override
    public void createGainInterestManagerChargeFlow(SystemAccount systemAccount, BigDecimal amount) {
        createFlow(systemAccount,amount, BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE,"收取用户的利息管理费:"+amount);
    }
}
