package com.bpm.framework.quartz;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.bpm.framework.task.SchedulePropCache;
import com.bpm.framework.utils.CollectionUtils;

/**
 * 
 * 定时任务管理器，提供手工动态添加定时任务
 * 
 * @author andyLee
 * @createDate 2016-08-26 16:16:00
 */
public final class QuartzManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6210940420199662556L;
	
	private static final Logger log = LoggerFactory.getLogger(QuartzManager.class);

	private static SchedulerFactory schedulerFactory = null;

	private static String DEFAULT_JOB_GROUP_NAME = "DEFAULT_JOB_GROUP_NAME";

	private static String DEFAULT_TRIGGER_GROUP_NAME = "DEFAULT_TRIGGER_GROUP_NAME";
	
	static {
		QuartzManagerInit.init();
	}
	
	private final static class QuartzManagerInit {
		
		private static synchronized void init() {
			if(null != schedulerFactory) return;
			Assert.isTrue(validate(), "schedule.run.target.host configure in schedule.properties is false.");
			schedulerFactory = new StdSchedulerFactory();
		}
		
		/**
		 * 
		 * 正常返回则表示验证通过，抛出异常表示验证不通过
		 * 
		 */
		private final static boolean validate() {
			try {
				// 获取当前机器ip
				InetAddress localhost = InetAddress.getLocalHost();
				InetAddress[] addrs = InetAddress.getAllByName(localhost.getHostName());
				// 计划任务允许运营ip
				String targetHost = SchedulePropCache.getInstance().getByKey("schedule.run.target.host");
				Assert.hasText(targetHost, "schedule.run.target.host must be configure in schedule.properties.");
				// 如果配置为本机运行，则校验通过
				if(targetHost.equalsIgnoreCase("127.0.0.1") || targetHost.equalsIgnoreCase("localhost")) {
					return true;
				}
				for(InetAddress addr : addrs) {
					// 验证通过则直接返回
					if(targetHost.equalsIgnoreCase(addr.getHostAddress())) {
						return true;
					}
				}
				// 验证不通过抛出异常
				log.warn("configure schedule.run.target.host not this host. schedule.run.target.host is {}", targetHost);
				return false;
			} catch(Exception e) {
				throw new RuntimeException("validate host exception：", e);
			}
		}
	}
	
	/**
	 * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名           新增完成后，不会启动，需要手工启动
	 * 
	 * @param jobName
	 *            任务名
	 * @param cls
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 */
	public static Scheduler addJob(String jobName, Class<? extends QuartzJob> jobClazz, String time,Map<String,Object> paramMap) {
		return addJob(jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME, jobClazz, time,paramMap);
	}

	/**
	 * @Description: 添加一个定时任务         新增完成后，不会启动，需要手工启动
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 */
	public static Scheduler addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Class<? extends QuartzJob> jobClazz, String time,Map<String,Object> paramMap) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobName, jobGroupName).build();
			
			if(CollectionUtils.isNotEmpty(paramMap)){
				jobDetail.getJobDataMap().putAll(paramMap);
			}
			
			// 触发器
			CronTrigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerName, triggerGroupName)
					.withSchedule(CronScheduleBuilder.cronSchedule(time))// 触发器时间设定
					.build();
			sched.scheduleJob(jobDetail, trigger);
			return sched;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 */
	public static Scheduler modifyJobTime(String jobName, String time) {
		return modifyJobTime(jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME, time);
	}

	/**
	 * @Description: 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 */
	@SuppressWarnings("unchecked")
	public static Scheduler modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String time) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(new TriggerKey(jobName, triggerGroupName));
			if (trigger == null) {
				return null;
			}
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time)) {
				JobDetail jobDetail = sched.getJobDetail(new JobKey(jobName, jobGroupName));
				Class<QuartzJob> objJobClass = ((Class<QuartzJob>) jobDetail.getJobClass());
				removeJob(jobName);
				return addJob(jobName, objJobClass, time,null);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 */
	public static void removeJob(String jobName) {
		removeJob(jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME);
	}

	/**
	 * @Description: 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));// 停止触发器
			sched.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));// 移除触发器
			sched.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务
			// 如果没有停止，则删除计划任务后停止
			if(!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:启动所有定时任务
	 */
	public static void startJobs() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @Description:关闭所有定时任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = schedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		QuartzManager.addJob("jobName", null, "*", null);
	}
}
