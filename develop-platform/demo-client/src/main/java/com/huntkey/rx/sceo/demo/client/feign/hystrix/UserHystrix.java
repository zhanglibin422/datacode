package com.huntkey.rx.sceo.demo.client.feign.hystrix;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.demo.client.feign.UserService;
import com.huntkey.rx.sceo.demo.provider.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by zhaomj on 2017/4/6.
 */
@Component
public class UserHystrix implements UserService {

    @Override
    public Result getUserInfo(String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用服务降级处理逻辑！");
        return result;
    }

    @Override
    public Result add(@RequestBody User user) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用服务降级处理逻辑！");
        return result;
    }

    @Override
    public Result select(@RequestParam(required = false, defaultValue = "") String userName,
                                  @RequestParam(defaultValue = "1") int pageNum,
                                  @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用服务降级处理逻辑！");
        return result;
    }

    @Override
    public Result update(@RequestBody User user) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用服务降级处理逻辑！");
        return result;
    }

    @Override
    public Result delete(@RequestParam String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
        result.setErrMsg("调用服务降级处理逻辑！");
        return result;
    }
}
