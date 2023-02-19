package com.minirmb.jpt.orm.services;

import com.google.common.base.Strings;
import com.minirmb.jpt.orm.entity.AnalysisLogEntity;
import com.minirmb.jpt.orm.entity.MetricEntity;
import com.minirmb.jpt.orm.repositorys.AnalysisLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AnalysisLogService {

	@Resource
	private AnalysisLogRepository analysisLogRepository;

	@Resource
	private MongoTemplate mongoTemplate;

	public void save(AnalysisLogEntity tracer) {
		analysisLogRepository.save(tracer);
	}

	public List<AnalysisLogEntity> find(String tracerId) {
		List<AnalysisLogEntity> result = null;
		AnalysisLogEntity row = new AnalysisLogEntity();
		if(!Strings.isNullOrEmpty(tracerId)){
			row.setTracerId(tracerId);
		}
		Sort sort =  Sort.by(Sort.Direction.ASC, "timestamp");
		result = analysisLogRepository.findAll(Example.of(row), sort);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS");
		result.stream().forEach( item -> item.setTimestampStr(sdf.format(new Date(item.getTimestamp()))));
		return result;
	}
}
