package com.test.runnerfactory;

import com.github.ltsopensource.tasktracker.runner.JobRunner;
import com.github.ltsopensource.tasktracker.runner.RunnerFactory;
import com.test.tasktracker.TestRunner;

public class TestRunnerFactory implements RunnerFactory {

	@Override
	public JobRunner newRunner() {
		return new TestRunner();
	}
}
