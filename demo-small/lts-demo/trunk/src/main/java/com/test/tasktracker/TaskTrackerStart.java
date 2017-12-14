package com.test.tasktracker;

import java.io.Serializable;

import com.github.ltsopensource.tasktracker.TaskTracker;

public class TaskTrackerStart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482397603283818067L;

	public static void main(String[] args) {
		final TaskTracker taskTracker = new TaskTracker();
		// 任务执行类，实现JobRunner 接口
		taskTracker.setJobRunnerClass(TestRunner.class);
		taskTracker.setRegistryAddress("zookeeper://10.22.0.160:2181");
		taskTracker.setNodeGroup("test_trade_TaskTracker"); // 同一个TaskTracker集群这个名字相同
		taskTracker.setClusterName("test_cluster");
		taskTracker.setWorkThreads(64);
		taskTracker.addConfig("job.fail.store", "mapdb");
		taskTracker.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				taskTracker.stop();
			}
		}));
	}
}
