package cn.wolfcode.p2p.base.query;

import cn.wolfcode.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by seemygo on 2017/12/14.
 */
@Getter@Setter
public class IpLogQueryObject extends QueryObject {
    private Date beginDate;
    private Date endDate;
    private int state = -1;
    private int userType = -1;
    private String username;
    public String getUsername(){
        return StringUtils.isEmpty(this.username)?null:username;
    }

    public Date getBeginDate() {
        return beginDate;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return DateUtil.getEndDate(endDate);
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
