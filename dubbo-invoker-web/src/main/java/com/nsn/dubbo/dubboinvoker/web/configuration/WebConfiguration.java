package com.nsn.dubbo.dubboinvoker.web.configuration;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nsn
 */
@Configuration
@ComponentScan("com.nsn")
public class WebConfiguration  {

	@Bean
	public ToStringSerializer toStringSerializer(){
		return new ToStringSerializer();
	}

	@Bean
	public FastJsonConfig fastJsonConfig(ToStringSerializer toStringSerializer){
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializeFilters(toStringSerializer);
		return config;
	}

	@Bean
	public FastJsonHttpMessageConverter jsonConverter(FastJsonConfig config){
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		converter.setSupportedMediaTypes(mediaTypes);
		converter.setFastJsonConfig(config);
		return converter;
	}

}

