package com.bpm.framework.annotation;

public enum OperationType {
	Login, Logout, Query, Add, Edit, Delete, Auth, Exe;
	public static String getName(String operationType) {
		String name = null;
		if ("Login".equals(operationType)) {
			name = "登录";
		} else if ("Logout".equals(operationType)) {
			name = "注销";
		} else if ("Query".equals(operationType)) {
			name = "查询";
		} else if ("Add".equals(operationType)) {
			name = "添加";
		} else if ("Edit".equals(operationType)) {
			name = "修改";
		} else if ("Delete".equals(operationType)) {
			name = "删除";
		} else if ("Auth".equals(operationType)) {
			name = "授权";
		} else if ("Exe".equals(operationType)) {
			name = "操作";
		} else {
			name = "";
		}
		return name;
	}
}