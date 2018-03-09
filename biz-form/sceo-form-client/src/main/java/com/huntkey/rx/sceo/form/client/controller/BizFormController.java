package com.huntkey.rx.sceo.form.client.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.BizFormService;
import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;

/**
 * Created by zhanglb on 2017/6/6.
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
     * @return result
     */
    @GetMapping("/getFormHistoryData")
    public Result getFormHistoryData(@RequestParam(value = "formId") String formId){
        return bizFormService.getFormHistoryData(formId);
    }

    /**
     * 对表单进行启用，停用方法
     * @param map versionId 需要启用/停用的表单Id, status 启用/停用状态，9：启用，1：未启用
     * @return result
     */
    @PostMapping("/changeFormStatus")
    public Result changeFormStatus(@RequestBody Map<String, String> map){
        return bizFormService.changeFormStatus(map);
    }

    /**
     * 根据表单编码得到下一最新版本号
     * @param formId 表单Id
     * @return result
     */
    @RequestMapping("/getVersionNext")
    public Result getVersionNext(@RequestParam(value = "formId") String formId){
        return bizFormService.getVersionNext(formId);
    }

    /**
     * 删除表单版本
     * @param versionId
     * @return result
     */
    @RequestMapping("/deleteFormVersion")
    public Result deleteFormVersion(@RequestParam(value = "versionId") String versionId){
        return bizFormService.deleteFormVersion(versionId);
    }

    /**
     * 根据versionId查询表单列表
     * @param versionId 版本Id
     * @return result
     */
    @RequestMapping("/selectByRelatedVersionId")
    public Result selectByRelatedVersionId(@RequestParam(value = "versionId")String versionId){
        return bizFormService.selectByRelatedVersionId(versionId);
    }

    /**
     * 创建表单列表
     * @param bizFormListSetting 表单列表
     */
    @RequestMapping("/createFormListSetting")
    public Result createFormListSetting(@RequestBody BizFormListSetting bizFormListSetting){
        return bizFormService.createFormListSetting(bizFormListSetting);
    }

    /**
     * 根据formListId删除表单列表
     * @param formListId 表单列表
     */
    @RequestMapping("/deleteFormListSettingById")
    public Result deleteFormListSettingById(@RequestParam(value = "formListId") String formListId){
        return bizFormService.deleteFormListSettingById(formListId);
    }

    /**
     * 根据formId删除表单列表
     * @param formId 表单列表
     */
    @RequestMapping("/deleteFormListSettingByFormId")
    public Result deleteFormListSettingByFormId(@RequestParam(value = "formId") String formId){
        return bizFormService.deleteFormListSettingByFormId(formId);
    }

}
