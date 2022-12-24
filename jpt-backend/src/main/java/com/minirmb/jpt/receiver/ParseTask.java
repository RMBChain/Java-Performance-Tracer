package com.minirmb.jpt.receiver;

import com.minirmb.jpt.common.TracerFlag;
import com.minirmb.jpt.orm.entity.Metric;
import com.minirmb.jpt.orm.services.MetricMongoService;
import com.minirmb.jpt.web.event.ReceivedDataEvent;
import lombok.extern.slf4j.Slf4j;
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

	private static AtomicInteger count = new AtomicInteger(0);

	@Async
	@EventListener
	public void onReceivedData(ReceivedDataEvent event){
		List<Metric> metrics = Collections.synchronizedList( new ArrayList<>());
		String[] rowStrings = new String((byte[]) event.getSource()).split(TracerFlag.LineBreaker);
		Stream.of(rowStrings).parallel().forEach( rs -> {
			if (rs.startsWith(TracerFlag.MeasureDataPrefix)) {
				Metric row = Metric.parse( rs );
				metrics.add(row);
			}
		});
		count.addAndGet(metrics.size());
		metricMongoService.saveToDB( metrics );
		log.info("Parsed count : " + count.get());
	}
}
