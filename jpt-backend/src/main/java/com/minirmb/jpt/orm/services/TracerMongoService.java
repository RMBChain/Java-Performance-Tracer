package com.minirmb.jpt.orm.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.minirmb.jpt.orm.entity.Tracer;
import com.minirmb.jpt.orm.repositorys.TracerMongoRepository;

import javax.annotation.Resource;

@Service
public class TracerMongoService {

	@Resource
	private TracerMongoRepository tracerMongoRepository;

	public void save(Tracer snapshot) {
		tracerMongoRepository.save(snapshot);
	}

	public Optional<Tracer> findById(String id) {
		return tracerMongoRepository.findById(id);
	}	
}
