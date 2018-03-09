package com.huntkey.rx.sceo.form.provider.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import com.huntkey.rx.sceo.form.provider.config.BizFormConstant;
import com.huntkey.rx.sceo.form.provider.service.BizFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglb on 2017/6/12.
 */
@RestController
@RequestMapping("/form")
public class BizFormController {

    private static Logger logger = LoggerFactory.getLogger(BizFormController.class);

    @Autowired
    private BizFormService bizFormService;

    /**
     * 根据表单Id查询表单历史版本
     * @param formId 表单Id
     * @retrun result
     */
    @RequestMapping("/getFormHistoryData")
    public Result getFormHistoryData(String formId){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        List<BizFormVersion> list;
        try{
            list = bizFormService.getBizFormVersion(formId);
            result.setData(list);
        }catch(Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
            logger.error("---e.getMessage={}", e.getMessage());
        }
        return result;
    }

    /**
     * 根据表单Id查询表单信息
     * @param formId 表单Id
     * @return result
     */
    @RequestMapping("/getFormId")
    public Result getByFormId(@RequestParam(value = "formId") String formId){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        BizFormSetting bizFormSetting = null;
        try{
            bizFormSetting = bizFormService.getBizFormSettingById(formId);
        }catch(Exception e){
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
            logger.error("---e.getMessage={}", e.getMessage());
        }
        result.setData(bizFormSetting);
        return result;
    }

    /**
     * 对表单进行启用，停用方法
     * @param map versionId 需要启用/停用的表单Id, status 启用/停用状态，9：启用，1：未启用
     * @return result
     */
    @RequestMapping("/changeFormStatus")
    public Result changeFormStatus(@RequestBody Map<String, String> map){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        int num;
        try {
            num = bizFormService.changeFormStatus(map);
            result.setData(num);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 根据表单编码得到下一最新版本号
     * @param formId 表单编码
     * @return result
     */
    @RequestMapping("/getVersionNext")
    public Result getVersionNext(String formId){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        int version = 0;
        try {
            version = bizFormService.getLatestVersion(formId);
            result.setData(version+1);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 修改列表页
     * @param bizFormListSetting 表单列表实体
     * @return result
     */
    @RequestMapping("/updateBizFormListSetting")
    public Result updateBizFormListSetting(BizFormListSetting bizFormListSetting){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        int num;
        try {
            num = bizFormService.updateBizFormListSetting(bizFormListSetting);
            if(num > 0){
                result.setData(true);
            }else{
                result.setData(false);
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 根据表单Id查询表单列表
     * @param versionId 表单版本Id
     * @return result
     */
    @RequestMapping("/selectByRelatedVersionId")
    public Result selectByRelatedVersionId(String versionId){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            result.setData(bizFormService.selectByRelatedVersionId(versionId));
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 创建表单列表
     * @param bizFormListSetting 表单列表
     */
    @RequestMapping("/createFormListSetting")
    public Result createFormListSetting(@RequestBody BizFormListSetting bizFormListSetting){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            bizFormService.createFormListSetting(bizFormListSetting);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("创建表单列表失败!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除表单的一个版本
     * @param versionId
     * @return result
     */
    @RequestMapping("/deleteFormVersion")
    public Result deleteFormVersion(String versionId){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            BizFormVersion bizFormVersion = new BizFormVersion();
            bizFormVersion.setVersionId(versionId);
            bizFormVersion.setDeleteFlag("1");
            bizFormService.deleteFormVersion(bizFormVersion);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            e.printStackTrace();
        }
        return result;
    }

}
