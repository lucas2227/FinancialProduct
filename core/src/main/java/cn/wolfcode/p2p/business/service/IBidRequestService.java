package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.business.domain.BidRequest;
import cn.wolfcode.p2p.business.query.BidRequestQueryObject;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by seemygo on 2017/12/19.
 */
public interface IBidRequestService {
     int save(BidRequest bidRequest);
     int update(BidRequest bidRequest);
     BidRequest get(Long id);

     /**
      * 借款申请
      * @param bidRequest
      */
     void apply(BidRequest bidRequest);

     /**
      * 判断用户是否有借款的资格
      * @param userinfo
      * @return
     */
     boolean canApply(Userinfo userinfo);

     PageInfo queryPage(BidRequestQueryObject qo);

     /**
      * 发标前审核
      * @param id
      * @param state
      * @param remark
     */
     void publishAudit(Long id, int state, String remark);

     List<BidRequest> queryIndexList(BidRequestQueryObject qo);

     /**
      * 投标
      * @param bidRequestId
      * @param amount
     */
     void bid(Long bidRequestId, BigDecimal amount);

     /**
      * 满标一审
      * @param id
      * @param state
      * @param remark
     */
     void audit1(Long id, int state, String remark);

     /**
      * 满标二审
      * @param id
      * @param state
      * @param remark
     */
     void audit2(Long id, int state, String remark);
}
