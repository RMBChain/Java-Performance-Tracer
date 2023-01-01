package com.minirmb.jpt.web.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class MetricTreeVO {
	private String id;
	private String tracerId;
	private String bundleId;
	private Long threadId;
	private Integer hierarchy;
	private Long serial;
	private Boolean merged;
	private String parentId;
	private String packageName;
	private String className;
	private String methodName;
	private Date startTime;
	private Date endTime;
	private Long usedTime;
	private String inOrOut;
	private String qualifiedClassName;
	private String qualifiedMethodName;	
	private String classAndMethodName;
	private String state;
	private List<MetricTreeVO> children;
}
