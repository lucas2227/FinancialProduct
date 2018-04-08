package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.domain.SystemDictionaryItem;
import cn.wolfcode.p2p.base.query.SystemDictionaryItemQueryObject;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.ISystemDictionaryService;
import cn.wolfcode.p2p.base.util.JSONResult;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by seemygo on 2017/12/16.
 */
@Controller
public class SystemDictionaryItemController {
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @RequestMapping("/systemDictionaryItem_list")
    public String SystemDictionaryItemPage(@ModelAttribute("qo") SystemDictionaryItemQueryObject qo, Model model){
        model.addAttribute("pageResult",systemDictionaryItemService.queryPage(qo));
        model.addAttribute("systemDictionaryGroups",systemDictionaryService.selectAll());
        return "systemdic/systemDictionaryItem_list";
    }
    @RequestMapping("/systemDictionaryItem_update")
    @ResponseBody
    public JSONResult update(SystemDictionaryItem systemDictionaryItem){
        JSONResult result = null;
        try{
            if(systemDictionaryItem.getParentId()==null){
                return new JSONResult(false,"需要选数据字典分类.");
            }
            systemDictionaryItemService.saveOrUpdate(systemDictionaryItem);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
