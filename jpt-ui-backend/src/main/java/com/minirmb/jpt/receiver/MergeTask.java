package com.minirmb.jpt.receiver;

import com.minirmb.jpt.orm.entity.Metric;
import com.minirmb.jpt.orm.services.MetricMongoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 由于Mongo数据写入再读取时，中间有延时，故使用定时任务，保证所有数据都被处理。
 *
 *
 */
@Configuration
@EnableScheduling
@Component
@Slf4j
public class MergeTask {

	@Resource
	private MetricMongoService metricMongoService;

	private static AtomicInteger count = new AtomicInteger(0);

	@Scheduled(cron = "0/5 * * * * ?")
	public void merge() {
		List<Metric> unMergedOutRow = metricMongoService.findUnMergedOutRow(100);
		while(!unMergedOutRow.isEmpty()) {
			count.addAndGet(unMergedOutRow.size());
			log.info("Found unmerged row count : " + unMergedOutRow.size() + ", merged count : " + count);

			unMergedOutRow.parallelStream().forEach(row -> {
				merge(row);
			});
			unMergedOutRow = metricMongoService.findUnMergedOutRow(100);
		}
		log.info("Merged count : " + count.get());
	}

	private void merge(Metric outRow) {
		Metric inRow = metricMongoService.findInByOut(outRow);

		if (null != inRow ) {
			// merger same row
			inRow.setOutTime(outRow.getOutTime());
			inRow.setUsedTime(outRow.getOutTime() - inRow.getInTime());
			inRow.setMerged(true);

			metricMongoService.setMergedInfo( inRow );
			metricMongoService.delete( outRow.getId() );
		}
	}
}
