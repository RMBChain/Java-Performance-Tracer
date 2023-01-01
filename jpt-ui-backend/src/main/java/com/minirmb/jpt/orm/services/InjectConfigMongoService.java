package com.minirmb.jpt.orm.services;

import com.minirmb.jpt.orm.entity.InjectConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InjectConfigMongoService {

	@Resource
	private MongoTemplate mongoTemplate;

	public InjectConfig getInjectConfig( ) {
		List<InjectConfig> list = mongoTemplate.find( new Query().limit(1), InjectConfig.class);
		return list.isEmpty() ? new InjectConfig(): list.get(0);
	}

	public InjectConfig saveInjectConfig( 	InjectConfig injectConfig) {
		if( null == injectConfig.getId()){
			injectConfig.setId(UUID.randomUUID().toString());
		}
		return mongoTemplate.save( injectConfig );
	}

	public void initOnStartup() {
		List<InjectConfig> list = mongoTemplate.find( new Query().limit(1), InjectConfig.class);
		if( list.isEmpty() ){
			InjectConfig ic = new InjectConfig();
			ic.setExclude("com.otherparty.Colors\n" +
					"com.otherparty.ddd.measure");
			ic.setInclude("com.thirdpart.jpt.test.xxx\n" +
					"com.thirdpart.jpt.test.xxx.tp1\n" +
					"com.thirdpart.jpt.test.xxx.tp2\n" +
					"com.thirdpart.jpt.test.xxx.tp3");
			saveInjectConfig(ic);
		}
	}
}
