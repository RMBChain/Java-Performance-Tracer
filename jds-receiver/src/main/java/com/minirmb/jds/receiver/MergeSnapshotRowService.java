package com.minirmb.jds.receiver;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.persistence.entity.SnapshotRow;
import com.minirmb.jds.persistence.services.SnapshotRowMongoService;

@Component
@EnableAsync
public class MergeSnapshotRowService {

	@Autowired
	private SnapshotRowMongoService snapshotRowMongoService;

	@Async
	public void merge(String snapshotId) {
		List<SnapshotRow> unMergedRows = snapshotRowMongoService.findUnMergedRow(snapshotId);
		for (int i = 0; i < unMergedRows.size() - 1; i++) {
			SnapshotRow inRow = unMergedRows.get(i);
			if(inRow.getMerged() ){
				continue;
			}
			if( SnapshotFlag.FlagForMethodOut.equals(inRow.getInOrOut())){
				continue;
			}
			SnapshotRow outRow = foundOutRow(inRow, unMergedRows);
			if (null != outRow) {
				// merger same row
				inRow.setEndTime(outRow.getEndTime());
				inRow.setUsedTime(outRow.getEndTime().getTime() - inRow.getStartTime().getTime());
				inRow.setMerged(true);
				outRow.setMerged(true);
				List<SnapshotRow> reorganized = Arrays.asList(inRow, outRow);
				snapshotRowMongoService.saveAll(reorganized);
				unMergedRows.removeAll(reorganized);
			}
		}
	}

	private SnapshotRow foundOutRow(SnapshotRow inRow, List<SnapshotRow> plant) {
		SnapshotRow result = null;
		for(SnapshotRow ele : plant){
			if( inRow.getMethodId().equals( ele.getMethodId()) && SnapshotFlag.FlagForMethodOut.equals(ele.getInOrOut())){
				result = ele;
				break;
			}
		}
		return result;
	}
}
