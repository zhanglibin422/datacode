package com.huntkey.rx.sceo.form.provider.service.impl;

import com.google.common.collect.Maps;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.sceo.form.common.model.BizFormClassify;
import com.huntkey.rx.sceo.form.common.model.BizFormSetting;
import com.huntkey.rx.sceo.form.common.model.BizFormVersion;
import com.huntkey.rx.sceo.form.provider.config.BizFormConstant;
import com.huntkey.rx.sceo.form.provider.dao.BizFormVersionDao;
import com.huntkey.rx.sceo.form.provider.dao.ClassifyMangeDao;
import com.huntkey.rx.sceo.form.provider.dao.FormManageDao;
import com.huntkey.rx.sceo.form.provider.service.ClassifyMangeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wangn1 on 2017/6/12.
 */
@Service
@Transactional(readOnly = true)
public class ClassifyMangeServiceImpl implements ClassifyMangeService {

    @Autowired
    private ClassifyMangeDao classifyMangeDao;

    @Autowired
    private FormManageDao formManageDao;

    @Autowired
    private BizFormVersionDao bizFormVersionDao;

    /**
     * 获取所有分类
     * @param formName 表单名称关键字
     */
    public List<BizFormClassify> getClassifyList(String formName) {
        List<BizFormClassify> classifyList = classifyMangeDao.getClassifyList();
        List<BizFormSetting> formList = formManageDao.getFormByFormName(escapeExprSpecialWord(formName));
        Map<String,String> param = Maps.newHashMap();
        BizFormClassify bizFormClassify = null;
        param.put("draftStatus", BizFormConstant.VERSION_DRAFT_STATUS);
        param.put("listFlag",BizFormConstant.LIST_OPEN_STATUS);
        List<BizFormVersion> versionList = bizFormVersionDao.getAllOpenVersions(param);
        List<BizFormSetting> tempList = null;
        int count = 0;
        for(BizFormClassify classify : classifyList){
            tempList = new ArrayList<BizFormSetting>();
            for(BizFormSetting form : formList){
                if(classify.getClassifyCode().equals(form.getClassifyCode())){
                    count = 0;
                    for (BizFormVersion bizFormVersion : versionList) {
                        if(bizFormVersion.getFormId().equals(form.getFormId())){
                            count ++;
                        }
                    }
                    form.setOpenCount(count);
                    tempList.add(form);
                }
            }
            classify.setBizFormSettingList(tempList);
            if("其它".equals(classify.getClassifyName())){
                bizFormClassify = classify;
            }
        }
        classifyList.remove(bizFormClassify);
        classifyList.add(bizFormClassify);
        return classifyList;
    }

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     */
    public BizFormClassify getClassifyById(String id) {
        return classifyMangeDao.getClassifyById(id);
    }

    /**
     * 新增表单分类
     * @param classifyName 分类名称
     */
    @Transactional(readOnly = false)
    public Map<String,String> addClassify(String classifyName) {
        BizFormClassify classify = new BizFormClassify();
        String classifyId = UuidCreater.uuid();
        String classifyCode = UuidCreater.uuid();
        classify.setCreatedTime(new Date()); //创建时间
        classify.setCreatedBy("developer"); //创建人
        classify.setClassifyId(classifyId);//ID
        classify.setClassifyName(classifyName); //分类名称
        classify.setClassifyCode(classifyCode); //分类编码
        classifyMangeDao.addClassify(classify);
        Map<String,String> map = new HashMap<String,String>();
        map.put("classifyId", classifyId);
        map.put("classifyCode", classifyCode);
        return map;
    }

    /**
     * 更新表单分类
     * @param bizFormClassify 分类对象
     */
    @Transactional(readOnly = false)
    public void updateClassify(BizFormClassify bizFormClassify) {
        bizFormClassify.setUpdatedTime(new Date()); //更新时间
        bizFormClassify.setUpdatedBy("developer"); //更新人
        classifyMangeDao.updateClassify(bizFormClassify);
    }

    /**
     * 删除单分类
     * @param classifyId 分类ID
     */
    @Transactional(readOnly = false)
    public void deleteClassify(String classifyId) {
        //1.查询分类信息根据ID
        BizFormClassify classify = classifyMangeDao.getClassifyById(classifyId);
        //2.查询该分类下的表单
        List<BizFormSetting> forms = formManageDao.getFormByClassifyCode(classify.getClassifyCode());
            //3.更新这些表单分类到"其他"分类中
        if(forms.size() > 0){
            for(BizFormSetting BizFormSetting : forms){
                BizFormSetting.setClassifyCode(BizFormConstant.FORM_CLASSIFY_OTHER);
                formManageDao.updateForm(BizFormSetting);
            }
        }
        //4.删除分类
        classifyMangeDao.deleteClassify(classifyId);
    }


    /**
     * 当前的分类名是否存在
     * @param classifyName 分类名
     */
    public BizFormClassify isExist(String classifyName) {
        return classifyMangeDao.getClassifyByName(classifyName);
    }

    /**
     * 获取分类列表作为下拉字典
     */
    @Override
    public List<Map<String, String>> classifyDictList() {
        List<BizFormClassify> list = classifyMangeDao.getClassifyList();
            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<String, String> temp = null;
        for(BizFormClassify classify : list){
            temp = new HashMap<String, String>();
            temp.put("id", classify.getClassifyId());
                temp.put("title", classify.getClassifyName());
            temp.put("value", classify.getClassifyCode());
            result.add(temp);
        }
        return result;
    }

    private String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|", "%" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
