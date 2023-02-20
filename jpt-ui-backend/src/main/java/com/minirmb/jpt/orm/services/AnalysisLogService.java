package com.minirmb.jpt.orm.services;

import com.google.common.base.Strings;
import com.minirmb.jpt.orm.entity.AnalysisLogEntity;
import com.minirmb.jpt.orm.repositorys.AnalysisLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
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

	public void save(AnalysisLogEntity tracer) {
		analysisLogRepository.save(tracer);
	}

	public List<AnalysisLogEntity> find(String tracerId) {
		AnalysisLogEntity row = new AnalysisLogEntity();
		if(!Strings.isNullOrEmpty(tracerId)){
			row.setTracerId(tracerId);
		}
		Sort sort =  Sort.by(Sort.Direction.ASC, "timestamp");
		List<AnalysisLogEntity>  result = analysisLogRepository.findAll(Example.of(row), sort);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS");
		result.forEach( item -> item.setTimestampStr(sdf.format(new Date(item.getTimestamp()))));
		return result;
	}
}
