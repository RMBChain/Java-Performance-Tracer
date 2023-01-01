package com.minirmb.jpt.web.vo;

import java.util.Date;
import lombok.Data;

@Data
public class TracerVO {
	private String id;
	private String description;
	private Date uploadedTime;
}
