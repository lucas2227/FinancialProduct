package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2017/12/16.
 */
public interface ISystemDictionaryItemService {
    int save(SystemDictionaryItem systemDictionaryItem);
    int update(SystemDictionaryItem systemDictionaryItem);
    SystemDictionaryItem get(Long id);

    PageInfo queryPage(SystemDictionaryItemQueryObject qo);

    void saveOrUpdate(SystemDictionaryItem systemDictionaryItem);

    List<SystemDictionaryItem> queryListBySn(String sn);
}
