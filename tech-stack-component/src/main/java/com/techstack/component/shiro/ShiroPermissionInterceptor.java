package com.techstack.component.shiro;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;

public class ShiroPermissionInterceptor implements MethodInterceptor{

	private String unauthorizedUrl;	//TODO: spring mvc会报异常，不能直接抛出字符串

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//判断是否有RequiresPermissions注解
		if (invocation.getMethod().isAnnotationPresent(RequiresPermissions.class)) {
			try{
				invocation.proceed();
			}catch(UnauthorizedException unauthorizedException){
				return unauthorizedUrl;
			}
		}
		return invocation.proceed();
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	
	
}
