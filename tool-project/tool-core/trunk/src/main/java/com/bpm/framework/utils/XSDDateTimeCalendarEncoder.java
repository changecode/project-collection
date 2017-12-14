// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XSDDateTimeCalendarEncoder.java

package com.bpm.framework.utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

// Referenced classes of package oracle.j2ee.ws.common.encoding.simpletype:
//            XSDDateTimeDateEncoder, SimpleTypeEncoder

public class XSDDateTimeCalendarEncoder extends XSDDateTimeDateEncoder {

	private XSDDateTimeCalendarEncoder() {
	}

	public static XSDDateTimeCalendarEncoder getInstance() {
		return encoder;
	}

	public Object stringToObject(String str) throws Exception {
		if (str == null)
			return null;
		StringBuffer zone = new StringBuffer(10);
		java.util.Date date = decodeDateUtil(str, zone);
		String zoneStr = zone.toString();
		Calendar cal;
		if (zoneStr.length() == 0) {
			cal = Calendar.getInstance();
			cal.setTime(date);
		} else {
			cal = Calendar.getInstance();
			cal.setTime(date);
			TimeZone tz = TimeZone.getTimeZone("GMT" + zoneStr);
			int rawOffset = tz.getRawOffset();
			TimeZone tz2 = TimeZone.getDefault();
			if (tz2.inDaylightTime(date))
				rawOffset -= ((Integer) getDSTSavingsMethod.invoke(tz2, null))
						.intValue();
			tz2.setRawOffset(rawOffset);
			tz2.setID("Custom");
			cal.setTimeZone(tz2);
		}
		return cal;
	}

	private static final XSDDateTimeCalendarEncoder encoder = new XSDDateTimeCalendarEncoder();
	private static Method getDSTSavingsMethod = null;
	private static final SimpleDateFormat calendarFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");
	private static final Calendar gmtCalendar;

	static {
		gmtCalendar = Calendar.getInstance();
		gmtCalendar.setTimeZone(gmtTimeZone);
		try {
			getDSTSavingsMethod = TimeZone.getDefault().getClass().getMethod(
					"getDSTSavings", null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
