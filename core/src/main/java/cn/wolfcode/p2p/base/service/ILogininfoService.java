package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.Logininfo;

import java.util.List;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/12.
 */
public interface ILogininfoService {
    /**
     * 用户注册的功能
     * @param username
     * @param password
     */
    void register(String username,String password);

    /**
     * 根据用户判断在数据库中是否已存在
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * 登录方法
     * @param username
     * @param password
     * @return
     */
    Logininfo login(String username, String password,int userType);

    /**
     * 初始化第一个管理员
     */
    void initAdmin();

    /**
     * 自动补全
     * @param keyword
     * @return
     */
    List<Map<String,Object>> autoComplate(String keyword);
}
