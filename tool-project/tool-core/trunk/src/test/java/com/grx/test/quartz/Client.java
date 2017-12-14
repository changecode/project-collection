package com.grx.test.quartz;

import com.bpm.framework.annotation.OperationType;

public class Client {

	public static void main(String[] args) {
		System.out.println(OperationType.getName(OperationType.Add.name()));
	}
}
