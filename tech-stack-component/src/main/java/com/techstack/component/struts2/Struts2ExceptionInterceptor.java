package com.techstack.component.struts2;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class Struts2ExceptionInterceptor extends Struts2BaseController implements Interceptor{

	private static final long serialVersionUID = 5085654227519338304L;
	
	private static final Logger log = LoggerFactory.getLogger(Struts2ExceptionInterceptor.class);

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch (AuthorizationException e) {
			log.error(e.getStackTrace().toString());
			return "login";
			//return operateError("服务异常，异常代码：" + e.getCode() + "，如有疑问，请联系管理员！");
		} catch (Exception e) {
			//ByteArrayOutputStream ostr = new ByteArrayOutputStream();
			//e.printStackTrace(new PrintStream(ostr));
			log.error(e.getStackTrace().toString());
			return "login";
			//return operateError("系统异常，请联系管理员！");
		}
	}

}
