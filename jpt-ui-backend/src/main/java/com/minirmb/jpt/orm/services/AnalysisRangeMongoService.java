package com.minirmb.jpt.orm.services;

import com.minirmb.jpt.common.AnalysisRangeItem;
import com.minirmb.jpt.orm.entity.AnalysisRangeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AnalysisRangeMongoService {

	@Resource
	private MongoTemplate mongoTemplate;

	public AnalysisRangeEntity getAnalysisRange( ) {
		List<AnalysisRangeEntity> list = mongoTemplate.find( new Query().limit(1), AnalysisRangeEntity.class);
		return list.isEmpty() ? new AnalysisRangeEntity(): list.get(0);
	}

	public AnalysisRangeEntity saveAnalysisRange(AnalysisRangeEntity analysisRangeEntity) {
		if( null == analysisRangeEntity.getId()){
			analysisRangeEntity.setId(UUID.randomUUID().toString());
		}
		return mongoTemplate.save(analysisRangeEntity);
	}

	public void initOnStartup() {
		List<AnalysisRangeEntity> list = mongoTemplate.find( new Query().limit(1), AnalysisRangeEntity.class);
		if( list.isEmpty() ){
			AnalysisRangeEntity ic = new AnalysisRangeEntity();

			List<AnalysisRangeItem> include = new ArrayList<>();
			include.add(new AnalysisRangeItem("com.thirdpart.jpt.test.*","*","*",true));
			include.add(new AnalysisRangeItem("com.thirdpart.jpt.test.xxx.tp1","*AAA3","main",true));
			include.add(new AnalysisRangeItem("com.thirdpart.jpt.test.xxx.tp2","AA*","*main*",true));
			include.add(new AnalysisRangeItem("com.thirdpart.jpt.test.xxx.tp3","*A*","*",true));
			ic.setInclude(include);

			saveAnalysisRange(ic);
		}
	}
}
