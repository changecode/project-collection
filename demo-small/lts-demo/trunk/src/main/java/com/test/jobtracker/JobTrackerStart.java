package com.test.jobtracker;

import java.io.Serializable;

import com.github.ltsopensource.jobtracker.JobTracker;

public class JobTrackerStart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482397603283818067L;

	public static void main(String[] args) {
		final JobTracker jobTracker = new JobTracker();
		jobTracker.setClusterName("test_cluster");
		jobTracker.setListenPort(35001);
		jobTracker.setRegistryAddress("zookeeper://10.22.0.160:2181");
		jobTracker.addConfig("job.logger", "mysql");
		jobTracker.addConfig("job.queue", "mysql");
		jobTracker.addConfig("jdbc.url", "jdbc:mysql://10.22.0.160:3306/lts");
		jobTracker.addConfig("jdbc.username", "root");
		jobTracker.addConfig("jdbc.password", "r123456");
		jobTracker.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                jobTracker.stop();
            }
        }));
	}
}
