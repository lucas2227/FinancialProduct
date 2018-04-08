package cn.wolfcode.p2p.base.service;

/**
 * Created by seemygo on 2017/12/15.
 */
public interface IVerifyCodeService {
    /**
     * 发送手机验证码
     * @param phoneNumber
     */
    void sendVerifyCode(String phoneNumber);

    /**
     * 校验验证码是否有效
     * @param phoneNumber
     * @param verifyCode
     * @return
     */
    boolean validate(String phoneNumber, String verifyCode);
}
