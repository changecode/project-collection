package com.bpm.framework.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.bpm.framework.cache.redis.RedisCache;
import com.bpm.framework.utils.CollectionUtils;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class EmailPropCache implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 722459658319065495L;

	private final static EmailPropCache instance = init();
	
	private static Map<String, String> oMap = null;
	
	private static final String BASE_KEY = "redis_key_";
	private static final String REDIS_KEY = BASE_KEY + EmailPropCache.class.getName();
	
	private EmailPropCache() {}

	public static EmailPropCache getInstance() {
		return instance;
	}

	private static EmailPropCache init() {
		return new EmailPropCache();
	}
	
	@SuppressWarnings("unchecked")
	private static void readFile() {
		InputStream is = null;
		try {
			// 如果redis里面已经存在，则不再读取properties文件
			oMap = RedisCache.get(REDIS_KEY, Map.class);
			if(CollectionUtils.isNotEmpty(oMap)) {
				return;
			}
			Properties p = new Properties();
			is = EmailPropCache.class.getResourceAsStream("/email.properties");
			p.load(is);
			oMap = new HashMap<>();
			Set<Map.Entry<Object, Object>> entrySet = p.entrySet();
			for(Map.Entry<Object, Object> e : entrySet) {
				if(null == e.getKey()) continue;
				oMap.put(e.getKey().toString(), null == e.getValue() ? null : e.getValue().toString());
			}
			// 缓存到redis
			RedisCache.set(REDIS_KEY, oMap);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
		}
	}
	
	public Map<String, String> getMap() {
		readFile();
		return oMap;
	}
}
