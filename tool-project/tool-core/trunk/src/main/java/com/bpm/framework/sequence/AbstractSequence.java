package com.bpm.framework.sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpm.framework.exception.FrameworkRuntimeException;

public abstract class AbstractSequence implements Sequence {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2950845024660843826L;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected abstract String generator(String prefix);
	
	/**
	 * 
	 * prefix  编号前缀，如果传null，则没有前缀
	 * 
	 */
	@Override
	public synchronized String next(String prefix) {
		try {
			return generator(prefix);
		} catch(Exception e) {
			log.error("获取下一个序列异常：", e);
			throw new FrameworkRuntimeException("获取下一个序列异常：", e);
		}
	}
}
