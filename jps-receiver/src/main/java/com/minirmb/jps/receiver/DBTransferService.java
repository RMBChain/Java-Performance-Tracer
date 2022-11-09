package com.minirmb.jps.receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.minirmb.jps.common.SnapshotFlag;
import com.minirmb.jps.persistence.entity.Snapshot;
import com.minirmb.jps.persistence.entity.SnapshotRow;
import com.minirmb.jps.persistence.repositorys.SnapshotMongoRepository;
import com.minirmb.jps.persistence.repositorys.SnapshotRowMongoRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DBTransferService {

	@Autowired
	private MergeSnapshotRowService mergeService;

	@Autowired
	private SnapshotMongoRepository snapshotMongoRepository;

	@Autowired
	private SnapshotRowMongoRepository snapshotRowMongoRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate; // 使用RabbitTemplate,这提供了接收/发送等等方法

	@Async
	public Set<String> transfer(String data) throws IOException {
		String rowString = null;
		List<SnapshotRow> snapshotRows = new ArrayList<>();
		Set<String> snapshotIdSet = new HashSet<>();
		try (StringReader sr = new StringReader(data); BufferedReader br = new BufferedReader(sr);) {
			while ((rowString = br.readLine()) != null) {
				if (rowString.startsWith(SnapshotFlag.PrefixForBaseData)) {
					if (log.isDebugEnabled()) {
						log.debug(rowString);
					}
					SnapshotRow row = SnapshotRowParser.ParseToSnapshotRow(rowString);
					snapshotRows.add(row);
					snapshotIdSet.add(row.getSnapshotId());
				}
			}
			snapshotRowMongoRepository.saveAll(snapshotRows);
		}

		List<Snapshot> snapshots = new ArrayList<>();
		for (String snapshotId : snapshotIdSet) {
			Snapshot ss = new Snapshot();
			ss.setId(snapshotId);
			ss.setDescription(snapshotId);
			ss.setUploadedTime(new Date());
			snapshots.add(ss);
		}
		snapshotMongoRepository.saveAll(snapshots);
		for (String snapshotId : snapshotIdSet) {
			mergeService.merge(snapshotId);
		}

		// send message to rabbit mq
		String messageId = String.valueOf(UUID.randomUUID());
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		Map<String, Object> map = new HashMap<>();
		map.put("messageId", messageId);
		map.put("messageType", "SnapshotRowListUpdated");
		map.put("snapshotIdSet", snapshotIdSet);
		map.put("createTime", createTime);
		rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);

		return snapshotIdSet;
	}
}
