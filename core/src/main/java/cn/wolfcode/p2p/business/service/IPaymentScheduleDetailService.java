package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;

import java.util.Date;

/**
 * Created by seemygo on 2017/12/24.
 */
public interface IPaymentScheduleDetailService {
    int save(PaymentScheduleDetail paymentScheduleDetail);
    int update(PaymentScheduleDetail paymentScheduleDetail);
    PaymentScheduleDetail get(Long id);

    void updatePayDate(Long paymentScheduleId, Date payDate);
}
