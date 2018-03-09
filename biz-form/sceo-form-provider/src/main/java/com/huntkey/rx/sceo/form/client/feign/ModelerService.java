package com.huntkey.rx.sceo.form.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huntkey.rx.commons.utils.rest.Result;

//@FeignClient(value = "MODELER-PROVIDER",url="http://192.168.13.34:2002")
@FeignClient(value = "MODELER-PROVIDER")
public interface ModelerService {
	
	/**
     * 根据id获取类的信息
     * @param id 模型ID
     */
    @RequestMapping(value = "/classes/simple/{id}", method = RequestMethod.GET)
	Result getSimpleClassById(@PathVariable("id") String id);
    
    /**
     * 设置类的默认值
     * @param classid 模型ID(类ID)
     * @param objectId 业务数据对象ID
     * @return
     */
    @RequestMapping(value = "/properties/default/values", method = RequestMethod.PUT)
    Result setDefaultValue(@RequestParam("classid") String classid,@RequestParam("objectId") String objectId);
}
