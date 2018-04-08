package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.RealAuthMapper;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.BitStatesUtils;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by seemygo on 2017/12/18.
 */
@Service@Transactional
public class RealAuthServiceImpl implements IRealAuthService {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private RealAuthMapper realAuthMapper;
    @Override
    public int save(RealAuth realAuth) {
        return realAuthMapper.insert(realAuth);
    }

    @Override
    public int update(RealAuth realAuth) {
        return realAuthMapper.updateByPrimaryKey(realAuth);
    }

    @Override
    public RealAuth get(Long id) {
        return realAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public void realAuthSave(RealAuth realAuth) {
        //1.判断是否已经实名认证
        Userinfo userinfo = userinfoService.getCurrent();
        if(!userinfo.getIsRealAuth()){
            //2.设置相关的参数
            RealAuth ra  =new RealAuth();
            ra.setRealName(realAuth.getRealName());
            ra.setIdNumber(realAuth.getIdNumber());
            ra.setSex(realAuth.getSex());
            ra.setBornDate(realAuth.getBornDate());
            ra.setAddress(realAuth.getAddress());
            ra.setImage1(realAuth.getImage1());
            ra.setImage2(realAuth.getImage2());

            ra.setApplier(UserContext.getCurrent());
            ra.setApplyTime(new Date());
            ra.setState(RealAuth.STATE_NORMAL);
            this.save(ra);
            //3.找到userinfo设置realAuthId
            userinfo.setRealAuthId(ra.getId());
            userinfoService.update(userinfo);
        }
    }

    @Override
    public PageInfo queryPage(RealAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = this.realAuthMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        //1.根据id获取RealAuth对象,判断这个对象处于待审核的状态
        RealAuth realAuth = this.get(id);
        if(realAuth!=null && realAuth.getState() == RealAuth.STATE_NORMAL){
            //2.给RealAuth设置相关的属性,审核人,审核时间,审核备注
            realAuth.setAuditor(UserContext.getCurrent());
            realAuth.setAuditTime(new Date());
            realAuth.setRemark(remark);
            Userinfo applierUserinfo = userinfoService.get(realAuth.getApplier().getId());
            if(state==RealAuth.STATE_PASS){
                //3.如果审核通过:
                realAuth.setState(RealAuth.STATE_PASS);
                //  设置RealAuth的状态为审核通过,找到申请人的userinfo对象,添加实名认证的状态码,设置的userinfo中的realName,idNUmber;
                applierUserinfo.addState(BitStatesUtils.OP_REAL_AUTH);
                applierUserinfo.setIdNumber(realAuth.getIdNumber());
                applierUserinfo.setRealName(realAuth.getRealName());
            }else{
                //4.如果审核拒绝
                realAuth.setState(RealAuth.STATE_REJECT);
                //  设置RealAuth的状态为审核拒绝,找到申请人的userinfo对象,设置RealAuthId=null
                applierUserinfo.setRealAuthId(null);
            }
            this.update(realAuth);
            userinfoService.update(applierUserinfo);
        }

    }
}
