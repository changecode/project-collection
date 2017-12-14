/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.remoting.caucho;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import com.bpm.framework.console.Application;
import com.bpm.framework.exception.FrameworkRuntimeException;
import com.bpm.framework.utils.StringUtils;

/**
 * Servlet-API-based HTTP request handler that exports the specified service
 * bean as Hessian service endpoint, accessible via a Hessian proxy.
 *
 * <p>
 * <b>Note:</b> Spring also provides an alternative version of this exporter,
 * for Sun's JRE 1.6 HTTP server: {@link SimpleHessianServiceExporter}.
 *
 * <p>
 * Hessian is a slim, binary RPC protocol. For information on Hessian, see the
 * <a href="http://www.caucho.com/hessian">Hessian website</a>. <b>Note: As of
 * Spring 4.0, this exporter requires Hessian 4.0 or above.</b>
 *
 * <p>
 * Hessian services exported with this class can be accessed by any Hessian
 * client, as there isn't any special handling involved.
 *
 * @author Juergen Hoeller
 * @author andyLee  参考客户端HessianProxyFactory进行base64加密
 * @since 13.05.2003
 * @see HessianClientInterceptor
 * @see HessianProxyFactoryBean
 * @see org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter
 * @see org.springframework.remoting.rmi.RmiServiceExporter
 */
public class HessianServiceExporter extends HessianExporter implements HttpRequestHandler {

	/**
	 * Processes the incoming Hessian request and creates a Hessian response.
	 */
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// begin lixx 2016-08-26 11:50:00 增加客户端用户名密码校验，提高安全性
		String clientAuth = request.getHeader("Authorization");
		if (!StringUtils.isNullOrBlank(clientAuth)) {
			String auth = "Basic " + base64(new StringBuilder()
					.append(Application.getInstance().getByKey("hessian.user"))
					.append(":")
					.append(Application.getInstance().getByKey("hessian.password")).toString());
			if (!clientAuth.equals(auth)) {
				throw new FrameworkRuntimeException("client authorization validate failed.");
			}
		}
		// end lixx 2016-08-26 11:50:00 增加客户端用户名密码校验，提高安全性
		
		if (!"POST".equals(request.getMethod())) {
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" },
					"HessianServiceExporter only supports POST requests");
		}

		response.setContentType(CONTENT_TYPE_HESSIAN);
		try {
			invoke(request.getInputStream(), response.getOutputStream());
		} catch (Throwable ex) {
			throw new NestedServletException("Hessian skeleton invocation failed", ex);
		}
	}

	private String base64(String value) {
		long chunk;
		StringBuffer cb = new StringBuffer();

		int i = 0;
		for (i = 0; i + 2 < value.length(); i += 3) {
			chunk = value.charAt(i);
			chunk = (chunk << 8) + value.charAt(i + 1);
			chunk = (chunk << 8) + value.charAt(i + 2);

			cb.append(encode(chunk >> 18));
			cb.append(encode(chunk >> 12));
			cb.append(encode(chunk >> 6));
			cb.append(encode(chunk));
		}

		if (i + 1 < value.length()) {
			chunk = value.charAt(i);
			chunk = (chunk << 8) + value.charAt(i + 1);
			chunk <<= 8;

			cb.append(encode(chunk >> 18));
			cb.append(encode(chunk >> 12));
			cb.append(encode(chunk >> 6));
			cb.append('=');
		} else if (i < value.length()) {
			chunk = value.charAt(i);
			chunk <<= 16;

			cb.append(encode(chunk >> 18));
			cb.append(encode(chunk >> 12));
			cb.append('=');
			cb.append('=');
		}

		return cb.toString();
	}

	public static char encode(long d) {
		d &= 63L;
		if (d < 26L)
			return (char) (int) (d + 65L);
		if (d < 52L)
			return (char) (int) (d + 97L - 26L);
		if (d < 62L)
			return (char) (int) (d + 48L - 52L);
		if (d == 62L)
			return '+';

		return '/';
	}
}
