package com.minirmb.jps.core.collect;

import java.io.IOException;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minirmb.jps.common.SnapshotFlag;
import com.minirmb.jps.common.Utils;
import com.minirmb.jps.core.collect.transfor.LocalFileTransfer;
import com.minirmb.jps.core.collect.transfor.NIOTransfer;
import com.minirmb.jps.core.config.PersistenceTargetConfig;
import com.minirmb.jps.core.config.RootConfig;
import com.minirmb.jps.core.utils.LogUtil;

/**
 * 
 * 
 * 
 * 
 * @author WeiHuaXu
 *
 */
public class DataTransferStation implements Runnable {

	private static DataTransferStation instance = null;
	private final Object transferNotifier = new Object();
	private AtomicBoolean isContinue = new AtomicBoolean(true);
	private LocalFileTransfer localFileTransfer = null;
	private NIOTransfer nioTransfer = null;
	private Queue<StringBuilder> dataQueue = new ConcurrentLinkedQueue<>();
	
	private DataTransferStation() {
	}
	
	public static void sendData(StringBuilder... data) {
		StringBuilder builder = new StringBuilder();
		for (StringBuilder s : data) {
			builder.append(s.toString());
			CacheForStringBuilder.put(s);
		}
		builder.append("\n");
		sendData(builder);
	}

	public static void sendData(StringBuilder data) {
		instance.dataQueue.offer(data.insert(0,SnapshotFlag.FlagForLineBreaker) );
		synchronized (instance.transferNotifier) {
			instance.transferNotifier.notifyAll();
		}
	}

	public static synchronized void Init(RootConfig rootConfig) throws IOException  {
		instance = new DataTransferStation();

		// Regular transfer data from MemoryTransferStation to FileTransferStation
		new Timer(true).schedule(new TimerTask() {
			public void run() {
				synchronized (instance.transferNotifier) {
					instance.transferNotifier.notifyAll();
				}
			}
		}, 5000, 5000);

		{//数据保存位置
			PersistenceTargetConfig persistenceTargetConfig = RootConfig.GetInstance().getLogConfig();
			if (persistenceTargetConfig.isSaveDataToRemote()) {
				// 保存数据到远程
				instance.nioTransfer = new NIOTransfer();
				instance.nioTransfer.init(rootConfig);
			}
			if (persistenceTargetConfig.isSaveDataToLocal()) {
				// 保存数据到本地
				instance.localFileTransfer = new LocalFileTransfer();
			}
		}

		// start thread
		Thread dataTransferStation = new Thread(instance, DataTransferStation.class.getSimpleName());
		dataTransferStation.setDaemon(true);
		dataTransferStation.start();
	}

	/**
	 * We don't use multi-thread in this method inner.
	 */
	@Override
	public void run() {
		try {
			while (isContinue.get()) {
				while(!dataQueue.isEmpty()) {
					StringBuilder tempStr = dataQueue.poll();
					if (null != nioTransfer) {// 保存到remote
						nioTransfer.transfer(tempStr);
					}
					if (null != localFileTransfer) {// 保存到本地
						localFileTransfer.transfer(tempStr);
					}
					CacheForStringBuilder.put(tempStr);
				}

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
		nioTransfer.close();
		localFileTransfer.close();
		LogUtil.log("***DataTransferStation shutdown!!!!!!");
	}
}
