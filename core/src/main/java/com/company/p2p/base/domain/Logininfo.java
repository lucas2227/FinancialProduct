package com.company.p2p.base.domain;

public class Logininfo extends BaseDomain{
    private static final int STATE_NOMAL = 0;
    private static final int STATE_LOCK = 1;
    private String username;
    private String password;
    private int state;
}
