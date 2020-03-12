package com.minirmb.jds.persistence.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.minirmb.jds.persistence.entity.MethodStatistics;
import com.minirmb.jds.persistence.entity.SnapshotRow;
import com.minirmb.jds.persistence.repositorys.SnapshotRowMongoRepository;

@Service
public class SnapshotRowMongoService {

	@Autowired
	private SnapshotRowMongoRepository snapshotRowMongoRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	public List<SnapshotRow> findUnMergedRow(String snapshotId) {
		SnapshotRow row = new SnapshotRow();
		row.setSnapshotId(snapshotId);
		row.setMergered(false);
		return snapshotRowMongoRepository.findAll(Example.of(row), Sort.by(Sort.Direction.ASC, "serial"));
	}

	public int findUnMergedRowCount(String snapshotId) {
		return findUnMergedRow(snapshotId).size();
	}
	
	public List<SnapshotRow> findRootSnapshotRow(String snapshotId) {
		SnapshotRow row = new SnapshotRow();
		row.setSnapshotId(snapshotId);
		row.setHierarchy(1l);
		row.setInOrOut("in");
		return snapshotRowMongoRepository.findAll(Example.of(row), Sort.by(Sort.Direction.DESC, "serial"));
	}

	public List<SnapshotRow> findSnapshotRowByParentId(String snapshotId, String parentId) {
		SnapshotRow row = new SnapshotRow();
		row.setSnapshotId(snapshotId);
		row.setParentId(parentId);
		row.setInOrOut("in");
		return snapshotRowMongoRepository.findAll(Example.of(row), Sort.by(Sort.Direction.DESC, "serial"));
	}

	public List<MethodStatistics> findBySnapshotId(String snapshotId) {
		Criteria criteria = Criteria.where("mergered").is(true);		
		criteria = criteria.and("inOrOut").is("in");
		criteria = criteria.and("snapshotId").is(snapshotId);		
		MatchOperation mo = Aggregation.match(criteria);

		GroupOperation gp = Aggregation.group("snapshotId", "packageName", "className", "methodName");
		gp = gp.count().as("invokedCount");
		gp = gp.min("startTime").as("firstInvokeTime");
		gp = gp.max("endTime").as("lastInvokeTime");
		gp = gp.sum("usedTime").as("usedTime");
		
		Aggregation agg = Aggregation.newAggregation(mo, gp, Aggregation.sort(Sort.Direction.DESC, "usedTime"));
		AggregationResults<MethodStatistics> results = mongoTemplate.aggregate(agg, "snapshotRow",
				MethodStatistics.class);

		return results.getMappedResults();
	}

	public void saveAll(List<SnapshotRow> unMergeedRows) {
		snapshotRowMongoRepository.saveAll(unMergeedRows);
	}

	public void save(SnapshotRow inMetric) {
		snapshotRowMongoRepository.save(inMetric);
	}
}
