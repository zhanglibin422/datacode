package com.huntkey.rx.sceo.form.provider.service;

import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglb on 2017/6/6.
 */

public interface BizFormService {

    /**
     * 查询表单历史版本信息
     * @param formId
     * @return
     */
    BizFormSetting getFormHistoryData(String formId);

    /**
     * 根据id查询表单信息
     * @param formId 自定义表单id
     * @return BizFormSetting
     */
    BizFormSetting getBizFormSettingById(String formId);

    /**
     * 对表单进行启用，停用方法
     * @param map
     * @return boolean
     */
    int changeFormStatus(Map<String, String> map);

    /**
     * 根据表单编码得到最新版本号
     * @param formId 表单Id
     * @return result
     */
    int getLatestVersion(String formId);

    /**
     * 修改列表页
     * @param bizFormListSetting 表单列表实体
     * @return int
     */
    int updateBizFormListSetting(BizFormListSetting bizFormListSetting);

    /**
     * 根据表单Id查询表单列表
     * @param versionId 表单Id
     * @return BizFormListSetting
     */
    BizFormListSetting selectByRelatedVersionId(String versionId);

    /**
     * 创建表单列表
     * @param bizFormListSetting 表单列表
     */
    int createFormListSetting(BizFormListSetting bizFormListSetting);

    /**
     * 根据表单Id查询表单历史版本
     * @param formId 表单Id
     */
    List<BizFormVersion> getBizFormVersion(String formId);

    /**
     * 删除表单一个版本
     * @param bizFormVersion
     * @return int
     */
    int deleteFormVersion(BizFormVersion bizFormVersion);
}
