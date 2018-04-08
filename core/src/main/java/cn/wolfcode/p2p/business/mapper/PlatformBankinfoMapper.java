package cn.wolfcode.p2p.business.mapper;

import cn.wolfcode.p2p.business.domain.PlatformBankinfo;
import cn.wolfcode.p2p.business.query.PlatformBankinfoQueryObject;

import java.util.List;

public interface PlatformBankinfoMapper {

    int insert(PlatformBankinfo record);

    PlatformBankinfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(PlatformBankinfo record);

    List queryPage(PlatformBankinfoQueryObject qo);

    List<PlatformBankinfo> selectAll();
}