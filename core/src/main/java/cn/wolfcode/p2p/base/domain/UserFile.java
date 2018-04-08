package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/19.
 */
@Setter@Getter
public class UserFile extends BaseAuthDomain {
    private String image;
    private int score;
    private SystemDictionaryItem fileType;
    public String getJsonString(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("applier",applier.getUsername());
        map.put("fileType",fileType.getTitle());
        map.put("image",image);
        return JSON.toJSONString(map);
    }
}
