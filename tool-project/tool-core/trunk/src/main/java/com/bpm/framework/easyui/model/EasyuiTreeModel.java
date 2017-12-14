package com.bpm.framework.easyui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EasyuiTreeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3107528916582071233L;

	public static final String STATE_CLOSED = "closed";
	public static final String STATE_OPEN = "open";
	
	private String id;
	
	private String text;
	
	private String code;   // 编码      数据字典数中使用到
	
	private String parentValue;   // 父节点值      数据字典数中使用到
	
	private String parentCode;   //父节点 编码        数据字典数中使用到
	
	private String type;   //节点类型，组织树中使用到
	
	private String companyOrgId; // 结点所属公司ID  组织树中使用到
	
	private String companyOrgName; // 结点所属公司名称  组织树中使用到
	
	private String organizationId;// 结点所属部门id    组织树中使用到
	
	private String organizationName;// 结点所属部门名称  组织树中使用到

	private String calendarName;   //  日历名称  组织树中使用到

	private Long calendarId;  //  日历id  组织树中使用到
	
	private boolean isLeaf = false;
	
	private String url;
	
	private String state = STATE_CLOSED;
	
	private boolean checked = false;// 当为多选的时候才有效
	
	private List<EasyuiTreeModel> children = new ArrayList<EasyuiTreeModel>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<EasyuiTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<EasyuiTreeModel> children) {
		this.children = children;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCompanyOrgId() {
		return companyOrgId;
	}

	public void setCompanyOrgId(String companyOrgId) {
		this.companyOrgId = companyOrgId;
	}

	public String getCompanyOrgName() {
		return companyOrgName;
	}

	public void setCompanyOrgName(String companyOrgName) {
		this.companyOrgName = companyOrgName;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public Long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}
}
