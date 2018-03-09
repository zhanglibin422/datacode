package com.huntkey.rx.sceo.form.client.feign;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.hystrix.FormManageHystrix;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by wangn1 on 2017/6/13.
 */
@FeignClient(value = "form-provider", fallback = FormManageHystrix.class)
public interface ClinetFormManageService {

    @RequestMapping(value = "/fm", method = RequestMethod.GET)
    Result  getFormById(@RequestParam("formId")String formId, @RequestParam(required = false,value = "versionId")String versionId, @RequestParam(required = false,value = "status")String status);

    /**
     * 创建表单
     * @param bizFormSetting 表单信息
     */
    @RequestMapping(value = "/fm", method = RequestMethod.POST)
    Result createForm(@RequestBody BizFormSetting bizFormSetting);

    /**
     * 更新表单
     * @param bizFormSetting 表单信息
     */
    @RequestMapping(value = "/fm", method = RequestMethod.PUT)
    Result updateForm(@RequestBody BizFormSetting bizFormSetting);

    /**
     * 删除表单
     * @param formId 表单ID
     */
    @RequestMapping(value = "/fm", method = RequestMethod.DELETE)
    Result deleteForm(@RequestParam(value = "formId") String formId);

    /**
     * 停用启用表单
     * @param param 表单信息
     */
    @RequestMapping(value = "/fm/status", method = RequestMethod.PUT)
    Result enabledForm(@RequestBody Map<String,String> param);

    /**
     * 保存表单数据
     * @param bizFormVersion 表单版本信息
     */
    @RequestMapping(value = "/fm/data", method = RequestMethod.POST)
    Result saveFormData(@RequestBody BizFormVersion bizFormVersion);

    /**
     * 根据formId获取所有的版本信息,不包括草稿状态
     * @param formId 表单ID
     */
    @RequestMapping(value = "/fm/versions", method = RequestMethod.GET)
    Result getVersionsByFormId(@RequestParam(required = true, value = "formId") String formId);

    /**
     * 保存表单业务数据
     * @param map 业务数据
     */
    @RequestMapping(value = "/fm/businessData", method = RequestMethod.POST)
    Result saveBusinessData(@RequestBody Map<String,String> map);

    /**
     * 获取表单业务数据
     * @param datas 业务数据
     */
    @RequestMapping(value = "/fm/businessData", method = RequestMethod.GET)
    Result getBusinessData(@RequestParam(value = "datas") String datas);

    /**
     * 删除表单业务数据
     * @param datas 业务数据
     */
    @RequestMapping(value = "/fm/businessData", method = RequestMethod.DELETE)
    Result deleteBusinessData(@RequestParam(value = "datas") String datas);

    /**
     * 获取流程待审列表
     * @return
     */
    @RequestMapping(value = "/fm/audits", method = RequestMethod.GET)
	Result getAuditList(@RequestParam("userId") String userId, @RequestParam("processStatus") String processStatus);
    
    /**
     * 获取流程待审列表
     * @return
     */
    @RequestMapping(value = "/fm/useForm", method = RequestMethod.GET)
	Result canUseForm();
    
    /**
     * 添加表单业务数据,可进行单个对象,多个对象与属性集的插入,传入ID则进行更新操作,不传则进行添加操作
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/fm/addFormData", method = RequestMethod.POST)
    Result addFormData(@RequestBody String formData);
    
    /**
     * 修改表单业务数据,可进行单个对象,多个对象的修改
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/fm/updateFormData", method = RequestMethod.POST)
	Result updateFormData(@RequestBody String formData);
    
    /**
     * 查询表单业务数据,根据传递的json参数可以进行分页,条件,排序查询
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/fm/findFormData", method = RequestMethod.POST)
    Result findFormData(@RequestBody String formData);
    
    /**
     * 删除表单业务数据,可以进行单个与批量删除
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/fm/deleteFormData", method = RequestMethod.POST)
    Result deleteFormData(@RequestBody String formData);
    
    /**
     * 获取自然人列表
     * @return
     */
    @RequestMapping(value = "/fm/people", method = RequestMethod.GET)
    Result getPeopleList();

    /**
 	 * 获取审核信息
 	 * @param taskId 任务节点ID
 	 * @return
 	 */
    @RequestMapping(value = "/fm/auditInfo",method = RequestMethod.GET)
	Result getAuditTaskInfo(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "formId") String formId,@RequestParam(value = "businessId") String businessId,@RequestParam(value = "userId") String userId,@RequestParam(value = "queryCondition") String queryCondition);

	/**
	 * 审核流程
	 * @param params
	 * @return
	 */
    @RequestMapping(value = "/fm/audit",method = RequestMethod.POST)
	Result audit(@RequestBody Map<String, String> params);

	
	/**
	 * 撤回流程
	 * @param hisTaskId 已审核的任务ID
	 * @param processInsId 执行实例ID
	 * @return
	 */
    @RequestMapping(value = "/fm/revoke",method = RequestMethod.GET)
	Result revoke(@RequestParam(value = "hisTaskId") String hisTaskId, @RequestParam(value = "processInsId") String processInsId);


	/**
	 * 根据员工ID查询员工信息
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/fm/staff/{staffId}",method = RequestMethod.GET)
	Result getStaffInfoById(@PathVariable("staffId") String staffId);
}
