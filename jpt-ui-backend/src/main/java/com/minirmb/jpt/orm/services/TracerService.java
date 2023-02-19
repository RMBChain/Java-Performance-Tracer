package com.minirmb.jpt.orm.services;

import com.minirmb.jpt.orm.entity.AnalysisLogEntity;
import com.minirmb.jpt.orm.entity.MetricEntity;
import com.minirmb.jpt.orm.entity.TracerEntity;
import com.minirmb.jpt.orm.repositorys.TracerMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TracerService {

	@Resource
	private TracerMongoRepository tracerMongoRepository;

	@Resource
	private MongoTemplate mongoTemplate;

	public List<TracerEntity> findAll() {
		List<TracerEntity> result = mongoTemplate.findAll(TracerEntity.class);
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// time to string
		result.stream().forEach( te -> te.setLastUpdateStr( formatter.format(new Date(te.getLastUpdate()))));
		// live
		result.stream().forEach( te -> te.setLive( ( System.currentTimeMillis() - te.getLastUpdate())/1000 < 10));
		return result;
	}

	public void save(TracerEntity tracerEntity) {
		tracerMongoRepository.save(tracerEntity);
	}

	public List<String> findHosts() {
		return mongoTemplate.findDistinct(new Query(), "hostName", TracerEntity.class,String.class);
	}

	public void delete(String tracerId) {
		Query query = Query.query(Criteria.where("tracerId").is(tracerId));
		mongoTemplate.remove( query, TracerEntity.class );
		mongoTemplate.remove( query, MetricEntity.class );
		mongoTemplate.remove( query, AnalysisLogEntity.class );
	}

	public List<TracerEntity> findTraces(String hostName) {
		Query query = new Query();
		if( null != hostName ){
			query.addCriteria(Criteria.where("hostName").is(hostName));
		}
		query.with(Sort.by( Sort.Direction.DESC,"tracerId"));

		List<TracerEntity> result = mongoTemplate.find(query,TracerEntity.class);
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// time to string
		result.stream().forEach( te -> te.setLastUpdateStr( formatter.format(new Date(te.getLastUpdate()))));
		// live
		result.stream().forEach( te -> te.setLive( ( System.currentTimeMillis() - te.getLastUpdate())/1000 < 10));
		return result;
	}
}
