package com.bpm.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

public abstract class ReflectionUtils {

	static Logger log = Logger.getLogger(ReflectionUtils.class);

	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 * 
	 * @param ex
	 *            the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: "
					+ ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: "
					+ ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		throw new IllegalStateException("Unexpected reflection exception - "
				+ ex.getClass().getName() + ": " + ex.getMessage());
	}

	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of such a root
	 * cause. Throws an IllegalStateException else.
	 * 
	 * @param ex
	 *            the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(
			InvocationTargetException ex) {
		if (ex.getTargetException() instanceof RuntimeException) {
			throw (RuntimeException) ex.getTargetException();
		}
		if (ex.getTargetException() instanceof Error) {
			throw (Error) ex.getTargetException();
		}
		throw new IllegalStateException(
				"Unexpected exception thrown by method - "
						+ ex.getTargetException().getClass().getName() + ": "
						+ ex.getTargetException().getMessage());
	}

	/**
	 * Attempt to find a {@link Method} on the supplied type with the supplied
	 * name and parameter types. Searches all superclasses up to
	 * <code>Object</code>. Returns '<code>null</code>' if no {@link Method} can
	 * be found.
	 */
	public static Method findMethod(Class<?> type, String name,
			Class<?>[] paramTypes) {
		Assert.notNull(type, "'type' cannot be null.");
		Assert.notNull(name, "'name' cannot be null.");
		Class<?> searchType = type;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods());
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName())
						&& Arrays
								.equals(paramTypes, method.getParameterTypes())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * 查找自身及其父类的方法（但是不包括私有的方法，通过class.getMethods()获取）
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static Method[] findMethods(Class<?> type, String name) {
		Assert.notNull(type, "'type' cannot be null.");
		Assert.notNull(name, "'name' cannot be null.");
		List<Method> methodList = new ArrayList<Method>();
		Class<?> searchType = type;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = searchType.getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName())) {
					methodList.add(method);
				}
			}
			searchType = searchType.getSuperclass();
		}
		return methodList.toArray(new Method[] {});
	}

	/**
	 * 查找自身及其父类的方法（包括私有的方法，通过class.getDeclaredMethods()获取）
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static Method[] findAllMethods(Class<?> type, String name) {
		Assert.notNull(type, "'type' cannot be null.");
		Assert.notNull(name, "'name' cannot be null.");
		List<Method> methodList = new ArrayList<Method>();
		Class<?> searchType = type;
		while (!Object.class.equals(searchType) && searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType
					.getMethods() : searchType.getDeclaredMethods());
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName())) {
					methodList.add(method);
				}
			}
			searchType = searchType.getSuperclass();
		}
		return methodList.toArray(new Method[] {});
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object
	 * with no arguments The target object can be <code>null</code> when
	 * invoking a static {@link Method}.
	 * 
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, null);
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object
	 * with the supplied arguments The target object can be null when invoking a
	 * static {@link Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * 
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target,
			Object[] args) {
		try {
			return method.invoke(target, args);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - "
							+ ex.getClass().getName() + ": " + ex.getMessage());
		} catch (InvocationTargetException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException(
					"Unexpected reflection exception - "
							+ ex.getClass().getName() + ": " + ex.getMessage());
		}
	}

	/**
	 * Determine whether the given field is a "public static final" constant.
	 * 
	 * @param field
	 *            the field to check
	 */
	public static boolean isPublicStaticFinal(Field field) {
		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier
				.isFinal(modifiers));
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if
	 * necessary. The <code>setAccessible(true)</code> method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 * 
	 * @param field
	 *            the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>
	 * The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by the MethodFilter
	 * 
	 * @param targetClass
	 *            class to start looking at
	 * @param mc
	 *            the callback to invoke for each method
	 */
	public static void doWithMethods(Class<?> targetClass, MethodCallback mc)
			throws IllegalArgumentException {
		doWithMethods(targetClass, mc, null);
	}

	/**
	 * Perform the given callback operation on all matching methods of the given
	 * class and superclasses.
	 * <p>
	 * The same named method occurring on subclass and superclass will appear
	 * twice, unless excluded by the MethodFilter
	 * 
	 * @param targetClass
	 *            class to start looking at
	 * @param mc
	 *            the callback to invoke for each method
	 * @param mf
	 *            the filter that determines the methods to apply the callback
	 *            to
	 */
	public static void doWithMethods(Class<?> targetClass, MethodCallback mc,
			MethodFilter mf) throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		do {
			Method[] methods = targetClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (mf != null && !mf.matches(methods[i])) {
					continue;
				}
				try {
					mc.doWith(methods[i]);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access method '"
									+ methods[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null);
	}

	/**
	 * Get all declared methods on the leaf class and all superclasses. Leaf
	 * class methods are included first.
	 */
	@SuppressWarnings("unchecked")
	public static Method[] getAllDeclaredMethods(Class leafClass)
			throws IllegalArgumentException {
		final List l = new LinkedList();
		doWithMethods(leafClass, new MethodCallback() {
			public void doWith(Method m) {
				l.add(m);
			}
		});
		return (Method[]) l.toArray(new Method[l.size()]);
	}

	/**
	 * Invoke the given callback on all private fields in the target class,
	 * going up the class hierarchy to get all declared fields.
	 * 
	 * @param targetClass
	 *            the target class to analyze
	 * @param fc
	 *            the callback to invoke for each field
	 */
	public static void doWithFields(Class<?> targetClass, FieldCallback fc)
			throws IllegalArgumentException {
		doWithFields(targetClass, fc, null);
	}

	/**
	 * Invoke the given callback on all private fields in the target class,
	 * going up the class hierarchy to get all declared fields.
	 * 
	 * @param targetClass
	 *            the target class to analyze
	 * @param fc
	 *            the callback to invoke for each field
	 * @param ff
	 *            the filter that determines the fields to apply the callback to
	 */
	public static void doWithFields(Class<?> targetClass, FieldCallback fc,
			FieldFilter ff) throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		do {
			// Copy each field declared on this class unless it's static or
			// file.
			Field[] fields = targetClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				// Skip static and final fields.
				if (ff != null && !ff.matches(fields[i])) {
					continue;
				}
				try {
					fc.doWith(fields[i]);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException(
							"Shouldn't be illegal to access field '"
									+ fields[i].getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null && targetClass != Object.class);
	}

	/**
	 * Given the source object and the destination, which must be the same class
	 * or a subclass, copy all fields, including inherited fields. Designed to
	 * work on objects with public no-arg constructors.
	 * 
	 * @throws IllegalArgumentException
	 *             if arguments are incompatible or either is <code>null</code>
	 */
	public static void shallowCopyFieldState(final Object src, final Object dest)
			throws IllegalArgumentException {
		if (src == null) {
			throw new IllegalArgumentException(
					"Source for field copy cannot be null");
		}
		if (dest == null) {
			throw new IllegalArgumentException(
					"Destination for field copy cannot be null");
		}
		if (!src.getClass().isAssignableFrom(dest.getClass())) {
			throw new IllegalArgumentException("Destination class ["
					+ dest.getClass().getName()
					+ "] must be same or subclass as source class ["
					+ src.getClass().getName() + "]");
		}
		doWithFields(src.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				makeAccessible(field);
				Object srcValue = field.get(src);
				field.set(dest, srcValue);
			}
		}, ReflectionUtils.COPYABLE_FIELDS);
	}

	public static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
		Assert.notNull(clazz);
		Assert.hasLength(fieldName);
		try {
			return clazz.getField(fieldName).get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 通过对象的属性名获取该属性的值，当对象为null时，再取其属性，将抛出NestedNullException异常
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Assert.hasLength(fieldName);
		try {
			return BeanUtils.getProperty(obj, fieldName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setFieldValue(Object obj, String name, Object value) {
		setFieldValue(obj, obj.getClass(), name, value);

	}

	
	public static void setFieldValue(Object obj, Class<?> clazz, String name,
			Object value) {
		Field filed;
		try {
		//	filed = clazz.getDeclaredField(name);
			filed = getDeclaredField(obj,name);
			if(filed!=null){
				filed.setAccessible(true);
				Class<?> typeClass = filed.getType();
				if(value!=null){
					Class<?> valueClass = value.getClass();
					if(Long.class.equals(typeClass) && Integer.class.equals(valueClass)){
						filed.set(obj, Long.valueOf(value.toString()));
					}else{
						filed.set(obj, value);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	
	/**
	 * 在自己类以及父类里面获取field，
	 * @param object
	 * @param fieldName
	 * @return
	 */
	  public static Field getDeclaredField(Object object, String fieldName){  
	        Field field = null ;  
	        Class<?> clazz = object.getClass() ;  
	        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
	            try {  
	                field = clazz.getDeclaredField(fieldName) ;  
	                return field ;  
	            } catch (Exception e) {  
	                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
	                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
	            }   
	        }  
	      
	        return null;  
	    }     
	      

	/**
	 * 通过对象的属性名获取该属性的值，当对象为null时，返回null
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValueAllowNull(Object obj, String fieldName) {
		Assert.hasLength(fieldName, "filedName can't be null.");
		Class<?>[] paramType = new Class[0];
		Object[] param = new Object[0];
		try {
			String[] names = fieldName.split("\\.");
			Object propValue = obj;
			for (String name : names) {
				if (propValue == null) {
					return null;
				} else {
					Class<?> tempClass = propValue.getClass();
					Method m = tempClass.getMethod(convertGetter(name),
							paramType);
					propValue = m.invoke(propValue, param);
				}
			}
			return propValue;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String convertGetter(String name) {
		String first = name.substring(0, 1).toUpperCase();
		String second = name.substring(1, name.length());
		return "get" + first + second;
	}

	/**
	 * Action to take on each method
	 */
	public static interface MethodCallback {

		/**
		 * Perform an operation using the given method.
		 * 
		 * @param method
		 *            method which will have been made accessible before this
		 *            invocation
		 */
		void doWith(Method method) throws IllegalArgumentException,
				IllegalAccessException;
	}

	/**
	 * Callback optionally used to method fields to be operated on by a method
	 * callback.
	 */
	public static interface MethodFilter {

		/**
		 * Return whether the given method matches.
		 * 
		 * @param method
		 *            the method to check
		 */
		boolean matches(Method method);
	}

	/**
	 * Callback interface invoked on each field in the hierarchy.
	 */
	public static interface FieldCallback {

		/**
		 * Perform an operation using the given field.
		 * 
		 * @param field
		 *            field which will have been made accessible before this
		 *            invocation
		 */
		void doWith(Field field) throws IllegalArgumentException,
				IllegalAccessException;
	}

	/**
	 * Callback optionally used to filter fields to be operated on by a field
	 * callback.
	 */
	public static interface FieldFilter {

		/**
		 * Return whether the given field matches.
		 * 
		 * @param field
		 *            the field to check
		 */
		boolean matches(Field field);
	}

	/**
	 * FieldFilter that matches all non-static, non-final fields.
	 */
	public static FieldFilter COPYABLE_FIELDS = new FieldFilter() {
		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()) || Modifier
					.isFinal(field.getModifiers()));
		}
	};

	public static boolean isInterface(Class<?> c, String szInterface) {
		Class<?>[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(szInterface)) {
				return true;
			} else {
				Class<?>[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), szInterface);
		}
		return false;
	}

	public static boolean isInterface(Class<?> c, Class<?> szInterface) {
		return isInterface(c, szInterface.getName());
	}

	public static boolean isSuperClass(Class<?> c, String superClass) {
		Class<?> clazz = c.getSuperclass();
		if (clazz == null) {
			return false;
		}
		if (clazz.getName().equals(superClass)) {
			return true;
		} else if (null != c.getSuperclass()) {
			return isSuperClass(c.getSuperclass(), superClass);
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T cloneBean(T t) {

		try {
			T t0 = (T) BeanUtils.cloneBean(t);
			return t0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void copy(Object source, Object dest) {

		Method s[] = source.getClass().getMethods();
		Method d[] = dest.getClass().getMethods();
		try {
			for (int i = 0; i < d.length; i++) {
				String dname = d[i].getName();
				String dformat = dname.substring(3);
				if (dname.startsWith("set")) {
					for (int j = 0; j < s.length; j++) {
						String sname = s[j].getName();
						String sformat = sname.substring(3);
						if (sname.startsWith("get") && sformat.equals(dformat)) {
							Object value = null;
							value = s[j].invoke(source);
							Object[] args = new Object[1];
							args[0] = value;
							d[i].invoke(dest, args);
						}
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getClazzNameNoPackage(Class<?> clazz){
		String name = clazz.getName();
		name = name.substring(name.lastIndexOf(".")+1);
		return name;
	}

}
