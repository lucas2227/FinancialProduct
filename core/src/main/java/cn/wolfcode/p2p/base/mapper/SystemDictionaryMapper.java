package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface SystemDictionaryMapper {

    int insert(SystemDictionary record);

    SystemDictionary selectByPrimaryKey(Long id);

    int updateByPrimaryKey(SystemDictionary record);

    List queryPage(SystemDictionaryQueryObject qo);

    List<SystemDictionary> selectAll();
}