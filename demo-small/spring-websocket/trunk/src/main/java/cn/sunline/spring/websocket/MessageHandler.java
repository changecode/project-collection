package cn.sunline.spring.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 
 * 消息处理类
 * 
 * @author andyLee
 *
 */
public class MessageHandler extends TextWebSocketHandler {

	// 定义Map，用于存放用户，ConcurrentHashMap是线程安全的
	public static final Map<String, WebSocketSession> userSocketSessionMap = new ConcurrentHashMap<>();
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * 发接收到消息后的处理
	 * 
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// super.handleTextMessage(session, message);
		// session.sendMessage(message);// 发送给当前用户
		// sendMessage(message);// 发送给所有用户
		sendMessage(session, message);// 发送给所有用户 ，除了当前发送者
	}
	
	/**
	 * 
	 * 异常时的处理
	 * 
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// super.handleTransportError(session, exception);
		log.info("{}用户会话异常，讲关闭连接", session.getId());
		if(session.isOpen()) {
			session.close();
		}
		// 并在集合中移除该用户
		removeSession(session);
	}
	
	/**
	 * 
	 * 建立连接后的操作
	 * 
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// super.afterConnectionEstablished(session);
		Map<String, Object> attrs = session.getAttributes();
		log.info("{}=>{}连接成功", attrs.get("sessionId"), session.getId());
		// 将用户添加到集合
		userSocketSessionMap.put(attrs.get("sessionId").toString(), session);
	}
	
	/**
	 * 
	 * 关闭连接后的操作
	 * 
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// super.afterConnectionClosed(session, status);
		log.info("{}关闭连接", session.getId());
		removeSession(session);
	}
	
	private void removeSession(WebSocketSession session) {
		Set<String> keys = new HashSet<>();
		for(Map.Entry<String, WebSocketSession> e : userSocketSessionMap.entrySet()) {
			if(e.getValue().getId().equals(session.getId())) {
				keys.add(e.getKey());
			}
		}
		for(String key : keys) {
			userSocketSessionMap.remove(key);
		}
	}
	
	/**
	 * 
	 * 发送消息给指定用户
	 * 
	 * @param user
	 * @param textMessage
	 */
	public void sendMessage(final String user, final TextMessage textMessage) {
		for(Map.Entry<String, WebSocketSession> e : userSocketSessionMap.entrySet()) {
			final String key = e.getKey();
			final WebSocketSession value = e.getValue();
			if(value.isOpen()) {
				if(key.equals(user)) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								value.sendMessage(textMessage);
								log.info("用户[{}]发送消息成功", user);
							} catch (IOException e) {
								log.error("用户[" + user + "]发送消息异常：", e);
							}
						}
					}).start();
					return;
				}
			} else {
				removeSession(value);
			}
		}
	}
	
	/**
	 * 
	 * 发送消息给所有在线用户
	 * 
	 * @param textMessage
	 */
	public void sendMessage(final TextMessage textMessage) {
		log.info("userSocketSessionMap = {}", userSocketSessionMap);
		for(Map.Entry<String, WebSocketSession> e : userSocketSessionMap.entrySet()) {
			final String key = e.getKey();
			final WebSocketSession value = e.getValue();
			if(value.isOpen()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							value.sendMessage(textMessage);
							log.info("用户[{}]发送消息成功", key);
						} catch (IOException e) {
							log.error("用户[" + key + "]发送消息异常：", e);
						}
					}
				}).start();
			} else {
				removeSession(value);
			}
		}
	}
	
	/**
	 * 
	 * 发送消息给所有在线用户（除了当前发送人自己）
	 * 
	 * @param textMessage
	 */
	public void sendMessage(final WebSocketSession session, final TextMessage textMessage) {
		log.info("userSocketSessionMap = {}", userSocketSessionMap);
		for(Map.Entry<String, WebSocketSession> e : userSocketSessionMap.entrySet()) {
			final String key = e.getKey();
			final WebSocketSession value = e.getValue();
			// 如果是当前发送人自己，则跳过
			if(value.getId().equals(session.getId())) {
				continue;
			}
			if(value.isOpen()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							value.sendMessage(textMessage);
							log.info("用户[{}]发送消息成功", key);
						} catch (IOException e) {
							log.error("用户[" + key + "]发送消息异常：", e);
						}
					}
				}).start();
			} else {
				removeSession(value);
			}
		}
	}
}
