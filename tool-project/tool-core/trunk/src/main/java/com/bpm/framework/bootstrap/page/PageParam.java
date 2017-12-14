package com.bpm.framework.bootstrap.page;

import java.io.Serializable;

/**
 * 
 * 封装分页统一参数
 * pageSize/pageNumber/sortName/sortType
 * 
 * 后续可根据每个表单的实际查询条件继承该类，扩展实际表单的查询条件
 * 
 * @author lixx
 * @createDate 2015-06-03 17:50:00
 */
public class PageParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8228282242892052712L;
	
	private int pageSize=10;
	
	private int pageNumber=1;
	
	private String sortName;// 排序字段
	
	private String sortOrder;// asc desc
	
	public final static String DIRECTION_ASC = "asc";
	
	public final static String DIRECTION_DESC = "desc";

	public int getPageSize() {
		if(pageSize==0){
			pageSize=10;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		if(pageNumber==0){
			pageNumber=1;
		}
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
