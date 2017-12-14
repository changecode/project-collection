package cn.sunline.java.websocket;

import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/sys/info")
public class Progress {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@OnMessage
	public void onMessage(String msg, Session session) throws Exception {
		log.info("-------onMessage-------");
		log.info("msg : {}", msg);
		log.info("query string：{}", session.getQueryString());

		for (int i = 0; i <= 100; i++) {
			String tmp = "让我们定义一个 Java EE websocket服务器端" + i;
			log.info("send msg : {}", tmp);
			session.getBasicRemote().sendText(tmp);
			TimeUnit.SECONDS.sleep(3);
		}
	}

	@OnOpen
	public void onOpen() {
		log.info("------------onOpen------------");
	}

	@OnClose
	public void onClose() {
		log.info("------------onClose------------");
	}
}
