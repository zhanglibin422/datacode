package com.huntkey.rx.sceo.commonService.provider;

import com.huntkey.rx.sceo.commonService.provider.config.DynamicDataSource;
import com.huntkey.rx.sceo.commonService.provider.config.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * Created by licj on 2017/6/22.
 */
@SpringBootApplication
@EnableDiscoveryClient
//@Import(DynamicDataSourceRegister.class)
public class CommonProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonProviderApplication.class,args);
    }
}
