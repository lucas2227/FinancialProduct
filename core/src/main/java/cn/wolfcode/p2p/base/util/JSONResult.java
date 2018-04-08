package cn.wolfcode.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by seemygo on 2017/12/12.
 */
@Setter@Getter
public class JSONResult {
    private boolean success = true;
    private String msg;
    //new JSONResult()---->{success:true,msg:''}
    public JSONResult() {
    }
    //new JSONResult(false,"保存失败")--->{success:false,msg:'保存失败'}
    public JSONResult(boolean success,String msg) {
        this.success = success;
        this.msg = msg;
    }
    //new JSONResult("保存成功");-->{success:true,msg:'保存成功'}
    public JSONResult(String msg) {
        this.msg = msg;
    }
}
