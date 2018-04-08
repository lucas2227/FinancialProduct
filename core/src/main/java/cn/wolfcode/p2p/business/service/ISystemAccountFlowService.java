package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.SystemAccount;
import cn.wolfcode.p2p.business.domain.SystemAccountFlow;

import java.math.BigDecimal;

/**
 * Created by seemygo on 2017/12/24.
 */
public interface ISystemAccountFlowService {
    int save(SystemAccountFlow systemAccountFlow);

    /**
     * 系统账户收取借款手续费流水
     * @param systemAccount
     * @param amount
     */
    void createGainAccountManagementChargeFlow(SystemAccount systemAccount, BigDecimal amount);

    /**
     * 系统账户收取利息管理费
     * @param systemAccount
     * @param amount
     */
    void createGainInterestManagerChargeFlow(SystemAccount systemAccount, BigDecimal amount);
}
