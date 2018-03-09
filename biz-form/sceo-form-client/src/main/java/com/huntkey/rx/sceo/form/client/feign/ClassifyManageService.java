package com.huntkey.rx.sceo.form.client.feign;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.client.feign.hystrix.ClassifyManageHystrix;
import com.huntkey.rx.sceo.form.common.model.BizFormClassify;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wangn1 on 2017/6/12.
 */
@FeignClient(value = "form-provider", fallback = ClassifyManageHystrix.class)
public interface ClassifyManageService {

    /**
     * 获取所有分类
     * @param formName 表单名称关键字
     */
    @RequestMapping(value = "/classify",method = RequestMethod.GET)
    Result getClassifyList(@RequestParam(name="formName") String formName);

    /**
     * 根据ID获取分类信息
     * @param id 分类ID
     */
    @RequestMapping(value = "/classify/{id}",method = RequestMethod.GET)
    Result getClassifyById(@PathVariable("id") String id);

    /**
     * 新增表单分类
     * @param bizFormClassify 分类对象
     */
    @RequestMapping(value = "/classify", method = RequestMethod.POST)
    Result addClassify(@RequestBody BizFormClassify bizFormClassify);

    /**
     * 更新表单分类
     * @param bizFormClassify 分类名称
     * @Param id 分类ID
     */
    @RequestMapping(value = "/classify", method = RequestMethod.PUT)
    Result updateClassify(@RequestBody BizFormClassify bizFormClassify);

    /**
     * 删除表单分类
     * @param classifyId 分类名称
     */
    @RequestMapping(value = "/classify", method = RequestMethod.DELETE)
    Result deleteClassify(@RequestParam(value = "classifyId") String classifyId);

    /**
     * 当前的分类名是否存在
     * @param classifyName 分类名
     */
    @RequestMapping(value = "/classify/isExist", method = RequestMethod.GET)
    Result isExist(@RequestParam(required = true, value = "classifyName") String classifyName);

    /**
     * 获取分类列表作为下拉字典
     */
    @RequestMapping(value = "/classify/dict", method = RequestMethod.GET)
    Result classifyDictList();
}
