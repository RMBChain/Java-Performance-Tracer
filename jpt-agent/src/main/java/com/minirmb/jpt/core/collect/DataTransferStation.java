package com.minirmb.jpt.core.collect;

import java.io.IOException;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minirmb.jpt.common.MetricBase;
import com.minirmb.jpt.core.utils.LogUtil;

import com.minirmb.jpt.core.utils.Utils;

import static com.minirmb.jpt.common.TracerFlag.MethodIn;

/**
 * 
 * 
 * 
 * 
 * @author WeiHuaXu
 *
 */
public class DataTransferStation implements Runnable {

	private final static DataTransferStation instance = new DataTransferStation();
	private final Object transferNotifier = new Object();
	private final AtomicBoolean isContinue = new AtomicBoolean(true);
	private final NIOTransfer nioTransfer = new NIOTransfer();
	private final Queue<MetricBase> metricQueue = new ConcurrentLinkedQueue<>();

	static {
		try {
			Init();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.log("***Init DataTransferStation " + Utils.ExceptionStackTraceToString(e));
		}
	}

	private DataTransferStation() {
	}

	public static void sendData(InfoInThread fe, String className, String methodName, long inTime, long outTime, String inOrOut) {
		MetricBase iie = get(fe, className, methodName,inTime, outTime,inOrOut);
		instance.metricQueue.offer( iie );
		synchronized (instance.transferNotifier) {
			instance.transferNotifier.notifyAll();
		}
	}

	private static MetricBase get(InfoInThread ii, String className, String methodName, long inTime, long outTime, String inOrOut){
		MetricBase iie = new MetricBase();
		iie.setHostName( ii.hostName );
		iie.setIp( ii.ip );
		iie.setTracerId( ii.tracerId );
		iie.setThreadId( ii.threadId );
		iie.setHierarchy( ii.hierarchy );
		iie.setSerial( ii.serial );

		iie.setClassName( className );
		iie.setMethodName( methodName );
		iie.setInTime( inTime );
		iie.setOutTime( outTime );
		iie.setInOrOut( inOrOut );
		iie.setMethodId( inOrOut.equals(MethodIn) ? ii.serial : ii.methodIdCache[ii.hierarchy] );
		iie.setParentId( ii.methodIdCache[ii.hierarchy-1] );

		return iie;
	}

	private static synchronized void Init() throws IOException  {
		Runtime.getRuntime().addShutdownHook(new Thread( ()->{
			instance.isContinue.set(false);
		}));

		// Regular transfer data from MemoryTransferStation to FileTransferStation
		new Timer(true).schedule(new TimerTask() {
			public void run() {
				synchronized (instance.transferNotifier) {
					instance.transferNotifier.notifyAll();
				}
			}
		}, 5000, 5000);

		instance.nioTransfer.init();

		// start thread
		Thread dataTransferStation = new Thread(instance, DataTransferStation.class.getSimpleName());
		dataTransferStation.setDaemon(true);
		dataTransferStation.start();
	}

	/**
	 * get inject config from remote
	 * @return
	 * @throws IOException
	 */
	public static synchronized byte[] getInjectConfig() throws IOException  {
		return instance.nioTransfer.getInjectConfig();
	}

	/**
	 * We don't use multi-thread in this method inner.
	 */
	@Override
	public void run() {
		try {
			while (isContinue.get()) {
				doSendMeasureData();
				synchronized (transferNotifier) {
					try {
						transferNotifier.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						LogUtil.log("***DataTransferStation " + Utils.ExceptionStackTraceToString(e));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			doSendMeasureData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		nioTransfer.close();
		LogUtil.log("***DataTransferStation shutdown!!!!!!");
	}

	private void doSendMeasureData() throws IOException {
		while(!metricQueue.isEmpty()) {
			MetricBase iie = metricQueue.poll();
			nioTransfer.sendMeasureData( iie.toBytes() );
		}
	}
}
