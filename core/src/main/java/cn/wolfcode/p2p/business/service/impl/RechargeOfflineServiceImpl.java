package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.domain.Account;
import cn.wolfcode.p2p.base.service.IAccountService;
import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.mapper.RechargeOfflineMapper;
import cn.wolfcode.p2p.business.query.RechargeOfflineQueryObject;
import cn.wolfcode.p2p.business.service.IAccountFlowService;
import cn.wolfcode.p2p.business.service.IRechargeOfflineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by seemygo on 2017/12/22.
 */
@Service@Transactional
public class RechargeOfflineServiceImpl implements IRechargeOfflineService {
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Override
    public int save(RechargeOffline rechargeOffline) {
        return rechargeOfflineMapper.insert(rechargeOffline);
    }

    @Override
    public int update(RechargeOffline rechargeOffline) {
        return rechargeOfflineMapper.updateByPrimaryKey(rechargeOffline);
    }

    @Override
    public RechargeOffline get(Long id) {
        return rechargeOfflineMapper.selectByPrimaryKey(id);
    }

    @Override
    public void rechargeSave(RechargeOffline rechargeOffline) {
        RechargeOffline ro = new RechargeOffline();
        ro.setState(RechargeOffline.STATE_NORMAL);//状态:待审核
        ro.setApplier(UserContext.getCurrent());//申请人
        ro.setAmount(rechargeOffline.getAmount());//充值金额
        ro.setBankInfo(rechargeOffline.getBankInfo());//平台银行账户信息
        ro.setTradeCode(rechargeOffline.getTradeCode());//转账交易号
        ro.setApplyTime(new Date());//申请时间
        ro.setTradeTime(rechargeOffline.getTradeTime());//转账时间
        ro.setNote(rechargeOffline.getNote());//充值说明
        this.save(ro);
    }

    @Override
    public PageInfo queryPage(RechargeOfflineQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = rechargeOfflineMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void audit(Long id, int state, String remark) {
        //1.根据id获取线下充值审核对象,判断状态为待审核
        RechargeOffline ro = this.get(id);
        if(ro!=null && ro.getState() == RechargeOffline.STATE_NORMAL){
            //2.给对象添加对应属性.
            ro.setAuditor(UserContext.getCurrent());
            ro.setAuditTime(new Date());
            ro.setRemark(remark);
            if(state==RechargeOffline.STATE_PASS){
                //3.审核通过
                //      给审核对象设置状态
                ro.setState(RechargeOffline.STATE_PASS);
                //      找到对应的申请人的账户,可用金额增加
                Account applierAccount = accountService.get(ro.getApplier().getId());
                applierAccount.setUsableAmount(applierAccount.getUsableAmount().add(ro.getAmount()));
                accountService.update(applierAccount);
                //      生成充值成功的流水
                accountFlowService.createRechargeOfflineFlow(applierAccount,ro.getAmount());
            }else{
                //4.审核拒绝
                //      给审核对象设置状态
                ro.setState(RechargeOffline.STATE_REJECT);
            }
            this.update(ro);
        }

    }
}
