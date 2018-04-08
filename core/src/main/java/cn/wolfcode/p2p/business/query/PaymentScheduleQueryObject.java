package cn.wolfcode.p2p.business.query;

import cn.wolfcode.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2017/12/24.
 */
@Setter@Getter
public class PaymentScheduleQueryObject extends QueryObject {
    private Long currentUserId;
}
