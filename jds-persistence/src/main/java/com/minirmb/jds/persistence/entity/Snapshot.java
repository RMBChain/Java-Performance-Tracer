package com.minirmb.jds.persistence.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Snapshot {
	private String id;
	private String description;
	private Date uploadedTime;
}
