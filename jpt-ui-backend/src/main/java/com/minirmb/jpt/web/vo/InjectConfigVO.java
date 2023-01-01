package com.minirmb.jpt.web.vo;

import lombok.Data;

import java.util.Date;

@Data
public class InjectConfigVO {
	private String id;
	private String description;
	private Date uploadedTime;
}
