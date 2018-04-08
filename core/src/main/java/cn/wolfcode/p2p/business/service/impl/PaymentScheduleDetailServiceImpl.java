package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import cn.wolfcode.p2p.business.mapper.PaymentScheduleDetailMapper;
import cn.wolfcode.p2p.business.service.IPaymentScheduleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by seemygo on 2017/12/24.
 */
@Service@Transactional
public class PaymentScheduleDetailServiceImpl implements IPaymentScheduleDetailService {
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;
    @Override
    public int save(PaymentScheduleDetail paymentScheduleDetail) {
        return paymentScheduleDetailMapper.insert(paymentScheduleDetail);
    }

    @Override
    public int update(PaymentScheduleDetail paymentScheduleDetail) {
        return paymentScheduleDetailMapper.updateByPrimaryKey(paymentScheduleDetail);
    }

    @Override
    public PaymentScheduleDetail get(Long id) {
        return paymentScheduleDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updatePayDate(Long paymentScheduleId, Date payDate) {
        paymentScheduleDetailMapper.updatePayDate(paymentScheduleId,payDate);
    }
}
