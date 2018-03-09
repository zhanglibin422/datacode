package com.huntkey.rx.sceo.form.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import feign.Request;
import feign.Retryer;

@Configuration
public class CorsConfig {

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}
	
    @Bean
    Retryer retryer(){
        return Retryer.NEVER_RETRY;
    }

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(/**connectTimeoutMillis**/60 * 1000, /** readTimeoutMillis **/60 * 1000);
    }
}
