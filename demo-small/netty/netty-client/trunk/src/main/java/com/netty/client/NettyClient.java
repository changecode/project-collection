package com.netty.client;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class NettyClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3728159460293270742L;

	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

	public static String HOST = "127.0.0.1";
	public static int PORT = 9999;

	public static Bootstrap bootstrap = getBootstrap();
	public static Channel channel = getChannel(HOST, PORT);

	/**
	 * 初始化Bootstrap
	 * 
	 * @return
	 */
	public static final Bootstrap getBootstrap() {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast("handler", new NettyClientHandler());
			}
		});
		b.option(ChannelOption.SO_KEEPALIVE, true);
		return b;
	}

	public static final Channel getChannel(String host, int port) {
		Channel channel = null;
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (Exception e) {
			throw new RuntimeException(String.format("连接Server(IP[%s],PORT[%s])失败", host, port), e);
		}
		return channel;
	}

	public static void send(String msg) throws Exception {
		if (channel != null) {
			channel.writeAndFlush(msg).sync();
		} else {
			throw new RuntimeException("消息发送失败,连接尚未建立!");
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				NettyClient.send("你好" + i);
			}
			long end = System.currentTimeMillis();
			logger.info((end - begin) / 1000 + " ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
