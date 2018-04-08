package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.SystemAccount;

/**
 * Created by seemygo on 2017/12/24.
 */
public interface ISystemAccountService {
    int save(SystemAccount systemAccount);
    int update(SystemAccount systemAccount);
    SystemAccount getCurrent();

    void initSystemAccount();
}
