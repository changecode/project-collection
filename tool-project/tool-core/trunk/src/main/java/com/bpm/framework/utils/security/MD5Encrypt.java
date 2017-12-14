package com.bpm.framework.utils.security;

import java.io.Serializable;
import java.security.MessageDigest;

import com.bpm.framework.SystemConst;

/**
 * Md5加密工具类
 * 
 * @author lixx
 * @since 1.0
 */
public class MD5Encrypt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -235664730991660647L;
	
	private final static String[] HEXDIGITS = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private MD5Encrypt() {}
	
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return HEXDIGITS[d1] + HEXDIGITS[d2];
	}

	/**
	 * 对输入的源字符串进行Md5加密，返回加密后的32位字符串
	 * 
	 * @param origin
	 *            加密源字符串
	 * @return
	 */
	public static String encrypt(String origin) {
		String resultString = null;

		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes(SystemConst.ENCODING_UTF8)));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return resultString;
	}

	public static void main(String[] args) {
		String encryptPwd = MD5Encrypt.encrypt("30006|20110511145326859|10001|20110511145417250|true|true|777.77|889997776665544455556|2unkonw|2unkonw|crashHJks&823()lw:,ds12@~@#@HJKiodslkfewkl3#@65k430dso9");
		System.out.println(encryptPwd);
	}
}
