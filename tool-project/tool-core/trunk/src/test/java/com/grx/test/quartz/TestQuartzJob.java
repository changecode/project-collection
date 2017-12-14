package com.grx.test.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.bpm.framework.quartz.AbstractQuartzJob;

public class TestQuartzJob extends AbstractQuartzJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2529954180149184670L;

	@Override
	protected void task(JobExecutionContext context) {
		
		JobDataMap map = context.getJobDetail().getJobDataMap();
		
		System.out.println("--------------");
	}
}
