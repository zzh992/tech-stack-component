package com.techstack.component.dwz;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.opensymphony.xwork2.ActionContext;
import com.techstack.component.mapper.BeanMapper;

public class DwzUtils {
	
	public static String operateSuccessInStruts2(String message){
		ajaxDone("200", message);
		return "operateSuccess";
	}
	
	public static ModelAndView operateSuccessInSpringMVC(String message, HttpServletRequest req, String url){
		return ajaxDone("200", message, req, url);
	}
	
	public static String operateErrorInStruts2(String message){
		ajaxDone("300", message);
		return "operateError";
	}
	
	public static ModelAndView operateErrorInSpringMVC(String message, HttpServletRequest req, String url){
		return ajaxDone("300", message, req, url);
	}
	
	private static void ajaxDone(String statusCode, String message) {
		DwzParam param = getDwzParam(statusCode, message, ServletActionContext.getRequest());
		ActionContext.getContext().getValueStack().push(param);
	}
	
	private static ModelAndView ajaxDone(String statusCode, String message, HttpServletRequest req, String url){
		DwzParam param = getDwzParam(statusCode, message, req);
		ModelMap modelMap = new ModelMap();
		modelMap.putAll(BeanMapper.map(param, Map.class));
		//ModelAndView mav = new ModelAndView("dwz/operateResult.jsp");
		ModelAndView mav = new ModelAndView(url);
		mav.addAllObjects(modelMap);
		return mav;
	}
	
	private static DwzParam getDwzParam(String statusCode, String message, HttpServletRequest req){
		// 获取DWZ Ajax响应参数值,并构造成参数对象
		//HttpServletRequest req = ServletActionContext.getRequest();
		String navTabId = req.getParameter("navTabId");
		String dialogId = req.getParameter("dialogId");
		String callbackType = req.getParameter("callbackType");
		String forwardUrl = req.getParameter("forwardUrl");
		String rel = req.getParameter("rel");
		return new DwzParam(statusCode, message, navTabId, dialogId, callbackType, forwardUrl, rel, null);
	}
	
	/**
	 * 与DWZ框架结合的表单属性长度校验方法.
	 * 
	 * @param propertyName
	 *            要校验的属性中文名称，如“登录名”.
	 * @param property
	 *            要校验的属性值，如“gzzyzz”.
	 * @param isRequire
	 *            是否必填:true or false.
	 * @param minLength
	 *            最少长度:大于或等于0，如果不限制则可请设为0.
	 * @param maxLength
	 *            最大长度:对应数据库字段的最大长度，如不限制则可设为0.
	 * @return 校验结果消息，校验通过则返回空字符串 .
	 */
	public static String lengthValidate(String propertyName, String property, boolean isRequire, int minLength, int maxLength) {
		int propertyLenght = property.length();
		if (isRequire && propertyLenght == 0) {
			return propertyName + "不能为空，"; // 校验不能为空
		} else if (isRequire && minLength != 0 && propertyLenght < minLength) {
			return propertyName + "不能少于" + minLength + "个字符，"; // 必填情况下校验最少长度
		} else if (maxLength != 0 && propertyLenght > maxLength) {
			return propertyName + "不能多于" + maxLength + "个字符，"; // 校验最大长度
		} else {
			return ""; // 校验通过则返回空字符串 .
		}
	}
	
	public static Pageable getPageableInStruts2(){
		Pageable pageable = new PageRequest(getPageNum(ServletActionContext.getRequest()), getNumPerPage(ServletActionContext.getRequest()));
		return pageable;
	}
	
	/**
	 * 获取当前页（DWZ-UI分页查询参数）.<br/>
	 * 如果没有值则默认返回1.
	 * 
	 */
	public static int getPageNum(HttpServletRequest req) {
		// 当前页数
		String pageNumStr = req.getParameter("pageNum");
		int pageNum = 1;
		if (StringUtils.isNotBlank(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		return pageNum;
	}

	/**
	 * 获取每页记录数（DWZ-UI分页查询参数）.<br/>
	 * 如果没有值则默认返回15.
	 * 
	 */
	public static int getNumPerPage(HttpServletRequest req) {
		String numPerPageStr = req.getParameter("numPerPage");
		int numPerPage = 20;
		if (StringUtils.isNotBlank(numPerPageStr)) {
			numPerPage = Integer.parseInt(numPerPageStr);
		}
		return numPerPage;
	}
}
