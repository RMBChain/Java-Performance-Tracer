package com.minirmb.jpt.receiver;

import com.minirmb.jpt.common.AnalysisLog;
import com.minirmb.jpt.common.HeartBeat;
import com.minirmb.jpt.common.TracerFlag;
import com.minirmb.jpt.orm.entity.AnalysisLogEntity;
import com.minirmb.jpt.orm.entity.MetricEntity;
import com.minirmb.jpt.orm.entity.TracerEntity;
import com.minirmb.jpt.orm.services.AnalysisLogService;
import com.minirmb.jpt.orm.services.MetricMongoService;
import com.minirmb.jpt.orm.services.TracerService;
import com.minirmb.jpt.web.event.ReceivedDataEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Configuration
@EnableScheduling
@Component
@Slf4j
public class ParseTask {

	@Resource
	private MetricMongoService metricMongoService;

	@Resource
	private TracerService tracerService;

	@Resource
	private AnalysisLogService analysisLogService;

	private static AtomicInteger count = new AtomicInteger(0);

	@Async
	@EventListener
	public void onReceivedData(ReceivedDataEvent event){
		List<MetricEntity> metricEntities = Collections.synchronizedList( new ArrayList<>());
		String[] rowStrings = ((String) event.getSource()).split(TracerFlag.LineBreaker);
		Stream.of(rowStrings).parallel().forEach( rs -> {
			if (rs.startsWith(TracerFlag.MeasureDataPrefix)) {
				MetricEntity row = MetricEntity.parse( rs );
				metricEntities.add(row);
			}else if (rs.startsWith(TracerFlag.HeartBeat)) {
				HeartBeat heartBeat = HeartBeat.parse( rs );

				TracerEntity tracerEntity = new TracerEntity();
				tracerEntity.setTracerId( heartBeat.getTracerId() );
				tracerEntity.setLastUpdate( System.currentTimeMillis() );
				tracerEntity.setHostName( heartBeat.getHostName() );
				tracerEntity.setTracerName( tracerEntity.getTracerId() );
				tracerEntity.setIp( heartBeat.getIp() );
				tracerService.save(tracerEntity);
			}else if (rs.startsWith(TracerFlag.AnalysisLog)) {
				AnalysisLog al = AnalysisLog.parse( rs );

				AnalysisLogEntity analysisLog = new AnalysisLogEntity();
				BeanUtils.copyProperties(al, analysisLog);
				analysisLogService.save( analysisLog );
			}else{
				log.info("Unrecognized row : " + rs );
			}
		});
		count.addAndGet(metricEntities.size());
		metricMongoService.saveAll(metricEntities);
		log.info("Parsed count : " + count.get());
	}
}
