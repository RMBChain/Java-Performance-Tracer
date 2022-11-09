package com.minirmb.jps.persistence.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minirmb.jps.persistence.entity.Snapshot;

public interface SnapshotMongoRepository extends MongoRepository<Snapshot,String>{}
