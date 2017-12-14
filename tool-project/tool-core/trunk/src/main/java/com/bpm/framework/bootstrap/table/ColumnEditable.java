package com.bpm.framework.bootstrap.table;

import java.io.Serializable;

public class ColumnEditable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1923515281677206425L;
	
	private String field;
	
	private Boolean editable = true;
	
	private String dataType = "text";
	
	private String dataUrl;
	
	private String dataValueField;
	
	private String dataTextField;
	
	private String dataFormat;   //  yyyy-mm-dd  时间类型使用
	
	private String dataMinViewMode;   //  0  表示选择到时分  不设置默认为年月日
	
	private int width;
	
	private String title;
	
	private Boolean visible = true;
	
	private Boolean checkbox = false;
	
	private String align;
	
	private Boolean dataShowButtons = false;
	
	private Boolean sortable = true;
	
	private Boolean required = false;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getDataValueField() {
		return dataValueField;
	}

	public void setDataValueField(String dataValueField) {
		this.dataValueField = dataValueField;
	}

	public String getDataTextField() {
		return dataTextField;
	}

	public void setDataTextField(String dataTextField) {
		this.dataTextField = dataTextField;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(Boolean checkbox) {
		this.checkbox = checkbox;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public Boolean getDataShowButtons() {
		return dataShowButtons;
	}

	public void setDataShowButtons(Boolean dataShowButtons) {
		this.dataShowButtons = dataShowButtons;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getDataMinViewMode() {
		return dataMinViewMode;
	}

	public void setDataMinViewMode(String dataMinViewMode) {
		this.dataMinViewMode = dataMinViewMode;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
}
