package com.bpm.framework.easy.sql;

import com.bpm.framework.utils.StringUtils;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class SqlFile {

	String resource;

	public String getResource() {
		if (StringUtils.hasLength(resource) && resource.startsWith("/")) {
			return resource;
		} else {
			return "/" + resource;
		}
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}
