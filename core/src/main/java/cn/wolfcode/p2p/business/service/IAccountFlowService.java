package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.AccountFlow;

import java.math.BigDecimal;

/**
 * Created by seemygo on 2017/12/22.
 */
public interface IAccountFlowService {
    int save(AccountFlow accountFlow);

    /**
     * 线下充值流水
     * @param account
     * @param amount
     */
    void createRechargeOfflineFlow(Account account, BigDecimal amount);

    /**
     * 投标冻结流水
     * @param account
     * @param amount
     */
    void createBidFreezedFlow(Account account, BigDecimal amount);

    /**
     * 投标失败
     * @param account
     * @param amount
     */
    void createBidFailedFlow(Account account, BigDecimal amount);

    /**
     * 借款成功的流水
     * @param account
     * @param amount
     */
    void createBorrowSuccessFlow(Account account, BigDecimal amount);

    /**
     * 支付平台借款手续费的流水
     * @param account
     * @param amount
     */
    void createPayAccountManagementChargeFlow(Account account, BigDecimal amount);

    /**
     * 投标成功的流水
     * @param account
     * @param amount
     */
    void createBidSuccessFlow(Account account, BigDecimal amount);

    /**
     * 还款成功的流水
     * @param account
     * @param amount
     */
    void createReturnMoneyFlow(Account account, BigDecimal amount);

    /**
     * 回款成功的流水
     * @param account
     * @param amount
     */
    void createGainReturnMoneyFlow(Account account, BigDecimal amount);

    /**
     * 支付平台利息管理费
     * @param account
     * @param amount
     */
    void createPayInterestManagerChargeFlow(Account account, BigDecimal amount);
}
