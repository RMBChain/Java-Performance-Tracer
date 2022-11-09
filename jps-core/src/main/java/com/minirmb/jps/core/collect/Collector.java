package com.minirmb.jps.core.collect;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.minirmb.jps.common.SnapshotFlag;
import com.minirmb.jps.common.Utils;

/**
 *
 * 非线程安全。
 *
 */
public class Collector {
	public static String SnapshotId= new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());

	private static class FlowEntity {
		private long serial = 0;//in和out是不同的
		private int hierarchy = 0;
		private String threadId = null;//当前线程id
		private long[] methodIdCache = new long[20];//用于存放methodId,是FILO
		private String dataPrefixInThread;
	}

	private static ThreadLocal<FlowEntity> flowEntity = ThreadLocal.withInitial(() ->  {
			FlowEntity fe = new FlowEntity();
			fe.threadId = String.valueOf(Thread.currentThread().getId());
			fe.dataPrefixInThread = SnapshotFlag.PrefixForBaseData + SnapshotId + SnapshotFlag.FlagForItemSpliter + Utils.padding(fe.threadId, 3) + SnapshotFlag.FlagForItemSpliter;
			return fe;
	});

	public static void LogEnter(String className, String methodName, long startTime) {	
		FlowEntity fe = flowEntity.get();
		{//为什么不写成一个方法? 为了减少方法调用产生的开销
			fe.serial++;
			fe.hierarchy++;

			//method id
			//如果cache小了，进行扩容
			long[] methodIdCache = fe.methodIdCache;
			if(fe.hierarchy >= methodIdCache.length-1){
				long[] newMethodIdCache = new long[ methodIdCache.length + 5];//
				System.arraycopy(methodIdCache,0,newMethodIdCache,0, methodIdCache.length);
				fe.methodIdCache = newMethodIdCache ;
			}

			//记录当前methodId
			fe.methodIdCache[fe.hierarchy] = fe.serial;
		}

		// SnapshotId, threadId,hierarchy,enter or exit,serial,className,methodName,time,methodId,parentId
		StringBuilder builder = CacheForStringBuilder.get();
		builder.append(fe.dataPrefixInThread);
		builder.append(Utils.padding(String.valueOf(fe.hierarchy),4) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(String.valueOf(fe.serial),6) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(SnapshotFlag.FlagForMethodIn );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.generate("  ", fe.hierarchy));
		builder.append(className);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(methodName);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(startTime);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(fe.serial);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(fe.methodIdCache[fe.hierarchy-1]);
		DataTransferStation.sendData(builder);
	}

	public static void LogExit(String className, String methodName, long endTime) {
		FlowEntity fe = flowEntity.get();
		long methodId = -1;
		{//为什么不写成一个方法? 为了减少方法调用产生的开销
			fe.serial++;
		}

		//MetricId, threadId,hierarchy,enter or exit,serial,className,methodName,time,methodId,parentId
		StringBuilder builder = CacheForStringBuilder.get();
		builder.append(fe.dataPrefixInThread);
		builder.append(Utils.padding(String.valueOf(fe.hierarchy),4) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.padding(String.valueOf(fe.serial),6) );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(SnapshotFlag.FlagForMethodOut );
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(Utils.generate("  ", fe.hierarchy));	
		builder.append(className);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(methodName);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(endTime);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(fe.methodIdCache[fe.hierarchy]);
		builder.append(SnapshotFlag.FlagForItemSpliter);
		builder.append(fe.methodIdCache[fe.hierarchy-1]);
		DataTransferStation.sendData(builder);
		fe.hierarchy--;
	}
}
