package cn.sunline.spring.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 
 * 获取用户访问信息
 * 
 * @author andyLee
 *
 */
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			String sessionId = (String) session.getAttribute("sessionId");
			if(null == sessionId) {
				log.info("[{}]游客将建立连接", session.getId());
				attributes.put("sessionId", session.getId());
			} else {
				// 标识当前的用户
				attributes.put("sessionId", "张三-" + sessionId);
				log.info("[{}]将建立连接", "张三-" + sessionId);
			}
		}
		return true;
	}

	/**
	 * 
	 * 握手之后的处理
	 * 
	 * @param request
	 * @param response
	 * @param wsHandler
	 * @param exception
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		
	}
}
