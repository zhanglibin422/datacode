package com.huntkey.rx.sceo.form.client.feign.hystrix;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.ClassifyManageService;
import com.huntkey.rx.sceo.form.common.model.BizFormClassify;
import org.springframework.stereotype.Component;

/**
 * Created by wangn1 on 2017/6/12.
 */
@Component
public class ClassifyManageHystrix implements ClassifyManageService{
    @Override
    public Result getClassifyList(String formName) {
        return null;
    }

    @Override
    public Result getClassifyById(String id) {
        return null;
    }

    @Override
    public Result addClassify(BizFormClassify bizFormClassify) {
        return null;
    }

    @Override
    public Result updateClassify(BizFormClassify bizFormClassify) {
        return null;
    }

    @Override
    public Result deleteClassify(String id) {
        return null;
    }

    @Override
    public Result isExist(String classifyName) {
        return null;
    }

    /**
     * 获取分类列表作为下拉字典
     */
    @Override
    public Result classifyDictList() {
        return null;
    }
}
