package com.minirmb.jpt.orm.entity;

import com.minirmb.jpt.common.MetricBase;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="TracerEntity")
@Data
public class TracerEntity {
	@Id
	private String tracerId;

	private Long lastUpdate;

	private String lastUpdateStr;

	private String hostName;

	private String tracerName;

	private String ip;

	private boolean live;

}
