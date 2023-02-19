package com.minirmb.jpt.orm.repositorys;

import com.minirmb.jpt.orm.entity.AnalysisLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnalysisLogRepository extends MongoRepository<AnalysisLogEntity,String>{
}
