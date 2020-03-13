package com.atguigu.gmall.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.atguigu.gmall.interceptors.LoginInterceptor;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private LoginInterceptor loginInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//配置这个拦截器其效果 拦截什么路径 
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
		super.addInterceptors(registry);
		
	}
	
	

}
