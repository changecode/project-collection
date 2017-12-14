package cn.sunline.test;

import org.springframework.util.ClassUtils;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class Test {

	private static final boolean jettyWsPresent = ClassUtils.isPresent(
			"org.eclipse.jetty.websocket.server.WebSocketServerFactory", DefaultHandshakeHandler.class.getClassLoader());
	
	public static void main(String[] args) throws Exception {
		System.out.println(jettyWsPresent);
	}
}
