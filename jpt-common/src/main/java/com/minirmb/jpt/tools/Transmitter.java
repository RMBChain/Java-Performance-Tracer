package com.minirmb.jpt.tools;

import com.minirmb.jpt.common.ClientConfig;
import com.minirmb.jpt.common.IData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author Spooner
 *
 */
public class Transmitter implements Runnable {

	private final static Transmitter instance = new Transmitter();
	private final Object transferNotifier = new Object();
	private final AtomicBoolean isContinue = new AtomicBoolean(true);
	private final Queue<IData> metricQueue = new ConcurrentLinkedQueue<>();

	static {
		// start thread
		Thread dataTransferStation = new Thread(instance, Transmitter.class.getSimpleName());
		dataTransferStation.setDaemon(true);
		dataTransferStation.start();
	}

	private Transmitter() {
	}

	public static void sendData(IData data) {
		instance.metricQueue.offer(data);
		instance.notifySendData();
	}

	private void notifySendData() {
		synchronized (instance.transferNotifier) {
			instance.transferNotifier.notifyAll();
		}
	}

	@Override
	public void run() {

		// shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			isContinue.set(false);
			notifySendData();
		}));

		// Regular flush data.
		new Timer(true).schedule(new TimerTask() {
			public void run() {
				notifySendData();
			}
		}, 5000, 5000);

		// get server info
		Map<String, String> env = System.getenv();
		String serverIp = ClientConfig.GetServerIp();
		int serverPort = ClientConfig.GetServerPort();
		JPTLogger.log("***** jpt_nio_server_ip : " + serverIp);
		JPTLogger.log("***** jpt_nio_server_port : " + serverPort);

		SocketChannel clientChannel = null;
		try {
			// connect to server
			clientChannel = SocketChannel.open();
			clientChannel.connect(new InetSocketAddress(serverIp, serverPort));

			while (isContinue.get()) {
				// waiting notify
				synchronized (transferNotifier) {
					transferNotifier.wait();
				}

				// send data.
				while (!metricQueue.isEmpty()) {
					IData iie = metricQueue.poll();
					clientChannel.write(ByteBuffer.wrap(iie.toBytes()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JPTLogger.log(e);
		}

		// close socket
		if (null != clientChannel) {
			try {
				clientChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
				JPTLogger.log("***Close Transmitter error!");
			}
		}
		JPTLogger.log("***Transmitter shutdown!!!!!!\n\n");
	}
}
