package cn.wolfcode.p2p.base.domain;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seemygo on 2017/12/14.
 */
@Setter
@Getter
public class SystemDictionary extends BaseDomain {
    private String title;//分类名称,如:教育背景
    private String sn;//分类编码 ,如:educationBackground
    public String getJsonString(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("title",title);
        map.put("sn",sn);
        //--->{id:1,title:xxx,sn:xxx}
        return JSON.toJSONString(map);
    }
}
