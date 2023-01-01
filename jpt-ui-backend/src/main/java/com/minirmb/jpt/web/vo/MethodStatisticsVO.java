package com.minirmb.jpt.web.vo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MethodStatisticsVO {

	private String tracerId;
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
