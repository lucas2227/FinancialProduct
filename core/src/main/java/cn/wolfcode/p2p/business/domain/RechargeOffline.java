package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseAuthDomain;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/22.
 */
@Setter@Getter
public class RechargeOffline extends BaseAuthDomain {
    private PlatformBankinfo bankInfo;//对应平台这帮胡
    private String tradeCode;//交易号
    private BigDecimal amount;//充值金额
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tradeTime;//充值时间
    private String note;//充值说明
    public String getJsonString(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("username",applier.getUsername());
        map.put("tradeCode",tradeCode);
        map.put("amount",amount);
        map.put("tradeTime",new SimpleDateFormat("yyyy-MM-dd").format(tradeTime));
        return JSON.toJSONString(map);
    }
}
