package com.minirmb.jds.client.collect;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.common.Utils;

public class Collector {
	public static String SnapshotId= new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
	private static class FlowEntity {
		private int serial = 0;
		private int hierarchy = 0;
		private String threadId = null;
	}

	private static ThreadLocal<FlowEntity> flowEntity = new ThreadLocal<FlowEntity>() {
		@Override
		public FlowEntity initialValue() {
			FlowEntity fe = new FlowEntity();
			fe.threadId = String.valueOf(Thread.currentThread().getId());
			return fe;
		}
	};

	public static void LogEnter(String className, String methodName, long startTime) {	
		FlowEntity fe = flowEntity.get();
		fe.serial++;
		fe.hierarchy++;
		// SnapshotId, threadId,hierarchy,enter or exit,serial,className,methodName,time
		StringBuilder builder = new StringBuilder();
		builder.append(SnapshotFlag.PrefixForBaseData);
		builder.append(SnapshotId);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(fe.threadId,3) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(String.valueOf(fe.hierarchy),4) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(String.valueOf(fe.serial),6) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(SnapshotFlag.FlagForMethodIn + SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.generate("  ", fe.hierarchy));
		builder.append(className);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(methodName);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(startTime);
		DataTransferStation.sendData(builder.toString());		
	}

	public static void LogExit(String className, String methodName, long endTime) {
		FlowEntity fe = flowEntity.get();
		fe.serial++;
		//MetricId, threadId,hierarchy,enter or exit,serial,className,methodName,time
		StringBuilder builder = new StringBuilder();
		builder.append(SnapshotFlag.PrefixForBaseData);
		builder.append(SnapshotId);
		builder.append( SnapshotFlag.FlagForItemSpliter );
		builder.append(Utils.padding(fe.threadId,3) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(String.valueOf(fe.hierarchy),4) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(String.valueOf(fe.serial),6) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(SnapshotFlag.FlagForMethodOut + SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.generate("  ", fe.hierarchy));	
		builder.append(className);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(methodName);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(endTime);
		DataTransferStation.sendData(builder.toString());		
		fe.hierarchy--;
	}
}
