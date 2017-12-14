package com.bpm.framework.image;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

public class CustomDefaultManageableImageCaptchaService extends DefaultManageableImageCaptchaService {

	public CustomDefaultManageableImageCaptchaService(int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
			int captchaStoreLoadBeforeGarbageCollection) {
		super(minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
	}

	public CustomDefaultManageableImageCaptchaService(CaptchaStore captchaStore, CaptchaEngine captchaEngine,
			int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
			int captchaStoreLoadBeforeGarbageCollection) {
		super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize,
				captchaStoreLoadBeforeGarbageCollection);
	}

	/**
	 * 
	 * 验证完成后，页面跳转后清除，调用：removeCaptcha 方法
	 * 
	 */
	@Override
	public Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException {
		if (!this.store.hasCaptcha(ID)) {
			// throw new CaptchaServiceException("Invalid ID, could not validate unexisting or already validated captcha.");
			return false;
		}
		Boolean valid = this.store.getCaptcha(ID).validateResponse(response);
		// 源码的这一句是没被注释的，这里我们注释掉，在下面暴露一个方法给我们自己来移除sessionId
		this.store.removeCaptcha(ID);
		return valid;
	}

	/**
	 * 移除session绑定的验证码信息. Method Name：removeCaptcha .
	 * 
	 * @param sessionId
	 *            the return type：void
	 */
//	public void removeCaptcha(String sessionId) {
//		if (null != sessionId && this.store.hasCaptcha(sessionId)) {
//			this.store.removeCaptcha(sessionId);
//		}
//	}
}
