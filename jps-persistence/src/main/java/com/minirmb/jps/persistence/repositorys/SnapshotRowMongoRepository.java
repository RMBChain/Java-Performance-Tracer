package com.minirmb.jps.persistence.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minirmb.jps.persistence.entity.SnapshotRow;

public interface SnapshotRowMongoRepository extends MongoRepository<SnapshotRow,Long>{	
}
