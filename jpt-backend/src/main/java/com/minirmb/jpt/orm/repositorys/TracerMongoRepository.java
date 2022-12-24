package com.minirmb.jpt.orm.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minirmb.jpt.orm.entity.Tracer;

public interface TracerMongoRepository extends MongoRepository<Tracer,String>{}
