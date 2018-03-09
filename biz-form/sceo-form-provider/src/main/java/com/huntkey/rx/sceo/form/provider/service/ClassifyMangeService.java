package com.huntkey.rx.sceo.form.provider.service;

import com.huntkey.rx.sceo.form.common.model.BizFormClassify;

import java.util.List;
import java.util.Map;

/**
 * Created by wangn1 on 2017/6/12.
 */
public interface ClassifyMangeService {

    /**
     * 获取所有分类
     * @param formName 表单名称关键字
     */
    List<BizFormClassify> getClassifyList(String formName);

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     */
    BizFormClassify getClassifyById(String id);

    /**
     * 新增表单分类
     * @param classifyName 分类名称
     */
    Map<String,String> addClassify(String classifyName);

    /**
     * 更新表单分类
     * @param bizFormClassify 分类对象
     */
    void updateClassify(BizFormClassify bizFormClassify);

    /**
     * 删除表单分类
     * @param classifyId 分类名称
     */
    void deleteClassify(String classifyId);

    /**
     * 当前的分类名是否存在
     * @param classifyName 分类名
     */
    BizFormClassify isExist(String classifyName);

    List<Map<String,String>> classifyDictList();
}
