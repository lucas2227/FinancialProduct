package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuthDomain;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2017/12/21.
 */
@Setter@Getter
public class BidRequestAuditHistory extends BaseAuthDomain {
    public static  final int AUDITTYPE_PUBLISH_AUDIT = 0;//发标前审核
    public static  final int AUDITTYPE_FULL_AUDIT1 = 1;//满标一审
    public static  final int AUDITTYPE_FULL_AUDIT2 = 2;//满标二审
    private Long bidRequestId;//关联借款对象
    private int auditType;//审核的类型.
}
