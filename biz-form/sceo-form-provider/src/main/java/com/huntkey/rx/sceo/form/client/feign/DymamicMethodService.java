package com.huntkey.rx.sceo.form.client.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.form.common.model.JsEntity;

@FeignClient(value = "biz-dynamic-methods-provider")
//@FeignClient(value = "biz-dynamic-methods-provider", url = "http://192.168.113.143:13002")
public interface DymamicMethodService {
	
	@RequestMapping(value = "/v1/js-executor/js", method = RequestMethod.POST)
	Result executorJs(@RequestBody JsEntity jsEntity);
}
