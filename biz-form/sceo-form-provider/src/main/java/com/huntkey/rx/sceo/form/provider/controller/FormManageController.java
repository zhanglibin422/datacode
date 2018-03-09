package com.huntkey.rx.sceo.form.provider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.form.client.feign.ServiceCenterService;
import com.huntkey.rx.sceo.form.client.feign.WorkFlowService;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import com.huntkey.rx.sceo.form.provider.config.BizFormConstant;
import com.huntkey.rx.sceo.form.provider.service.FormManageService;

/**
 * Created by wangn1 on 2017/6/13.
 */
@RefreshScope
@RestController
@RequestMapping("/fm")
public class FormManageController {

    @Autowired
    private FormManageService formManageService;

    @Autowired
    private ServiceCenterService serviceCenterService; //服务中心接口地址
    
    @Autowired
    private WorkFlowService workFlowService;
    
    /**
     * 根据表单ID查询表单
     * @param formId 表单ID
     * @param versionId 版本ID 可以为空
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result getFormById(@RequestParam(value = "formId") String formId, @RequestParam(required = false,value = "versionId") String versionId, @RequestParam(required = false,value = "status")String status){
        Result result = new Result();
        try{
            result.setData(formManageService.getFormById(formId, versionId, status));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 创建表单
     * @param bizFormSetting 表单信息
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result createForm(@RequestBody BizFormSetting bizFormSetting){
        Result result = new Result();
        try{
            BizFormSetting bizForm = formManageService.getFormByName(bizFormSetting.getFormName());
            if(!StringUtil.isNullOrEmpty(bizForm)){
                result.setRetCode(Result.RECODE_VALIDATE_ERROR);
                result.setErrMsg("该表单名称已存在!");
                return result;
            }
            result.setData(formManageService.createForm(bizFormSetting));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch (Exception e){
                e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 更新表单
     * @param bizFormSetting 表单信息
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Result updateForm(@RequestBody BizFormSetting bizFormSetting){
        Result result = new Result();
        try{
            BizFormSetting bizForm = formManageService.getFormByName(bizFormSetting.getFormName());
            if(!StringUtil.isNullOrEmpty(bizForm) && !bizForm.getFormId().equals(bizFormSetting.getFormId())){
                result.setRetCode(Result.RECODE_VALIDATE_ERROR);
                result.setErrMsg("该表单名称已存在!");
                return result;
            }
            formManageService.updateForm(bizFormSetting);
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 删除表单
     * @param formId 表单ID
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteForm(@RequestParam(value = "formId") String formId){
        Result result = new Result();
        try{
            formManageService.deleteForm(formId);
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 停用启用表单
     * @param param 表单对象 非空字段fromId表单ID formStatus状态 可空字段versionId 版本ID
     */
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public Result enabledForm(@RequestBody Map<String,String> param){
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try{
            if(BizFormConstant.FORM_START_STATUS.equals(param.get("formStatus"))){ //启用表单
                Map<String,String> query_param = new HashMap<String, String>();
                query_param.put("formId", param.get("formId"));
                List<BizFormVersion> versions = formManageService.getVersionsByFormId(query_param);
                if(null == versions || versions.size() == 0){
                    result.setRetCode(Result.RECODE_VALIDATE_ERROR);
                    result.setErrMsg("该表单还未进行设计!");
                    return result;
                }else{
                    BizFormVersion bizFormVersion = formManageService.getVersionByStatus(versions, BizFormConstant.FORM_START_STATUS);
                    if(null == bizFormVersion){
                        result.setRetCode(Result.RECODE_VALIDATE_ERROR);
                        result.setErrMsg("该表单没有启用的版本!");
                        return result;
                    }
                }
                //formManageService.startForm(versions,param); //启用表单
            }
            formManageService.enabledForm(param);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 保存表单数据
     * @param bizFormVersion 表单版本信息
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public Result saveFormData(@RequestBody BizFormVersion bizFormVersion){
        Result result = new Result();
        try{
            result.setData(formManageService.saveFormData(bizFormVersion));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 根据formId获取所有的版本信息,不包括草稿状态
     * @param formId 表单ID
     */
    @RequestMapping(value = "/versions", method = RequestMethod.GET)
    public Result getVersionsByFormId(@RequestParam(required = true, value = "formId") String formId){
        Result result = new Result();
        try{
            Map<String,String> param = new HashMap<String, String>();
            param.put("formId", formId);
            param.put("draftStatus", BizFormConstant.VERSION_DRAFT_STATUS);
            result.setData(formManageService.getVersionsByFormId(param));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

//    /**
//     * 保存更新表单业务数据
//     * @param map 业务数据
//     */
//        @RequestMapping(value = "/businessData", method = RequestMethod.POST)
//    public Result saveBusinessData(@RequestBody Map<String,String> map){
//        Result result = new Result();
//        try{
//            String datas = map.get("datas");
//            result = modelerService.addDatasToEsAndHbase(datas);
//        }catch (Exception e){
//            e.printStackTrace();
//            result.setRetCode(Result.RECODE_ERROR);
//            result.setErrMsg(e.getMessage());
//        }
//        return result;
//    }
//
//    /**
//     * 获取表单业务数据
//     * @param datas 查询参数
//     */
//    @RequestMapping(value = "/businessData", method = RequestMethod.GET)
//    public Result getBusinessData(@RequestParam(value = "datas") String datas){
//        Result result = new Result();
//        try{
//            result = modelerService.queryFromEsAndHbase(datas);
//        }catch (Exception e){
//            e.printStackTrace();
//            result.setRetCode(Result.RECODE_ERROR);
//            result.setErrMsg(e.getMessage());
//        }
//        return result;
//    }
//
//    /**
//     * 删除表单业务数据
//     * @param datas 删除条件参数
//     */
//    @RequestMapping(value = "/businessData", method = RequestMethod.DELETE)
//    public Result deleteBusinessData(@RequestParam(value = "datas") String datas){
//        Result result = new Result();
//        try{
//            result = modelerService.deleteEsAndHbase(datas);
//        }catch (Exception e){
//            e.printStackTrace();
//            result.setRetCode(Result.RECODE_ERROR);
//            result.setErrMsg(e.getMessage());
//        }
//        return result;
//    }
    
   /**
    * 获取可制单据信息
    * @return
    */
   @RequestMapping(value = "/useForm", method = RequestMethod.GET)
   public Result canUseForm(){
	   Result result = new Result();
       try{
    	   result.setRetCode(Result.RECODE_SUCCESS);
    	   result.setData(formManageService.canUseForm());
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
   }
   
   /**
    * 添加表单业务数据,可进行单个对象,多个对象与属性集的插入,传入ID则进行更新操作,不传则进行添加操作
    * @param formData 参数json
    * @return 操作结果
    */
   @RequestMapping(value = "/addFormData", method = RequestMethod.POST)
   public Result addFormData(@RequestBody String formData){
	   Result result = new Result();
	   try{
		   result = formManageService.addFormData(formData);
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);	
           result.setErrMsg(e.getMessage());
       }
       return result;
   }
   
   /**
    * 修改表单业务数据,可进行单个对象,多个对象的修改
    * @param formData 参数json
    * @return 操作结果
    */
   @RequestMapping(value = "/updateFormData", method = RequestMethod.POST)
   public Result updateFormData(@RequestBody String formData){
	   Result result = new Result();
	   try{
    	   result = serviceCenterService.update(formData);
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
   }
   
   /**
    * 查询表单业务数据,根据传递的json参数可以进行分页,条件,排序查询
    * @param formData 参数json
    * @return 操作结果
    */
   @RequestMapping(value = "/findFormData", method = RequestMethod.POST)
   public Result findFormData(@RequestBody String formData){
	   Result result = new Result();
	   try{
    	   result = serviceCenterService.find(JSON.parseObject(formData).getString("formData"));
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
   }
   
   /**
    * 删除表单业务数据,可以进行单个与批量删除
    * @param formData 参数json
    * @return 操作结果
    */
   @RequestMapping(value = "/deleteFormData", method = RequestMethod.POST)
   public Result deleteFormData(@RequestBody String formData){
	   Result result = new Result();
	   try{
    	   result = serviceCenterService.delete(formData);
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
   }
   
   /**
    * 获取自然人列表
    * @return
    */
   @RequestMapping(value = "/people", method = RequestMethod.GET)
   public Result getPeopleList() {
	   Result result = new Result();
	   try{
		   result.setRetCode(Result.RECODE_SUCCESS);
    	   result.setData(formManageService.getPeopleList());
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
	}
   
   /**
	 * 获取员工列表
	 */
   @RequestMapping(value = "/staff", method = RequestMethod.GET)
	public Result getStaffList() {
	   Result result = new Result();
	   try{
		   result.setRetCode(Result.RECODE_SUCCESS);
    	   result.setData(formManageService.getStaffList());
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
	}
   
   /** 
    * 获取流程待审列表
    * @return
    */
   @RequestMapping(value = "/audits",method = RequestMethod.GET)
   public Result getAuditList(@RequestParam("userId") String userId, @RequestParam("processStatus") String processStatus){
	   Result result = new Result();
       try{
    	   result = formManageService.getAuditList(userId, processStatus);
       }catch (Exception e){
           e.printStackTrace();
           result.setRetCode(Result.RECODE_ERROR);
           result.setErrMsg(e.getMessage());
       }
       return result;
   }
   
   /**
  	 * 获取审核信息
  	 * @param taskId 任务节点ID
  	 * @return
  	 */
  	@RequestMapping(value = "/auditInfo",method = RequestMethod.GET)
  	public Result getAuditTaskInfo(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "formId") String formId,@RequestParam(value = "businessId") String businessId,@RequestParam(value = "userId") String userId,@RequestParam(value = "queryCondition") String queryCondition){
  	    Result result = new Result();
  	    try{
 		   result.setRetCode(Result.RECODE_SUCCESS);
     	   result.setData(formManageService.getAuditTaskInfo(taskId,formId,businessId,userId,queryCondition));
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
  	}
  	
  	/**
  	 * 审核流程
  	 * @param params 审核信息
  	 * @return
  	 */
  	@RequestMapping(value = "/audit",method = RequestMethod.POST)
  	public Result audit(@RequestBody Map<String,String> params){
  		String action = (String)params.get("approvalAction"); //动作
  		String state = (String)params.get("state"); //状态
  		if("revoke".equals(action)){
  			if("audited".equals(state)){
  				return workFlowService.revoke((String)params.get("taskId"), "");
  			}else{
  				return workFlowService.revoke("",(String)params.get("taskId"));
  			}
  		}
  		return workFlowService.audit(params);
  	}
  	
  	/**
  	 * 撤回流程
  	 * @param hisTaskId 已审核的任务ID
  	 * @param processInsId 执行实例ID
  	 * @return
  	 */
  	@RequestMapping(value = "/revoke",method = RequestMethod.GET)
  	public Result revoke(@RequestParam(value = "hisTaskId") String hisTaskId, @RequestParam(value = "processInsId") String processInsId){
  		return workFlowService.revoke(hisTaskId, processInsId);
  	}
  	
 	/**
	 * 更新表单数据当前状态
	 * @param formId 表单ID
	 * @param businessId 业务数据ID
	 * @param status 状态值
	 */
  	@RequestMapping(value = "/updateStatus",method = RequestMethod.PUT)
	public Result updateFormDataStatus(@RequestParam(value = "formId") String formId,@RequestParam(value = "businessId") String businessId,@RequestParam(value = "status") String status){
  		Result result = new Result();
  	    try{
 		   result.setRetCode(formManageService.updateFormDataStatus(formId,businessId,status));
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
	}
  	
  	/**
	 * 根据员工ID查询员工信息
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/staff/{staffId}",method = RequestMethod.GET)
	public Result getStaffInfoById(@PathVariable("staffId") String staffId){
		Result result = new Result();
  	    try{
  	    	result.setRetCode(Result.RECODE_SUCCESS);
  	    	result.setData(formManageService.getStaffInfoById(staffId));
        }catch (Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
	}
}
