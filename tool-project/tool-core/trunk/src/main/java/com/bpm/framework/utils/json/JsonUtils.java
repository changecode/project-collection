package com.bpm.framework.utils.json;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bpm.framework.utils.Assert;

public class JsonUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4451907442174063196L;

	/**
	 * 
	 * JSON反序列化为Java对象集合
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toList(String json,Class<T> clazz) {
		Assert.notNull(json, "parameter json must not be null.");
		return JSON.parseArray(json, clazz);//JSON.parseObject(json, clazz);
	}
	
	/**
	 * 
	 * JSON反序列化为Java对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T toObject(String json,Class<T> clazz) {
		Assert.notNull(json, "parameter json must not be null.");
		return JSON.parseObject(json, clazz);
	}
	
	/**
	 * 对象序列化成JSON
	 * 
	 * @param Object obj
	 * @return json
	 */
	public static String fromObject(Object obj) {
		return JSON.toJSONString(obj);
	}
	
	/**
	 * 
	 * 将Java对象转换成Map
	 * 
	 * @param o
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object o) {
		if(o instanceof String) {
			return JsonUtils.toObject(null == o ? null : o.toString(), Map.class);
		}
		String json = JsonUtils.fromObject(o);
		return JsonUtils.toObject(json, Map.class);
	}
}
