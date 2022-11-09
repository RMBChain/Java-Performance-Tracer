package com.minirmb.jps.web.vo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MethodStatisticsVO {

	private String snapshotId;
	private String packageName;
	private String className;
	private String methodName;
	private Long invokedCount;
	private Date firstInvokeTime;
	private String firstInvokeTimeString;
	private Date lastInvokeTime;
	private String lastInvokeTimeString;
	private Long usedTime;

}
