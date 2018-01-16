package com.huntkey.rx.sceo.demo.provider.controller;

import com.huntkey.rx.commons.utils.rest.Pagination;
import com.huntkey.rx.commons.utils.rest.Result;
//import com.huntkey.rx.sceo.demo.common.model.User;
import com.huntkey.rx.sceo.demo.provider.model.User;
import com.huntkey.rx.sceo.demo.provider.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public UserService userService;
    /**
     * 国际化
     */
    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result getUserInfo(@PathVariable("id") String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            result.setData(userService.selectByPrimaryKey(id));
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(messageSource.getMessage(
                    "message.system.exception",
                    null,
                    LocaleContextHolder.getLocale()));
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result query(@RequestParam(required = false, defaultValue = "") String userName,
                        @RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            log.info("zipkin test: query provider");
            log.error("zipkin test: query provider");
            Pagination<User> users = userService.selectByExample(userName, pageNum, pageSize);
            result.setData(users);
        } catch (Exception e) {
            log.error("call user query service fail");
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(messageSource.getMessage(
                    "message.system.exception",
                    null,
                    LocaleContextHolder.getLocale()));
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@Valid @RequestBody User user, BindingResult bindingResult) {
        Result result = new Result();

        if (bindingResult.hasErrors()) {
            result.setRetCode(Result.RECODE_VALIDATE_ERROR);
            result.setErrMsg(messageSource.getMessage(
                    "message.validate.exception",
                    null,
                    LocaleContextHolder.getLocale()));
            result.setData(bindingResult.getAllErrors());
            return result;
        }
        result.setRetCode(Result.RECODE_SUCCESS);
        userService.insert(user);
        return result;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Result update(@RequestBody User user) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            userService.updateByPrimaryKey(user);
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(messageSource.getMessage(
                    "message.system.exception",
                    null,
                    LocaleContextHolder.getLocale()));
        }
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            userService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(messageSource.getMessage(
                    "message.system.exception",
                    null,
                    LocaleContextHolder.getLocale()));
        }
        return result;
    }

    /**
     * 异常处理（局部）：
     * Controller 及其子类的可共用的异常处理代码，
     * 可以放在Controller 一个方法中，
     * 用@ExceptionHandler注解表示该方法为异常处理器，
     * 要处理的异常类型用@ExceptionHandler参数指定，或者用该方法的第一个形参类型来指定
     *
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result DuplicateKeyExceptionHandler(DuplicateKeyException e, HttpServletRequest request) {
        e.printStackTrace();
        Result result = new Result();
        result.setRetCode(Result.RECODE_ERROR);
//        result.setErrMsg("主键["+user.getId()+"]重复！");
        return result;
    }

}
