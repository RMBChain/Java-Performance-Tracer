package com.minirmb.jpt.web.controller;

import com.minirmb.jpt.orm.entity.*;
import com.minirmb.jpt.orm.repositorys.AnalysisLogRepository;
import com.minirmb.jpt.orm.repositorys.TracerMongoRepository;
import com.minirmb.jpt.orm.services.AnalysisLogService;
import com.minirmb.jpt.orm.services.MetricMongoService;
import com.minirmb.jpt.orm.services.TracerService;
import com.minirmb.jpt.web.vo.KeyValue;
import com.minirmb.jpt.web.vo.MethodStatisticsVO;
import com.minirmb.jpt.web.vo.MetricTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/management")
public class ManagementController {
	@Resource
	private MetricMongoService metricMongoService;

	@Resource
	private TracerService tracerService;

	@Resource
	private TracerMongoRepository tracerMongoRepository;

	@Resource
	private AnalysisLogService analysisLogService;

	@RequestMapping(value = "/saveTracer")
	@ResponseBody
	public void saveTracer(@RequestBody TracerEntity tracerEntity) {
		TracerEntity te = tracerMongoRepository.findById(tracerEntity.getTracerId()).get();
		te.setTracerId(tracerEntity.getTracerId());
		te.setTracerName(tracerEntity.getTracerName());
		tracerService.save( te );
	}

	@RequestMapping(value = "/deleteTracer")
	public void deleteTracer(@RequestParam(name = "tracerId") String tracerId) {
		tracerService.delete(tracerId);
	}

	@RequestMapping(value = "/findLogs")
	@ResponseBody
	public List<AnalysisLogEntity> findLogs(String tracerId) {
		List<AnalysisLogEntity> result = analysisLogService.find( tracerId );
		return result;
	}
}
