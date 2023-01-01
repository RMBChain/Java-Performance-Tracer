package com.minirmb.jpt.core.collect;

import com.minirmb.jpt.common.MetricBase;
import com.minirmb.jpt.common.TracerFlag;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.minirmb.jpt.common.TracerFlag.MethodIn;

public class Collector {
	public static String BundleId = UUID.randomUUID().toString().substring(0, 8);

	private static final ThreadLocal<InfoInThread> flowEntity = ThreadLocal.withInitial(() -> {
		InfoInThread fe = new InfoInThread();
		try {
			InetAddress address = InetAddress.getLocalHost();
			fe.hostName = address.getHostName();
			fe.ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
		fe.bundleId = BundleId;
		fe.threadId = Thread.currentThread().getId();
		return fe;
	});

	public static void RecordMethodIn(String traceId, String className, String methodName, long startTime) {
		InfoInThread fe = flowEntity.get();
		fe.tracerId = traceId;
		fe.serial++;
		fe.hierarchy++;

		// method id
		// 如果cache小了，进行扩容
		long[] methodIdCache = fe.methodIdCache;
		if (fe.hierarchy >= methodIdCache.length - 1) {
			long[] newMethodIdCache = new long[methodIdCache.length + 50];//
			System.arraycopy(methodIdCache, 0, newMethodIdCache, 0, methodIdCache.length);
			fe.methodIdCache = newMethodIdCache;
		}

		// 记录当前methodId
		fe.methodIdCache[fe.hierarchy] = fe.serial;

		MetricBase iie = get(fe, className, methodName, startTime, 0l, TracerFlag.MethodIn);
		Transmitter.sendData(iie);
	}

	public static void RecordMethodOut(String traceId, String className, String methodName, long endTime) {
		InfoInThread fe = flowEntity.get();
		fe.tracerId = traceId;
		fe.serial++;
		MetricBase iie = get(fe, className, methodName, 0l, endTime, TracerFlag.MethodOut);
		Transmitter.sendData(iie);
		fe.hierarchy--;
	}

	private static MetricBase get(InfoInThread ii, String className, String methodName, long inTime, long outTime,
			String inOrOut) {
		MetricBase iie = new MetricBase();
		iie.setHostName(ii.hostName);
		iie.setIp(ii.ip);
		iie.setTracerId(ii.tracerId);
		iie.setBundleId(ii.bundleId);
		iie.setThreadId(ii.threadId);
		iie.setHierarchy(ii.hierarchy);
		iie.setSerial(ii.serial);

		iie.setClassName(className);
		iie.setMethodName(methodName);
		iie.setInTime(inTime);
		iie.setOutTime(outTime);
		iie.setInOrOut(inOrOut);
		iie.setMethodId(inOrOut.equals(MethodIn) ? ii.serial : ii.methodIdCache[ii.hierarchy]);
		iie.setParentId(ii.methodIdCache[ii.hierarchy - 1]);

		return iie;
	}
}
