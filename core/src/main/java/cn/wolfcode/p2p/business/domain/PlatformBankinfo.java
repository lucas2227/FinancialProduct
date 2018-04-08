package cn.wolfcode.p2p.business.domain;

import cn.wolfcode.p2p.base.domain.BaseDomain;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/22.
 */
@Setter@Getter
public class PlatformBankinfo extends BaseDomain {
    private String bankName;    //银行名称
    private String accountNumber;  //银行账号
    private String bankForkname;    //支行名称
    private String accountName; //开户人姓名
    public String getJsonString(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("bankName",bankName);
        map.put("accountName",accountName);
        map.put("accountNumber",accountNumber);
        map.put("bankForkname",bankForkname);
        return JSON.toJSONString(map);
    }
}
