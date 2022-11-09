package com.minirmb.jps.persistence.entity;

import java.util.Date;
import lombok.Data;

@Data
public class SnapshotRow {
	private String id;
	private String snapshotId;
	private Long threadId;
	private Long hierarchy;
	private Long serial;
	private Boolean merged;
	private String packageName;
	private String className;
	private String methodName;
	private Date startTime;
	private Date endTime;
	private Long usedTime;
	private String inOrOut;
	private Long methodId;
	private Long parentId;//parent serial
}
