package cn.wolfcode.p2p.business.service;

import cn.wolfcode.p2p.business.domain.PlatformBankinfo;
import cn.wolfcode.p2p.business.domain.RechargeOffline;
import cn.wolfcode.p2p.business.query.PlatformBankinfoQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2017/12/22.
 */
public interface IPlatformBankinfoService {
    int save(PlatformBankinfo platformBankinfo);
    int update(PlatformBankinfo platformBankinfo);
    PlatformBankinfo get(Long id);

    PageInfo queryPage(PlatformBankinfoQueryObject qo);

    void saveOrUpdate(PlatformBankinfo platformBankinfo);

    List<PlatformBankinfo> selectAll();
}
