package com.bpm.framework.cache.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.bpm.framework.spring.SpringContext;
import com.bpm.framework.utils.json.JsonUtils;

/**
 * 
 * 提供redis分布式缓存工具，与MemcachedCache.java实现功能类似
 * 
 * @author andyLee
 * @createDate 2016-06-14 14:28:00
 */
public class RedisCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3270301497334900100L;

	private static RedisTemplate<String, String> redisTemplate = null;
	
	@SuppressWarnings("unchecked")
	private static void check() {
		if(null == redisTemplate) {
			redisTemplate = SpringContext.getBean(RedisTemplate.class);
		}
	}
	
	public static final void set(final String key, final Object value) {
		check();
		redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(redisTemplate.getStringSerializer().serialize(key), 
						redisTemplate.getStringSerializer().serialize(JsonUtils.fromObject(value)));
				return null;
			}
		});
	}
	
	public static final void setEx(final String key, final long seconds, final Object value) {
		check();
		redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(redisTemplate.getStringSerializer().serialize(key), seconds, 
						redisTemplate.getStringSerializer().serialize(JsonUtils.fromObject(value)));
				return null;
			}
		});
	}
	
	/**
	 * 
	 * set if Not Exists
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static final Boolean setNX(final String key, final Object value) {
		check();
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(redisTemplate.getStringSerializer().serialize(key), 
						redisTemplate.getStringSerializer().serialize(JsonUtils.fromObject(value)));
			}
		});
	}
	
	public static final <T> T get(final String key, final Class<T> clazz) {
		check();
		return redisTemplate.execute(new RedisCallback<T>() {
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				final byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
				if(!connection.exists(byteKey)) {
					return null;
				}
				byte[] v = connection.get(byteKey);
				String value = redisTemplate.getStringSerializer().deserialize(v);
				T t = JsonUtils.toObject(value, clazz);
				return t;
			}
		});
	}
	
	public static final <T> List<T> getListByPattern(final String keyPattern,final Class<T> clazz){
		check();
		return redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<String> keys = redisTemplate.keys(keyPattern);
				String key;
				if(keys!=null&&keys.size()>0){
					List<T> resultList = new ArrayList<T>();
					for(Iterator<String> it = keys.iterator();it.hasNext();){
						key = it.next();
						final byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
						if(!connection.exists(byteKey)) {
							continue;
						}		
						byte[] v = connection.get(byteKey);
						String value = redisTemplate.getStringSerializer().deserialize(v);
						T val = JsonUtils.toObject(value, clazz);
						resultList.add(val);
					}
					return resultList;
				}
				return null;
			}
		});
	}
	
	public static final <T> List<T> getList(final String key, final Class<T> clazz) {
		check();
		return redisTemplate.execute(new RedisCallback<List<T>>() {
			@Override
			public List<T> doInRedis(RedisConnection connection) throws DataAccessException {
				final byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
				if(!connection.exists(byteKey)) {
					return null;
				}
				byte[] v = connection.get(byteKey);
				String value = redisTemplate.getStringSerializer().deserialize(v);
				List<T> tList = JsonUtils.toList(value, clazz);
				return tList;
			}
		});
	}
	
	public static final Long del(final String... keys) {
		check();
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				final List<byte[]> keyList = new ArrayList<>();
				for(String key : keys) {
					final byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
					if(!connection.exists(byteKey)) {
						continue;
					}
					keyList.add(byteKey);
				}
				return connection.del(keyList.toArray(new byte[][]{}));
			}
		});
	}
	
	public static final Boolean exists(final String key) {
		check();
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				final byte[] byteKey = redisTemplate.getStringSerializer().serialize(key);
				return connection.exists(byteKey);
			}
		});
	}
	
	public static final void flushDb() {
		check();
		redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return null;
			}
		});
	}
}
