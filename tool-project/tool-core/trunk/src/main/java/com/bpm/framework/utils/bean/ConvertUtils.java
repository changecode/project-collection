package com.bpm.framework.utils.bean;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.bpm.framework.utils.date.DateUtils;

public class ConvertUtils extends org.apache.commons.beanutils.ConvertUtils {

	private ConvertUtils() {

	}

	/**
	 * 对org.apache.commons.beanutils.ConvertUtils.convert方法进行封装，
	 * 增加泛型的支持，使调用更加方便，减少显示转型带来的复杂度，并且增加了从String
	 * 类型到Date、Timestamp、Calendar等类型转型的支持
	 * 
	 * @param <T>
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertGt(String value, Class<T> clazz) {
		if (value == null) { // 如果值为null,则返回null
			return null;
		} else if (value.equals("")
				&& !clazz.getName().equals(String.class.getName())) { // 如果value值为"",而且要转为的类型不是string类型，那么就统一返回null，也就是空字符串不能转成任何其他类型的实体，只能返回null
			return null;
		} else if (Date.class.getName().equalsIgnoreCase(clazz.getName())) {// 增加对从String类型到Date、Timestamp、Calendar等类型的转换功能
			return (T) DateUtils.toDate(value);
		} else if (Timestamp.class.getName().equalsIgnoreCase(clazz.getName())) {
			return (T) DateUtils.toDate(value);
		} else if (Calendar.class.getName().equalsIgnoreCase(clazz.getName())
				|| GregorianCalendar.class.getName().equalsIgnoreCase(
						clazz.getName())) {
			GregorianCalendar temp = new GregorianCalendar();
			temp.setTime(DateUtils.toDate(value));
			return (T) temp;
		}

		return (T) ConvertUtils.convert(value, clazz);
	}

	public static  String getTimeString(oracle.sql.TIMESTAMP value) {
		Class<?> clz = value.getClass();
		try {
			Method m = clz.getMethod("timestampValue", new Class[]{});
			// m = clz.getMethod("timeValue", null); 时间类型
			// m = clz.getMethod("dateValue", null); 日期类型
			Timestamp timestamp = (Timestamp) m.invoke(value, new Object[]{});
			if(timestamp!=null){
				return DateUtils.toString(new Date(timestamp.getTime()) ,DateUtils.YEAR_MONTH_DAY_HH_MM_SS) ; 
			}
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
		return null ;
	}

	public static String convertToString(Object value) {
		if (value == null) { // 如果值为null,则返回null
			return null;
		} else if ("oracle.sql.TIMESTAMP".equals(value.getClass().getName())) {
			return ConvertUtils.getTimeString((oracle.sql.TIMESTAMP)value) ;
		} else if ("java.sql.TIMESTAMP".equals(value.getClass().getName())) {
			return DateUtils.toString( new Date(((java.sql.Timestamp)value).getTime()) ,DateUtils.YEAR_MONTH_DAY) ;
		}
		return ConvertUtils.convert(value);
	}

	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 */
	public static Object convertStringToObject(String value, Class<?> toType) {
		return ConvertUtils.convert(value, toType);
	}
}
