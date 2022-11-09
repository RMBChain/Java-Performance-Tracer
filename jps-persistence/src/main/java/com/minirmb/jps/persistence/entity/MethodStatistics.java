package com.minirmb.jps.persistence.entity;

import java.util.Date;

import lombok.Data;

@Data
public class MethodStatistics {
	private String snapshotId;
	private String packageName;
	private String className;
	private String methodName;
	private Long invokedCount;
	private Date firstInvokeTime;
	private Date lastInvokeTime;
	private Long usedTime;
}
