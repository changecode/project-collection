package com.bpm.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.bpm.framework.SystemConst;
import com.bpm.framework.exception.FrameworkRuntimeException;
/**
 * 
 * 类  编  号：
 * 类  名  称：HttpRequest
 * 类  描  述：
 * 完成日期：2015-4-23 下午2:26:05
 * 编码作者：谢正光
 */
public class HttpRequest {
	private static final  Logger log = Logger.getLogger(HttpRequest.class);
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                log.info(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            conn.setUseCaches(false);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    public static String serialParameters(Map<String, String> parameters,
			boolean isEncodeAll) throws UnsupportedEncodingException {
		StringBuffer serialParameters = new StringBuffer();
		Set<String> parameterKey = parameters.keySet();
		Iterator<String> iterator = parameterKey.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = parameters.get(key);
			if (!isEncodeAll) {
				serialParameters.append(key + "="
						+ URLEncoder.encode(StringUtils.toString(value), "UTF-8"));
			} else {
				serialParameters.append(key + "=" + value);
			}
			serialParameters.append("&");
		}
		String strSerialParameters = serialParameters.deleteCharAt(
				serialParameters.length() - 1).toString();
		if (isEncodeAll) {
			strSerialParameters = URLEncoder.encode(strSerialParameters,
					"UTF-8");
		}
		log.info(strSerialParameters);
		return strSerialParameters;
	}
    
    public static String sendHttpsPOST(String url, String data) {
		final StringBuilder result = new StringBuilder();
		try {
			// 设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { myX509TrustManager },
					null);

			// 打开连接
			// 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
			URL requestUrl = new URL(url);
			HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
					.openConnection();

			// 设置套接工厂
			httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

			// 加入数据
			httpsConn.setRequestMethod("POST");
			httpsConn.setDoOutput(true);
			OutputStream out = httpsConn.getOutputStream();

			if (data != null) {
				out.write(data.getBytes(SystemConst.ENCODING_UTF8));
			}
			out.flush();
			out.close();

			// 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream(), SystemConst.ENCODING_UTF8));
			int code = httpsConn.getResponseCode();
			if (HttpsURLConnection.HTTP_OK == code) {
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					result.append(temp);
				}
			}
		} catch (KeyManagementException e) {
			throw new FrameworkRuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new FrameworkRuntimeException(e);
		} catch (MalformedURLException e) {
			throw new FrameworkRuntimeException(e);
		} catch (ProtocolException e) {
			throw new FrameworkRuntimeException(e);
		} catch (IOException e) {
			throw new FrameworkRuntimeException(e);
		}

		return result.toString();
	}

	public static String sendHttpsGET(String url) {
		final StringBuilder result = new StringBuilder();
		try {
			// 设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { myX509TrustManager },
					null);

			// 打开连接
			// 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
			URL requestUrl = new URL(url);
			HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
					.openConnection();

			// 设置套接工厂
			httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

			// 加入数据
			httpsConn.setRequestMethod("GET");

			// 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpsConn.getInputStream(), SystemConst.ENCODING_UTF8));
			int code = httpsConn.getResponseCode();
			if (HttpsURLConnection.HTTP_OK == code) {
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					result.append(temp);
				}
			}
		} catch (KeyManagementException e) {
			throw new FrameworkRuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new FrameworkRuntimeException(e);
		} catch (MalformedURLException e) {
			throw new FrameworkRuntimeException(e);
		} catch (ProtocolException e) {
			throw new FrameworkRuntimeException(e);
		} catch (IOException e) {
			throw new FrameworkRuntimeException(e);
		}

		return result.toString();
	}
	
	private static TrustManager myX509TrustManager = new X509TrustManager() {

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};
}
