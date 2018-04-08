package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.RealAuth;
import cn.wolfcode.p2p.base.domain.Userinfo;
import cn.wolfcode.p2p.base.service.IRealAuthService;
import cn.wolfcode.p2p.base.service.IUserinfoService;
import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.website.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by seemygo on 2017/12/18.
 */
@Controller
public class RealAuthController {
    @Autowired
    private IUserinfoService userinfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Value("${file.path}")
    private String filePath;
    @RequestMapping("/realAuth")
    public String realAuthPage(Model model){
        Userinfo userinfo = userinfoService.getCurrent();
        //判断用户是否有实名认证的状态码
        if(userinfo.getIsRealAuth()){
            model.addAttribute("realAuth",realAuthService.get(userinfo.getRealAuthId()));
            //如果有,获取到userinfo中的realAuthId查询对应的实名认证度夏凝
            model.addAttribute("auditing",false);
            return "realAuth_result";
        }else{
            //如果没有,判断userinfo中是否有realAuthId
            //如果有,处于待审核的状态
            if(userinfo.getRealAuthId()==null){
                //如果没有,跳转申请页面
                return "realAuth";
            }else{
                model.addAttribute("auditing",true);
                return "realAuth_result";
            }
        }
    }
    @RequestMapping("/realAuth_save")
    @ResponseBody
    public JSONResult realAuthSave(RealAuth realAuth){
        JSONResult result = null;
        try{
            realAuthService.realAuthSave(realAuth);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(MultipartFile image){
        String imagePath = UploadUtil.upload(image, filePath);
        return imagePath;
    }
}
