package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;

import java.util.List;

/**
 * Created by seemygo on 2017/12/21.
 */
public interface IBidRequestAuditHistoryService {
    int save(BidRequestAuditHistory bidRequestAuditHistory);

    List<BidRequestAuditHistory> queryLisByBidRequestId(Long bidRequestId);
}
