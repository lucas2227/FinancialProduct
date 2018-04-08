package cn.wolfcode.p2p.mgrsite.controller;

import cn.wolfcode.p2p.base.query.IpLogQueryObject;
import cn.wolfcode.p2p.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by seemygo on 2017/12/15.
    后台日志的控制器
 */
@Controller
public class IpLogController {
    @Autowired
    private IIpLogService ipLogService;
    @RequestMapping("/ipLog")
    public String ipLogPage(@ModelAttribute("qo") IpLogQueryObject qo, Model model){
        model.addAttribute("pageResult",ipLogService.queryPage(qo));
        return "ipLog/list";
    }
}
