package cn.wolfcode.p2p.business.service.impl;

import cn.wolfcode.p2p.base.util.UserContext;
import cn.wolfcode.p2p.business.domain.PlatformBankinfo;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.mapper.PlatformBankinfoMapper;
import cn.wolfcode.p2p.business.query.PlatformBankinfoQueryObject;
import cn.wolfcode.p2p.business.service.IPlatformBankinfoService;
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
public class PlatformBankinfoServiceImpl implements IPlatformBankinfoService {
    @Autowired
    private PlatformBankinfoMapper platformBankinfoMapper;
    @Override
    public int save(PlatformBankinfo platformBankinfo) {
        return platformBankinfoMapper.insert(platformBankinfo);
    }

    @Override
    public int update(PlatformBankinfo platformBankinfo) {
        return platformBankinfoMapper.updateByPrimaryKey(platformBankinfo);
    }

    @Override
    public PlatformBankinfo get(Long id) {
        return platformBankinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(PlatformBankinfoQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = platformBankinfoMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void saveOrUpdate(PlatformBankinfo platformBankinfo) {
        if(platformBankinfo.getId()==null){
            this.save(platformBankinfo);
        }else{
            this.update(platformBankinfo);
        }
    }

    @Override
    public List<PlatformBankinfo> selectAll() {
        return platformBankinfoMapper.selectAll();
    }

}
