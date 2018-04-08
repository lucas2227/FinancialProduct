package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.VedioAuth;
import cn.wolfcode.p2p.base.query.VedioAuthQueryObject;
import com.github.pagehelper.PageInfo;

/**
 * Created by seemygo on 2017/12/19.
 */
public interface IVedioAuthService {
    int save(VedioAuth vedioAuth);
    VedioAuth get(Long id);

    PageInfo queryPage(VedioAuthQueryObject qo);

    /**
     * 视频审核
     * @param logininfoValue
     * @param state
     * @param remark
     */
    void aduit(Long logininfoValue, int state, String remark);
}
