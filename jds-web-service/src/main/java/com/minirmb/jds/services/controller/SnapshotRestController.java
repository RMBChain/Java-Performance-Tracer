package com.minirmb.jds.services.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.minirmb.jds.persistence.entity.MethodStatistics;
import com.minirmb.jds.persistence.entity.Snapshot;
import com.minirmb.jds.persistence.entity.SnapshotRow;
import com.minirmb.jds.persistence.repositorys.SnapshotMongoRepository;
import com.minirmb.jds.persistence.services.SnapshotRowMongoService;


@RestController
public class SnapshotRestController {

	@Autowired
	private SnapshotMongoRepository snapshotMongoRepository;
	
	@Autowired
	private SnapshotRowMongoService snapshotRowMongoService;
	
	@RequestMapping(value = "/listSnapshot")
	@ResponseBody
	public List<Snapshot> listSnapshot() {
		return snapshotMongoRepository.findAll();
	}
	
	@RequestMapping(value = "/listStatistics")
	public List<MethodStatistics> listStatistics(@RequestParam(name = "snapshotId") String snapshotId) {
		return snapshotRowMongoService.findBySnapshotId(snapshotId);
	}

	@RequestMapping(value = "/listMethodHierarchy")
	public List<SnapshotRow> listMethodHierarchy(@RequestParam(name = "snapshotId") String snapshotId,
			@RequestParam(name = "parentId") String parentId) {
		List<SnapshotRow> result;
		if (null == parentId || parentId.trim().length() == 0) {
			result = snapshotRowMongoService.findRootSnapshotRow(snapshotId);
		} else {
			result = snapshotRowMongoService.findSnapshotRowByParentId(snapshotId, parentId);
		}
		return result;
	}

}
