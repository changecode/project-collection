package com.bpm.framework.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取本机ip
 * 
 * @author lixx
 * @createDate 2015-01-26 18:29:00
 */
public class InetAddressUtils {
	public static String[] localServers =null;
	static{
	 
		try {
			// 得到本机ip
			InetAddress addr = InetAddress.getLocalHost();
			String hostName = addr.getHostName().toString();

			// 获取本机ip
			InetAddress[] ipsAddr = InetAddress.getAllByName(hostName);
			localServers = new String[ipsAddr.length];
			for (int i = 0; i < ipsAddr.length; i++) {
				if (ipsAddr[i] != null) {
					localServers[i] = ipsAddr[i].getHostAddress().toString();
				}
			}
 		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String getHostAddress() {

		try {// 得到本机ip
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String[] getHostMultiAddress() {
		if(localServers != null){
			return localServers;
		}
	 
		return new String[0];
	}

	public static String getHostMultiAddressToString() {
		return StringUtils.arrayToCommaDelimitedString(localServers);
	}
	
	public static boolean isV4Ip(String s) {
		if(s == null){
			return false;
		}
		String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
		String regex1 = "1\\d{2}";
		String regex2 = "[1-9]\\d";
		String regex3 = "\\d";
		String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|("
				+ regex3 + ")";
		regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex
				+ ")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	public static boolean isHost(String ip){
		boolean isHost = false;
 		for (String tip : localServers) {
			if (tip.equals(ip)) {
				isHost = true;
				break;
			}
		}
		return isHost;
	}
	
	/**
	 *  获取ip
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
 
        String proxs[] = { "X-Forwarded-For", "Proxy-Client-IP",
                "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
 
        String ip = null;
 
        for (String prox : proxs) {
            ip = request.getHeader(prox);
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                continue;
            } else {
                break;
            }
        }
 
        if (StringUtils.isEmpty(ip)) {
            return request.getRemoteAddr();
        }
 
        return ip;
    }
	
	public static void main(String[] args) {
//		System.out.println(InetAddressUtils.getHostMultiAddressToString());
	}
}
