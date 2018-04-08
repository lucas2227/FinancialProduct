package com.company.p2p.base.mapper;

import com.company.p2p.base.domain.Logininfo;
import java.util.List;

public interface LogininfoMapper {

    int insert(Logininfo record);

    Logininfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Logininfo record);
}