package cn.wolfcode.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by seemygo on 2017/12/16.
 */
@Setter@Getter
public class MailVerify extends BaseDomain {
    private Long userid;//发送邮件的用户Id
    private String uuid;//拼接在地址栏的uuid
    private String email;//发送的邮件
    private Date sendTime;//发送的事件
}
