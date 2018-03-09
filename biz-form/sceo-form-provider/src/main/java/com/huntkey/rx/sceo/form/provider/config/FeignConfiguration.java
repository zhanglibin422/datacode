package com.huntkey.rx.sceo.form.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;
import feign.Retryer;

/**
 * Created by xuyf on 2017/7/27.
 */
@Configuration
public class FeignConfiguration {

    @Bean
    Retryer retryer(){
        return Retryer.NEVER_RETRY;
    }

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(/**connectTimeoutMillis**/60 * 1000, /** readTimeoutMillis **/60 * 1000);
    }
}
