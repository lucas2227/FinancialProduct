package cn.wolfcode.p2p.base.mapper;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.query.UserFileQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {

    int insert(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    int updateByPrimaryKey(UserFile record);

    List<UserFile> queryUnSelectFileTypeList(Long logininfoId);

    List<UserFile> queryListByFileTypeCondition(@Param("isSelectType") boolean isSelectType,@Param("logininfoId") Long logininfoId);

    List queryPage(UserFileQueryObject qo);

    List<UserFile> queryListByLogininfoId(@Param("logininfoId")Long logininfoId,@Param("state")int state);
}