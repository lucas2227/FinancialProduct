package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.PaymentScheduleDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentScheduleDetailMapper {

    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PaymentScheduleDetail record);

    void updatePayDate(@Param("paymentScheduleId") Long paymentScheduleId, @Param("payDate") Date payDate);
}