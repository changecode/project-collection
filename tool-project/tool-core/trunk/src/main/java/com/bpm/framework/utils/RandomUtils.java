package com.bpm.framework.utils;

import java.io.Serializable;
import java.util.Random;

/**
 * 
 * 获取随机字符串工具类 1、可指定长度 2、可以指定字符串规则
 * 
 * @author andyLee
 * @createDate 2015-10-12 21:55:00
 */
public abstract class RandomUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822805835465409264L;

	public static final int LOWERCASE = 0;
	public static final int CAPITAL = 1;
	public static final int NUMBER = 2;
	public static final int LOWERCASE_AND_NUMBER = 3;
	public static final int CAPITAL_AND_NUMBER = 4;
	public static final int LETTER_AND_NUMBER = 5;
	public static final String S_LOWERCASE = "abcdefghijklmnopqrstuvwxys";
	public static final String S_CAPITAL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String S_NUMBER = "0123456789";
	public static final String S_LOWERCASE_AND_NUMBER = "012abcdefg345hijklmnopqr6789stuvwxyz";
	public static final String S_UPPERCASE_AND_NUMBER = "012ABCDEFG345HIJKLMNOPQR6789STUVWXYZ";
	public static final String S_LETTER_AND_NUMBER = "0123456789AaBbCcDdEeFfGg0123456789HhIiJjKkLlMmNnOoPpQqRr0123456789SsTtUuVvWwXxYyZz";

	public static final int length = 6;

	public RandomUtils() {
	}

	/**
	 * 获取随机字符串
	 * 
	 * 参数1：type表示随机字符串的组成字符的类型，可以通过RandomUtil的静态属性获得。
	 * 
	 * 参数2：randomStringLength表示生成随机字符串的长度
	 * 
	 * @param type
	 * @param randomStringLength
	 * @param seed
	 * @return
	 */
	public static String generate(int type, int randomStringLength) {
		String source = getSourceByType(type);
		return generate(source, randomStringLength);
	}

	private static String getSourceByType(int type) {
		String source = null;
		switch (type) {
		case 0:
			source = S_LOWERCASE;
			break;
		case 1:
			source = S_CAPITAL;
			break;
		case 2:
			source = S_NUMBER;
			break;
		case 3:
			source = S_LOWERCASE_AND_NUMBER;
			break;
		case 4:
			source = S_UPPERCASE_AND_NUMBER;
			break;
		case 6:
			source = S_LETTER_AND_NUMBER;
			break;
		default:
			source = S_LETTER_AND_NUMBER;
			break;
		}
		return source;
	}

	public static String generate(String source, int randomStringLength) {
		String randStr = source;
		StringBuffer generateRandStr = new StringBuffer("");
		int strLength = randStr.length();
		Random rand = new Random(System.currentTimeMillis() + 123L);
		int randStrLength = randomStringLength;
		for (int i = 0; i < randStrLength; i++) {
			int randNum = rand.nextInt(strLength);
			generateRandStr.append(randStr.substring(randNum, randNum + 1));
		}
		return generateRandStr.toString();
	}

	public static String generate() {
		return generate(S_LETTER_AND_NUMBER, length);
	}

	public static void main(String[] args) {
		System.out.println(RandomUtils.generate());
	}
}
