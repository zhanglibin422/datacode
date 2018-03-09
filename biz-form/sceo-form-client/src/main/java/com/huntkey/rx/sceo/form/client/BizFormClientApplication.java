package com.huntkey.rx.sceo.form.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * Created by zhanglb on 2017/6/6.
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableHystrix
public class BizFormClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizFormClientApplication.class,args);
    }
}
