package com.bpm.framework.utils.security;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 
 * 随机盐生成器
 * 
 * @author andyLee
 * @createDate 2016-03-29 15:56:00
 */
public final class SaltGenerator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8329989200382969017L;

	public static final String generator() {
		Random ranGen = new SecureRandom();
        byte[] aesKey = new byte[4];
        ranGen.nextBytes(aesKey);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < aesKey.length; i++) {
            String hex = Integer.toHexString(0xff & aesKey[i]);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(generator());
	}
}
