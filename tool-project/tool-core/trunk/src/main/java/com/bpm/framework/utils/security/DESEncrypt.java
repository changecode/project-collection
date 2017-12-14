package com.bpm.framework.utils.security;

import java.io.Serializable;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.bpm.framework.SystemConst;

/**
 * DES加密类
 * 
 * 2015-01-27 把对sun.misc.BASE64Decoder（sun内部api可能随着jdk的版本升级而变化）的依赖
 * 换成对org.apache.commons.codec.binary.Base64的依赖，避免了升级jdk时出现该类变化的情况
 * 
 * @author lixx
 * @since 1.0
 */
public class DESEncrypt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5903207832889240650L;

	private final byte[] DESkey = new byte[128];// 设置密钥

	private final byte[] DESIV = new byte[8];// 设置向量

	private AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现

	private Key key = null;
	
	// 不要修改DEFAULT_KEY，移动端单点登录已经约定好用该key进行加解密
	private static final String DEFAULT_KEY = "wertyuFGJTYUdfghjkl;'zxbnm,./!@#$%^&*()_+1234567890-=&HB8ujn%TV6yh";

	public DESEncrypt() {
		this(DEFAULT_KEY.getBytes());
	}

	public DESEncrypt(String desKey) {
		this(desKey.getBytes());
	}

	public DESEncrypt(byte[] desKey) {
		try {
			for (int i = 0; desKey != null && i < desKey.length && i < 8; i++) {
				DESkey[i] = desKey[i];
			}
			DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
			iv = new IvParameterSpec(DESIV);// 设置向量
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
			key = keyFactory.generateSecret(keySpec);// 得到密钥对象
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public String encode(String data) {
		try {
			Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
			enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
			byte[] pasByte = enCipher.doFinal(data.getBytes(SystemConst.ENCODING_UTF8));
			String result = new String(Base64.encodeBase64(pasByte), SystemConst.ENCODING_UTF8);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String decode(String data) {
		try {
			Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			deCipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] byteData = Base64.decodeBase64(data.getBytes(SystemConst.ENCODING_UTF8));
			byte[] pasByte = deCipher.doFinal(byteData);
			return new String(pasByte, SystemConst.ENCODING_UTF8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		DESEncrypt desEncrypt = new DESEncrypt("pic");
		String encryptPwd = desEncrypt
				.encode("!@#$%^&*()qertyuip -987531`12fdd21345890=-0eDBNM<?><MNBDSFG{}|}{POIUYd ");
		System.out.println(encryptPwd);
		encryptPwd = desEncrypt.decode(encryptPwd);
		System.out.println(encryptPwd);

	}
}
