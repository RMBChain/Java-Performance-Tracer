package com.minirmb.jpt.orm.entity;

import com.minirmb.jpt.common.MetricBase;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
public class InjectConfig {
	@Id
	private String id;

	private String exclude;
	private String include;
}
