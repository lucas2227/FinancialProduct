package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.BidRequestAuditHistory;
import cn.wolfcode.p2p.business.mapper.BidRequestAuditHistoryMapper;
import cn.wolfcode.p2p.business.service.IBidRequestAuditHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by seemygo on 2017/12/21.
 */
@Service@Transactional
public class BidRequestAuditHistoryServiceImpl implements IBidRequestAuditHistoryService {
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
    @Override
    public int save(BidRequestAuditHistory bidRequestAuditHistory) {
        return bidRequestAuditHistoryMapper.insert(bidRequestAuditHistory);
    }

    @Override
    public List<BidRequestAuditHistory> queryLisByBidRequestId(Long bidRequestId) {
        return bidRequestAuditHistoryMapper.queryLisByBidRequestId(bidRequestId);
    }
}
