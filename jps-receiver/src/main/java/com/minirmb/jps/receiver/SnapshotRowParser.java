package com.minirmb.jps.receiver;

import java.util.Date;
import java.util.UUID;

import com.minirmb.jps.common.SnapshotFlag;
import com.minirmb.jps.persistence.entity.SnapshotRow;

class SnapshotRowParser {

	//++ 20200509_013010_915, 14,   1,    12,out,    com.thirdpart.jds.test.xxx.tp3.AAAA2,main([Ljava/lang/String;)V,1588959013041,1,0
	static SnapshotRow ParseToSnapshotRow(String data) {
		SnapshotRow metric = new SnapshotRow();
		metric.setId(UUID.randomUUID().toString());
		String[] items = data.split(SnapshotFlag.FlagForItemSpliter);
		//snapshotId
		metric.setSnapshotId(items[0].substring(SnapshotFlag.PrefixForBaseData.length()));
		//threadId
		metric.setThreadId(Long.parseLong(items[1].trim()));
		//hierarchy
		metric.setHierarchy(Long.parseLong(items[2].trim()));
		//serial
		metric.setSerial(Long.parseLong(items[3].trim()));
		//in or out
		String type = items[4].trim();
		metric.setInOrOut(type);
		//start or end time
		Date date = new Date(Long.parseLong((items[7].trim())));
		if (SnapshotFlag.FlagForMethodIn.trim().equals(type)) {
			metric.setStartTime(date);
		} else if (SnapshotFlag.FlagForMethodOut.trim().equals(type)) {
			metric.setEndTime(date);
		}
		//class name and method name and package name
		String longClassName = items[5].trim();
		String methodName = items[6].trim();
		metric.setMethodName(methodName);
		int dotIndex = longClassName.lastIndexOf(".");
		String packageName = longClassName.substring(0, dotIndex);
		String shortClassName = longClassName.substring(dotIndex + 1);
		metric.setPackageName(packageName);
		metric.setClassName(shortClassName);
		//method id
		metric.setMethodId( Long.parseLong( items[8]));
		//parent serial id
		metric.setParentId(Long.parseLong(items[9]));

		metric.setMerged(false);
		return metric;
	}
}
