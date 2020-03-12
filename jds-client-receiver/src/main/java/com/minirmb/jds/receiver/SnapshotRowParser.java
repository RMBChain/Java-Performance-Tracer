package com.minirmb.jds.receiver;

import java.util.Date;
import java.util.UUID;

import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.persistence.entity.SnapshotRow;

public class SnapshotRowParser {

	public static SnapshotRow ParseToSnapshotRow(String data) {
		SnapshotRow metric = new SnapshotRow();
		metric.setId(UUID.randomUUID().toString());
		String[] items = data.split(SnapshotFlag.FlagForItemSpliter);
		metric.setSnapshotId(items[0].substring(SnapshotFlag.PrefixForBaseData.length()));
		metric.setThreadId(Long.parseLong(items[1].trim()));
		metric.setHierarchy(Long.parseLong(items[2].trim()));
		metric.setSerial(Long.parseLong(items[3].trim()));
		String type = items[4].trim();
		metric.setInOrOut(type);
		Date date = new Date(Long.parseLong((items[7].trim())));
		if (SnapshotFlag.FlagForMethodIn.trim().equals(type)) {
			metric.setStartTime(date);
		} else if (SnapshotFlag.FlagForMethodOut.trim().equals(type)) {
			metric.setEndTime(date);
		}
		String longClassName = items[5].trim();
		String methodName = items[6].trim();
		metric.setMethodName(methodName);
		int dotIndex = longClassName.lastIndexOf(".");
		String packageName = longClassName.substring(0, dotIndex);
		String shortClassName = longClassName.substring(dotIndex + 1);
		metric.setPackageName(packageName);
		metric.setClassName(shortClassName);
		metric.setMergered(false);
		return metric;
	}
}
