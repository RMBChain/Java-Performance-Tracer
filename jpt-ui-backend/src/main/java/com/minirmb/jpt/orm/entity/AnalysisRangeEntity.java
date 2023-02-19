package com.minirmb.jpt.orm.entity;

import com.minirmb.jpt.common.AnalysisRange;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class AnalysisRangeEntity extends AnalysisRange {
	@Id
	private String id;

}
