package com.techstack.component.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring上下文
 *
 */
@Component
public class SpringApplicationContextAware implements ApplicationContextAware{

	private static ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext(){
		return ctx;
	}

}
