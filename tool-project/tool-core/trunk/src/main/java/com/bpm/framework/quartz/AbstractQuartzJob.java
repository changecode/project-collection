package com.bpm.framework.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpm.framework.exception.FrameworkRuntimeException;

/**
 * 
 * 抽象一层quartzJob
 * 
 * @author andyLee
 *
 */
public abstract class AbstractQuartzJob implements QuartzJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1588211439173383510L;
	
	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected abstract void task(JobExecutionContext context);
	
	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("开始执行计划任务...");
			task(context);
			log.info("结束执行计划任务...");
		} catch(Exception e) {
			throw new FrameworkRuntimeException("计划任务执行失败：", e);
		}
	}
}
