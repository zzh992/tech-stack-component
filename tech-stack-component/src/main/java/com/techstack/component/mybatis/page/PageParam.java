package com.techstack.component.mybatis.page;

import java.io.Serializable;

/**
 * @Title: PageParam.java 
 * @Description: 分页参数传递工具类 
 * @author zzh
 */
public class PageParam implements Serializable{
	
	private static final long serialVersionUID = 6297178964005032338L;
	
	/**	当前页数	*/
	private int pageNum; 
	
	/**	每页记录数	*/
	private int numPerPage; 
	
	public PageParam(int pageNum, int numPerPage) {
		super();
		this.pageNum = pageNum;
		this.numPerPage = numPerPage;
	}

	/** 当前页数 */
	public int getPageNum() {
		return pageNum;
	}

	/** 当前页数 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/** 每页记录数 */
	public int getNumPerPage() {
		return numPerPage;
	}

	/** 每页记录数 */
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

}
