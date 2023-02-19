package com.minirmb.jpt.orm.repositorys;

import com.minirmb.jpt.orm.entity.AnalysisRangeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InjectConfigMongoRepository extends MongoRepository<AnalysisRangeEntity,String>{
}
