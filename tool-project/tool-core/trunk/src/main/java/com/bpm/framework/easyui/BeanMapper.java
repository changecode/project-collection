package com.bpm.framework.easyui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.dozer.DozerBeanMapper;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BeanMapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6038887340852661692L;

	private static DozerBeanMapper dozer = new DozerBeanMapper();

	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	public static <T> List<T> mapList(Collection sourceList,
			Class<T> destinationClass) {
		List destList = new ArrayList();
		for (Iterator i$ = sourceList.iterator(); i$.hasNext();) {
			Object sourceObject = i$.next();
			Object destObject = dozer.map(sourceObject, destinationClass);
			destList.add(destObject);
		}
		return destList;
	}

	public static void copy(Object source, Object destObject) {
		dozer.map(source, destObject);
	}
}
