package cn.wolfcode.p2p.base.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by seemygo on 2017/12/15.
 */
@Setter@Getter
public class VerifyCodeVo implements Serializable {
    private String verifyCode;
    private String phoneNumber;
    private Date sendTime;
}
