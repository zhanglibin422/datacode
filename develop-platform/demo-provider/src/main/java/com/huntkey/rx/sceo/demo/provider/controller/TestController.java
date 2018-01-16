package com.huntkey.rx.sceo.demo.provider.controller;

import com.huntkey.rx.sceo.springboot.starter.log.annotation.Log;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by zhaomj on 2017/4/11.
 */
@RefreshScope
@RestController
@RequestMapping("/test")
public class TestController {

    private static Logger log = LoggerFactory.getLogger(TestController.class);

    @Value("${test.cloud.config.change}")
    private String testValue;

    @Value("${sceo.sso.home-url}")
    private String homeUrl;

    @Autowired
    MessageSource messageSource;

    @Autowired
    LocaleResolver localeResolver;

    @RequestMapping("/config")
    public String confChange(){
        return testValue;
    }

    /**
     *
     * testI18N: 语言切换
     * @author zhaomj
     * @param request
     * @param response
     * @param language 语言
     * @param country 国家
     * @return
     */
    @RequestMapping(value = "/i18n")
    public String testI18N(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(defaultValue="zh") String language, @RequestParam(defaultValue="CN") String country) {
        Locale userLocale = new Locale(language, country);
        // localeResolver设置的Local，允许修改，修改会同步到session或cookie
        localeResolver.setLocale(request, response, userLocale);
        return "ok";
    }

    /**
     * LocaleContextHolder.getLocale()查找localeResolver设置的Local，允许修改，
     * 修改会同步到session或cookie localeResolver先找session中有没有设置SessionLocaleResolver.
     * LOCALE_SSSION_ATTRIBUTE_NAME， 如果有就使用，没有的话看defaultLocale有没设置，如果还没有才使用http
     * Headers中的Accept-Language字段
     *
     * @return
     */
    @RequestMapping("/language")
    public String showLanguage() {
        return messageSource.getMessage("language.info", null, LocaleContextHolder.getLocale());
    }

    @RequestMapping("/session")
    public String session(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
//        String userId = (String )subject.getSession().getAttribute("userId");
        String userId = (String )request.getSession().getAttribute("userId");
        log.info("set session attribute {} value '{}'","userId",userId);

        return "userId: " + userId;
    }

    @Log
    @RequestMapping("/log")
    public void testLog(HttpServletRequest request) throws Exception{
        log.info("===============test log pattern====================");
    }

}
