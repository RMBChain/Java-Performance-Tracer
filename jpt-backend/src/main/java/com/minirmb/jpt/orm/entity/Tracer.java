package com.minirmb.jpt.orm.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Tracer {
	private String id;
	private String description;
	private Date uploadedTime;
}
