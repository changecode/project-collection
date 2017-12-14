package com.grx.test.quartz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.impl.StdScheduler;

import com.bpm.framework.quartz.QuartzManager;

public class Test implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4029116849500136888L;

	public static void main(String[] args) throws Exception {
		String job_name = "动态任务调度";
		System.out.println("【系统启动】开始(每1秒输出一次)...张三");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("111", "xx");
		Scheduler schedule = QuartzManager.addJob(job_name, TestQuartzJob.class, "0/1 * * * * ?",paramMap);
		
		StdScheduler c = null;
		schedule.start();
	}
}
