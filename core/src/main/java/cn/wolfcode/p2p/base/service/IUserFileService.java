package cn.wolfcode.p2p.base.service;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by seemygo on 2017/12/19.
 */
public interface IUserFileService {
    int save(UserFile userFile);
    int update(UserFile userFile);
    UserFile get(Long id);

    /**
     * 风控材料上传
     * @param imagePath
     */
    void apply(String imagePath);

    /**
     * 查询对应用户风控材料类型为null的数据
     * @return
     */
    List<UserFile> queryUnSelectFileTypeList();

    /**
     * 选择风控类型
     * @param id
     * @param fileType
     */
    void selectType(Long[] ids, Long[] fileTypes);

    /**
     * 根据条件查询风控材料集合
     * @param isSelectType:查询是否已经选择了风控类型的记录.
     * @return
     */
    List<UserFile> queryListByFileTypeCondition(boolean isSelectType);

    PageInfo queryPage(UserFileQueryObject qo);

    /**
     * 风控材料审核
     * @param id
     * @param score
     * @param state
     * @param remark
     */
    void aduit(Long id, int score, int state, String remark);

    List<UserFile> queryListByLogininfoId(Long logininfoId);
}
