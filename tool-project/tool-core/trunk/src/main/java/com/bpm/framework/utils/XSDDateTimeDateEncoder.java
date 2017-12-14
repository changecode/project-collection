package com.bpm.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class XSDDateTimeDateEncoder {

	protected XSDDateTimeDateEncoder() {
	}

	public static XSDDateTimeDateEncoder getInstance() {
		return encoder;
	}

	public Object stringToObject(String str) throws Exception {
		if (str == null)
			return null;
		else
			return decodeDateUtil(str, null);
	}

	public static void validateDateStr(String dateStr) throws Exception {
		if (dateStr.length() < 19)
			throw new Exception("xsd.invalid.date" + dateStr);
		else
			return;
	}

	protected static String getDateFormatPattern(String xsdDateTime) {
		StringBuffer formatPattern = new StringBuffer("yyyy");
		int idx = xsdDateTime.indexOf('-', 4);
		for (int i = 4; i < idx; i++)
			formatPattern.append("y");

		formatPattern.append("-MM-dd'T'HH:mm:ss");
		idx = xsdDateTime.indexOf('.');
		for (int i = idx; i < xsdDateTime.length() - 1 && i < idx + 3
				&& Character.isDigit(xsdDateTime.charAt(i + 1)); i++) {
			if (i == idx)
				formatPattern.append(".");
			formatPattern.append("S");
		}

		return formatPattern.toString();
	}

	protected static Date decodeDateUtil(String str, StringBuffer zone)
			throws Exception {
		if (str == null)
			return null;
		str = collapseWhitespace(str);
		validateDateStr(str);
		StringBuffer strBuf = new StringBuffer(30);
		int dateLen = getDateFormatPattern(str, strBuf);
		String pattern = strBuf.toString();
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		df.setTimeZone(gmtTimeZone);
		Date date = df.parse(str.substring(0, dateLen));
		if (dateLen < str.length()) {
			int start = dateLen;
			if (Character.isDigit(str.charAt(start))) {
				int end;
				for (end = start; end < str.length()
						&& Character.isDigit(str.charAt(end)); end++)
					;
				int fractmilli = Integer.parseInt(str.substring(start,
						start + 1));
				if (fractmilli >= 5)
					date.setTime(date.getTime() + 1L);
				start = end;
			}
			if (start < str.length() && str.charAt(start) != 'Z') {
				if (zone != null)
					zone.append(str.substring(start));
				synchronized (timeZoneFormatter) {
					Date tzOffset = timeZoneFormatter.parse(str
							.substring(start + 1));
					long millis = str.charAt(start) != '+' ? tzOffset.getTime()
							: -tzOffset.getTime();
					date.setTime(date.getTime() + millis);
				}
			}
		}
		return date;
	}

	protected static int getDateFormatPattern(String dateStr,
			StringBuffer strBuf) {
		String formatPattern = "yyyy";
		strBuf.append(formatPattern);
		int idx = dateStr.indexOf('-', 4);
		for (int i = 4; i < idx; i++)
			strBuf.append('y');

		strBuf.append("-MM-dd'T'HH:mm:ss");
		idx = dateStr.indexOf('.');
		for (int i = idx; idx > 0 && i < dateStr.length() - 1 && i < idx + 3
				&& Character.isDigit(dateStr.charAt(i + 1)); i++) {
			if (i == idx)
				strBuf.append('.');
			strBuf.append('S');
		}

		return strBuf.length() - 2;
	}

	public static String collapseWhitespace(String str) {
		if (!needsCollapsing(str))
			return str;
		int len = str.length();
		char buf[] = new char[len];
		str.getChars(0, len, buf, 0);
		int leadingWSLen = 0;
		int trailingWSLen = 0;
		int spanLen = 0;
		for (int idx = 0; idx < len; idx++) {
			if (Character.isWhitespace(buf[idx])) {
				spanLen++;
				continue;
			}
			if (spanLen <= 0)
				continue;
			if (spanLen == idx) {
				leadingWSLen = spanLen;
			} else {
				int firstWSIdx = idx - spanLen;
				buf[firstWSIdx] = ' ';
				if (spanLen > 1) {
					System.arraycopy(buf, idx, buf, firstWSIdx + 1, len - idx);
					len -= spanLen - 1;
					idx = firstWSIdx + 1;
				}
			}
			spanLen = 0;
		}

		if (spanLen > 0)
			trailingWSLen = spanLen;
		return new String(buf, leadingWSLen, len - leadingWSLen - trailingWSLen);
	}

	public static boolean needsCollapsing(String str) {
		int len = str.length();
		int spanLen = 0;
		for (int idx = 0; idx < len; idx++) {
			if (Character.isWhitespace(str.charAt(idx))) {
				spanLen++;
				continue;
			}
			if (spanLen <= 0)
				continue;
			if (spanLen == idx)
				return true;
			if (str.charAt(idx - spanLen) != ' ')
				return true;
			if (spanLen > 1)
				return true;
			spanLen = 0;
		}

		return spanLen > 0;
	}

	private static final XSDDateTimeDateEncoder encoder = new XSDDateTimeDateEncoder();
	protected static final SimpleDateFormat timeZoneFormatter;
	protected static final TimeZone gmtTimeZone;
	protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	static {
		timeZoneFormatter = new SimpleDateFormat("HH:mm");
		gmtTimeZone = TimeZone.getTimeZone("GMT");
		timeZoneFormatter.setTimeZone(gmtTimeZone);
	}
}
