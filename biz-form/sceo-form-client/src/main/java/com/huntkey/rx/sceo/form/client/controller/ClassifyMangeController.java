package com.huntkey.rx.sceo.form.client.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.ClassifyManageService;
import com.huntkey.rx.sceo.form.common.model.BizFormClassify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wangn1 on 2017/6/12.
 */
@RestController
@RequestMapping("/classify")
public class ClassifyMangeController {

    @Autowired
    private ClassifyManageService classifyManageService;

    /**
     * 获取所有分类
     * @param formName 表单名称关键字
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result getClassifyList(@RequestParam(name="formName") String formName){
        return classifyManageService.getClassifyList(formName);
    }

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result getClassifyById(@PathVariable("id") String id){
        return classifyManageService.getClassifyById(id);
    }

    /**
     * 新增表单分类
     * @param bizFormClassify 分类封装对象
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result addClassify(@RequestBody BizFormClassify bizFormClassify){
        return classifyManageService.addClassify(bizFormClassify);
    }

    /**
     * 更新表单分类
     * @param bizFormClassify 分类名称
     * @Param id 分类ID
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Result updateClassify(@RequestBody BizFormClassify bizFormClassify){
        return classifyManageService.updateClassify(bizFormClassify);
    }

    /**
     * 删除表单分类
     * @param classifyId 分类ID
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteClassify(@RequestParam(value = "classifyId") String classifyId){
        return classifyManageService.deleteClassify(classifyId);
    }

    /**
     * 当前的分类名是否存在
     * @param classifyName 分类名
     */
    @RequestMapping(value = "/isExist", method = RequestMethod.GET)
    public Result isExist(@RequestParam(required = true, value = "classifyName") String classifyName){
        return classifyManageService.isExist(classifyName);
    }

    /**
     * 获取分类列表作为下拉字典
     */
    @RequestMapping(value = "/dict", method = RequestMethod.GET)
    public Result classifyDictList(){
        return classifyManageService.classifyDictList();
    }
}
