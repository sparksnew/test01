package com.oristartech.cinema.utils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextUtil implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextUtil.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
}