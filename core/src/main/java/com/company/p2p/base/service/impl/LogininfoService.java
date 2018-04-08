package com.company.p2p.base.service.impl;

import com.company.p2p.base.mapper.LogininfoMapper;
import com.company.p2p.base.service.ILogininfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogininfoService implements ILogininfoService{

    @Autowired
    private LogininfoMapper logininfoMapper;

    @Override
    public void register(String username, String password) {

    }
}
