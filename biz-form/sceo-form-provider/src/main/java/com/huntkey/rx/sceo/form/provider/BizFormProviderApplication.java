package com.huntkey.rx.sceo.form.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.huntkey.rx.sceo.form.client.feign")
public class BizFormProviderApplication {
	public static void main(String[] args) {	
		SpringApplication.run(BizFormProviderApplication.class, args);
		System.out.println("----项目启动完成----");
	}
}
