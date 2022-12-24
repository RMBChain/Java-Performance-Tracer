package com.minirmb.jpt.core.collect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.minirmb.jpt.common.TracerFlag;
import com.minirmb.jpt.core.utils.LogUtil;


public class NIOTransfer{

	private SocketChannel clientChannel;

	public void init() throws IOException {
		Map<String, String> env = System.getenv();
		String serverIp = env.getOrDefault("jds_client_receiver_host", "localhost");
		int serverPort = Integer.parseInt(env.getOrDefault("jds_client_receiver_port", "8877"));
		LogUtil.log("***** jds_client_receiver_host : " + serverIp);
		LogUtil.log("***** jds_client_receiver_port : " + serverPort);
		clientChannel = SocketChannel.open();
		clientChannel.connect(new InetSocketAddress(serverIp, serverPort));
	}

	public void sendMeasureData(byte[] measureData) throws IOException {
		clientChannel.write(ByteBuffer.wrap(measureData));
	}

	public byte[] getInjectConfig() throws IOException {
		clientChannel.write(ByteBuffer.wrap(TracerFlag.InjectConfigPrefix.getBytes()));
		ByteBuffer icBuffer = ByteBuffer.allocate(2000);
		int serverReceivedLength = clientChannel.read(icBuffer);
		LogUtil.log( "***** getInjectConfig length : " + serverReceivedLength + "\n");
		return icBuffer.array();
	}

	public void close(){
		LogUtil.log("***Closing NIOTransfer...");
		if(null != clientChannel) {
			try {
				clientChannel.close();
				LogUtil.log("***NIOTransfer closed!");
			} catch (IOException e) {
				e.printStackTrace();
				LogUtil.log("***Close NIOTransfer error!");
			}
		}
	}
}
