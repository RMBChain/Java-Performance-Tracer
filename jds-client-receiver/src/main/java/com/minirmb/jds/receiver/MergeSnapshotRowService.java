package com.minirmb.jds.receiver;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.persistence.entity.SnapshotRow;
import com.minirmb.jds.persistence.services.SnapshotRowMongoService;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableAsync
@Slf4j
public class MergeSnapshotRowService {

	@Autowired
	private SnapshotRowMongoService snapshotRowMongoService;

	@Async
	public void merge(String snapshotId) {
		List<SnapshotRow> unMergeedRows = snapshotRowMongoService.findUnMergedRow(snapshotId);
		for (int i = 0; i < unMergeedRows.size() - 1; i++) {
			SnapshotRow inRow = unMergeedRows.get(i);
			SnapshotRow outRow = foundOutRow(i, unMergeedRows);
			if (null != outRow) {
				// merger same row
				inRow.setEndTime(outRow.getEndTime());
				inRow.setUsedTime(outRow.getEndTime().getTime() - inRow.getStartTime().getTime());
				inRow.setMergered(true);
				outRow.setMergered(true);
				if (inRow.getHierarchy() != 1) {
					String parentId = foundParentId(i, unMergeedRows);
					inRow.setParentId(parentId);
					outRow.setParentId(parentId);
				}
			}
			snapshotRowMongoService.saveAll(unMergeedRows);
		}
	}

	private String foundParentId(int fromIndex, List<SnapshotRow> rows) {
		String parentId = null;
		SnapshotRow baseRow = rows.get(fromIndex);
		for (int i = fromIndex; i >= 0; i--) {
			SnapshotRow row = rows.get(i);
			if (row.getHierarchy() < baseRow.getHierarchy() && baseRow.getThreadId() == row.getThreadId()) {
				parentId = row.getId();
				break;
			}
		}

		return parentId;
	}

	private SnapshotRow foundOutRow(int inRowIndex, List<SnapshotRow> plant) {
		SnapshotRow result = null;
		SnapshotRow inRow = plant.get(inRowIndex);
		for (int i = inRowIndex + 1; i < plant.size(); i++) {
			SnapshotRow tempRow = plant.get(i);
			if (isSameMethod(inRow, tempRow)) {
				result = tempRow;
				break;
			}
		}
		return result;
	}

	/**
	 * 如果数据丢失，则结果有可能是错的。
	 * 
	 * @param rows
	 */
	private boolean isSameMethod(SnapshotRow inRow, SnapshotRow outRow) {
		boolean result = false;
		if ((outRow.getSerial() > inRow.getSerial()) && inRow.getInOrOut().equals(SnapshotFlag.FlagForMethodIn.trim())
				&& outRow.getInOrOut().equals(SnapshotFlag.FlagForMethodOut)) {
			if (inRow.getSnapshotId().equals(outRow.getSnapshotId()) && inRow.getThreadId() == outRow.getThreadId()
					&& inRow.getHierarchy() == outRow.getHierarchy()
					&& inRow.getPackageName().equals(outRow.getPackageName())
					&& inRow.getClassName().equals(outRow.getClassName())
					&& inRow.getMethodName().equals(outRow.getMethodName())) {
				result = true;
			}
		}
		return result;
	}

	@PreDestroy
	public void preDestroy() throws Exception {
	}

	@PostConstruct
	public void postConstruct() throws Exception {
	}
}
