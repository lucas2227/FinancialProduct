package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.query.RealAuthQueryObject;

import java.util.List;

public interface RealAuthMapper {

    int insert(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RealAuth record);

    List queryPage(RealAuthQueryObject qo);
}