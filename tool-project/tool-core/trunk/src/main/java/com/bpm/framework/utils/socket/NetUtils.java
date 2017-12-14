package com.bpm.framework.utils.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import javax.net.ServerSocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;

import com.bpm.framework.SystemConst;
import com.bpm.framework.utils.StringUtils;

/**
 * 网络连接工具类
 * 
 */
public class NetUtils {
	
	public final static String ENCODING = SystemConst.ENCODING_GBK;
	
	public final static String ENCODING_UTF8 = SystemConst.ENCODING_UTF8;
	
	// 连接超时时间
	public static int CONNECT_TIMEOUT = 8 * 1000;
	
	public static int HTTPS_CONNECT_TIMEOUT = 20 * 1000;
	
	// 是否需要SSL
	private static boolean needSSL = false;
	
	private static ServerSocketFactory factory = null;

	private NetUtils() {}
	
	/**
	 * 在指定端口上创建ServerSocket
	 * 
	 * @param port
	 * @return
	 * @throws Exception
	 *             方法中不处理任何异常，所有异常都抛出由调用方处理
	 */
	public static ServerSocket createServer(int port) throws Exception {
		return getServerSocketFactory().createServerSocket(port);
	}

	private static ServerSocketFactory getServerSocketFactory()
			throws Exception {
		if (factory == null) {
			if (needSSL) {
				SSLContext sslContext = SSLContext.getInstance("SSLv3");
				// 初始化
				factory = sslContext.getServerSocketFactory();
			} else {
				factory = ServerSocketFactory.getDefault();
			}
		}
		return factory;
	}

	/**
	 * 创建Socket连接到指定IP、端口，超时时间为timeout（单位：毫秒）。
	 * 超时时间表示调用这个socket关联的InputStream的read()方法阻塞的时间，timeout设为0的话表示一直阻塞
	 * 
	 * @param ip
	 *            主机名或ip，null代表loopback地址
	 * @param port
	 *            端口号
	 * @param timeout
	 *            指定的超时时间，单位毫秒
	 * @return
	 * @throws Exception
	 *             方法中不处理任何异常，所有异常都抛出由调用方处理
	 */
	public static Socket createSocket(String ip, int port, int timeout)
			throws Exception {
		return createSocket(ip, port, timeout, CONNECT_TIMEOUT);
	}
	
	/**
	 * 创建Socket连接到指定IP、端口，超时时间为timeout（单位：毫秒）。
	 * 超时时间表示调用这个socket关联的InputStream的read()方法阻塞的时间，timeout设为0的话表示一直阻塞
	 * 
	 * @param ip
	 *            主机名或ip，null代表loopback地址
	 * @param port
	 *            端口号
	 * @param timeout
	 *            指定的超时时间，单位：毫秒
	 * @param connectTimeOut 单位：毫秒
	 * @return
	 * @throws Exception
	 *             方法中不处理任何异常，所有异常都抛出由调用方处理
	 */
	public static Socket createSocket(String ip, int port, int timeout, int connectTimeOut)
			throws Exception {
		if (needSSL) {
			return SSLSocketFactory.getDefault().createSocket(ip, port);
		} else {
			// Socket socket = new Socket(ip, port);
			Socket socket = new Socket();
			// 绑定本地可用地址和端口
			socket.bind(null);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(timeout);
			socket.connect(new InetSocketAddress(ip, port), connectTimeOut);
			return socket;
		}
	}

	/**
	 * 创建HTTP连接的方法,指定URL,超时时间timeout（单位：毫秒）。
	 * 
	 * @param url
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection createHttpURLConnection(String url,
			int timeout) throws Exception {
		return createHttpURLConnection(url, timeout, CONNECT_TIMEOUT);
	}
	
	/**
	 * 创建HTTP连接的方法,指定URL,超时时间timeout（单位：毫秒）。
	 * 
	 * @param url
	 * @param timeout
	 * @param connectTimeOut 单位：毫秒
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection createHttpURLConnection(String url,
			int timeout, int connectTimeOut) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(timeout);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		return conn;
	}
	
	/**
	 * 创建HTTP连接的方法,指定URL,超时时间timeout（单位：毫秒）。
	 * 
	 * @param url
	 * @param timeout
	 * @param connectTimeOut 单位：毫秒
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection createHttpURLConnection(String url,
			int timeout, int connectTimeOut, String method) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(timeout);
		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		return conn;
	}

	/**
	 * 创建HTTPS连接的方法,指定URL,超时时间timeout（单位：毫秒）。
	 * 
	 * @param url
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static HttpsURLConnection createHttpsURLConnection(String url,
			int timeout) throws Exception {
		return createHttpsURLConnection(url, timeout, HTTPS_CONNECT_TIMEOUT);
	}
	
	/**
	 * 创建HTTPS连接的方法,指定URL,超时时间timeout（单位：毫秒）。
	 * 
	 * @param url
	 * @param timeout
	 * @param connectTimeOut 单位：毫秒
	 * @return
	 * @throws Exception
	 */
	public static HttpsURLConnection createHttpsURLConnection(String url,
			int timeout, int connectTimeOut) throws Exception {
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(timeout);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		return conn;
	}
	
	/**
	 * 创建HTTPS连接的方法,指定URL,超时时间timeout（单位：毫秒）。
	 * 
	 * @param url
	 * @param timeout
	 * @param connectTimeOut 单位：毫秒
	 * @return
	 * @throws Exception
	 */
	public static HttpsURLConnection createHttpsURLConnection(String url,
			int timeout, int connectTimeOut, String method) throws Exception {
		HttpsURLConnection conn = (HttpsURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(connectTimeOut);
		conn.setReadTimeout(timeout);
		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		return conn;
	}

	/**
	 * 从输入流is中获取指定长度的字节
	 * 
	 * @param is
	 *            输入流，需要调用方自行打开、关闭
	 * @param wantLength
	 *            需要读取的长度
	 * @return 字节数组
	 * @throws Exception
	 *             方法内不处理任何异常
	 */
	public static byte[] read(InputStream is, int wantLength) throws Exception {
		byte[] result = new byte[wantLength];

		int readedLength = 0;
		int tmp = -1;
		while (readedLength < wantLength) {
			tmp = is.read();
			if (tmp == -1) // 读完了
				break;

			result[readedLength] = (byte) tmp;
			readedLength++;
		}
		return result;
	}
	
	public static int bytes2int(byte[] b) {
		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	public static byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Socket createLongSocket(String ip, int port) throws Exception {
		if (needSSL) {
			return SSLSocketFactory.getDefault().createSocket(ip, port);
		} else {
			// Socket socket = new Socket(ip, port);
			Socket socket = new Socket();
			// 绑定本地可用地址和端口
			// 绑定本地可用地址和端口
			socket.bind(null);
			socket.setTcpNoDelay(true);
			socket.connect(new InetSocketAddress(ip, port));
			return socket;
		}
	}
	
	/**
	 * 获取远程IP地址
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

	public static void main(String... strings) throws Exception {
		// createLongSocket("127.0.0.1",9999);
	}
}
