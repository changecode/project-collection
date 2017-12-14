package com.test.tasktracker;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.ltsopensource.core.domain.Action;
import com.github.ltsopensource.tasktracker.Result;
import com.github.ltsopensource.tasktracker.runner.JobContext;
import com.github.ltsopensource.tasktracker.runner.JobRunner;

public class TestRunner2 implements JobRunner {

	@Override
	public Result run(JobContext context) throws Throwable {
		try {
			context.getBizLogger().info("开始执行TestRunner2...");
			// doSomething
			System.out.println("执行了TestRunner2");
			Map<String, String> paramMap = context.getJob().getExtParams();
			System.out.println("参数2：" + JSONObject.toJSONString(paramMap));
			context.getBizLogger().info("结束执行TestRunner2...");
		} catch(Exception e) {
			e.printStackTrace();
			return new Result(Action.EXECUTE_EXCEPTION, e.toString());
		}
		return new Result(Action.EXECUTE_SUCCESS, "TestRunner2执行成功");
	}
}
