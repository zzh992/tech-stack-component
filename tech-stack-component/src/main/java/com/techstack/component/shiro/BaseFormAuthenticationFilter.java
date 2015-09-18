package com.techstack.component.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;


public class BaseFormAuthenticationFilter extends FormAuthenticationFilter {

	/**
	 * 重写login success之后的跳转，由于原来的跳转判断由WebUtils.redirectToSavedRequest
	 * method完成，而里面的实现有可能不往原先设置的successUrl跳转。
	 * http://my.oschina.net/u/661028/blog/72580
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		WebUtils.getAndClearSavedRequest(request);
		return super.onLoginSuccess(token, subject, request, response);
	}

	
}
