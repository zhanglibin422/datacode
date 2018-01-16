package com.huntkey.rx.sceo.demo.provider;

import com.huntkey.rx.sceo.sso.client.annotations.EnableCasShiroClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableCasShiroClient
public class DemoProviderApplication {
	public static void main(String[] args) {	
		SpringApplication.run(DemoProviderApplication.class, args);
	}
}
