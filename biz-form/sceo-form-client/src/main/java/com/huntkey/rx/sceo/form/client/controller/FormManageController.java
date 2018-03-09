package com.huntkey.rx.sceo.form.client.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultGenerateStorageClient;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.ClinetFormManageService;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by wangn1 on 2017/6/13.
 */
@RestController
@RequestMapping("/fm")
public class FormManageController {

    @Autowired
    private ClinetFormManageService clinetFormManageService;

    @Autowired
    private DefaultGenerateStorageClient defaultGenerateStorageClient;

    /**
     * 根据表单ID查询表单,预览的时候可以使用
     * @param formId 表单ID
     * @param versionId 版本ID
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result getFormById(@RequestParam("formId")String formId, @RequestParam(required = false,value = "versionId")String versionId, @RequestParam(required = false,value = "status")String status){
        return clinetFormManageService.getFormById(formId, versionId, status);
    }

    /**
     * 创建表单
     * @param bizFormSetting 表单信息
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result createForm(@RequestBody BizFormSetting bizFormSetting){
        return clinetFormManageService.createForm(bizFormSetting);
    }

    /**
     * 更新表单
     * @param bizFormSetting 表单信息
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Result updateForm(@RequestBody BizFormSetting bizFormSetting){
        return clinetFormManageService.updateForm(bizFormSetting);
    }

    /**
     * 删除表单
     * @param formId 表单ID
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteForm(@RequestParam(value = "formId") String formId){
        return clinetFormManageService.deleteForm(formId);
    }

    /**
     * 停用启用表单
     * @param param 操作参数
     */
    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public Result enabledForm(@RequestBody Map<String,String> param){
        return clinetFormManageService.enabledForm(param);
    }

    /**
     * 保存表单数据
     * @param bizFormVersion 表单基本信息
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public Result saveFormData(@RequestBody BizFormVersion bizFormVersion){
        return clinetFormManageService.saveFormData(bizFormVersion);
    }

    /**
     * 根据formId获取所有的版本信息,不包括草稿状态
     * @param formId 表单ID
     */
    @RequestMapping(value = "/versions", method = RequestMethod.GET)
        public Result getVersionsByFormId(@RequestParam(required = true, value = "formId") String formId){
        return clinetFormManageService.getVersionsByFormId(formId);
    }

    /**
     * 保存表单业务数据
     * @param map 业务数据
     */
    @RequestMapping(value = "/businessData", method = RequestMethod.POST)
    public Result saveBusinessData(@RequestBody Map<String,String> map){
        return clinetFormManageService.saveBusinessData(map);
    }

    /**
     * 获取表单业务数据
     * @param datas 业务数据
     */
    @RequestMapping(value = "/businessData", method = RequestMethod.GET)
    public Result getBusinessData(@RequestParam(value = "datas") String datas){
        return clinetFormManageService.getBusinessData(datas);
    }

    /**
     * 删除表单业务数据
     * @param datas 业务数据
     */
    @RequestMapping(value = "/businessData", method = RequestMethod.DELETE)
    public Result deleteBusinessData(@RequestParam(value = "datas") String datas){
        return clinetFormManageService.deleteBusinessData(datas);
    }

    /**
     * 表单附件上传
     * @param file 上传的文件
     * @author wangn1
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result fileUpload(@RequestParam("file") MultipartFile file, HttpServletResponse response){
        Result result = new Result();
        try{
            InputStream is = file.getInputStream();
            long fileSize = is.available(); //文件大小
            String fileExtName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1); //文件后缀名
            StorePath path = defaultGenerateStorageClient.uploadFile("group1", is, fileSize, fileExtName);
            result.setData(path.getFullPath());
            is.close();
        }catch(Exception e){
            e.printStackTrace();
            response.setStatus(500);
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
    	return clinetFormManageService.getAuditList(userId, processStatus);
    }
    
    /**
     * 获取可制单据信息
     * @return
     */
    @RequestMapping(value = "/useForm", method = RequestMethod.GET)
    public Result canUseForm(){
    	return clinetFormManageService.canUseForm();
    }
    
    
    /**
     * 修改表单业务数据,可进行单个对象,多个对象的修改
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/updateFormData", method = RequestMethod.POST)
    public Result updateFormData(@RequestBody String formData){
		return clinetFormManageService.updateFormData(formData);
    }
    
    /**
     * 添加表单业务数据,可进行单个对象,多个对象与属性集的插入,传入ID则进行更新操作,不传则进行添加操作
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/addFormData", method = RequestMethod.POST)
    public Result addFormData(@RequestBody String formData){
		return clinetFormManageService.addFormData(formData);
    }
    
    /**
     * 查询表单业务数据,根据传递的json参数可以进行分页,条件,排序查询
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/findFormData", method = RequestMethod.POST)
    public Result findFormData(@RequestBody String formData){
    	return clinetFormManageService.findFormData(formData);
    }
    
    /**
     * 删除表单业务数据,可以进行单个与批量删除
     * @param formData 参数json
     * @return 操作结果
     */
    @RequestMapping(value = "/deleteFormData", method = RequestMethod.POST)
    public Result deleteFormData(@RequestBody String formData){
    	return clinetFormManageService.deleteFormData(formData);
    }
    
    /**
     * 获取自然人列表
     * @return
     */
    @RequestMapping(value = "/people", method = RequestMethod.GET)
    public Result getPeopleList() {
		return clinetFormManageService.getPeopleList();
	}
    
    /**
	 * 获取审核信息
	 * @param taskId 任务节点ID
	 * @param formId 表单ID
	 * @param businessId 业务数据ID
	 * @return
	 */
	@RequestMapping(value = "/auditInfo",method = RequestMethod.GET)
	public Result getAuditTaskInfo(@RequestParam(value = "taskId") String taskId,@RequestParam(value = "formId") String formId,@RequestParam(value = "businessId") String businessId,@RequestParam(value = "userId") String userId,@RequestParam(value = "queryCondition") String queryCondition){
		return clinetFormManageService.getAuditTaskInfo(taskId,formId,businessId,userId,queryCondition);
	}
	
	/**
	 * 审核流程
	 * @param params 审核信息
	 * @return
	 */
	@RequestMapping(value = "/audit",method = RequestMethod.POST)
	public Result audit(@RequestBody Map<String,String> params){
		return clinetFormManageService.audit(params);
	}
	
	/**
	 * 撤回流程
	 * @param hisTaskId 已审核的任务ID
	 * @param processInsId 执行实例ID
	 * @return
	 */
	@RequestMapping(value = "/revoke",method = RequestMethod.GET)
	public Result revoke(@RequestParam(value = "hisTaskId") String hisTaskId, @RequestParam(value = "processInsId") String processInsId){
		return clinetFormManageService.revoke(hisTaskId, processInsId);
	}
	
	/**
	 * 根据员工ID查询员工信息
	 * @param staffId
	 * @return
	 */
	@RequestMapping(value = "/staff/{staffId}",method = RequestMethod.GET)
	public Result getStaffInfoById(@PathVariable("staffId") String staffId){
		return clinetFormManageService.getStaffInfoById(staffId);
	}
}
