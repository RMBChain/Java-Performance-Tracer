package com.minirmb.jps.web.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class SnapshotRowTreeVO {
	private String id;
	private String snapshotId;
	private Long threadId;
	private Long hierarchy;
	private Long serial;
	private Boolean mergered;
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
	private List<SnapshotRowTreeVO> children;
}
