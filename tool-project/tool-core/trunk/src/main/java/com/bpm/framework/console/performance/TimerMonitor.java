package com.bpm.framework.console.performance;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

/**
 * 
 * 监控性能，主要监控服务层每个方法执行时长
 * 
 * @author andyLee
 * @createDate 2015-09-07 12:50:00
 */
public class TimerMonitor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4168312061454115711L;
	
	protected final Logger log = Logger.getLogger(getClass());

	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Class<?> targetClass = pjp.getTarget().getClass();
		MethodSignature sign = (MethodSignature) pjp.getSignature();
		String methodName = sign.getMethod().getName();
		StopWatch stopWatch = new Log4JStopWatch(targetClass.getName() + "." + methodName);

		Object obj = null;
		try {
			obj = pjp.proceed();
		} catch (Throwable ex) {
			log.error("异常：", ex);
			throw ex;
		}

		stopWatch.stop();

//		String stopInfo = stopWatch.stop();
//		StringBuffer info = new StringBuffer();
//		info.append("Class: ").append(targetClass.getName());
//		info.append(", Method: ").append(methodName);
//		info.append(", ");
//		info.append(stopInfo);
//		log.info(info.toString());
		return obj;
	}
}
