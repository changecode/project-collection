package com.bpm.framework.image;

import java.io.Serializable;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * @ClassName: CaptchaServiceSingleton
 * @author lixx
 * @createDate 2015-05-11 13:20:00
 */
public class CaptchaServiceSingleton implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1333404392606867489L;

	// 不允许构造实例
	private CaptchaServiceSingleton() {
	}

	private static ImageCaptchaService instance = null;

	// 传入样式类
	static {
		instance = new CustomDefaultManageableImageCaptchaService(new FastHashMapCaptchaStore(),
				new ImageCaptchaEngineExtend(), 180, 100000, 75000);
	}

	public static ImageCaptchaService getInstance() {
		return instance;
	}
}
