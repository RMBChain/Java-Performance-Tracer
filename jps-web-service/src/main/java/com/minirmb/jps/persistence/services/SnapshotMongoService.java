package com.minirmb.jps.persistence.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minirmb.jps.persistence.entity.Snapshot;
import com.minirmb.jps.persistence.repositorys.SnapshotMongoRepository;

@Service
public class SnapshotMongoService {

	@Autowired
	private SnapshotMongoRepository snapshotMongoRepository;

	public void save(Snapshot snapshot) {
		snapshotMongoRepository.save(snapshot);
	}

	public Optional<Snapshot> findById(String id) {
		return snapshotMongoRepository.findById(id);
	}	
}
