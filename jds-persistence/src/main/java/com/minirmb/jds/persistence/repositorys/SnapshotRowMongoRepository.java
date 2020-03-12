package com.minirmb.jds.persistence.repositorys;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.minirmb.jds.persistence.entity.SnapshotRow;

public interface SnapshotRowMongoRepository extends MongoRepository<SnapshotRow,Long>{	
}
