package com.bpm.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bpm.framework.exception.FrameworkRuntimeException;
import com.bpm.framework.utils.bean.ConvertUtils;

/**
 * 该通用类只能运行在 JDK5.0 及以上版本。
 * 
 * HttpUtil为一个通用类，该类定义了很多静态的公用方法供使用，这些方法都是对HttpServletRequest,
 * HttpSession等类方法的进一步的封装，使其更符合我们使用的实际情况。具体使用方法见方法说明。
 */

public class HttpUtils {

	public static final String SPLITOR = ",";

	private HttpUtils() {

	}

	/**
	 * 根据参数名从HttpServletRequest对象中取出对应的参数值，并转化成需要的数据类型，
	 * 包括Java基础数据类型和通用日期类型，如果没有对应的参数值则返回null。
	 * 
	 * 目前支持的通用日期类型包括：
	 * java.util.Date,java.sql.Timestamp,java.util.Calendar,java.util
	 * .GregorianCalendar
	 * 
	 * @param <T>
	 * @param request
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getValue(HttpServletRequest request, String key,
			Class<T> clazz) {

		String value = request.getParameter(key);
		if (value == null) { // 如果获取参数值为null,则返回null
			return null;
		} else if (!value.equals("")) { // 如果获取参数值不为"",则通过convertGt方法进行类型转换后返回结果
			return ConvertUtils.convertGt(value, clazz);
		} else if (clazz.getName().equals(String.class.getName())) { // 如果获取参数值为""
			// ,
			// 并且clazz是String类型
			// ,返回""
			// 字符串
			return ConvertUtils.convertGt(value, clazz);
		} else {// 如果获取参数值为"",并且clazz不是是String类型,则返回null
			return null;
		}
	}

	/**
	 * 根据参数名从HttpServletRequest对象中取出对应的参数值，并转化成需要的数据类型，
	 * 包括Java基础数据类型和通用日期类型，如果没有对应的参数值则抛出运行时异常。
	 * 
	 * 目前支持的通用日期类型包括：
	 * java.util.Date,java.sql.Timestamp,java.util.Calendar,java.util
	 * .GregorianCalendar
	 * 
	 * @param <T>
	 * @param request
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> T getValueNotNull(HttpServletRequest request, String key,
			Class<T> clazz) {

		String value = request.getParameter(key);
		Assert.hasLength(value, "can't get value by paramter name! - [" + key
				+ "]");
		T convert = ConvertUtils.convertGt(value, clazz);
		return convert;
	}

	/**
	 * 根据属性名从HttpSession对象中取出对应的参数值，并转化成需要的数据类型，
	 * 包括Java基础数据类型和通用日期类型，如果没有对应的参数值则返回null。
	 * 
	 * @param <T>
	 * @param session
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(HttpSession session, String key, Class<T> clazz) {
		T value = (T) session.getAttribute(key);
		return value;
	}

	/**
	 * 根据属性名从HttpSession对象中取出对应的参数值，并转化成需要的数据类型，
	 * 包括Java基础数据类型和通用日期类型，如果没有对应的参数值则抛出运行时异常。
	 * 
	 * @param <T>
	 * @param session
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValueNotNull(HttpSession session, String key,
			Class<T> clazz) {
		T value = (T) session.getAttribute(key);
		Assert.notNull(value, "can't get value by attribute name! - [" + key
				+ "]");
		return value;
	}

	/**
	 * 获取Long类型参数值的便捷方法
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static Long getLong(HttpServletRequest request, String key) {
		return HttpUtils.getValue(request, key, Long.class);
	}

	public static Long getLongNotNull(HttpServletRequest request, String key) {
		return HttpUtils.getValueNotNull(request, key, Long.class);
	}

	public static Double getDouble(HttpServletRequest request, String key) {
		return HttpUtils.getValue(request, key, Double.class);
	}

	public static Double getDoubleNotNull(HttpServletRequest request, String key) {
		return HttpUtils.getValueNotNull(request, key, Double.class);
	}

	public static Float getFloat(HttpServletRequest request, String key) {
		return HttpUtils.getValue(request, key, Float.class);
	}

	public static Float getFloatNotNull(HttpServletRequest request, String key) {
		return HttpUtils.getValueNotNull(request, key, Float.class);
	}

	public static Integer getInt(HttpServletRequest request, String key) {
		return HttpUtils.getValue(request, key, Integer.class);
	}

	public static Integer getIntNotNull(HttpServletRequest request, String key) {
		return HttpUtils.getValueNotNull(request, key, Integer.class);
	}

	public static Date getDate(HttpServletRequest request, String key) {
		return HttpUtils.getValue(request, key, Date.class);
	}

	public static Date getDateNotNull(HttpServletRequest request, String key) {
		return HttpUtils.getValueNotNull(request, key, Date.class);
	}

	public static Boolean getBool(HttpServletRequest request, String key) {
		return HttpUtils.getValue(request, key, Boolean.class);
	}

	public static Boolean getBoolNotNull(HttpServletRequest request, String key) {
		return HttpUtils.getValueNotNull(request, key, Boolean.class);
	}

	/**
	 * 根据HttpServletRequest对象中的参数值组建对象
	 * 
	 * @param <T>
	 * @param request
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(HttpServletRequest request, Class<T> c) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String[] value = null;
		Class<?>[] paramTypes = new Class[1];

		Object obj = null;
		try {
			obj = c.newInstance();
			Field[] f = c.getDeclaredFields();
			List<Field[]> flist = new ArrayList<Field[]>();
			flist.add(f);
			Class<?> superClazz = c.getSuperclass(); // 获取父类,如果父类存在则取父类的Field
			while (superClazz != null) {
				f = superClazz.getDeclaredFields();
				flist.add(f);
				superClazz = superClazz.getSuperclass();
			}
			for (Field[] temp : flist) {
				for (Field field : temp) {
					String fName = field.getName();

					value = paramMap.get(fName);
					if (value != null) {
						paramTypes[0] = field.getType();
						Method method = null;
						StringBuffer methodName = new StringBuffer("set");
						methodName.append(fName.substring(0, 1).toUpperCase());
						methodName.append(fName.substring(1, fName.length()));
						method = c.getMethod(methodName.toString(), paramTypes);
						method.invoke(obj, getValue(request, fName,
								paramTypes[0]));
					}

				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return (T) obj;
	}

	/**
	 * 根据HttpServletRequest对象中的参数值组建对象
	 * 
	 * @param <T>
	 * @param request
	 * @param propClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBeanByProp(HttpServletRequest request,
			Class<T> propClass, String prop) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String[] value = null;
		Class<?>[] paramTypes = new Class[1];

		Object obj = null;
		try {
			obj = propClass.newInstance();
			Field[] f = propClass.getDeclaredFields();
			List<Field[]> flist = new ArrayList<Field[]>();
			flist.add(f);
			Class<?> superClazz = propClass.getSuperclass(); // 获取父类,如果父类存在则取父类的Field
			while (superClazz != null) {
				f = superClazz.getDeclaredFields();
				flist.add(f);
				superClazz = superClazz.getSuperclass();
			}
			for (Field[] temp : flist) {
				for (Field field : temp) {
					String fName = field.getName();

					value = paramMap.get(prop + "." + fName);
					if (value != null) {
						paramTypes[0] = field.getType();
						Method method = null;
						StringBuffer methodName = new StringBuffer("set");
						methodName.append(fName.substring(0, 1).toUpperCase());
						methodName.append(fName.substring(1, fName.length()));
						method = propClass.getMethod(methodName.toString(),
								paramTypes);
						method.invoke(obj, getValue(request,
								prop + "." + fName, paramTypes[0]));
					}

				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return (T) obj;
	}

	/**
	 * 根据HttpServletRequest对象中的参数值组建对象
	 * 
	 * @param <T>
	 * @param request
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(HttpServletRequest request, Class<T> c,
			Object obj) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String[] value = null;
		Class<?>[] paramTypes = new Class[1];

		try {
			Field[] f = c.getDeclaredFields();
			List<Field[]> flist = new ArrayList<Field[]>();
			flist.add(f);
			Class<?> superClazz = c.getSuperclass(); // 获取父类,如果父类存在则取父类的Field
			while (superClazz != null) {
				f = superClazz.getDeclaredFields();
				flist.add(f);
				superClazz = superClazz.getSuperclass();
			}
			for (Field[] temp : flist) {
				for (Field field : temp) {
					String fName = field.getName();

					value = paramMap.get(fName);
					if (value != null) {
						paramTypes[0] = field.getType();
						Method method = null;
						StringBuffer methodName = new StringBuffer("set");
						methodName.append(fName.substring(0, 1).toUpperCase());
						methodName.append(fName.substring(1, fName.length()));
						method = c.getMethod(methodName.toString(), paramTypes);
						method.invoke(obj, getValue(request, fName,
								paramTypes[0]));
					}

				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return (T) obj;
	}

	/**
	 * 根据HttpServletRequest对象中的参数值组建对象的自定义属性对象
	 * 
	 * @param request
	 * @param objProp
	 *            修改的属性对象
	 * @param prop
	 *            修改属性名
	 * @return
	 */
	public static Object getBeanForUpdateByProp(HttpServletRequest request,
			Object objProp, String prop) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String[] value = null;
		Class<?>[] paramTypes = new Class[1];
		Class<?> c = objProp.getClass();
		try {
			Field[] f = c.getDeclaredFields();
			List<Field[]> flist = new ArrayList<Field[]>();
			flist.add(f);
			Class<?> superClazz = c.getSuperclass(); // 获取父类,如果父类存在则取父类的Field
			while (superClazz != null) {
				f = superClazz.getDeclaredFields();
				flist.add(f);
				superClazz = superClazz.getSuperclass();
			}
			for (Field[] temp : flist) {
				for (Field field : temp) {
					String fName = field.getName();

					value = paramMap.get(prop + "." + fName);
					if (value != null) {
						paramTypes[0] = field.getType();
						Method method = null;
						StringBuffer methodName = new StringBuffer("set");
						methodName.append(fName.substring(0, 1).toUpperCase());
						methodName.append(fName.substring(1, fName.length()));
						method = c.getMethod(methodName.toString(), paramTypes);
						method.invoke(objProp, getValue(request, prop + "."
								+ fName, paramTypes[0]));
					}

				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return objProp;
	}

	/**
	 * 获取HttpServletRequest中传递参数的值，当HttpServletRequest中存在paramName参数的时候，
	 * 得到这个值根据Symbol分割后的整型数组(Integer[])，如果不存在则抛出RuntimeException异常。
	 * 
	 * @param request
	 * @param attributeName
	 * @param symbol
	 *            ：分割的符号
	 * @return
	 */
	public static <T> T[] getNumsNotNull(HttpServletRequest request,
			String param, String symbol, T[] t) {
		String value = request.getParameter(param);
		Assert.notNull(value, "Parameters is null.");
		return StringUtils.splitToNumArray(value, symbol, t);
	}

	public static <T> T[] getNumsNotNull(HttpServletRequest request,
			String param, T[] t) {
		String value = request.getParameter(param);
		Assert.notNull(value, "Parameters is null.");
		return StringUtils.splitToNumArray(value, HttpUtils.SPLITOR, t);
	}

	/**
	 * 获取HttpServletRequest中传递参数的值，当HttpServletRequest中存在paramName参数的时候，
	 * 得到这个值根据Symbol分割后的字符数组(String[])，如果不存在则抛出RuntimeException异常。
	 * 
	 * @param request
	 * @param attributeName
	 * @param symbol
	 *            ：分割的符号
	 * @return
	 */
	public static String[] getStringsNotNull(HttpServletRequest request,
			String attributeName, String symbol) {
		String value = request.getParameter(attributeName);
		Assert.notNull(value, "Parameters is null.");
		if (value.equals("")) {
			return new String[0];
		}
		String[] values = value.split(symbol);
		return values;
	}

	public static String[] getStrings(HttpServletRequest request,
			String attributeName, String symbol) {
		String value = request.getParameter(attributeName);
		if (value == null || value.equals("")) {
			return new String[0];
		}
		String[] values = value.split(symbol);
		return values;
	}

	/**
	 * convert param string to map
	 * 
	 * @param paramString
	 *            'orgId=1&orderInfo=-1,asc&loginId=12'
	 * @param encode
	 * @return
	 */
	public static Map<String, String> paramStrToMap(String paramString,
			String encode) {
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtils.hasLength(paramString)) {
			return map;
		}
		try {
			paramString = URLDecoder.decode(paramString, encode);
			String[] paramArray = paramString.split("&");
			paramArray = paramArray == null ? new String[0] : paramArray;
			for (String temp : paramArray) {
				String[] param = temp.split("=");
				if (param == null || param.length < 1
						|| StringUtils.isNullOrBlank(param[0])
						|| map.containsKey(param[0]))
					continue;
				if (param.length < 2 || StringUtils.isNullOrBlank(param[1])) {
					map.put(param[0], "");
				} else {
					map.put(param[0], URLDecoder.decode(param[1], encode));
				}

			}
			return map;
		} catch (Exception e) {
			throw new FrameworkRuntimeException(e);
		}

	}

	/**
	 * 判断经过ServletRequestWrapper封装过的request，是否属于Class<? extends ServletRequest>
	 * parentClass的一个实现
	 * 
	 * @param child
	 * @param parentClass
	 * @return
	 */
	public static boolean isWrapperContainRequest(ServletRequestWrapper child,
			Class<? extends ServletRequest> parentClass) {

		ServletRequest request = child.getRequest();
		if (request == null) {
			return false;
		}
		Class<?> requestClazz = request.getClass();
		if (requestClazz.getName().equals(parentClass.getName())
				|| ReflectionUtils.isSuperClass(requestClazz, parentClass
						.getName())
				|| ReflectionUtils.isInterface(requestClazz, parentClass)) {
			return true;
		} else if (request instanceof ServletRequestWrapper) {
			return isWrapperContainRequest((ServletRequestWrapper) request,
					parentClass);
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 判断经过ServletRequestWrapper封装过的request，是否属于Class<? extends ServletRequest>
	 * parentClass的一个实现。如果是，则返回这个实现，如果不是，则返回null。
	 * 
	 * @param child
	 * @param parentClass
	 * @return
	 */
	public static ServletRequest wrapperToRequest(ServletRequestWrapper child,
			Class<? extends ServletRequest> parentClass) {
		ServletRequest request = child.getRequest();
		if (request == null) {
			return null;
		}
		Class<?> requestClazz = request.getClass();

		if (requestClazz.getName().equals(parentClass.getName())
				|| ReflectionUtils.isSuperClass(requestClazz, parentClass
						.getName())
				|| ReflectionUtils.isInterface(requestClazz, parentClass)) {
			return request;
		} else if (request instanceof ServletRequestWrapper) {
			return wrapperToRequest((ServletRequestWrapper) request,
					parentClass);
		} else {
			return null;
		}
	}

}
