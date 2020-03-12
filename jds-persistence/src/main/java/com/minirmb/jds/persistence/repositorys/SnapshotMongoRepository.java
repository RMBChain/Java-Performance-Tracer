package com.minirmb.jds.persistence.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minirmb.jds.persistence.entity.Snapshot;

public interface SnapshotMongoRepository extends MongoRepository<Snapshot,String>{}
