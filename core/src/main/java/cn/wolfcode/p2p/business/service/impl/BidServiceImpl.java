package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.Bid;
import cn.wolfcode.p2p.business.mapper.BidMapper;
import cn.wolfcode.p2p.business.service.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seemygo on 2017/12/19.
 */
@Service@Transactional
public class BidServiceImpl implements IBidService {
    @Autowired
    private BidMapper bidMapper;
    @Override
    public int save(Bid bid) {
        return bidMapper.insert(bid);
    }

    @Override
    public int update(Bid bid) {
        return bidMapper.updateByPrimaryKey(bid);
    }

    @Override
    public Bid get(Long id) {
        return bidMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateState(Long bidRequestId, int bidRequestState) {
        bidMapper.updateState(bidRequestId,bidRequestState);
    }
}
