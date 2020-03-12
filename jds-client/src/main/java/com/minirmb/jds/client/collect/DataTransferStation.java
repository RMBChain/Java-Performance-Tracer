package com.minirmb.jds.client.collect;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minirmb.jds.client.config.RootConfig;
import com.minirmb.jds.client.utils.LogUtil;

public class DataTransferStation implements Runnable {

	private static DataTransferStation instance = null;
//	NIOTransfer transfer;
	public final Object transferNotifier = new Object();
	private AtomicBoolean isContinue = new AtomicBoolean(true);
//	private FileTransfer transfer = new FileTransfer();
	private NIOTransfer transfer = new NIOTransfer();
	private Queue<String> dataQueue = new ConcurrentLinkedQueue<String>();
	
	public static void sendData(String... data) {
		StringBuilder builder = new StringBuilder();
		for (Object s : data) {
			builder.append(String.valueOf(s));
		}
		builder.append("\n");
		sendData(builder.toString());
	}

	public static void sendData(String data) {
		instance.dataQueue.offer(data + "\n");
		synchronized (instance.transferNotifier) {
			instance.transferNotifier.notifyAll();
		}
	}

	private DataTransferStation() {
	}

	public static synchronized void Init(RootConfig rootConfig) throws Exception {
		LogUtil.log("Init MemoryTransferStation...");

		instance = new DataTransferStation();

		// shut down hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				synchronized (instance.transferNotifier) {
					sendData(">> System shutdown!!!!!!");
					instance.isContinue.set(false);
					instance.transferNotifier.notifyAll();
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

		instance.transfer = new NIOTransfer();
		instance.transfer.init(rootConfig);
				
//		// Regular transfer to remote server
//		final HttpTransfer transfer = new HttpTransfer();
//		new Timer(true).schedule(new TimerTask() {
//			public void run() {
//				transfer.upload();
//			}
//		}, 10000, 5000);
		
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
					transfer.transfer(buffer.toString());
				}

				synchronized (transferNotifier) {
					try {
						transferNotifier.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
