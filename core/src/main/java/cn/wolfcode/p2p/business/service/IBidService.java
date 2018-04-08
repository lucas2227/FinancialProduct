package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.business.domain.Bid;

import java.math.BigDecimal;

/**
 * Created by seemygo on 2017/12/19.
 */
public interface IBidService {
    int save(Bid bid);
    int update(Bid bid);
    Bid get(Long id);

    void updateState(Long bidRequestId, int bidRequestState);

}
