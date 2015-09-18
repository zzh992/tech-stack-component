package com.techstack.component.spring.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.techstack.component.shiro.ShiroUser;

public abstract class SpringMVCBaseController {

	@Autowired
	private HttpServletRequest httpRequest;

	public Double getDouble(String key) {
		String value = httpRequest.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Double.parseDouble(value);
		}
		return null;
	}

	public Integer getInteger(String key) {
		String value = httpRequest.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		}
		return null;
	}

	public Long getLong(String key) {
		String value = httpRequest.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Long.parseLong(value);
		}
		return null;
	}

	public String getString(String key) {
		return httpRequest.getParameter(key);
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
	public ShiroUser getCurrentUser(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

	/**
	 * 根据request对象，获取页面提交过来的DWZ框架的AjaxDone响应参数值.
	 * 
	 * @param statusCode
	 *            状态码.
	 * @param message
	 *            操作结果提示消息.
	 * @return DwzParam :封装好的DwzParam对象 .
	 *//*
	public DwzParam getDwzParam(String statusCode, String message) {
		// 获取DWZ Ajax响应参数值,并构造成参数对象
		String navTabId = request.getParameter("navTabId");
		String dialogId = request.getParameter("dialogId");
		String callbackType = request.getParameter("callbackType");
		String forwardUrl = request.getParameter("forwardUrl");
		String rel = request.getParameter("rel");
		return new DwzParam(statusCode, message, navTabId, dialogId, callbackType, forwardUrl, rel, null);
	}

	*//**
	 * 响应DWZ的Ajax请求.
	 * 
	 * @param statusCode
	 *            statusCode:{ok:200, error:300, timeout:301}.
	 * @param message
	 *            提示消息.
	 *//*
	private ModelAndView ajaxDone(String statusCode, String message, String path) {
		DwzParam param = getDwzParam(statusCode, message);
		ModelMap modelMap = new ModelMap();
		modelMap.putAll(transBeanToMap(param));
		ModelAndView mav = new ModelAndView(path);
		mav.addAllObjects(modelMap);
		return mav;
	}

	*//**
	 * 响应DWZ-UI的Ajax成功请求（statusCode="200"）,<br/>
	 * 跳转到operateSuccess视图并提示“操作成功”.
	 * 
	 * @param message
	 *            提示消息.
	 * @return operateSuccess .
	 *//*
	public ModelAndView operateSuccess() {
		return ajaxDone("200", "Operate Success","page/common/operateSuccess.jsp");
	}

	*//**
	 * 响应DWZ的Ajax成功请求（statusCode="200"）,<br/>
	 * 跳转到operateSuccess视图，提示设置的消息内容.
	 * 
	 * @param message
	 *            提示消息.
	 * @return operateSuccess .
	 *//*
	public ModelAndView operateSuccess(String message) {
		return ajaxDone("200", message ,"page/common/operateSuccess.jsp");
	}

	*//**
	 * 响应DWZ的ajax失败请求（statusCode="300"）,跳转到ajaxDone视图.
	 * 
	 * @param message
	 *            提示消息.
	 * @return ajaxDone .
	 *//*
	public ModelAndView operateError(String message) {
		return ajaxDone("300", message ,"page/common/operateSuccess.jsp");
	}
	
	
	*//**
	 * 获取当前页（DWZ-UI分页查询参数）.
	 * 如果没有值则默认返回1.
	 * 
	 *//*
	private int getPageNum() {
		// 当前页数
		String pageNumStr = request.getParameter("pageNum");
		int pageNum = 1;
		if (StringUtils.isNotBlank(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		return pageNum;
	}

	*//**
	 * 获取每页记录数（DWZ-UI分页查询参数）.
	 * 如果没有值则默认返回15.
	 * 
	 *//*
	private int getNumPerPage() {
		String numPerPageStr = request.getParameter("numPerPage");
		int numPerPage = 20;
		if (StringUtils.isNotBlank(numPerPageStr)) {
			numPerPage = Integer.parseInt(numPerPageStr);
		}
		return numPerPage;
	}

	*//**
	 * 获取分页参数，包含当前页、每页记录数.
	 * 
	 * @return PageParam .
	 *//*
	public PageParam getPageParam() {
		return new PageParam(getPageNum(), getNumPerPage());
	}*/


	/*// Bean --> Map : 利用Introspector和PropertyDescriptor 将Bean --> Map
	public Map<String, Object> transBeanToMap(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			System.out.println("transBeanToMap Error " + e);
		}
		return map;

	}*/

	
	
	
	
}
