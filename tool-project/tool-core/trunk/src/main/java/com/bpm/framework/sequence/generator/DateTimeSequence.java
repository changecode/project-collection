package com.bpm.framework.sequence.generator;

import java.util.Date;

import com.bpm.framework.cache.redis.RedisCache;
import com.bpm.framework.sequence.AbstractSequence;
import com.bpm.framework.sequence.Sequence;
import com.bpm.framework.utils.date.DateUtils;

/**
 * 
 * 按日期生成序列，按天重置
 * 
 * @eg.P20170419000001
 * @author andyLee
 * @createDate 2017-04-19 14:09:00
 */
public class DateTimeSequence extends AbstractSequence {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 319422146749970334L;
	
	private String format = DateUtils.YEARMONTHDAY;// 日期格式
	
	private int length = 6;// 序列数长度，当长度不够的时候，位数会增加
	
	private final String REDIS_KEY_DATE = "redis_date_" + this.getClass().getName();// 当前序列所在日期
	private final String REDIS_KEY_NUMBER = "redis_number_" + this.getClass().getName();// 当前序列最大值
	
	public DateTimeSequence() {
		this(6);
	}
	
	public DateTimeSequence(int length) {
		this(DateUtils.YEARMONTHDAY, length);
	}
	
	public DateTimeSequence(String format, int length) {
		this.format = format;
		this.length = length;
	}

	@Override
	protected String generator(String prefix) {
		Date cacheDate = RedisCache.get(REDIS_KEY_DATE, Date.class);
		if(null == cacheDate) cacheDate = DateUtils.now(format);
		Long cacheNumber = RedisCache.get(REDIS_KEY_NUMBER, Long.class);
		if(null == cacheNumber) cacheNumber = 0L;
		StringBuilder no = new StringBuilder();
		no.append(null == prefix ? "" : prefix);
		if(DateUtils.compareDate(DateUtils.now(format), cacheDate) == 0) {// 如果日期还没有跨天
			no.append(DateUtils.toString(cacheDate, format));
			no.append(paddingLeftNumber(++cacheNumber));
		} else {// 如果日期已经跨天
			cacheDate = DateUtils.now(format);
			cacheNumber = 0L;
			no.append(DateUtils.toString(cacheDate, format));
			no.append(paddingLeftNumber(++cacheNumber));
		}
		// refreshRedis
		refreshRedis(cacheDate, cacheNumber);
		
		return no.toString();
	}
	
	private String paddingLeftNumber(Long number) {
		String n = number.toString();
		if(n.length() >= length) return n;
		int diff = length - n.length();
		StringBuilder no = new StringBuilder();
		no.append(n);
		for(int i = 1; i <= diff; i++) {
			no.insert(0, "0");
		}
		return no.toString();
	}
	
	private void refreshRedis(Date cacheDate, Long cacheNumber) {
		RedisCache.set(REDIS_KEY_DATE, cacheDate);
		RedisCache.set(REDIS_KEY_NUMBER, cacheNumber);
	}

	public static void main(String... args) throws Exception {
		Sequence seq = new DateTimeSequence();
		for(;;) {
			String s = seq.next("P");
			System.out.println(s);
			Thread.sleep(2000L);
		}
	}
}
