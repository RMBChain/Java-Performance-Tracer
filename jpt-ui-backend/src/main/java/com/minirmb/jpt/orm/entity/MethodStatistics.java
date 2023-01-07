package com.minirmb.jpt.orm.entity;

import java.util.Date;

import lombok.Data;

@Data
public class MethodStatistics {
	private String tracerId;
	private String className;
	private String methodName;
	private Long invokedCount;
	private Date firstInvokeTime;
	private Date lastInvokeTime;
	private Long usedTime;
}
