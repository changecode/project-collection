package com.bpm.framework.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

import com.bpm.framework.cache.redis.RedisCache;
import com.bpm.framework.utils.CollectionUtils;
import com.bpm.framework.utils.StringUtils;
import com.bpm.framework.utils.date.CalendarUtils;
import com.bpm.framework.utils.date.DateFormator;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class Application implements Serializable {

	private static final long serialVersionUID = -8300017787244051698L;
	
	private final static Application instance = init();
	
	private static Map<String, String> oMap = null;
	
	private static final String BASE_KEY = "redis_key_";
	private static final String REDIS_KEY = BASE_KEY + Application.class.getName();
	
	private static String APPID = null;
	
	private String appContext;
	
	private Application() {}

	public static Application getInstance() {
		return instance;
	}

	private static Application init() {
		return new Application();
	}
	
	@SuppressWarnings("unchecked")
	private static void readFile() {
		InputStream is = null;
		try {
			if(APPID!=null){
				// 如果redis里面已经存在，则不再读取properties文件
				oMap = RedisCache.get(APPID+"_"+REDIS_KEY, Map.class);
				if(CollectionUtils.isNotEmpty(oMap)) {
					return;
				}
			}
			Properties p = new Properties();
			is = Application.class.getResourceAsStream("/application.properties");
			p.load(is);
			oMap = new HashMap<>();
			Set<Map.Entry<Object, Object>> entrySet = p.entrySet();
			for(Map.Entry<Object, Object> e : entrySet) {
				if(null == e.getKey()) continue;
				oMap.put(e.getKey().toString(), null == e.getValue() ? null : e.getValue().toString());
			}
			APPID = oMap.get("app.id");
			// 缓存到redis
			RedisCache.set(APPID+"_"+REDIS_KEY, oMap);
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

	/**
	 * 
	 * 当前日期
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getNowDate() {
		return CalendarUtils.now(CalendarUtils.YEAR_MONTH_DAY);
	}
	
	/**
	 * 
	 * 6个月之前
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getSixMonthAgo() {
		Date nowDate = com.bpm.framework.utils.date.DateUtils.getNowDate();
		int nowDateMonth = com.bpm.framework.utils.date.DateUtils.getMonth(nowDate);
		if(nowDateMonth>6){
			Date ago = DateUtils.setDays(nowDate, 1);
			ago = DateUtils.setMonths(ago, nowDateMonth-7);
			return com.bpm.framework.utils.date.DateUtils.toString(ago,DateFormator.YEAR_MONTH_DAY);
		}else{
			int nowDateYear = com.bpm.framework.utils.date.DateUtils.getYear(nowDate);
			Date ago = DateUtils.setYears(nowDate, nowDateYear-1);
			ago = DateUtils.setDays(ago, 1);
			ago = DateUtils.setMonths(ago, 12+(nowDateMonth-7));
			return com.bpm.framework.utils.date.DateUtils.toString(ago,DateFormator.YEAR_MONTH_DAY);
		}
	}
	
	
	/**
	 * 
	 * 7天前
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getLastWeek() {
		Date nowDate = com.bpm.framework.utils.date.DateUtils.preDays(new Date(), 7);
		return com.bpm.framework.utils.date.DateUtils.toString(nowDate,DateFormator.YEAR_MONTH_DAY);
	}
	
	/**
	 * 
	 * 30天前
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getLastMonth() {
		Date nowDate = com.bpm.framework.utils.date.DateUtils.preDays(new Date(), 30);
		return com.bpm.framework.utils.date.DateUtils.toString(nowDate,DateFormator.YEAR_MONTH_DAY);
	}


	private Calendar getFirstCalendarOfMonth() {
		GregorianCalendar c = CalendarUtils.now(GregorianCalendar.class);
		Calendar firsetCalendarOfMonth = CalendarUtils.beginTimeOfMonth(
				c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
		return firsetCalendarOfMonth;
	}
	
	private Calendar getLastCalendarOfMonth() {
		GregorianCalendar c = CalendarUtils.now(GregorianCalendar.class);
		Calendar firsetCalendarOfMonth = CalendarUtils.endTimeOfMonth(
				c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
		return firsetCalendarOfMonth;
	}

	/**
	 * 
	 * 当前月份第一天
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getFirstDayOfMonth() {
		Calendar firstDayOfMonth = getFirstCalendarOfMonth();
		return CalendarUtils.toString(firstDayOfMonth,
				CalendarUtils.YEAR_MONTH_DAY);
	}
	
	/**
	 * 
	 * 当前月份最后一天
	 * 
	 * @return yyyy-MM-dd
	 */
	public String getLastDayOfMonth() {
		Calendar lastDayOfMonth = getLastCalendarOfMonth();
		return CalendarUtils.toString(lastDayOfMonth,
				CalendarUtils.YEAR_MONTH_DAY);
	}

	/**
	 * 
	 * 当前月份开始时间
	 * 
	 * @return yyyy-MM-dd 24hh:mi:ss
	 */
	public String getBeginTimeOfMonth() {
		Calendar firsetDayOfMonth = getFirstCalendarOfMonth();
		return CalendarUtils.toString(firsetDayOfMonth,
				CalendarUtils.YEAR_MONTH_DAY_HH_MM_SS);
	}
	
	/**
	 * 
	 * 当前时间
	 * 
	 * @return yyyy-MM-dd 24hh:mi:ss
	 */
	public String getNowTime() {
		return CalendarUtils.now();
	}
	
	public String getByKey(String key) {
		readFile();
		return oMap.get(key);
	}

	public String getAppContext() {
		return appContext;
	}

	public void setAppContext(String appContext) {
		this.appContext = appContext;
	}
	
	public String getVersion() {
		readFile();
		return oMap.get("app.version");
	}
	
	public String getBuildTime() {
		readFile();
		return oMap.get("app.buildTime");
	}

	public String getUploadFileDirectory() {
		readFile();
		return oMap.get("app.attach.file_upload_base.dir");
	}
	
	public String getTempFileDirectory() {
		readFile();
		return oMap.get("app.attach.file_upload_temp.dir");
	}
	
	public boolean needRowNo() {
		readFile();
		return Boolean.valueOf(oMap.get("app.table.needRowNo"));
	}
	
	public boolean needDataAuth() {
		readFile();
		return Boolean.valueOf(oMap.get("app.auth.data.auth"));
	}
	
	public String getMobileUrl() {
		readFile();
		return oMap.get("app.mobile.url");
	}
	
	public String getAppName() {
		readFile();
		return oMap.get("app.name");
	}
	
	public String getCopyright() {
		readFile();
		return oMap.get("app.copyright");
	}
	
	public String getDescription(){
		readFile();
		return oMap.get("app.description");
	}
	
	public String getStaticResourceUrl() {
		readFile();
		return oMap.get("static.resource.url");
	}
	
	/**
	 * 
	 * 界面上取值：${app.map['key']}
	 * 
	 * @return
	 */
	public Map<String, String> getMap() {
		readFile();
		return oMap;
	}
	
	public String getAppWebUrl() {
		readFile();
		String url = oMap.get("app.web.url");
		if(StringUtils.isEmpty(url)) return null;
		if(url.endsWith("/")) return url;
		return url + "/";
	}
}
