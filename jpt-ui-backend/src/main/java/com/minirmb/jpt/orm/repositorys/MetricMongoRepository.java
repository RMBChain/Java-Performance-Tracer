package com.minirmb.jpt.orm.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minirmb.jpt.orm.entity.MetricEntity;

public interface MetricMongoRepository extends MongoRepository<MetricEntity,String>{
}
