package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.PaymentSchedule;
import cn.wolfcode.p2p.business.query.PaymentScheduleQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2017/12/24.
 */
public interface IPaymentScheduleService {
    int save(PaymentSchedule paymentSchedule);
    int update(PaymentSchedule paymentSchedule);
    PaymentSchedule get(Long id);

    PageInfo queryPage(PaymentScheduleQueryObject qo);

    /**
     * 还款
     * @param id
     */
    void returnMoney(Long id);

    /**
     * 根据借款id查询该借款下的所有还款对象
     * @param bidRequestId
     * @return
     */
    List<PaymentSchedule> queryPaymentScheduleByBidRequestId(Long bidRequestId);
}
