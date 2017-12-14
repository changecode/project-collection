package com.bpm.framework.exception.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.bpm.framework.exception.utils.ExceptionUtils;

@Component("defaultExceptionHandler")
public class DefaultExceptionHandler implements HandlerExceptionResolver {

	private final Logger log = Logger.getLogger(DefaultExceptionHandler.class);
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String fullStackTrace = ExceptionUtils.getFullStackTrace(ex);
		response.setContentType("text/json;charset=UTF-8");
		try {
			response.getWriter().write(getScript(fullStackTrace));
		} catch (IOException e) {
			log.error("异常：", e);
		}
		return null;
	}
	
	private String getScript(String msg) {
		StringBuilder script = new StringBuilder();
		script.append("<script>");
		script.append("bpm.msg.error('$(\"#msg\")', ").append(msg).append(")");
		script.append("</script>");
		return script.toString();
	}
}
