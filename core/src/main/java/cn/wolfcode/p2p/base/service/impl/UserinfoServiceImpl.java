package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.MailVerify;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserinfoMapper;
import cn.wolfcode.p2p.base.service.IMailVerifyService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVerifyCodeService;
import cn.wolfcode.p2p.base.util.BidConst;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.DateUtil;
import cn.wolfcode.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by seemygo on 2017/12/14.
 */
@Service@Transactional
public class UserinfoServiceImpl implements IUserinfoService {
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private IMailVerifyService mailVerifyService;
    @Override
    public int save(Userinfo userinfo) {
        return userinfoMapper.insert(userinfo);
    }

    @Override
    public int update(Userinfo userinfo) {
        int count = userinfoMapper.updateByPrimaryKey(userinfo);
        if(count==0){
            throw new RuntimeException("乐观锁异常,userinfoId:"+userinfo.getId());
        }
        return count;
    }

    @Override
    public Userinfo get(Long id) {
        return userinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Userinfo getCurrent() {
        return this.get(UserContext.getCurrent().getId());
    }

    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        //1.验证传入的手机和发送的手机号是否一致,验证码一致,发送的时间和当前时间的间隔没有超过短信的有效时间
        boolean isVaild = verifyCodeService.validate(phoneNumber,verifyCode);
        if(isVaild){
            //2.判断用户是否已经绑定手机号码,如果绑定就抛出.
            Userinfo userinfo = this.get(UserContext.getCurrent().getId());
            if(userinfo.getHasBindPhone()){
                throw new RuntimeException("您已经绑定手机了,请不要重复绑定");
            }
            //3.如果没有绑定,给当前的用户的userinfo对象添加手机绑定的验证码,设置userinfo中的phoneNumber
            userinfo.addState(BitStatesUtils.OP_BIND_PHONE);
            userinfo.setPhoneNumber(phoneNumber);
            this.update(userinfo);
        }else{
            throw new RuntimeException("验证码校验失败,请重新发送.");
        }
    }

    @Override
    public void bindEmail(String key) {
        //1.根据key查询MailVerify对象
        MailVerify mailVerify = mailVerifyService.selectByUUID(key);
        //2.如果mailVerify对象为null,说明这个key不是我们生成,抛出异常.
        if(mailVerify==null){
            throw new RuntimeException("认证地址有误,请重新发送.");
        }
        //3.判断时间是否在有效时间之内
        if(DateUtil.getBetweenTime(mailVerify.getSendTime(),new Date())> BidConst.EMAIL_VAILD_DAY*24*60*60){
            throw new RuntimeException("认证邮件已经过期,请重新发送");
        }
        Userinfo userinfo = this.get(mailVerify.getUserid());
        //4.判断用户是否已经邮箱绑定了,如果已经绑定抛出异常
        if(userinfo.getHasBindEmail()){
            throw new RuntimeException("您已经绑定过邮箱了,请不要重复绑定.");
        }
        //5.给用户添加邮箱认证的状态码,给userinfo设置email属性
        userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
        userinfo.setEmail(mailVerify.getEmail());
        this.update(userinfo);
    }

    @Override
    public void basicInfoSave(Userinfo userinfo) {
        //1.获取当前登录用户的userinfo对象
        Userinfo current = this.getCurrent();
        //2.设置相关的属性
        current.setEducationBackground(userinfo.getEducationBackground());
        current.setHouseCondition(userinfo.getHouseCondition());
        current.setKidCount(userinfo.getKidCount());
        current.setIncomeGrade(userinfo.getIncomeGrade());
        current.setMarriage(userinfo.getMarriage());
        //3.如果用户之前没有填写过基本资料,第一次填写之后,给当前用户添加基本信息状态码
        if(!current.getIsBasicInfo()){
            current.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        this.update(current);
    }
}
