package com.huntkey.rx.sceo.form.client.feign;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.hystrix.BizFormHystrix;
import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by zhanglb on 2017/6/6.
 */
@FeignClient(value = "form-provider", fallback = BizFormHystrix.class)
public interface BizFormService {

    /**
     * 根据表单Id查询表单历史版本
     * @param formId 表单Id
     * @return result
     */
    @RequestMapping(value = "/form/getFormHistoryData", method = RequestMethod.GET)
    Result getFormHistoryData(@RequestParam(value = "formId") String formId);

    /**
     * 对表单进行启用，停用方法
     * @param map versionId 需要启用/停用的表单Id, status 启用/停用状态，9：启用，1：未启用
     * @return result
     */
    @RequestMapping(value = "/form/changeFormStatus", method = RequestMethod.POST)
    Result changeFormStatus(@RequestBody Map<String, String> map);

    /**
     * 根据表单编码得到下一最新版本号
     * @param formId 表单Id
     * @return result
     */
    @RequestMapping(value = "/form/getVersionNext", method = RequestMethod.GET)
    Result getVersionNext(@RequestParam(value = "formId") String formId);

    /**
     * 根据versionId查询表单列表
     * @param versionId 版本Id
     * @return result
     */
    @RequestMapping(value = "/form/selectByRelatedVersionId", method = RequestMethod.GET)
    Result selectByRelatedVersionId(@RequestParam(value = "versionId") String versionId);

    /**
     * 创建表单列表
     * @param bizFormListSetting 表单列表
     */
    @RequestMapping(value = "/form/createFormListSetting", method = RequestMethod.POST)
    Result createFormListSetting(@RequestBody BizFormListSetting bizFormListSetting);

    /**
     * 根据formListId删除表单列表
     * @param formListId 表单列表
     */
    @RequestMapping(value = "/form/deleteFormListSettingById", method = RequestMethod.DELETE)
    Result deleteFormListSettingById(@RequestParam(value = "formListId") String formListId);

    /**
     * 根据formId删除表单列表
     * @param formId 表单列表
     */
    @RequestMapping(value = "/form/deleteFormListSettingByFormId", method = RequestMethod.DELETE)
    Result deleteFormListSettingByFormId(@RequestParam(value = "formId") String formId);

    /**
     * 删除表单版本
     * @param versionId
     * @return result
     */
    @RequestMapping(value = "/form/deleteFormVersion", method = RequestMethod.DELETE)
    Result deleteFormVersion(@RequestParam(value = "versionId")String versionId);

}
