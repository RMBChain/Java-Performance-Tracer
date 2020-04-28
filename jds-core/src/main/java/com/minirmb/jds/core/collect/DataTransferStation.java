package com.minirmb.jds.core.collect;

import java.io.IOException;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.common.Utils;
import com.minirmb.jds.core.collect.transfor.LocalFileTransfer;
import com.minirmb.jds.core.collect.transfor.NIOTransfer;
import com.minirmb.jds.core.config.PersistenceTargetConfig;
import com.minirmb.jds.core.config.RootConfig;
import com.minirmb.jds.core.utils.LogUtil;

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
	public final Object transferNotifier = new Object();
	private AtomicBoolean isContinue = new AtomicBoolean(true);
	private LocalFileTransfer localFileTransfer = null;
	private NIOTransfer nioTransfer = null;
	private Queue<String> dataQueue = new ConcurrentLinkedQueue<String>();
	
	private DataTransferStation() {
	}
	
	public static void sendData(String... data) {
		StringBuilder builder = new StringBuilder();
		for (Object s : data) {
			builder.append(String.valueOf(s));
		}
		builder.append("\n");
		sendData(builder.toString());
	}

	public static void sendData(String data) {
		instance.dataQueue.offer(SnapshotFlag.FlagForLineBreaker + data );
		synchronized (instance.transferNotifier) {
			instance.transferNotifier.notifyAll();
		}
	}

	public static synchronized void Init(RootConfig rootConfig) throws IOException  {
		instance = new DataTransferStation();

		// shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				synchronized (instance.transferNotifier) {
					LogUtil.log("***send " + SnapshotFlag.PrefixForDisconnect + " !!!!!!");					
					sendData(SnapshotFlag.PrefixForDisconnect);
					instance.transferNotifier.notifyAll();
					instance.isContinue.set(false);
					instance.transferNotifier.notifyAll();
					LogUtil.log("***find sign for System shutdown !!!!!!");				
				}
			}
		});

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
		Thread  dataTransferStation = new Thread(instance, DataTransferStation.class.getSimpleName());
		dataTransferStation.setDaemon(true);
		dataTransferStation.start();
	}

	/**
	 * We don't want to use multi-thread in this method inner.
	 */
	@Override
	public void run() {
		StringBuffer buffer = new StringBuffer();		
		try {
			while (isContinue.get()) {
				buffer.delete(0, buffer.length());
				while(!dataQueue.isEmpty()) {
					buffer.append(dataQueue.poll());
				}
				if (buffer.length() > 0) {
					String data = buffer.toString();
					if (null != nioTransfer) {// 保存到remote
						nioTransfer.transfer(data);
					}
					if (null != localFileTransfer) {// 保存到本地
						localFileTransfer.transfer(data);
					}
				}
				if(buffer.indexOf(SnapshotFlag.PrefixForDisconnect)>=0) {
					//收到 shutdownhook 发送的断开连接的信息.
					isContinue.set(false);
					LogUtil.log("Will shutdown...   ");
				}else {
					synchronized (transferNotifier) {
						try {
							transferNotifier.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
							LogUtil.log("***DataTransferStation " + Utils.ExceptionStackTraceToString(e));
						}
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
