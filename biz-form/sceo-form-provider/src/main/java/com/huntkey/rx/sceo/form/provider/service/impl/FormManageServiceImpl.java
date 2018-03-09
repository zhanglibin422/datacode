package com.huntkey.rx.sceo.form.provider.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.sceo.form.client.feign.CommomService;
import com.huntkey.rx.sceo.form.client.feign.DymamicMethodService;
import com.huntkey.rx.sceo.form.client.feign.ModelerService;
import com.huntkey.rx.sceo.form.client.feign.ServiceCenterService;
import com.huntkey.rx.sceo.form.client.feign.WorkFlowService;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import com.huntkey.rx.sceo.form.common.model.Condition;
import com.huntkey.rx.sceo.form.common.model.JsEntity;
import com.huntkey.rx.sceo.form.common.model.QueryParam;
import com.huntkey.rx.sceo.form.common.model.Search;
import com.huntkey.rx.sceo.form.provider.config.BizFormConstant;
import com.huntkey.rx.sceo.form.provider.dao.BizFormVersionDao;
import com.huntkey.rx.sceo.form.provider.dao.FormManageDao;
import com.huntkey.rx.sceo.form.provider.service.FormManageService;


/**
 * Created by wangn1 on 2017/6/14.
 */
@Service
@Transactional(readOnly = true)
public class FormManageServiceImpl implements FormManageService {

    @Autowired
    private FormManageDao formManageDao;

    @Autowired
    private BizFormVersionDao bizFormVersionDao;
    
    @Autowired
    private WorkFlowService workFlowService; //工作流信息接口
    
    @Autowired
    private ServiceCenterService serviceCenterService; //服务中心接口地址
    
    @Autowired
    private CommomService commomService; //公共服务接口
    
    @Autowired
    private ModelerService modelerService;//modeler
    
    @Autowired
    private DymamicMethodService dymamicMethodService; //回写资源的接口
    
    /**
     * 根据表单ID查询表单
     * @param formId 表单ID
     * @param versionId 版本ID 可以为空
     */
    public BizFormSetting getFormById(String formId, String versionId, String status) {
        Map<String,String> param = new HashMap<String, String>();
        param.put("formId", formId);
        BizFormVersion versionInfo = null;
        if(StringUtil.isNullOrEmpty(versionId)){
            if(StringUtil.isNullOrEmpty(status)){
                param.put("formStatus", BizFormConstant.VERSION_DRAFT_STATUS);
            }else{
                param.put("formStatus", status);
            }
            versionInfo = bizFormVersionDao.getStartVersionByFormId(param);
        }else{
            versionInfo = bizFormVersionDao.getVersionById(versionId);
        }
        BizFormSetting bizFormSetting = formManageDao.getFormById(formId);
        if(!StringUtil.isNullOrEmpty(versionInfo)){
            bizFormSetting.setBizFormVersion(versionInfo);
        }
        return bizFormSetting;
    }

    /**
     * 创建表单
     * @param bizFormSetting 表单信息
     */
    @Transactional(readOnly = false)
    public String createForm(BizFormSetting bizFormSetting) {
        String formId = UuidCreater.uuid();
        bizFormSetting.setFormId(formId); //设置ID
        bizFormSetting.setCreatedTime(new Date()); //创建时间
        bizFormSetting.setCreatedBy("developer"); //创建者
        Result rs = modelerService.getSimpleClassById(bizFormSetting.getRelatedModelId());
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(rs.getData())); //流程JSON信息
        bizFormSetting.setEdmcNameEn(json.getString("edmcNameEn"));
        if(StringUtil.isNullOrEmpty(bizFormSetting.getFormCode())){
            bizFormSetting.setFormCode(UuidCreater.uuid());
        }
        bizFormSetting.setFormStatus(BizFormConstant.FORM_STOP_STATUS);
        formManageDao.createForm(bizFormSetting);
        return formId;
    }

    /**
     * 根据表单名查询是否存在相同名称的表单
     * @param formName 表单名称
     */
    @Override
    public BizFormSetting getFormByName(String formName) {
        return formManageDao.getFormByName(formName);
    }

    /**
     * 更新修改表单
     * @param bizFormSetting 表单信息
     */
    @Transactional(readOnly = false)
    public void updateForm(BizFormSetting bizFormSetting) {
        bizFormSetting.setUpdatedTime(new Date()); //创建时间
        bizFormSetting.setUpdatedBy("developer"); //创建者
        formManageDao.updateForm(bizFormSetting);
    }

    /**
     * 删除表单
     * @param formId 表单ID
     */
    @Transactional(readOnly = false)
    public void deleteForm(String formId){
        formManageDao.deleteForm(formId);
    }

    /**
     * 停用启用表单
     * @param param 表单对象
     */
    @Override
    @Transactional(readOnly = false)
    public void enabledForm(Map<String,String> param) {
        formManageDao.enabledForm(param);
    }

    /**
     * 根据表单ID查询表单版本列表
     *
     * @param param 表单ID
     */
    @Override
    public List<BizFormVersion> getVersionsByFormId(Map<String,String> param) {
        return bizFormVersionDao.getVersionsByFormId(param);
    }

    /**
     * 停用/启用表单版本信息
     *
     * @param param 对象
     */
    @Override
    @Transactional(readOnly = false)
    public void enabledVersion(Map<String, String> param) {
        bizFormVersionDao.enabledVersion(param);
    }

    /**
     * 停用所有版本信息
     * @param param 更新参数
     */
    @Transactional(readOnly = false)
    public void stopVersion(Map<String,String> param){
        bizFormVersionDao.stopVersion(param);
    }

    /**
     * 启用表单
     *
     * @param versions 版本列表
     * @param param   非空字段fromId表单ID formStatus状态 可空字段versionId 版本ID
     */
    @Override
    @Transactional(readOnly = false)
    public void startForm(List<BizFormVersion> versions, Map<String, String> param) {
        BizFormVersion bizFormVersion = null;
        String versionId = UuidCreater.uuid();
        //1.如果只有一条记录,则肯定为草稿数据
        if(versions.size() == 1){
            bizFormVersion = versions.get(0); //草稿数据
            //将草稿复制一条,生存一个新的版本
            bizFormVersion.setVersionId(versionId); //设置ID
            bizFormVersion.setFormVersion(bizFormVersionDao.getLatestVersion(param.get("formId"))+1); //设置最大版本号
            bizFormVersion.setCreatedBy("developer");
            bizFormVersion.setCreatedTime(new Date());
            bizFormVersion.setUpdatedTime(new Date());
            bizFormVersion.setFormStatus(BizFormConstant.FORM_START_STATUS); //设置状态为启用状态
            bizFormVersionDao.createVersion(bizFormVersion);
        }else{ //2.版本信息大于一条
            //判断是否传入了指定要启用的版本ID
            if(StringUtil.isNullOrEmpty(param.get("versionId"))){ //没有指定versionId
                //(1)判断存不存在启用状态的版本,如果存在,则不做任何操作
                bizFormVersion = getVersionByStatus(versions, BizFormConstant.FORM_START_STATUS); //获取草稿状态的数据
                if(null == bizFormVersion){
                    //用草稿创建新的版本
                    bizFormVersion = getVersionByStatus(versions, BizFormConstant.VERSION_DRAFT_STATUS); //获取草稿状态的数据
                    if(null != bizFormVersion){
                        bizFormVersion.setVersionId(versionId); //设置ID
                        bizFormVersion.setFormVersion(bizFormVersionDao.getLatestVersion(param.get("formId"))+1); //设置最大版本号
                        bizFormVersion.setCreatedBy("developer");
                        bizFormVersion.setCreatedTime(new Date());
                        bizFormVersion.setUpdatedTime(new Date());
                        bizFormVersion.setFormStatus(BizFormConstant.FORM_START_STATUS); //设置状态为启用状态
                        bizFormVersionDao.createVersion(bizFormVersion);
                    }
                }
            }else{ //指定了versionId
                //启用指定的版本为启用状态
                param.put("formStatus", BizFormConstant.FORM_STOP_STATUS);
                param.put("draftStatus", BizFormConstant.VERSION_DRAFT_STATUS);
                bizFormVersionDao.stopVersion(param); //停用所有版本(不包括草稿状态)
                param.put("formStatus", BizFormConstant.FORM_START_STATUS);
                bizFormVersionDao.enabledVersion(param); //启用指定版本
            }
        }
        //3.启用表单
        formManageDao.enabledForm(param);
    }

    /**
     * 根据传入的状态后去list中的指定状态的数据
     * @param versions 版本列表
     * @param status 状态
     */
    public BizFormVersion getVersionByStatus(List<BizFormVersion> versions, String status){
        for(BizFormVersion version : versions){
            if(version.getFormStatus().equals(status)){
                return version;
            }
        }
        return null;
    }

    /**
     * 保存表单数据,生成草稿数据
     *
     * @param bizFormVersion 表单版本信息
     */
    @Override
    @Transactional(readOnly = false)
    public String saveFormData(BizFormVersion bizFormVersion) {
        String versionId = "";
//        if(StringUtil.isNullOrEmpty(bizFormVersion.getVersionId())){ // 新增操作
//            versionId = UuidCreater.uuid();
//            bizFormVersion.setCreatedBy("developer");
//            bizFormVersion.setCreatedTime(new Date());
//            //2.插入一条版本数据
//            bizFormVersion.setVersionId(versionId);
//            bizFormVersion.setFormStatus(BizFormConstant.FORM_STOP_STATUS); //未启用状态
//            int versionNo = bizFormVersionDao.getLatestVersion(bizFormVersion.getFormId());
//            bizFormVersion.setFormVersion(versionNo+1); //设置最大版本号
//            bizFormVersionDao.createVersion(bizFormVersion);
//        }else{ //更新
//            bizFormVersion.setUpdatedTime(new Date());
//            bizFormVersion.setUpdatedBy("developer");
//            bizFormVersionDao.updateVersion(bizFormVersion);
//        }

        //查询当前表单是否有草稿状态的版本
        Map<String,String> param = new HashMap<String, String>();
        param.put("formId", bizFormVersion.getFormId());
        param.put("formStatus", BizFormConstant.VERSION_DRAFT_STATUS);
        BizFormVersion versionInfo = bizFormVersionDao.getDraftVersionsByFormId(param);
        if(null == versionInfo){ //新增草稿
            versionId = UuidCreater.uuid();
            bizFormVersion.setVersionId(versionId);
            bizFormVersion.setFormStatus(BizFormConstant.VERSION_DRAFT_STATUS); //草稿状态
            bizFormVersion.setFormVersion(0); //初始版本
            bizFormVersion.setCreatedBy("developer");
            bizFormVersion.setCreatedTime(new Date());
            bizFormVersion.setUpdatedTime(new Date());
            bizFormVersionDao.createVersion(bizFormVersion);
        }else{ //覆盖草稿
            versionId = versionInfo.getVersionId();
            versionInfo.setFormData(bizFormVersion.getFormData()); //覆盖表单设计数据
            versionInfo.setUpdatedTime(new Date());
            versionInfo.setUpdatedBy("developer");
            versionInfo.setFormListFlag(bizFormVersion.getFormListFlag());
            bizFormVersionDao.updateVersion(versionInfo);
        }
        return versionId;
    }
    
    /**
     * 获取可制单据信息
     * @return
     */
	public List<BizFormSetting> canUseForm(){
		Map<String,String> param = Maps.newHashMap();
		param.put("formStatus", BizFormConstant.FORM_START_STATUS);
		param.put("deleteFlag", BizFormConstant.UN_DELETE_FLAG);
		return formManageDao.canUseForm(param);
	}

	/** 
	 * 获取流程待审列表
	 * @return
	 */
	public Result getAuditList(String userId, String processStatus){
		Result rs = new Result();
		try{
			rs = workFlowService.getToDoList(userId, processStatus);
		}catch(Exception e){
			rs.setRetCode(Result.RECODE_ERROR);
			rs.setErrMsg("流程服务异常!");
			return rs;
		}
		JSONArray rs_arrsy = new JSONArray(); //返回值
		if(rs.getRetCode() == 1){
			JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(rs.getData())); //转成json数组对象
			QueryParam param = new QueryParam();
			JSONObject jsonObject = null;
			BizFormSetting bizFormSetting = null;
			String formId = "",tableName = "",businessId = ""; //表单ID,EDM模型名称,业务数据ID
			Result temp_rs = null;
			for(Object o : jsonArray){
				jsonObject = JSONObject.parseObject(o.toString());
				formId = jsonObject.getString("formId");
				tableName = jsonObject.getString("tableName");
				businessId = jsonObject.getString("businessId");
				if(!StringUtil.isNullOrEmpty(formId)){
					bizFormSetting = formManageDao.getFormById(formId);
					jsonObject.put("formName", bizFormSetting.getFormName()); //单据类型
				}
				//通过服务中心获取表单业务数据
				param.setEdmName(tableName);
				param.setSearch(new Search(new Condition[]{new Condition("id","=",businessId)}));
				try{
					rs = serviceCenterService.find(JSON.toJSONString(param));//查询出业务数据,然后取出表单业务编号orde031
				}catch(Exception e){
					rs.setRetCode(Result.RECODE_ERROR);
					rs.setErrMsg("数据服务中心异常!");
					return rs;
				}
				JSONObject temp_obj = JSONObject.parseObject(JSON.toJSONString(rs.getData()));
				JSONArray temp_array =  JSON.parseArray(temp_obj.getString("dataset"));
				JSONObject data_obj = JSONObject.parseObject(JSON.toJSONString(temp_array.get(0)));
				String orderNo = data_obj.getString("orde031"); //单号
				String cur_status = data_obj.getString("orde024");//单据状态
				jsonObject.put("processStatus", cur_status); //单据状态
				try{
					temp_rs = commomService.getDictByCodeVal(BizFormConstant.DICT_FLOW_AUDIT_STATUS, cur_status); //获取字段值
				}catch(Exception e){
					rs.setRetCode(Result.RECODE_ERROR);
					rs.setErrMsg("公共服务异常!");
					return rs;
				}
				jsonObject.put("processStatusCn", JSON.parseObject(JSON.toJSONString(temp_rs.getData())).getString("label")); //状态中文名
				jsonObject.put("orderNo", orderNo); //单号
				jsonObject.put("formDesc", ""); //描述
				rs_arrsy.add(jsonObject); 
			}
		}
		rs.setData(rs_arrsy);
		return rs;
	}
	
	/**
	 * 添加表单业务数据,并启动流程
	 * @param formData 参数json
	 * @return 操作结果
	 */
	public Result addFormData(String formData) {
		JSONObject json = JSON.parseObject(formData);
		
		String formId = json.getString("formId"); //表单ID
		BizFormSetting bizForm = formManageDao.getFormById(formId); //表单详情
		
		boolean submit = json.getBoolean("submit"); //true 走流程 false只保存,不做任何操作
		String userId = json.getString("userId"); //用户ID
		String flowId = bizForm.getRelatedFlowId(); //流程ID
		
		JSONArray json_array = json.getJSONArray("params");
		JSONObject temp_obj = json_array.getJSONObject(0);
		String id = temp_obj.getString("id"); //业务数据ID
		
		Result rs = new Result();
		String businessId = "";
		if(!StringUtil.isNullOrEmpty(id)){
			String rs_save = saveFormData(formData, bizForm.getFormCode(), submit, flowId, bizForm.getRelatedModelId()); //保存业务数据
			businessId = id;
			rs.setRetCode(Integer.parseInt(rs_save));
		}else{
			businessId = saveFormData(formData, bizForm.getFormCode(), submit, flowId, bizForm.getRelatedModelId()); //保存业务数据,同时返回业务ID
		}
		if(!submit)
			return rs;
		rs.setRetCode(Result.RECODE_SUCCESS);
		if(!StringUtil.isNullOrEmpty(bizForm.getRelatedFlowId()) &&!StringUtil.isNullOrEmpty(businessId)){ //未关联流程并且保存成功,直接进行回调方法
			String deptId = getDeptIdByStaffId(userId); //部门ID
			Map<String,String> flow_param = Maps.newHashMap(); //存储流程启动相关参数
			flow_param.put("processId", bizForm.getRelatedFlowId()); //流程定义ID
			flow_param.put("formId", formId); //表单标识
			flow_param.put("edmId", bizForm.getRelatedModelId()); //EDM标识
			flow_param.put("tableName", bizForm.getEdmcNameEn()); //主表表名
			flow_param.put("deptCode", deptId); //部门初始编号
			flow_param.put("sendUserId", userId); //表单发起人标识
			flow_param.put("businessId", businessId); //业务ID
			rs = workFlowService.startProcess(flow_param); //启动流程
		}
		if(StringUtil.isNullOrEmpty(bizForm.getRelatedFlowId()) && !StringUtil.isNullOrEmpty(businessId)){
			String edmNameEn = bizForm.getEdmcNameEn();
			JsEntity jsEntity = new JsEntity();
			jsEntity.setClassName("User");
			jsEntity.setMethodName("save"+edmNameEn.substring(0,1).toUpperCase()+edmNameEn.substring(1,edmNameEn.length()));
			Map<String,Object> init_param = Maps.newHashMap();
			init_param.put("user_01", bizForm.getEdmcNameEn());
			init_param.put("user_02", businessId);
			init_param.put("user_03", userId);
			jsEntity.setParamSet(init_param);
			System.out.println("dymamicMethodService result ==>"+rs.toString());
			try{
				rs = dymamicMethodService.executorJs(jsEntity);
			}catch(Exception e){
				rs.setErrMsg("回调方法执行出错!");
				rs.setRetCode(Result.RECODE_ERROR);
			}
			System.out.println("dymamicMethodService result ==>"+rs.toString());
			return rs;
		}
		return rs;
	}
	
	/**
	 * 保存表单数据,并返回业务ID
	 * @param formData 表单数据,表单编号(生成业务流水号)
	 * @return 业务ID
	 */
	@SuppressWarnings("unchecked")
	private String saveFormData(String formData, String formCode, boolean submit, String flowId,String edmId){
		Map<String,String> init_data = Maps.newHashMap();
		JSONArray temp_array = new JSONArray();
		JSONObject json = JSON.parseObject(formData);
		String edmName = json.getString("edmName");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMmmss");
		JSONArray json_array = json.getJSONArray("params");
		JSONObject temp_obj = json_array.getJSONObject(0);
		
		if(!submit){ //点击"保存"
			temp_obj.put("orde024", BizFormConstant.FORM_DATA_STATUS_DRAFT); //单据状态,草稿
		}else{
			if(StringUtil.isNullOrEmpty(flowId)) //点击提交
				temp_obj.put("orde024", BizFormConstant.AUDIT_STATUS_PASSED); //单据状态-通过
			else
				temp_obj.put("orde024", BizFormConstant.AUDIT_STATUS_PENDING); //单据状态-待审
		}
		//判断是否有ID,然后进行插入或者更新操作
		String id = temp_obj.getString("id");
		if(StringUtil.isNullOrEmpty(id)){ //ID不存在
			temp_obj.put("adduser", "form_system"); //添加人
			temp_obj.put("orde001", temp_obj.getString("versionId")); //设置版本ID
			temp_obj.put("orde031", formCode+"-"+sdf.format(new Date())); //单号
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			temp_obj.put("orde021", sdf.format(new Date())); //单据生效日期
		}else{
			temp_obj.put("moduser", "form_system"); //更新人
		}
		temp_obj.remove("versionId");
		init_data.put("edmName", edmName);
        temp_array.add(temp_obj);
        init_data.put("params",temp_array.toJSONString());
        
        Result rs = null;
        if(StringUtil.isNullOrEmpty(id)){ //ID不存在
        	rs = serviceCenterService.add(JSON.toJSONString(init_data)); //调用服务中心接口将初始岗位写入到资源类
        	String businessId = ((List<String>)rs.getData()).get(0);
        	if(rs.getRetCode() == 1)
        		rs = modelerService.setDefaultValue(edmId, businessId);
        	return businessId;
        }else{
        	rs = serviceCenterService.update(JSON.toJSONString(init_data)); //调用服务中心接口将初始岗位写入到资源类
        	if(rs.getRetCode() == 1)
        		modelerService.setDefaultValue(edmId, id);
        	return String.valueOf(rs.getRetCode());
        }
	}
		
	/**
	 * 
	 * @param staffId
	 * @return
	 */
	private String getDeptIdByStaffId(String staffId){
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.STAFF_NAME);
		param.setSearch(new Search(new Condition[]{new Condition("id","=",staffId)}));
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONArray temp_array = JSONObject.parseObject(JSON.toJSONString(rs.getData())).getJSONArray("dataset");
		JSONObject temp_obj = temp_array.getJSONObject(0);
		String jobId = temp_obj.getString("staf121"); //所属岗位
		return getDeptId(jobId);
	}
	
	/**
	 * 获取岗位名称jobp008
	 * @param jobId
	 * @return
	 */
	private String getDeptId(String jobId){
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.JOBP_NAME);
		param.setSearch(new Search(new Condition[]{new Condition("id","=",jobId)}));
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONArray temp_array = JSONObject.parseObject(JSON.toJSONString(rs.getData())).getJSONArray("dataset");
		JSONObject temp_obj = temp_array.getJSONObject(0);
		return temp_obj.getString("jobp008");
	}
	
	/**
	 * 获取岗位名称
	 * @param jobId
	 * @return
	 */
	private String getPostionName(String postId){
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.JOBP_NAME);
		param.setSearch(new Search(new Condition[]{new Condition("id","=",postId)}));
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONObject temp_obj = JSONObject.parseObject(JSON.toJSONString(rs.getData()));
		JSONArray temp_array = temp_obj.getJSONArray("dataset");
		if(temp_array.size()==0){
			return "未查到岗位";
		}
		return temp_array.getJSONObject(0).getString("jobp002");
	}
	
	/**
	 * 获取部门名称
	 * @param jobId
	 * @return
	 */
	private String getDeptName(String deptId){
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.DEPT_NAME);
		param.setSearch(new Search(new Condition[]{new Condition("id","=",deptId)}));
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONObject temp_obj = JSONObject.parseObject(JSON.toJSONString(rs.getData()));
		JSONArray temp_array = temp_obj.getJSONArray("dataset");
		if(temp_array.size()==0){
			return "未查到部门";
		}
		return temp_array.getJSONObject(0).getString("dept002");
	}
	
	/**
	 * 获取员工信息	
	 * @param jobId
	 * @return
	 */
	private JSONObject getStaffById(String staffId){
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.STAFF_NAME);
		param.setSearch(new Search(new Condition[]{new Condition("id","=",staffId)}));
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONObject temp_obj = JSONObject.parseObject(JSON.toJSONString(rs.getData()));
		JSONArray temp_array = temp_obj.getJSONArray("dataset");
		return temp_array.getJSONObject(0);
	}

	/**
	 * 获取自然人列表
	 */
	@Override
	public JSONArray getPeopleList() {
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.PEOPLE_NAME);
		param.setSearch(new Search(new Condition[]{new Condition("peop002","!=","null"),new Condition("peop003","!=","null")}));
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONObject temp_obj = JSONObject.parseObject(JSON.toJSONString(rs.getData()));
		JSONArray temp_array = temp_obj.getJSONArray("dataset");
		return temp_array;
	}
	
	/**
	 * 获取员工列表
	 */
	public JSONArray getStaffList() {
		QueryParam param = new QueryParam();
		param.setEdmName(BizFormConstant.STAFF_NAME);
		param.setSearch(new Search());
		Result rs = serviceCenterService.find(JSON.toJSONString(param));
		JSONObject temp_obj = JSONObject.parseObject(JSON.toJSONString(rs.getData()));
		JSONArray temp_array = temp_obj.getJSONArray("dataset");
		return temp_array;
	}

	/**
	 * 查询审核详情
	 */
	@Override
	public JSONObject getAuditTaskInfo(String taskId,String formId, String businessId,String userId,String queryCondition) {
		JSONObject rs_obj = new JSONObject();
		Result rs_flow = workFlowService.getAuditTaskInfo(taskId,userId,queryCondition); //获取流程详情
		BizFormSetting bizFormSetting = formManageDao.getFormById(formId);
		
		//查询表单业务数据
		QueryParam param = new QueryParam();
		param.setEdmName(bizFormSetting.getEdmcNameEn());
		param.setSearch(new Search(new Condition[]{new Condition("id","=",businessId)}));
		Result rs_form = serviceCenterService.find(JSON.toJSONString(param));
		JSONObject flow_obj = JSONObject.parseObject(JSON.toJSONString(rs_flow.getData())); //流程JSON信息
		JSONObject form_obj = JSONObject.parseObject(JSON.toJSONString(rs_form.getData())); //表单JSON信息
		JSONArray temp_array = form_obj.getJSONArray("dataset");
		Map<String,String> params = new HashMap<String, String>();
		params.put("formId", formId);
		params.put("formStatus", BizFormConstant.VERSION_DRAFT_STATUS);
        BizFormVersion versionInfo = bizFormVersionDao.getStartVersionByFormId(params);
        bizFormSetting.setBizFormVersion(versionInfo);
		
		JSONObject temp_obj = new JSONObject();
		temp_obj.put("formData", temp_array.get(0));
		temp_obj.put("formInfo", bizFormSetting);
		rs_obj.put("flowDetail", flow_obj); //流程相关
		rs_obj.put("formDetail", temp_obj); //表单相关
		return rs_obj;
	}
	
	/**
	 * 更新表单数据当前状态
	 * @param formId 表单ID
	 * @param businessId 业务数据ID
	 * @param status 状态值
	 */
	public Integer updateFormDataStatus(String formId,String businessId,String status){
		BizFormSetting bizFormSetting = formManageDao.getFormById(formId);
		Map<String,String> init_bussiness = Maps.newHashMap();
		init_bussiness.put("edmName" , bizFormSetting.getEdmcNameEn());
        JSONArray temp_array = new JSONArray();
        JSONObject temp_obj = new JSONObject();
        temp_obj.put("orde024", status); //单据状态
        temp_obj.put("id", businessId); //业务数据ID
        temp_obj.put("moduser", "form_system"); //业务数据ID
        temp_array.add(temp_obj);
        init_bussiness.put("params",temp_array.toJSONString());
        Result rs = serviceCenterService.update(JSON.toJSONString(init_bussiness)); //调用服务中心接口将员工信息写入到资源类
        return rs.getRetCode();
	}

	/**
	 * 根据员工ID查询员工信息
	 * @param staffId
	 * @return
	 */
	@Override
	public Map<String, String> getStaffInfoById(String staffId) {
		Map<String, String> rs = Maps.newHashMap();
		JSONObject staff_obj = getStaffById(staffId);
		rs.put("staffName", staff_obj.getString("staf002")); //员工姓名
		rs.put("staffNo", staff_obj.getString("staf001")); //员工编号
		String postId = staff_obj.getString("staf121"); //岗位ID
		if(StringUtil.isNullOrEmpty(postId)){
			rs.put("postionName", "未查到岗位"); //岗位名称
			rs.put("departmentName", "未查到部门"); //员工编号
		}else{
			String postionName = getPostionName(postId); //岗位名
			String deptId = getDeptId(postId); //获取部门ID
			String departmentName = getDeptName(deptId); //部门名称
			rs.put("postionName", postionName); //岗位名称
			rs.put("departmentName", departmentName); //员工编号
		}
		return rs;
	}
	
	
}
