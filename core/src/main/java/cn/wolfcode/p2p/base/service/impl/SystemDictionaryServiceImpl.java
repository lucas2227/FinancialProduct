package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionary;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by seemygo on 2017/12/16.
 */
@Service@Transactional
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {
    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;
    @Override
    public int save(SystemDictionary systemDictionary) {
        return systemDictionaryMapper.insert(systemDictionary);
    }

    @Override
    public int update(SystemDictionary systemDictionary) {
        return systemDictionaryMapper.updateByPrimaryKey(systemDictionary);
    }

    @Override
    public SystemDictionary get(Long id) {
        return systemDictionaryMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(SystemDictionaryQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = systemDictionaryMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void saveOrUpdate(SystemDictionary systemDictionary) {
        if(systemDictionary.getId()==null){
            this.save(systemDictionary);
        }else{
            this.update(systemDictionary);
        }
    }

    @Override
    public List<SystemDictionary> selectAll() {
        return systemDictionaryMapper.selectAll();
    }
}
