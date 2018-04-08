package cn.wolfcode.p2p.website.controller;

import cn.wolfcode.p2p.base.domain.UserFile;
import cn.wolfcode.p2p.base.service.ISystemDictionaryItemService;
import cn.wolfcode.p2p.base.service.IUserFileService;
import cn.wolfcode.p2p.base.util.JSONResult;
import cn.wolfcode.p2p.website.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seemygo on 2017/12/19.
 */
@Controller
public class UserFileController {
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;
    @Value("${file.path}")
    private String filePath;
    @RequestMapping("/userFile")
    public String userFilePage(Model model){
       /* 1.查询出数据库中当前用户没有选择风控类型的集合?unSelectList
        如果unSelectList.size()==0,说明数据库中当前用户的所有风控材料记录有已经选择的风控类型,跳转到userFiles.ftl查看页面.
                如果unSelectList.size()>0,说明数据库当前用户存在没有选择风控类型的风控材料记录,跳转到userFiles_commit.ftl.让用户选择对应风控材料类型.*/
        List<UserFile> unSelectList = userFileService.queryListByFileTypeCondition(false);//没有选择风控类型的集合
        if(unSelectList.size()==0){
            //说明数据库中要么从来没上传过图片,要么全部选择风控材料.
            //查询出所有的已经选择风控类型的材料集合
            List<UserFile> selectList = userFileService.queryListByFileTypeCondition(true);
            model.addAttribute("userFiles",selectList);
            return "userFiles";
        }else{
            model.addAttribute("userFiles",unSelectList);
            model.addAttribute("fileTypes",systemDictionaryItemService.queryListBySn("userFileType"));
            return "userFiles_commit";
        }
        /* model.addAttribute("userFiles",new ArrayList());
        return "userFiles";*//*
        //1.查询当前用户没有选择风控材料类型的数据集合
        List<UserFile> unSelectList = userFileService.queryUnSelectFileTypeList();
        model.addAttribute("userFiles",unSelectList);
        //2.查询风控材料数据字典明细的集合
        model.addAttribute("fileTypes",systemDictionaryItemService.queryListBySn("userFileType"));
        return "userFiles_commit";*/
    }
    @RequestMapping("/userFileUpload")
    @ResponseBody
    public String uploadImage(MultipartFile file){
        String imagePath = UploadUtil.upload(file, filePath);
        //往userfile表中插入数据
        userFileService.apply(imagePath);
        return imagePath;
    }
    @RequestMapping("/userFile_selectType")
    @ResponseBody
    public JSONResult selectType(Long[] id,Long[] fileType){
        JSONResult result = null;
        try{
            userFileService.selectType(id,fileType);
            result = new JSONResult();
        }catch(Exception e){
            e.printStackTrace();
            result = new JSONResult(false,e.getMessage());
        }
        return result;
    }
}
