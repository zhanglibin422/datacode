package com.huntkey.rx.sceo.form.provider.dao;

import com.huntkey.rx.sceo.form.common.model.BizFormClassify;

import java.util.List;

/**
 * Created by wangn1 on 2017/6/12.
 */
public interface ClassifyMangeDao {

    /**
     * 获取所有分类
     */
    List<BizFormClassify> getClassifyList();

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     */
    BizFormClassify getClassifyById(String id);

    /**
     * 新增表单分类
     * @param classify 分类信息
     */
    void addClassify(BizFormClassify classify);

    /**
     * 更新表单分类
     * @param classify 分类信息
     */
    void updateClassify(BizFormClassify classify);

    /**
     * 删除表单分类
     * @param id 分类名称
     */
    void deleteClassify(String id);

    /**
     * 根据分类名查询分类信息
     * @param classifyName 分类名
     */
    BizFormClassify getClassifyByName(String classifyName);
}
