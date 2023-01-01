package com.minirmb.jpt.orm.repositorys;

import com.minirmb.jpt.orm.entity.InjectConfig;
import com.minirmb.jpt.orm.entity.Metric;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InjectConfigMongoRepository extends MongoRepository<InjectConfig,String>{
}
