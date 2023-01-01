package com.minirmb.jpt.core.collect;

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

import com.minirmb.jpt.common.IData;
import com.minirmb.jpt.core.utils.LogUtil;

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
		String serverIp = env.getOrDefault("jpt_nio_server_ip", "localhost");
		int serverPort = Integer.parseInt(env.getOrDefault("jpt_nio_server_port", "8877"));
		LogUtil.log("***** jpt_nio_server_ip : " + serverIp);
		LogUtil.log("***** jpt_nio_server_port : " + serverPort);

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
			LogUtil.log(e);
		}

		// close socket
		if (null != clientChannel) {
			try {
				clientChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
				LogUtil.log("***Close Transmitter error!");
			}
		}
		LogUtil.log("***Transmitter shutdown!!!!!!\n\n");
	}
}
