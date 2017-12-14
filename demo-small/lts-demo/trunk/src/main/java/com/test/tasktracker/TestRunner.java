package com.test.tasktracker;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

public class TestRunner implements JobRunner {

	@Override
	public Result run(JobContext context) throws Throwable {
		try {
			context.getBizLogger().info("开始执行TestRunner...");
			// doSomething
			System.out.println("执行了TestRunner");
			Map<String, String> paramMap = context.getJob().getExtParams();
			System.out.println("参数：" + JSONObject.toJSONString(paramMap));
			context.getBizLogger().info("结束执行TestRunner...");
		} catch(Exception e) {
			e.printStackTrace();
			return new Result(Action.EXECUTE_EXCEPTION, e.toString());
		}
		return new Result(Action.EXECUTE_SUCCESS, "TestRunner执行成功");
	}
}
