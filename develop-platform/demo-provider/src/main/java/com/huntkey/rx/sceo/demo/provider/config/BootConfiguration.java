package com.huntkey.rx.sceo.demo.provider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Created by zhaomj on 2017/4/13.
 */
@Configuration
public class BootConfiguration {

    @Bean
    public LocaleResolver localeResolver(){
       return new SessionLocaleResolver();
    }
}
