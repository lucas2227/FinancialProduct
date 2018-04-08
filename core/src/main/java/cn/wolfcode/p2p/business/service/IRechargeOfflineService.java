package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2017/12/22.
 */
public interface IRechargeOfflineService {
    int save(RechargeOffline rechargeOffline);
    int update(RechargeOffline rechargeOffline);
    RechargeOffline get(Long id);

    /**
     * 线下充值申请
     * @param rechargeOffline
     */
    void rechargeSave(RechargeOffline rechargeOffline);

    PageInfo queryPage(RechargeOfflineQueryObject qo);

    /**
     * 线下充值审核
     * @param id
     * @param state
     * @param remark
     */
    void audit(Long id, int state, String remark);
}
