package cn.sunline.spring.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 
 * websocket初始化参数
 * 
 * @author andyLee
 *
 */
@Configuration
@ComponentScan(basePackages={ "cn.sunline" })
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		System.out.println("WebSocketConfig initializer...");
		// 添加消息处理和握手的拦截器
		registry.addHandler(messageHandler(), "/sys/info").addInterceptors(interceptors());
		// 浏览器不支持websocket的情况下使用socketjs
		registry.addHandler(messageHandler(), "/sockjs/sys/info").addInterceptors(interceptors()).withSockJS();
		System.out.println("WebSocketConfig initializerd...");
	}
	
	/**
	 * 
	 * 系统消息处理
	 * 
	 * @return
	 */
	@Bean
	public WebSocketHandler messageHandler() {
		return new MessageHandler();
	}
	
	@Bean
	public HandshakeInterceptor interceptors() {
		return new WebSocketHandshakeInterceptor();
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// super.configureDefaultServletHandling(configurer);
		configurer.enable();
	}
}
