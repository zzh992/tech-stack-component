package com.techstack.component.spring.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class SpringMVCExceptionResolver implements HandlerExceptionResolver {
	
	private static final Logger log = LoggerFactory.getLogger(SpringMVCExceptionResolver.class);
	
	private String unauthorizedUrl;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		log.error(ex.toString());
		ModelAndView mav = new ModelAndView();
		if(ex instanceof AuthorizationException){
			mav.setViewName(unauthorizedUrl);
		}else{
			mav.setViewName(unauthorizedUrl);
		}
		return mav;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	
}
