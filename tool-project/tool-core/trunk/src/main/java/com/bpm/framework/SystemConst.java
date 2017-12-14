package com.bpm.framework;

import java.io.Serializable;

/**
 * 
 * 系统常量类
 * 
 * @see SystemConst.java
 * @author lixx
 * @createDate 2015-01-27 19:18:00
 */
public abstract class SystemConst implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8658343017884620901L;

	private SystemConst() {}
	
	public static final String APPLICATION_CONTEXT = "app";
	
	public static final String ENCODING_GBK = "GBK";
	
	public static final String ENCODING_UTF8 = "UTF-8";
	
	public static final String USER_STATUS_NORMAL = "normal";// 正常
	
	public static final String USER_STATUS_LOGOUT = "logout";// 注销，永远不能使用
	
	public static final String USER_STATUS_FREEZE = "freeze";// 冻结
	
	public static final String USER_TYPE_ADMIN = "administrator";// 管理员
	
	public static final String USER_TYPE_DEVELOPER = "developer";// 开发用户
	
	public static final String USER_TYPE_AUTO = "auto";// 自动运行的用户，方便记录日志
	
	public static final String USER_TYPE_BUSINESS = "business";// 业务用户
	
	public static final String USER_SEX_F = "F";// 女士
	
	public static final String USER_SEX_M = "M";// 男士
	
	public static final String USER_SESSION_KEY = "user_session";// 用户存在session中的key
	
	public static final String USERV_SESSION_KEY = "userv_session";// 用户视图存在session中的key
	
	public static final String ENTERPRISE_ID_SESSION_KEY = "enterprise_id_session";// 企业编号存在session中的key
	
	public static final String ROLE_ID_LIST_SESSION_KEY = "role_id_session";// 当前角色id存在session中的key
	
	public static final String MENU_TYPE_ADMIN = "administrator";// 管理员菜单
	
	public static final String MENU_TYPE_DEVELOPER = "developer";// 开发用户菜单
	
	public static final String MENU_TYPE_BUSINESS = "business";// 业务菜单
	
	public static final String MENU_TYPE_COMMON = "common";// 通用菜单
	
	public static final String[] COMMON_IGNORE_FIELD= { "createBy", "createTime", "lastUpdateBy", "lastUpdateTime"};
	
	public static final String LANGUAGE_ZH = "zh_CN";
	public static final String LANGUAGE_EN = "en_US";
	
	public static final String LOGIN_BEAN_KEY = "loginBean";// 登录错误信息记录bean对应的session key
	
    /**
     * 初始化数据-常量-管理员权限code
     */
    public static final String PERMISSION_ADMIN_CODE = "ADMIN";
    /**
     * 初始化数据-常量-管理员权限名称
     */
    public static final String PERMISSION_ADMIN_NAME = "管理员";
    /**
     * 初始化数据-常量-催收主管权限code
     */
    public static final String PERMISSION_CUIMNG_CODE = "CUIMNG";
    /**
     * 初始化数据-常量-催收主管权限名称
     */
    public static final String PERMISSION_CUIMNG_NAME = "催收主管";
    /**
     * 初始化数据-常量-催收员权限code
     */
    public static final String PERMISSION_CUIEMP_CODE = "CUIEMP";
    /**
     * 初始化数据-常量-催收员权限名称
     */
    public static final String PERMISSION_CUIEMP_NAME = "催收员";
    /**
     * 初始化数据-常量-不允许修改
     */
    public static final String NOT_OPERATION = "请勿修改";
    
    /**
     * 初始化部门时，顶级部门的code
     */
    public static final String DEPARTMENT_ROOT_CODE = "ROOT";
}
