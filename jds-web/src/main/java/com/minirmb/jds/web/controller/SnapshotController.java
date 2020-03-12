package com.minirmb.jds.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.minirmb.jds.web.vo.MethodStatisticsVO;
import com.minirmb.jds.web.vo.SnapshotRowTreeVO;
import com.minirmb.jds.web.vo.SnapshotVO;

@RestController
public class SnapshotController {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${outUrl.jds.module.listStatistics}")
	private String  urlForListStatistics;

	@Value("${outUrl.jds.module.listSnapshot}")
	private String  urlForListSnapshot;
	
	@Value("${outUrl.jds.module.listMethodHierarchy}")
	private String  urlForListMethodHierarchy;
   	
	@RequestMapping(value = "/listSnapshot")
	@ResponseBody
	public SnapshotVO[] listSnapshot() {
		return restTemplate.getForObject(urlForListSnapshot, SnapshotVO[].class);
	}
	
	@RequestMapping(value = "/listStatistics")
	public List<MethodStatisticsVO> listStatistics(@RequestParam(name = "snapshotId") String snapshotId) {
		String url = urlForListStatistics + "?snapshotId=" + snapshotId;
		return convertToMethodStatisticsVO(restTemplate.getForObject(url, MethodStatisticsVO[].class));
	}

	private List<MethodStatisticsVO> convertToMethodStatisticsVO(MethodStatisticsVO[] msEntities){
		List<MethodStatisticsVO> result = new ArrayList<MethodStatisticsVO>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMdd HH:mm:ss SSS");
		for(MethodStatisticsVO ms : msEntities) {
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
	public List<SnapshotRowTreeVO> listMethodHierarchy(@RequestParam(name = "snapshotId") String snapshotId,
			@RequestParam(name = "parentId") String parentId) {
		String url = urlForListMethodHierarchy + "?snapshotId=" + snapshotId + "&parentId=" + parentId;
		SnapshotRowTreeVO[] resultInRest = restTemplate.getForObject(url, SnapshotRowTreeVO[].class);
		return convertToSnapshotRowTreeVO(resultInRest);
	}

	private List<SnapshotRowTreeVO> convertToSnapshotRowTreeVO(SnapshotRowTreeVO[] metrics) {
		List<SnapshotRowTreeVO> result = new ArrayList<>();
		for (SnapshotRowTreeVO metric : metrics) {
			SnapshotRowTreeVO vo = new SnapshotRowTreeVO();
			BeanUtils.copyProperties(metric, vo);
			vo.setChildren(new ArrayList<>());
			vo.setState("closed");
			vo.setQualifiedClassName(metric.getPackageName() + "." + metric.getClassName());
			vo.setQualifiedMethodName(
					metric.getPackageName() + "." + metric.getClassName() + "." + metric.getMethodName());
			vo.setClassAndMethodName(metric.getClassName() + "." + metric.getMethodName());
			result.add(vo);
		}
		return result;
	}
}
