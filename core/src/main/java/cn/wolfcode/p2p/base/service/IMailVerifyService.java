package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.MailVerify;

/**
 * Created by seemygo on 2017/12/16.
 */
public interface IMailVerifyService {
    int save(MailVerify mailVerify);
    MailVerify selectByUUID(String uuid);
}
