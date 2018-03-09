package com.huntkey.rx.sceo.form.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.huntkey.rx.commons.utils.rest.Result;

/**
 * 公共服务fegin接口
 * @author wangn1
 *
 */
@FeignClient(value = "commonService-provider")
//@FeignClient(value = "commonService-provider", url = "http://192.168.13.34:10002")
public interface CommomService {
	
	/**
     * 根据CODE值查询字典值的信息
     * @param code 字典信息的CODE
     * @return 字典列表
     */
    @RequestMapping(value="/dict/code/{code}",method = RequestMethod.GET)
    Result getDictsByCode(@PathVariable("code") String code);
    
    /**
     * 根据CODE值跟VALUE查询字典值的信息
     * @param code 字典信息的CODE
     * @return 字典列表
     */
    @RequestMapping(value="/dict/codeVal",method = RequestMethod.GET)
    Result getDictByCodeVal(@RequestParam(value = "code") String code,@RequestParam(value = "value") String value);
}
