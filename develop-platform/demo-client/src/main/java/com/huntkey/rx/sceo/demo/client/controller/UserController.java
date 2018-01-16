package com.huntkey.rx.sceo.demo.client.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.demo.client.feign.UserService;
import com.huntkey.rx.sceo.demo.provider.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhaomj on 2017/4/6.
 */
@RestController
@RequestMapping("/v1/users")
@Api(value = "用户信息维护服务", description = "提供用户信息的新增、修改、删除和查询服务")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result getUserInfo(@PathVariable String id){
        Result result = new Result();
        try {
            result = userService.getUserInfo(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "查询用户列表", notes = "支持分页，支持用户名作为查询参数")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "pageNum", value = "当前页", required = false, dataType = "Integer"),
//            @ApiImplicitParam(name = "pageSize", value = "每页记录数")}
//    )
    public Result query(@RequestParam(required = false, defaultValue = "") String userName,
                        @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            log.info("zipkin test : call api user query service");
            result = userService.select(userName, pageNum, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("call api user query service fail");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("添加新用户")
    public Result add(@RequestBody User user) {
        Result result = new Result();
        try {
            result = userService.add(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("根据主键修改用户信息")
    @RequestMapping(method = RequestMethod.PUT)
    public Result update(@RequestBody User user) {
        Result result = new Result();
        try {
            result = userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation("根据主键删除用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        Result result = new Result();
        try {
            result = userService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
