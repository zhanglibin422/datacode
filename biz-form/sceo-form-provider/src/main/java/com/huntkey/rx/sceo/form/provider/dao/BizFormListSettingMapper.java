package com.huntkey.rx.sceo.form.provider.dao;

import com.huntkey.rx.sceo.form.common.model.BizFormListSetting;

import java.util.List;

public interface BizFormListSettingMapper {

    long countByExample(BizFormListSetting bizFormListSetting);

    int deleteByExample(BizFormListSetting bizFormListSetting);

    int deleteByPrimaryKey(String formListId);

    int insert(BizFormListSetting bizFormListSetting);

    int insertSelective(BizFormListSetting bizFormListSetting);

    List<BizFormListSetting> selectByBizFormListSetting(BizFormListSetting bizFormListSetting);

    BizFormListSetting selectByPrimaryKey(String formListId);

    /**
     * 修改列表页
     * @param bizFormListSetting 表单列表实体
     * @return int
     */
    int updateByPrimaryKeySelective(BizFormListSetting bizFormListSetting);

    /**
     * 根据versionId修改列表页
     * @param bizFormListSetting 表单列表实体
     * @return int
     */
    int updateByVersionId(BizFormListSetting bizFormListSetting);

    /**
     * 根据表单Id查询表单列表
     * @param relatedVersionId 表单Id
     * @return BizFormListSetting
     */
    BizFormListSetting selectByRelatedVersionId(String relatedVersionId);


}