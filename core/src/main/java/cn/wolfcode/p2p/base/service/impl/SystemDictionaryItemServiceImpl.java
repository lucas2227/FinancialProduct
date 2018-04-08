package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryItemMapper;
import cn.wolfcode.p2p.base.mapper.SystemDictionaryMapper;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
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
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;
    @Override
    public int save(SystemDictionaryItem systemDictionaryItem) {
        return systemDictionaryItemMapper.insert(systemDictionaryItem);
    }

    @Override
    public int update(SystemDictionaryItem systemDictionaryItem) {
        return systemDictionaryItemMapper.updateByPrimaryKey(systemDictionaryItem);
    }

    @Override
    public SystemDictionaryItem get(Long id) {
        return systemDictionaryItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo queryPage(SystemDictionaryItemQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = systemDictionaryItemMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void saveOrUpdate(SystemDictionaryItem systemDictionaryItem) {
        if(systemDictionaryItem.getId()==null){
            this.save(systemDictionaryItem);
        }else{
            this.update(systemDictionaryItem);
        }
    }

    @Override
    public List<SystemDictionaryItem> queryListBySn(String sn) {
        return systemDictionaryItemMapper.queryListBySn(sn);
    }
}
