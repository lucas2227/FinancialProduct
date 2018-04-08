package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2017/12/21.
 */
@Setter@Getter
public class BidRequestQueryObject extends QueryObject {
    private int bidRequestState = -1;
    public int[] states;
    private String orderByCondition;
}



