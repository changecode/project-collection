package com.bpm.framework.bootstrap.page;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.bpm.framework.utils.Assert;

/**
 * 
 * 分页返回对象，只要前台是分页，则统一返回该对象
 * 
 * @author lixx
 * @createDate 2015-06-03 17:50:00
 * @param <T>
 */
public class Page<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2786622989382780846L;

	private PageParam pageParam;
	
	private int total;
	
	private int totalRows;
	
	private List<T> data;
	
	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotal() {
		total = totalRows;
		return total;
	}

	/**
	 * 
	 * 不建议设置total，直接设置 totalRows即可
	 * 
	 * @param total
	 */
	@Deprecated
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 
	 * 因为不是所有类都继承自AbstractEntity.java，比如：pic-snaker模块中的实体
	 * 所以在此用反射实现序号初始化，只要有setRowNo(long rowNo)方法，则初始化序号
	 * 
	 * @return
	 */
	public List<T> getData() {
		// 数据查出来之后，填充行号字段：rowNo
		IF:if(CollectionUtils.isNotEmpty(data)) {
			T clazz = data.get(0);
			Method method = null;
			try {
				// 实体类里面字段类型为基本类型long，所以调用方法必须指定为基本类型long.class
				method = clazz.getClass().getMethod("setRowNo", long.class);
			} catch(Exception e) {
				break IF;// 跳出IF
			}
			
			Assert.notNull(pageParam);// 如果有数据，则pageParam必须要有值，否则抛出异常
			int rowNo = (pageParam.getPageNumber() - 1) * pageParam.getPageSize() + 1;
			for(T t : data) {
				try {
					method.invoke(t, rowNo++);
				} catch(Exception e) {
					break;// 一旦有一条数据出现异常，则终止循环
				}
			}
		}
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public PageParam getPageParam() {
		return pageParam;
	}

	public void setPageParam(PageParam pageParam) {
		this.pageParam = pageParam;
	}
}
