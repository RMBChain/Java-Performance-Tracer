package com.minirmb.jpt.orm.services;

import java.util.*;

import com.minirmb.jpt.common.TracerFlag;
import com.minirmb.jpt.orm.entity.Tracer;
import com.minirmb.jpt.orm.repositorys.TracerMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.minirmb.jpt.orm.entity.MethodStatistics;
import com.minirmb.jpt.orm.entity.Metric;
import com.minirmb.jpt.orm.repositorys.MetricMongoRepository;

import javax.annotation.Resource;

@Service
@Slf4j
public class MetricMongoService {

	@Resource
	private MetricMongoRepository metricMongoRepository;

	@Resource
	private TracerMongoRepository tracerMongoRepository;

	@Resource
	private MongoTemplate mongoTemplate;

	public List<Metric> findUnMergedOutRow(int count) {
		Criteria criteria = Criteria.where("merged").is(false);
		criteria = criteria.and("inOrOut").is(TracerFlag.MethodOut);
		Query query = new Query();
		query.addCriteria(criteria).limit(count);
		return mongoTemplate.find( query, Metric.class);
	}

	public void setMergedInfo(Metric snapshotRow) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(snapshotRow.getId()));
		Update update = new Update();
		update.set("merged", snapshotRow.getMerged());
		update.set("inTime", snapshotRow.getInTime());
		update.set("outTime", snapshotRow.getOutTime());
		update.set("usedTime", snapshotRow.getUsedTime());
		mongoTemplate.updateFirst(query, update, Metric.class);
	}

	public void delete(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		mongoTemplate.remove(query, Metric.class);
	}

	public List<Metric> findRootSnapshotRow(String tracerId) {
		Metric row = new Metric();
		row.setTracerId(tracerId);
		row.setHierarchy(1);
		row.setInOrOut("in");
		Sort sort =  Sort.by(Sort.Direction.ASC, "serial").by(Sort.Direction.ASC, "threadId");
		return metricMongoRepository.findAll(Example.of(row), sort);
	}

	public List<Metric> findSnapshotRowByParent(String tracerId, Long threadId, Long serial) {
		Metric row = new Metric();
		row.setTracerId(tracerId);
		row.setThreadId(threadId);
		row.setParentId(serial);
		row.setInOrOut("in");
		Sort sort =  Sort.by(Sort.Direction.ASC, "serial").by(Sort.Direction.ASC, "threadId");
		return metricMongoRepository.findAll(Example.of(row), sort);
	}

	public Metric findInByOut(Metric outRow) {
		Metric row = new Metric();
		row.setTracerId(outRow.getTracerId());
		row.setThreadId(outRow.getThreadId());
		row.setMethodId(outRow.getMethodId());
		row.setInOrOut(TracerFlag.MethodIn);

		Query query = new Query(Criteria.byExample(row));
		return mongoTemplate.findOne( query, Metric.class);
	}

	public List<MethodStatistics> findByTracerId(String tracerId) {
		Criteria criteria = Criteria.where("merged").is(true);
		criteria = criteria.and("inOrOut").is("in");
		criteria = criteria.and("tracerId").is(tracerId);
		MatchOperation mo = Aggregation.match(criteria);

		GroupOperation gp = Aggregation.group("tracerId", "packageName", "className", "methodName");
		gp = gp.count().as("invokedCount");
		gp = gp.min("inTime").as("firstInvokeTime");
		gp = gp.max("outTime").as("lastInvokeTime");
		gp = gp.sum("usedTime").as("usedTime");
		
		Aggregation agg = Aggregation.newAggregation(mo, gp, Aggregation.sort(Sort.Direction.DESC, "usedTime"));
		AggregationResults<MethodStatistics> results = mongoTemplate.aggregate(agg, "snapshotRow",
				MethodStatistics.class);

		return results.getMappedResults();
	}

	@Async
	public void  saveAll(List<Metric> snapshotRows) {
		metricMongoRepository.saveAll(snapshotRows);
	}

	public void save(Metric snapshotRow) {
		metricMongoRepository.save(snapshotRow);
	}

	@Async
	public void saveToDB(List<Metric> metrics)  {
		saveAll(metrics);

		Set<String> tracerIds = new HashSet<>();
		metrics.forEach( m ->{
			tracerIds.add(m.getTracerId());
		});
		List<Tracer> snapshots = new ArrayList<>();
		for (String ti : tracerIds) {
			Tracer tracer = new Tracer();
			tracer.setId(ti);
			tracer.setDescription(ti);
			tracer.setUploadedTime(new Date());
			snapshots.add(tracer);
		}
		tracerMongoRepository.saveAll(snapshots);
	}
}
