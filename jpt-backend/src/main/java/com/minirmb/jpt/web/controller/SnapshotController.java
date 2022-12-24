package com.minirmb.jpt.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.minirmb.jpt.orm.entity.MethodStatistics;
import com.minirmb.jpt.orm.entity.Tracer;
import com.minirmb.jpt.orm.entity.Metric;
import com.minirmb.jpt.orm.repositorys.TracerMongoRepository;
import com.minirmb.jpt.orm.services.MetricMongoService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.minirmb.jpt.web.vo.MethodStatisticsVO;
import com.minirmb.jpt.web.vo.MetricTreeVO;

import javax.annotation.Resource;

@RestController
public class SnapshotController {
	@Resource
	private TracerMongoRepository tracerMongoRepository;

	@Resource
	private MetricMongoService metricMongoService;

	@RequestMapping(value = "/listSnapshot")
	@ResponseBody
	public List<Tracer>  listSnapshot() {
		return tracerMongoRepository.findAll();
	}

	@RequestMapping(value = "/listStatistics")
	public List<MethodStatisticsVO> listStatistics(@RequestParam(name = "tracerId") String tracerId) {
		List<MethodStatistics> aa =  metricMongoService.findByTracerId(tracerId);
		return convertToMethodStatisticsVO(aa);
	}

	private List<MethodStatisticsVO> convertToMethodStatisticsVO(List<MethodStatistics> msEntities){
		List<MethodStatisticsVO> result = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMdd HH:mm:ss SSS");
		for(MethodStatistics ms : msEntities) {
			MethodStatisticsVO vo = new MethodStatisticsVO();
			BeanUtils.copyProperties(ms, vo);
			if(null != ms.getFirstInvokeTime()) {
				vo.setFirstInvokeTimeString(sdf.format(ms.getFirstInvokeTime()));
			}
			if( null != ms.getLastInvokeTime()) {
				vo.setLastInvokeTimeString(sdf.format(ms.getLastInvokeTime()));
			}			
			result.add(vo);
		}
		return result;
	}

	@RequestMapping(value = "/listMethodHierarchy")
	public List<MetricTreeVO> listMethodHierarchy(@RequestParam(name = "tracerId") String tracerId,
												  @RequestParam(name = "threadId", required = false) Long threadId,
												  @RequestParam(name = "serial", required = false) Long serial) {
		List<Metric> result;
		if (Objects.isNull( threadId ) || Objects.isNull(serial)) {
			result = metricMongoService.findRootSnapshotRow(tracerId);
		} else {
			result = metricMongoService.findSnapshotRowByParent(tracerId, threadId, serial);
		}
		return convertToSnapshotRowTreeVO(result);
	}

	private List<MetricTreeVO> convertToSnapshotRowTreeVO(List<Metric> metrics) {
		List<MetricTreeVO> result = new ArrayList<>();
		for (Metric metric : metrics) {
			MetricTreeVO vo = new MetricTreeVO();
			BeanUtils.copyProperties(metric, vo);
			vo.setChildren(new ArrayList<>());
			vo.setState("closed");
			vo.setHierarchy(metric.getHierarchy());
			vo.setQualifiedClassName(metric.getPackageName() + "." + metric.getClassName());
			vo.setQualifiedMethodName(
					metric.getPackageName() + "." + metric.getClassName() + "." + metric.getMethodName());
			vo.setClassAndMethodName(metric.getClassName() + "." + metric.getMethodName());
			result.add(vo);
		}
		return result;
	}
}
