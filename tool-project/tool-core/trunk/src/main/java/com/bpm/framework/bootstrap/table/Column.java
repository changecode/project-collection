package com.bpm.framework.bootstrap.table;

import java.io.Serializable;

public class Column implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1923515281677206425L;
	
	private Boolean radio = false;
	
	private Boolean checkbox = false;
	
	private Boolean checkboxEnabled = true;
	
	private String field;
	
	private String title = field;
	
	private String align;
	
	private int width;
	
	private Boolean sortable = true;
	
	private Boolean visible = true;
	
	private String formatter;
	
	private String footerFormatter;
	
	private String events;
	
	private String cellStyle;
	
	private String codeKey;
	
	private Boolean isUser = false;
	
	private Boolean isEmployee = false;

	public Boolean getRadio() {
		return radio;
	}

	public void setRadio(Boolean radio) {
		this.radio = radio;
	}

	public Boolean getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(Boolean checkbox) {
		this.checkbox = checkbox;
	}

	public Boolean getCheckboxEnabled() {
		return checkboxEnabled;
	}

	public void setCheckboxEnabled(Boolean checkboxEnabled) {
		this.checkboxEnabled = checkboxEnabled;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getAlign() {
		return align;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public String getFooterFormatter() {
		return footerFormatter;
	}

	public void setFooterFormatter(String footerFormatter) {
		this.footerFormatter = footerFormatter;
	}

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	public String getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(String cellStyle) {
		this.cellStyle = cellStyle;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(String codeKey) {
		this.codeKey = codeKey;
	}

	public Boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(Boolean isUser) {
		this.isUser = isUser;
	}

	public Boolean getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(Boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
