package com.huntkey.rx.sceo.form.client.feign.hystrix;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.ClinetFormManageService;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;

/**
 * Created by wangn1 on 2017/6/13.
 */
@Component
public class FormManageHystrix implements ClinetFormManageService{

    @Override
    public Result getFormById(String formId, String versionId, String status) {
        return null;
    }

    @Override
    public Result createForm(BizFormSetting bizFormSetting) {
        return null;
    }

    /**
     * 更新表单
     * @param bizFormSetting 表单信息
     */
    @Override
    public Result updateForm(BizFormSetting bizFormSetting) {
        return null;
    }

    /**
     * 删除表单
     *
     * @param formId 表单ID
     */
    @Override
    public Result deleteForm(String formId) {
        return null;
    }

    /**
     * 停用启用表单
     *
     * @param param 表单对象
     */
    @Override
    public Result enabledForm(Map<String,String> param) {
        return null;
    }

    /**
     * 停用启用表单
     *
     * @param bizFormVersion 表单版本信息
     */
    @Override
    public Result saveFormData(BizFormVersion bizFormVersion) {
        return null;
    }

    /**
     * 根据formId获取所有的版本信息,不包括草稿状态
     *
     * @param formId 表单ID
     */
    @Override
    public Result getVersionsByFormId(String formId) {
        return null;
    }

    /**
     * 保存表单业务数据
     *
     * @param map
     */
    @Override
    public Result saveBusinessData(Map<String, String> map) {
        return null;
    }

    /**
     * 获取表单业务数据
     *
     * @param datas
     */
    @Override
    public Result getBusinessData(@RequestParam(value = "datas") String datas) {
        return null;
    }

    /**
     * 删除表单业务数据
     *
     * @param datas 业务数据
     */
    @Override
    public Result deleteBusinessData(@RequestParam(value = "datas") String datas) {
        return null;
    }

	@Override
	public Result getAuditList(@RequestParam("userId") String userId, @RequestParam("processStatus") String processStatus) {
		return null;
	}

	@Override
	public Result canUseForm() {
		return null;
	}

	@Override
	public Result addFormData(String formData) {
		return null;
	}

	@Override
	public Result findFormData(String formData) {
		return null;
	}

	@Override
	public Result deleteFormData(String formData) {
		return null;
	}

	@Override
	public Result updateFormData(String formData) {
		return null;
	}

	@Override
	public Result getPeopleList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result audit(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result revoke(String hisTaskId, String processInsId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result getAuditTaskInfo(String taskId, String formId, String bussinessId,String userId,String queryCondition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result getStaffInfoById(String staffId) {
		// TODO Auto-generated method stub
		return null;
	}
}
