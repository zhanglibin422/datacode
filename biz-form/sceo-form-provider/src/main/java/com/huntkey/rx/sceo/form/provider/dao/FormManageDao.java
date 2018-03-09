package com.huntkey.rx.sceo.form.provider.dao;

import com.huntkey.rx.sceo.form.common.model.BizFormSetting;

import java.util.List;
import java.util.Map;

/**
 * Created by wangn1 on 2017/6/14.
 */
public interface FormManageDao {

    /**
     * 根据分类信息查询表单集合
     * @param formName 表单名称关键字
     */
    List<BizFormSetting> getFormByFormName(String formName);

    /**
     * 根据表单ID查询表单
     * @param formId 表单ID
     */
    BizFormSetting getFormById(String formId);

    /**
     * 根据表单名称查询表单基本信息
     * @param formName 表单名称
     */
    BizFormSetting getFormByName(String formName);

    /**
     * 创建表单
     * @param bizFormSetting 表单信息
     */
    void createForm(BizFormSetting bizFormSetting);

    /**
     * 更新修改表单
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
     *
     * @param param  表单对象
     */
    void enabledForm(Map<String,String> param);

    /**
     * 查询分类信息作为下拉字典
     * @param classifyCode 分类编码
     */
    List<BizFormSetting> getFormByClassifyCode(String classifyCode);
    
    /**
     * 获取可制单据信息
     * @return
     */
	List<BizFormSetting> canUseForm(Map<String, String> param);
}
