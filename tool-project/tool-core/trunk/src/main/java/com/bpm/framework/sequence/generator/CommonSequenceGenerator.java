package com.bpm.framework.sequence.generator;

import com.bpm.framework.sequence.AbstractSequence;
import com.bpm.framework.sequence.Sequence;

/**
 * 
 * 通用序列生成器 --> 根据当前纳秒生成
 * 
 * @author andyLee
 * @createDate 2016-08-22 18:20:00
 */
public class CommonSequenceGenerator extends AbstractSequence {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9116238103566624708L;

	/**
	 * 
	 * prefix  编号前缀，如果传null，则没有前缀
	 * 
	 */
	@Override
	protected String generator(String prefix) {
		if(null == prefix) prefix = "";
		return prefix + System.nanoTime();
	}
	
	public static void main(String[] args) {
		Sequence seq = new CommonSequenceGenerator();
		String next = seq.next("F");
		System.out.println(next);
	}
}
