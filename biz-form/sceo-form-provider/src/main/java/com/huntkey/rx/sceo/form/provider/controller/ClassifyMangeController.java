package com.huntkey.rx.sceo.form.provider.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.common.model.BizFormClassify;
import com.huntkey.rx.sceo.form.provider.service.ClassifyMangeService;

/**
 * Created by wangn1 on 2017/6/12.
 */
@RestController
@RequestMapping("/classify")
public class ClassifyMangeController {

    @Autowired
    private ClassifyMangeService classifyMangeService;

    private static Logger logger = LoggerFactory.getLogger(ClassifyMangeController.class);


    /**
     * 获取所有分类
     * @param formName 表单名称关键字
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result getClassifyList(@RequestParam(name="formName") String formName){
        Result result = new Result();
        try{
            result.setData(classifyMangeService.getClassifyList(StringEscapeUtils.escapeSql(formName)));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch(Exception e){
            e.printStackTrace();
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result getClassifyById(@PathVariable("id") String id){
        Result result = new Result();
        try{
            result.setData(classifyMangeService.getClassifyById(id));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 新增表单分类
     * @param bizFormClassify 分类名称
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result addClassify(@Valid@RequestBody BizFormClassify bizFormClassify){
        Result result = new Result();
        BizFormClassify flag = classifyMangeService.isExist(bizFormClassify.getClassifyName());
        if(null != flag){
            result.setRetCode(Result.RECODE_VALIDATE_ERROR);
            result.setErrMsg("当前的分类名称已经存在!");
        }else{
            try{
                result.setData(classifyMangeService.addClassify(bizFormClassify.getClassifyName()));
                result.setRetCode(Result.RECODE_SUCCESS);
            }catch(Exception e){
                logger.error(e.getMessage());
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 更新表单分类
     * @param bizFormClassify 分类对象
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Result updateClassify(@Valid@RequestBody BizFormClassify bizFormClassify){
        Result result = new Result();
        BizFormClassify flag = classifyMangeService.isExist(bizFormClassify.getClassifyName());
        if(null != flag && !bizFormClassify.getClassifyId().equals(flag.getClassifyId())){
            result.setRetCode(Result.RECODE_VALIDATE_ERROR);
            result.setErrMsg("当前的分类名称已经存在!");
        }else {
            try {
                classifyMangeService.updateClassify(bizFormClassify);
                result.setRetCode(Result.RECODE_SUCCESS);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg(e.getMessage());
            }
        }
        return result;
    }

    /**
     * 删除表单分类
     * @param classifyId 分类名称
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Result deleteClassify(@RequestParam(value = "classifyId") String classifyId){
        Result result = new Result();
        try{
            classifyMangeService.deleteClassify(classifyId);
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 当前的分类名是否存在
     * @param classifyName 分类名
     */
    @RequestMapping(value = "/isExist", method = RequestMethod.GET)
    public Result isExist(@RequestParam(required = true, value = "classifyName") String classifyName){
        Result result = new Result();
        try{
            result.setData(classifyMangeService.isExist(classifyName));
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 获取分类列表作为下拉字典
     */
    @RequestMapping(value = "dict", method = RequestMethod.GET)
    public Result classifyDictList(){
        Result result = new Result();
        try{
            result.setData(classifyMangeService.classifyDictList());
            result.setRetCode(Result.RECODE_SUCCESS);
        }catch(Exception e){
            logger.error(e.getMessage());
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
        }
        return result;
    }
}
