package com.huntkey.rx.sceo.form.provider.dao;

import com.huntkey.rx.sceo.form.common.model.BizFormVersion;

import java.util.List;
import java.util.Map;

/**
 * Created by wangn1 on 2017/6/20.
 */
public interface BizFormVersionDao {

    /**
     * 根据表单ID查询版本信息
     * @param param 查询参数
     */
    List<BizFormVersion> getVersionsByFormId(Map<String,String> param);

    /**
     * 根据表单ID获取当前启用的的版本
     * @param param 参数
     */
    BizFormVersion getStartVersionByFormId(Map<String,String> param);

    /**
     * 创建一个新的版本信息
     * @param bizFormVersion 版本信息
     */
    void createVersion(BizFormVersion bizFormVersion);

    /**
     * 更新一个新的版本信息
     * @param bizFormVersion 版本信息
     */
    void updateVersion(BizFormVersion bizFormVersion);

    /**
     * 删除一个版本信息
     * @param versionId 版本ID
     */
    void deleteVersion(String versionId);

    /**
     * 启用/停用一个版本信息
     * @param bizFormVersion 版本信息
     */
    void enabledVersion(BizFormVersion bizFormVersion);

    /**
     * 停用所有版本信息
     * @param param 版本信息
     */
    void stopVersion(Map<String,String> param);


    /**
     * 启用/停用一个版本信息
     * @param param 版本信息
     */
    void enabledVersion(Map<String,String> param);

    /**
     * 根据表单ID查询草稿版本信息
     * @param param 参数
     */
    BizFormVersion getDraftVersionsByFormId(Map<String,String> param);

    /**
     * 取表单的最大版本号
     * @param formId 表单Id
     */
    int getLatestVersion(String formId);

    /**
     * 更新表单版本
     * @param bizFormVersion
     * @return int
     */
    int updateVersionByFormIdAndStatus(BizFormVersion bizFormVersion);

    /**
     * 根据主键更新表单
     * @param bizFormVersion
     * @return
     */
    int updateVersionById(BizFormVersion bizFormVersion);

    /**
     * 根据主键查询表单版本信息
     * @param versionId
     * @return BizFormVersion
     */
    BizFormVersion getVersionById(String versionId);

    /**
     * 删除表单一个版本
     * @param bizFormVersion
     * @return int
     */
    int deleteFormVersion(BizFormVersion bizFormVersion);


    /**
     * 根据表单Id查询表单历史版本
     * @param map 表单Id
     */
    List<BizFormVersion> getAllVersionsByFormId(Map<String, String> map);

    /**
     * 查询所有列表的版本信息集合
     * @param param 参数
     */
    List<BizFormVersion> getAllOpenVersions(Map<String,String> param);
}
