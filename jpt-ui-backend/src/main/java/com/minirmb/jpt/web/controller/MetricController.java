package com.minirmb.jpt.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import com.minirmb.jpt.orm.entity.StatisticsEntity;
import com.minirmb.jpt.orm.entity.MetricEntity;
import com.minirmb.jpt.orm.entity.TracerEntity;
import com.minirmb.jpt.orm.services.MetricMongoService;
import com.minirmb.jpt.orm.services.TracerService;
import com.minirmb.jpt.web.vo.KeyValue;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import com.minirmb.jpt.web.vo.MethodStatisticsVO;
import com.minirmb.jpt.web.vo.MetricTreeVO;

import javax.annotation.Resource;

@RestController
@RequestMapping("/metric")
public class MetricController {
	@Resource
	private MetricMongoService metricMongoService;

	@Resource
	private TracerService tracerService;

	@RequestMapping(value = "/findHost")
	@ResponseBody
	public List<KeyValue> findHost() {
		List<String> traceList = tracerService.findHosts();
		Collections.sort(traceList);
		List<KeyValue> result = new ArrayList<>();
		for( String m: traceList ){
			result.add(new KeyValue(m,m));
		}
		return result;
	}

	@RequestMapping(value = "/findTracers")
	@ResponseBody
	public List<TracerEntity> findTracers(String hostName) {
		List<TracerEntity> result = tracerService.findTraces(hostName);
		return result;
	}

	@RequestMapping(value = "/listMetrics")
	public List<MetricTreeVO> listMetrics(@RequestParam(name = "tracerId") String tracerId,
												  @RequestParam(name = "threadId", required = false) Long threadId,
												  @RequestParam(name = "serial", required = false) Long serial) {
		List<MetricEntity> result;
		if (Objects.isNull( threadId ) || Objects.isNull(serial)) {
			result = metricMongoService.findRootSnapshotRow(tracerId);
		} else {
			result = metricMongoService.findSnapshotRowByParent(tracerId, threadId, serial);
		}
		return convertToSnapshotRowTreeVO(result);
	}

	@RequestMapping(value = "/statistics")
	public List<MethodStatisticsVO> statistics(@RequestParam(name = "tracerId") String tracerId) {
		List<StatisticsEntity> aa =  metricMongoService.statistics(tracerId);
		return convertToMethodStatisticsVO(aa);
	}

	private List<MethodStatisticsVO> convertToMethodStatisticsVO(List<StatisticsEntity> msEntities){
		List<MethodStatisticsVO> result = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS");
		for(StatisticsEntity ms : msEntities) {
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

	private List<MetricTreeVO> convertToSnapshotRowTreeVO(List<MetricEntity> metricEntities) {
		List<MetricTreeVO> result = new ArrayList<>();
		for (MetricEntity metricEntity : metricEntities) {
			MetricTreeVO vo = new MetricTreeVO();
			BeanUtils.copyProperties(metricEntity, vo);
			vo.setChildren(new ArrayList<>());
			vo.setState("closed");
			vo.setHierarchy(metricEntity.getHierarchy());
			vo.setQualifiedClassName(metricEntity.getPackageName() + "." + metricEntity.getClassName());
			vo.setQualifiedMethodName(
					metricEntity.getPackageName() + "." + metricEntity.getClassName() + "." + metricEntity.getMethodName());
			vo.setClassAndMethodName(metricEntity.getClassName() + "." + metricEntity.getMethodName());
			result.add(vo);
		}
		return result;
	}

	@RequestMapping(value = "/clearAllData")
	public void clearAllData() {
		metricMongoService.clearAllData();
	}
}
