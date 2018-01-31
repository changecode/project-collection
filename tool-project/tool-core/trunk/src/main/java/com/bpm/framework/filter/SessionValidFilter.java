package com.bpm.framework.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.bpm.framework.annotation.ValidateSession;
import com.bpm.framework.annotation.Web;
import com.bpm.framework.annotation.WebValue;
import com.bpm.framework.console.Application;
import com.bpm.framework.controller.ControllerUtils;
import com.bpm.framework.entity.user.User;
import com.bpm.framework.spring.SpringContext;
import com.bpm.framework.utils.StringUtils;

/**
 * 用户session过滤器，用于检查用户session是否过期，根据session的验证结果做出不同的处理
 * 
 * @author lixx
 * @since 1.0
 */
public class SessionValidFilter implements Filter {

	private final static Logger log = LoggerFactory.getLogger(SessionValidFilter.class);

	final Application app = Application.getInstance();

	final String page = "/index.jsp";

	@Override
	public void init(FilterConfig config) throws ServletException {
		// this.page = config.getInitParameter("page");
	}

	@Override
	public void doFilter(ServletRequest srequest, ServletResponse sresponse,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		
		if (StringUtils.isNullOrBlank(app.getAppContext())) {
			app.setAppContext(request.getContextPath());
		}
		
		String context = app.getAppContext();
		if (!StringUtils.hasLength(context)) {
			context = "/";
		}
		
		String uri = request.getRequestURI();
		Map<RequestMappingInfo, HandlerMethod> methodMap = getSpringMethodMapping();
		for(Map.Entry<RequestMappingInfo, HandlerMethod> e : methodMap.entrySet()) {
			RequestMappingInfo mapping = e.getKey();
			HandlerMethod method = e.getValue();
			String controllerBeanId = (String) method.getBean();
			// 判断uri是否包含设置的controller和method
			if((!uri.contains(mapping.getPatternsCondition().toString().replaceAll("\\[|\\]", ""))) && (!uri.contains(controllerBeanId) || !uri.contains(method.getMethod().getName()))) continue;
			
			Object controller = SpringContext.getBean(controllerBeanId);
			
			Class<?> clazz = controller.getClass();
			ValidateSession methodValidateSession = method.getMethodAnnotation(ValidateSession.class);
			ValidateSession clazzValidateSession = clazz.getAnnotation(ValidateSession.class);
			// 如果方法上或者controller上设置了ValidateSession注解，并且设置了validate()为false，则表示不需要验证session
			// 优先判断方法，其次判断方法所在controller
			if(null != methodValidateSession && !methodValidateSession.validate()) {
				log.info("class=" + clazz.getName() + ";method=" + method.getMethod().getName() + "不需要验证session.");
				chain.doFilter(srequest, sresponse);
				return;
			}
			if(null != clazzValidateSession && !clazzValidateSession.validate()) {
				log.info("class=" + clazz.getName() + ";method=" + method.getMethod().getName() + "不需要验证session.");
				chain.doFilter(srequest, sresponse);
				return;
			}
			
			// 定义返回值变量
			boolean bool = isLogin();
			if (!bool) {
				log.info("SessionValidFilter用户未登录.");
				// 如果是ajax请求响应头会有，x-requested-with
				if (request.getHeader("x-requested-with") != null 
						&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
		            response.setHeader("sessionstatus", "timeOut");//在响应头设置session状态  
		            response.addHeader("loginPath", context);
				} else {
//					response.sendRedirect("/");
					PrintWriter out = response.getWriter();
					out.println("<html>");
					out.println("<script>");
					out.println("window.open ('"+request.getContextPath()+"/','_top')");
					out.println("</script>");
					out.println("</html>");
				}
				return;
			}
 			Web web = clazz.getAnnotation(Web.class);
			
			// 如果是ALL，则不验证前台还是后台用户
			if(null != web && web.value().equals(WebValue.ALL)) {
				chain.doFilter(srequest, sresponse);
				return;
			}
			
			User user = ControllerUtils.getCurrentUser();// 当前登录的管理端用户
			// 如果没有设置web注解，或者web设置的是ADMIN，并且user为空，则表示管理端用户未登录，进行提示
			if ((null == web || (null != web && web.value().equals(WebValue.ADMIN))) && null == user) {
				log.info("禁止非后台管理用户访问后台controller...");
				request.getSession().setAttribute("error_msg_404", "对不起，你没有权限访问该页面.");
				// response.sendRedirect("/404.jsp");
				// 如果是ajax请求响应头会有，x-requested-with
				if (request.getHeader("x-requested-with") != null 
						&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
		            response.setHeader("sessionstatus", "timeOut");//在响应头设置session状态  
		            response.addHeader("loginPath", "/404.jsp");
				}else{
					response.sendRedirect("/404.jsp");
				}
				return;
			}
			// 如果包含web注解，则进行用户类型验证，主要避免前/后台用户互相穿插
			if (null != web && null == user) {
				log.info("禁止后台管理用户访问web端controller...");
				request.getSession().setAttribute("error_msg_404", "对不起，你没有权限访问该页面.");
				// response.sendRedirect("/404.jsp");
				// 如果是ajax请求响应头会有，x-requested-with
				if (request.getHeader("x-requested-with") != null 
						&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
		            response.setHeader("sessionstatus", "timeOut");//在响应头设置session状态  
		            response.addHeader("loginPath", "/404.jsp");
				}else{
					response.sendRedirect("/404.jsp");
				}
				return;
			}
		}
		chain.doFilter(srequest, sresponse);
	}
	
	/**
	 * 
	 * 判断用户是否已经登录
	 * 
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private boolean isLogin() throws ServletException, IOException {
		Object user = ControllerUtils.getCurrentUser();
		// 判断当前登录的前台用户和后台用户是否都为空，如果都为空，则认为没有登录
		if (null == user) {
			return false;
		}
		return true;
	}
	
	private Map<RequestMappingInfo, HandlerMethod> getSpringMethodMapping() {
        RequestMappingHandlerMapping mapping = SpringContext.getBean(RequestMappingHandlerMapping.class);
        return mapping.getHandlerMethods();
	}

	@Override
	public void destroy() {
	}
}
