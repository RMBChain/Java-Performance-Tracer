package com.minirmb.jpt.orm.repositorys;

import com.minirmb.jpt.orm.entity.TracerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TracerMongoRepository extends MongoRepository<TracerEntity,String>{
}
