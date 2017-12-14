package com.bpm.framework.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.captcha.service.CaptchaServiceException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
* @ClassName: ImageCaptchaServlet
* @author lixx
* @createDate 2015-05-11 13:20:00
*/
@SuppressWarnings("serial")
public class ImageCaptchaServlet extends HttpServlet {

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		byte[] captchaChallengeAsJpeg = null;
		// the output stream to render the captcha image as jpeg into
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		ServletOutputStream responseOutputStream = null;
		try {
			try {
				// get the session id that will identify the generated captcha.
				// the same id must be used to validate the response, the session id
				// is a good candidate!
				String captchaId = request.getSession().getId();
				// call the ImageCaptchaService getChallenge method
				BufferedImage challenge = CaptchaServiceSingleton.getInstance()
						.getImageChallengeForID(captchaId,
								request.getLocale());
	
				// a jpeg encoder
				JPEGImageEncoder jpegEncoder = JPEGCodec
						.createJPEGEncoder(jpegOutputStream);
				jpegEncoder.encode(challenge);
			} catch (IllegalArgumentException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} catch (CaptchaServiceException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
	
			captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
	
			// flush it in the response
			response.setHeader("Cache-Control", "no-store");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			responseOutputStream = response.getOutputStream();
			responseOutputStream.write(captchaChallengeAsJpeg);
			responseOutputStream.flush();
		} finally {
			if(null != responseOutputStream) {
				responseOutputStream.close();
				responseOutputStream = null;
			}
			if(null != jpegOutputStream) {
				jpegOutputStream.close();
				jpegOutputStream = null;
			}
		}
	}
}
