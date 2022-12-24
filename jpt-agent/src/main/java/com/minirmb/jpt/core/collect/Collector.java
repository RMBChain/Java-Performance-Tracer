package com.minirmb.jpt.core.collect;

import com.minirmb.jpt.common.TracerFlag;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Collector {
	public static String TraceId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "-" + UUID.randomUUID();
	private static String HostName = "NoHostName";
	private static String IPAddress = "NotIP";
	static{
		try {
			InetAddress address = InetAddress.getLocalHost();
			HostName = address.getHostName();
			IPAddress = address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	private static final ThreadLocal<InfoInThread> flowEntity = ThreadLocal.withInitial(() ->  {
		InfoInThread fe = new InfoInThread();
		fe.hostName = HostName;
		fe.ip = IPAddress;
		fe.tracerId = TraceId;
		fe.threadId = Thread.currentThread().getId();
		return fe;
	});

	public static void RecordMethodIn(String className, String methodName, long startTime) {
		InfoInThread fe = flowEntity.get();

		fe.serial++;
		fe.hierarchy++;

		//method id
		//如果cache小了，进行扩容
		long[] methodIdCache = fe.methodIdCache;
		if(fe.hierarchy >= methodIdCache.length-1){
			long[] newMethodIdCache = new long[ methodIdCache.length + 50];//
			System.arraycopy(methodIdCache,0,newMethodIdCache,0, methodIdCache.length);
			fe.methodIdCache = newMethodIdCache ;
		}

		//记录当前methodId
		fe.methodIdCache[fe.hierarchy] = fe.serial;

		DataTransferStation.sendData(fe, className, methodName, startTime, 0l, TracerFlag.MethodIn);
	}

	public static void RecordMethodOut(String className, String methodName, long endTime) {
		InfoInThread fe = flowEntity.get();
		fe.serial++;
		DataTransferStation.sendData(fe, className, methodName, 0l,endTime, TracerFlag.MethodOut);
		fe.hierarchy--;
	}
}
