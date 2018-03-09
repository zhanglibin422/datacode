package com.huntkey.rx.sceo.form.provider.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;

/**
 * Created by wangn1 on 2017/6/14.
 */
public interface FormManageService {

    /**
     * 根据表单ID查询表单
     * @param formId 表单ID
     * @param versionId 版本ID 可以为空
     */
    BizFormSetting getFormById(String formId, String versionId, String status);

    /**
     * 创建表单
     * @param bizFormSetting 表单信息
     */
    String createForm(BizFormSetting bizFormSetting);

    /**
     * 根据表单名查询是否存在相同名称的表单
     * @param formName 表单名称
     */
    BizFormSetting getFormByName(String formName);

    /**
     * 更新表单
     * @param bizFormSetting 表单信息
     */
    void updateForm(BizFormSetting bizFormSetting);

    /**
     * 删除表单
     * @param formId 表单ID
     */
    void deleteForm(String formId);

    /**
     * 停用启用表单
     * @param param 表单对象
     */
    void enabledForm(Map<String,String> param);

    /**
     * 根据表单ID查询表单版本列表
     * @param param 表单ID
     */
    List<BizFormVersion> getVersionsByFormId(Map<String,String> param);

    /**
     * 停用/启用表单版本信息
     * @param param 对象
     */
    void enabledVersion(Map<String,String> param);

    /**
     * 保存表单数据
     * @param bizFormVersion 表单版本信息
     */
    String saveFormData(BizFormVersion bizFormVersion);

    /**
     * 停用所有版本信息
     * @param param 更新参数
     */
    void stopVersion(Map<String,String> param);

    /**
     * 启用表单
     * @param versions 版本列表
     * @param param 参数
     */
    void startForm(List<BizFormVersion> versions, Map<String, String> param);

    BizFormVersion getVersionByStatus(List<BizFormVersion> versions, String status);

    /**
     * 获取可制单据信息
     * @return
     */
	List<BizFormSetting> canUseForm();

	 /** 
	  * 获取流程待审列表
	  * @return
	  */
	Result getAuditList(String userId, String processStatus);

	/**
    * 添加表单业务数据,可进行单个对象,多个对象与属性集的插入,传入ID则进行更新操作,不传则进行添加操作
    * @param formData 参数json
    * @return 操作结果
    */
	Result addFormData(String formData);
	
	/**
	 * 获取员工列表
	 */
	JSONArray getStaffList();

	/**
	 * 获取自然人列表
	 * @return
	 */
	JSONArray getPeopleList();

	/**
  	 * 获取审核信息
  	 * @param taskId 任务节点ID
  	 * @return
  	 */
	JSONObject getAuditTaskInfo(String taskId, String formId, String businessId,String userId,String queryCondition);

	Integer updateFormDataStatus(String formId, String businessId, String status);

	Map<String,String> getStaffInfoById(String staffId);
}
