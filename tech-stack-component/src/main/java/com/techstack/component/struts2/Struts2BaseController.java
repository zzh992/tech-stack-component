package com.techstack.component.struts2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.techstack.component.shiro.ShiroUser;


@SuppressWarnings("serial")
public class Struts2BaseController extends ActionSupport{
	private static ThreadLocal<Map<String, Object>> outPutMsg = new ThreadLocal<Map<String, Object>>();

	//public PageBean pageBean;

	//public Integer pageNum;
	
	private static final Logger log = LoggerFactory.getLogger(Struts2BaseController.class);
	
	
	
	/**
	 * 取出当前登录操作员对象
	 *//*
	@SuppressWarnings("unchecked")
	public PmsUser getLoginedUser() {
		Map<String, Object> userInfoMap = (Map<String, Object>) getSessionMap().get("userInfoMap");
		if(userInfoMap == null){
			return null;
		}else{
			return (PmsUser) userInfoMap.get("pmsUser");
		}
		
	}*/
	
	public ShiroUser getCurrentUser(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

	/**
	 * @Description: 线程绑定，其内容会在outPrint方法调用后清空
	 * @param @return    
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getOutputMsg() {
		Map<String, Object> output = outPutMsg.get();
		if (output == null) {
			output = new HashMap<String, Object>();
			outPutMsg.set(output);
		}
		return output;
	}

	protected void setOutputMsg(String key, String value) {
		getOutputMsg().put(key, value);
	}

	/**
	 * @Description: 输出，同时清空outPutMsg
	 * @param @param response
	 * @param @param result    
	 * @return void
	 */
	public void outPrint(HttpServletResponse response, Object result) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
			out.print(result.toString());
			getOutputMsg().clear();
		} catch (IOException e) {
			log.error("==== error ==== 输出流出现异常",e);
		} finally {
			out.close();
		}
	}


	/**
	 * pageBean.
	 * 
	 * @return the pageBean
	 */
	/*public PageBean getPageBean() {
		return pageBean;
	}

	*//**
	 * @param pageBean
	 *            the pageBean to set
	 *//*
	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}*/

	/**
	 * 取得当前request
	 * 
	 * @return
	 */
	public HttpServletRequest getHttpRequest() {
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 取得当前response
	 * 
	 * @return
	 */
	public HttpServletResponse getHttpResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * 获取session里面的属性
	 * 
	 * @return
	 */
	public Map<String, Object> getSessionMap() {
		return ActionContext.getContext().getSession();
	}


	/**
	 * 响应DWZ-UI的Ajax成功请求（statusCode="200"）,<br/>
	 * 跳转到operateSuccess视图并提示“操作成功”.
	 * @param message
	 *            提示消息.
	 * @return operateSuccess .
	 *//*
	public String operateSuccess() {
		ajaxDone("200", "操作成功");
		return "operateSuccess";
	}

	*//**
	 * 响应DWZ的Ajax成功请求（statusCode="200"）,<br/>
	 * 跳转到operateSuccess视图，提示设置的消息内容.
	 * @param message
	 *            提示消息.
	 * @return operateSuccess .
	 *//*
	public String operateSuccess(String message) {
		ajaxDone("200", message);
		return "operateSuccess";
	}

	*//**
	 * 响应DWZ的ajax失败请求（statusCode="300"）,跳转到ajaxDone视图.
	 * @param message
	 *            提示消息.
	 * @return ajaxDone .
	 *//*
	public String operateError(String message) {
		ajaxDone("300", message);
		return "operateError";
	}*/
	
	/**
	 * @Description: 响应DWZ的ajax失败请求（statusCode="300"）,弹出错误提示框并关闭当前TAB
	 * @param @param message
	 * @param @return    
	 * @return String
	 *//*
	public String operateErrorAndCloseTab(String message){
		DwzParam param = getDwzParam("300", message);
		//param.setCloseCurrentTab("true");
		param.setCallbackType("closeCurrent");
		ActionContext.getContext().getValueStack().push(param);
		return "operateError";
	}*/


	/**
	 * 响应DWZ的Ajax请求.
	 * 
	 * @param statusCode
	 *            statusCode:{ok:200, error:300, timeout:301}.
	 * @param message
	 *            提示消息.
	 *//*
	private void ajaxDone(String statusCode, String message) {
		DwzParam param = getDwzParam(statusCode, message);
		ActionContext.getContext().getValueStack().push(param);
	}*/

	/**
	 * 根据request对象，获取页面提交过来的DWZ框架的AjaxDone响应参数值.
	 * @param statusCode
	 *            状态码.
	 * @param message
	 *            操作结果提示消息.
	 * @return DwzParam :封装好的DwzParam对象 .
	 */
	/*public DwzParam getDwzParam(String statusCode, String message) {
		// 获取DWZ Ajax响应参数值,并构造成参数对象
		HttpServletRequest req = ServletActionContext.getRequest();
		String navTabId = req.getParameter("navTabId");
		String dialogId = req.getParameter("dialogId");
		String callbackType = req.getParameter("callbackType");
		String forwardUrl = req.getParameter("forwardUrl");
		String rel = req.getParameter("rel");
		return new DwzParam(statusCode, message, navTabId, dialogId, callbackType, forwardUrl, rel, null);
	}*/

	// ///////////////////////////////////////////////////////////////
	// ///////////////// 结合DWZ-UI的分页参数获取方法 ///////////////////////////
	/**
	 * 获取当前页（DWZ-UI分页查询参数）.<br/>
	 * 如果没有值则默认返回1.
	 * 
	 *//*
	private int getPageNum() {
		// 当前页数
		String pageNumStr = getHttpRequest().getParameter("pageNum");
		int pageNum = 1;
		if (StringUtils.isNotBlank(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		return pageNum;
	}

	*//**
	 * 获取每页记录数（DWZ-UI分页查询参数）.<br/>
	 * 如果没有值则默认返回15.
	 * 
	 *//*
	private int getNumPerPage() {
		String numPerPageStr = getHttpRequest().getParameter("numPerPage");
		int numPerPage = 20;
		if (StringUtils.isNotBlank(numPerPageStr)) {
			numPerPage = Integer.parseInt(numPerPageStr);
		}
		return numPerPage;
	}*/

	/**
	 * 获取分页参数，包含当前页、每页记录数.
	 * 
	 * @return PageParam .
	 *//*
	public PageParam getPageParam() {
		return new PageParam(getPageNum(), getNumPerPage());
	}*/

	// //////////////////////// 存值方法 /////////////////////////////////
	/**
	 * 将数据放入Struts2上下文的值栈中.<br/>
	 * ActionContext.getContext().getValueStack().push(obj);
	 */
	public void pushData(Object obj) {
		ActionContext.getContext().getValueStack().push(obj);
	}

	/**
	 * 将数据放入Struts2上下文中.<br/>
	 * ActionContext.getContext().put(key, value);
	 */
	public void putData(String key, Object value) {
		ActionContext.getContext().put(key, value);
	}

	// ///////////////////////getHttpRequest()方法扩展//////////////////////////
	/**
	 * 根据参数名从HttpRequest中获取Double类型的参数值，无值则返回null .
	 * 
	 * @param key
	 *            .
	 * @return DoubleValue or null .
	 */
	public Double getDouble(String key) {
		String value = getHttpRequest().getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Double.parseDouble(value);
		}
		return null;
	}

	/**
	 * 根据参数名从HttpRequest中获取Integer类型的参数值，无值则返回null .
	 * 
	 * @param key
	 *            .
	 * @return IntegerValue or null .
	 */
	public Integer getInteger(String key) {
		String value = getHttpRequest().getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		}
		return null;
	}

	/**
	 * 根据参数名从HttpRequest中获取Long类型的参数值，无值则返回null .
	 * 
	 * @param key
	 *            .
	 * @return LongValue or null .
	 */
	public Long getLong(String key) {
		String value = getHttpRequest().getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Long.parseLong(value);
		}
		return null;
	}

	/**
	 * 根据参数名从HttpRequest中获取String类型的参数值，无值则返回null .
	 * 
	 * @param key
	 *            .
	 * @return String or null .
	 */
	public String getString(String key) {
		return getHttpRequest().getParameter(key);
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
	/*protected String lengthValidate(String propertyName, String property, boolean isRequire, int minLength, int maxLength) {
		int propertyLenght = StringUtil.strLengthCn(property);
		if (isRequire && propertyLenght == 0) {
			return propertyName + "不能为空，"; // 校验不能为空
		} else if (isRequire && minLength != 0 && propertyLenght < minLength) {
			return propertyName + "不能少于" + minLength + "个字符，"; // 必填情况下校验最少长度
		} else if (maxLength != 0 && propertyLenght > maxLength) {
			return propertyName + "不能多于" + maxLength + "个字符，"; // 校验最大长度
		} else {
			return ""; // 校验通过则返回空字符串 .
		}
	}*/
}
