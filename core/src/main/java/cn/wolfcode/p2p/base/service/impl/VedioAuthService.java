package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.Logininfo;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.mapper.VedioAuthMapper;
import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.service.IVedioAuthService;
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
 * Created by seemygo on 2017/12/19.
 */
@Service@Transactional
public class VedioAuthService implements IVedioAuthService {
    @Autowired
    private VedioAuthMapper vedioAuthMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Override
    public int save(VedioAuth vedioAuth) {
        return vedioAuthMapper.insert(vedioAuth);
    }

    @Override
    public VedioAuth get(Long id) {
        return vedioAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(VedioAuthQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = vedioAuthMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void aduit(Long logininfoValue, int state, String remark) {
        //1.根据logininfoValue查询对应的申请人的userinfo,判断是否有视频认证.
        Userinfo applierUserinfo = userinfoService.get(logininfoValue);
        if(applierUserinfo!=null && !applierUserinfo.getIsVedioAuth()){
            //2.添加VedioAuth对象,设置相关的参数
            VedioAuth va = new VedioAuth();
            Logininfo logininfo = new Logininfo();
            logininfo.setId(logininfoValue);
            va.setApplier(logininfo);
            va.setApplyTime(new Date());
            va.setAuditor(UserContext.getCurrent());
            va.setAuditTime(new Date());
            va.setRemark(remark);
            //3.如果审核成功
            if(state==VedioAuth.STATE_PASS){
                va.setState(VedioAuth.STATE_PASS);
                //  找到申请人,给他添加视频认证的状态码
                applierUserinfo.addState(BitStatesUtils.OP_VEDIO_AUTH);
                userinfoService.update(applierUserinfo);
            }else{
                va.setState(VedioAuth.STATE_REJECT);
            }
            //
            this.save(va);
        }



    }
}
