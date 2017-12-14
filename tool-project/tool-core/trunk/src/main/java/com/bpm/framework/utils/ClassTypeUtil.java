/**
 * 
 */
package com.bpm.framework.utils;

/**
 * @author Tom Koo
 * 
 */
public class ClassTypeUtil<T> {

	public final static String INT = "int";

	public final static String INT_OBJ = "java.lang.Integer";

	public final static String LONG = "long";

	public final static String LONG_OBJ = "java.lang.long";

	public final static String DOUBLE = "double";

	public final static String DOUBLE_OBJ = "java.lang.double";

	public final static String FLOAT = "float";

	public final static String FLOAT_OBJ = "java.lang.float";

	public final static String BOOL = "boolean";

	public final static String BOOL_OBJ = "java.lang.boolean";

	public final static String STRING = "java.lang.String";

	public final static String DATE = "java.util.Date";

	public final static String TIMESTAMP = "java.sql.Timestamp;";

	public static boolean isInteger(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.INT.equalsIgnoreCase(name)
				|| ClassTypeUtil.INT_OBJ.equalsIgnoreCase(name);
	}

	public static boolean isLong(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.LONG.equalsIgnoreCase(name)
				|| ClassTypeUtil.LONG_OBJ.equalsIgnoreCase(name);
	}

	public static boolean isDouble(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.DOUBLE.equalsIgnoreCase(name)
				|| ClassTypeUtil.DOUBLE_OBJ.equalsIgnoreCase(name);
	}

	public static boolean isFloat(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.FLOAT.equalsIgnoreCase(name)
				|| ClassTypeUtil.FLOAT_OBJ.equalsIgnoreCase(name);
	}

	public static boolean isBoolean(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.BOOL.equalsIgnoreCase(name)
				|| ClassTypeUtil.BOOL_OBJ.equalsIgnoreCase(name);
	}

	public static boolean isString(Class<?> clazz) {
		return ClassTypeUtil.STRING.equalsIgnoreCase(clazz.getName());
	}

	public static boolean isDate(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.DATE.equalsIgnoreCase(name);
	}

	public static boolean isTimestamp(Class<?> clazz) {
		String name = clazz.getName();
		return ClassTypeUtil.TIMESTAMP.equalsIgnoreCase(name);
	}

}
