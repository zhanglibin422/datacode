package com.huntkey.rx.sceo.demo.client.feign;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.demo.client.feign.hystrix.UserHystrix;
import com.huntkey.rx.sceo.demo.provider.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhaomj on 2017/4/6.
 */
@FeignClient(value = "demo-provider", fallback = UserHystrix.class)
public interface UserService {

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    Result getUserInfo(@PathVariable("id") String id);

    @RequestMapping(value="/users", method = RequestMethod.POST)
    Result add(@RequestBody User user);

    /**
     * FeignClient 的@RequestParam 参数必须指定 value，
     * 否则会出现异常：
     * java.lang.IllegalStateException: QueryMap parameter must be a Map: int
     * 原因：spring-cloud-feign处理@RequestParam和Spring mvc的不一样，
     * 当@RequestParam的value为empty时，参数会被当作Map来处理
     *
     * @param userName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    Result select(@RequestParam(required = false, defaultValue = "", value = "userName") String userName,
                           @RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                           @RequestParam(defaultValue = "10", value = "pageSize") int pageSize);

    @RequestMapping(value="/users", method = RequestMethod.PUT)
    Result update(@RequestBody User user);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    Result delete(@PathVariable("id") String id);
}
