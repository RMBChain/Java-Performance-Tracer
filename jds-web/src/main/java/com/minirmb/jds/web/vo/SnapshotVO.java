package com.minirmb.jds.web.vo;

import java.util.Date;
import lombok.Data;

@Data
public class SnapshotVO {
	private String id;
	private String description;
	private Date uploadedTime;
}
