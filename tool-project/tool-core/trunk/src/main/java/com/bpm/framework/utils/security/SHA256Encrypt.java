package com.bpm.framework.utils.security;

import java.io.Serializable;
import java.security.MessageDigest;

import com.bpm.framework.SystemConst;

/**
 * 
 * SHA-256算法加密
 * 
 * @author andyLee
 * @createDate 2016-03-29 15:40:00
 */
public final class SHA256Encrypt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3912016662598067763L;

	/**
	 * 对字符串加密,加密算法使用SHA-256
	 * 
	 * @param strSrc
	 *            要加密的字符串
	 * @param encName
	 *            加密类型
	 * @return
	 */
	public static String encrypt(String value) {
		MessageDigest md = null;
		String strDes = null;

		try {
			byte[] bt = value.getBytes(SystemConst.ENCODING_UTF8);
			md = MessageDigest.getInstance("SHA-256");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (Exception e) {
			return null;
		}
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public static void main(String[] args) {
		System.out.println(encrypt("1234567890asdfghjkl"));
	}
}
