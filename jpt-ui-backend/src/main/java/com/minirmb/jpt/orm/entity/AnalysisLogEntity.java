package com.minirmb.jpt.orm.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class AnalysisLogEntity {

	private String tracerId;

	private String className;

	private String methodName;

	private String methodDesc;

	private String signature;

	private Long timestamp;

	private String timestampStr;
}
