package com.bpm.framework.utils.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.Converter;
import org.springframework.beans.BeanUtils;

import com.bpm.framework.utils.ArrayUtils;

/**
 * 该类是弥补org.apache.commons.beanutils.BeanUtils类在某些功能上的不足
 * 
 * @author lixx
 * @since 1.0
 * @version create time : 2010-2-10 上午10:49:06
 */
public class BeanUtils2 {

	public static final Map<String, Converter> EMPTY_CONVERTER_MAP = new HashMap<String, Converter>();

	/**
	 * 通过class的默认构造函数创建class对应的实例，如果不存在默认构造函数，则抛出异常，打印异常的堆栈信息
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	public static <T> T newInstanceWithDefaultConstructor(Class<T> clazz)
			throws IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return newInstanceWithDefaultConstructor(clazz, true);
	}

	/**
	 * 通过class的默认构造函数创建class对应的实例
	 * 
	 * @param <T>
	 * @param clazz
	 * @param isPrintStackTrace
	 *            如果为true，当class没有默认构造函数时，打印出NoSuchMethodException异常堆栈信息，为false则不打印
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 */
	public static <T> T newInstanceWithDefaultConstructor(Class<T> clazz,
			boolean isPrintStackTrace) throws IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Constructor<T> c = null;
		T bean = null;
		try {
			c = clazz.getConstructor(ArrayUtils.EMPTY_CLASS_ARRAY);
			bean = c.newInstance(ArrayUtils.EMPTY_OBJECT_ARRAY);
		} catch (NoSuchMethodException e) {
			if (isPrintStackTrace) {
				throw e;
			}
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	protected static <T> T covert(Object value, Class<T> clazz,
			Map<String, Converter> converters) {
		Converter converter = null;
		if (converters != null) {
			String className = clazz.getName();
			converter = converters.get(className);
		}
		if (converter == null) {
			return ConvertUtils.convertGt(value.toString(), clazz);
		} else {
			return (T) converter.convert(clazz, value);
		}
	}
	
	
	
	
	  /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

	
	  public static void copyProperties(Object source, Object target, String[] ignoreProperties)
	  {
	    Integer addLength = Integer.valueOf(1);
	    String addIgnoreProperty = "serialVersionUID";

	    if (ignoreProperties == null) {
	      ignoreProperties = new String[addLength.intValue()];
	      ignoreProperties[0] = addIgnoreProperty;
	    }
	    else {
	      String[] newIgnoreProperties = (String[])Arrays.copyOf(ignoreProperties, ignoreProperties.length + addLength.intValue());
	      newIgnoreProperties[(newIgnoreProperties.length - 1)] = "serialVersionUID";

	      ignoreProperties = newIgnoreProperties;
	    }
	    
	    BeanUtils.copyProperties(source, target, ignoreProperties);
	  }
	  
	  
	  /** 
	   * 复制集合 
	   * @param <E> 
	   * @param source 
	   * @param destinationClass 
	   * @return 
	   * @throws InstantiationException  
	   * @throws InvocationTargetException  
	   * @throws IllegalAccessException  
	   */  
	  public static <E> List<E> copyTo(List<?> source, Class<E> destinationClass) throws IllegalAccessException, InvocationTargetException, InstantiationException{  
	      return copyTo(source,destinationClass ,null);  
	  }  
	  
	  
	 /**
	  * 复制集合  过滤部分字段
	  * @param source
	  * @param destinationClass
	  * @param ignoreProperties
	  * @return
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  */
	  public static <E> List<E> copyTo(List<?> source, Class<E> destinationClass,String[] ignoreProperties) throws IllegalAccessException, InvocationTargetException, InstantiationException{  
	      if (source.size()==0) return Collections.emptyList();  
	      List<E> res = new ArrayList<E>(source.size());  
	      for (Object o : source) {  
	          E e = destinationClass.newInstance();  
	          copyProperties(o, e,ignoreProperties);  
	          res.add(e);  
	      }  
	      return res;  
	  }  
}
