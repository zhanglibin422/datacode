package com.huntkey.rx.sceo.form.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huntkey.rx.commons.utils.rest.Result;

/**
 * 服务中心fegin调用接口
 * @author wangn1
 *
 */
//@FeignClient(value = "SERVICECENTER-PROVIDER",url="http://192.168.13.34:2008")
@FeignClient(value = "SERVICECENTER-PROVIDER")
public interface ServiceCenterService {
	
	/**
     * 新增数据数据到es和hbase
     * @param json JSON数据
     */
    @RequestMapping(value = "/servicecenter/add", method = RequestMethod.POST)
	Result add(@RequestBody String json);
    
    /**
     * 修改数据数据到es和hbase
     * @param json JSON数据
     */
    @RequestMapping(value = "/servicecenter/update", method = RequestMethod.POST)
	Result update(@RequestBody String json);
    
    /**
     * 查询数据
     * @param json JSON数据
     */
    @RequestMapping(value = "/servicecenter/find", method = RequestMethod.POST)
	Result find(@RequestBody String json);
    

    /**
     * 根据id删除es索引和hbase表
     * @param json JSON数据
     */
    @RequestMapping(value = "/servicecenter/delete", method = RequestMethod.POST)
    Result delete(@RequestBody String json);
}
