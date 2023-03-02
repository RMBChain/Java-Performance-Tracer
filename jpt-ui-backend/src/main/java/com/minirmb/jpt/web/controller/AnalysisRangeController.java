package com.minirmb.jpt.web.controller;

import com.minirmb.jpt.orm.entity.AnalysisRangeEntity;
import com.minirmb.jpt.orm.services.AnalysisRangeMongoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/analysisRange")
public class AnalysisRangeController {

	@Resource
	private AnalysisRangeMongoService analysisRangeMongoService;

	@RequestMapping(value = "/getAnalysisRange")
	public AnalysisRangeEntity getAnalysisRange() {
		AnalysisRangeEntity analysisRangeEntity = analysisRangeMongoService.getAnalysisRange();
		return analysisRangeEntity;
	}

	@PostMapping(value = "/saveAnalysisRange")
	@ResponseBody
	public AnalysisRangeEntity saveAnalysisRange(@RequestBody AnalysisRangeEntity ic) {
		return analysisRangeMongoService.saveAnalysisRange( ic );
	}
}
