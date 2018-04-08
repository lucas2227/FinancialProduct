package cn.wolfcode.p2p.base.service.impl;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.mapper.UserFileMapper;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by seemygo on 2017/12/19.
 */
@Service@Transactional
public class UserFileServiceImpl implements IUserFileService {
    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private IUserinfoService userinfoService;
    @Override
    public int save(UserFile userFile) {
        return userFileMapper.insert(userFile);
    }

    @Override
    public int update(UserFile userFile) {
        return userFileMapper.updateByPrimaryKey(userFile);
    }

    @Override
    public UserFile get(Long id) {
        return userFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public void apply(String imagePath) {
        UserFile uf = new UserFile();
        uf.setApplier(UserContext.getCurrent());
        uf.setApplyTime(new Date());
        uf.setState(UserFile.STATE_NORMAL);
        uf.setImage(imagePath);
        this.save(uf);
    }

    @Override
    public List<UserFile> queryUnSelectFileTypeList() {
        return userFileMapper.queryUnSelectFileTypeList(UserContext.getCurrent().getId());
    }

    @Override
    public void selectType(Long[] ids, Long[] fileTypes) {
        if(ids!=null&&fileTypes!=null&&ids.length==fileTypes.length){
            UserFile uf = null;
            SystemDictionaryItem fileType;
            for(int i=0;i<ids.length;i++){
                uf = this.get(ids[i]);
                //判断该条风控材料的申请是否是当前操作人.
                if(uf.getApplier().getId().equals(UserContext.getCurrent().getId())){
                    fileType = new SystemDictionaryItem();
                    fileType.setId(fileTypes[i]);
                    uf.setFileType(fileType);
                    this.update(uf);
                }
            }
        }
    }

    @Override
    public List<UserFile> queryListByFileTypeCondition(boolean isSelectType) {
        return userFileMapper.queryListByFileTypeCondition(isSelectType,UserContext.getCurrent().getId());
    }

    @Override
    public PageInfo queryPage(UserFileQueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize());
        List result = userFileMapper.queryPage(qo);
        return new PageInfo(result);
    }

    @Override
    public void aduit(Long id, int score, int state, String remark) {
        //1.根据id获取风控材料审核对象,判断这个对象处于待审核的状态
        UserFile uf = this.get(id);
        if(uf!=null && uf.getState()==UserFile.STATE_NORMAL){
            //2.设置相关属性审核人,审核时间,审核备注
            uf.setAuditor(UserContext.getCurrent());
            uf.setAuditTime(new Date());
            uf.setRemark(remark);
            //3.是否审核通过:
            if(state==UserFile.STATE_PASS){
                //  3.1修改审核的状态,设置对应的分数.
                uf.setState(UserFile.STATE_PASS);
                uf.setScore(score);
                //  3.2找到申请人的userinfo对象,给score添加对应分数.
                Userinfo applierUserinfo = userinfoService.get(uf.getApplier().getId());
                applierUserinfo.setScore(applierUserinfo.getScore()+score);
                userinfoService.update(applierUserinfo);
            }else{
                uf.setState(UserFile.STATE_REJECT);
            }
            this.update(uf);

        }


    }

    @Override
    public List<UserFile> queryListByLogininfoId(Long logininfoId) {
        return this.userFileMapper.queryListByLogininfoId(logininfoId,UserFile.STATE_PASS);
    }
}
