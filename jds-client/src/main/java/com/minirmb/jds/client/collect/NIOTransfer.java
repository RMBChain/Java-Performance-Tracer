package com.minirmb.jds.client.collect;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Map;

import com.minirmb.jds.client.config.RootConfig;
import com.minirmb.jds.client.utils.LogUtil;
import com.minirmb.jds.common.Utils;

public class NIOTransfer {

	private SocketChannel clientChannel;

	public void init(RootConfig rootConfig) throws Exception {
		Map<String, String> env = System.getenv();
		System.out.println("----------------------------------");
		String serverIp = env.getOrDefault("jds_client_receiver_host", "localhost");
		int serverPort = Integer.parseInt(env.getOrDefault("jds_client_receiver_port", "8091"));
		System.out.println("!!! jds_client_receiver_host : " + serverIp);
		System.out.println("!!! jds_client_receiver_port : " + serverPort);
		Thread.sleep(1000);
		clientChannel = SocketChannel.open();
		clientChannel.connect(new InetSocketAddress(serverIp, serverPort));
	}

	public long transfer(String measurData) throws Exception {
		try {
			byte[] inputBytes = measurData.getBytes();
			clientChannel.write(ByteBuffer.wrap(inputBytes));

			ByteBuffer buffer = ByteBuffer.allocate(8);
			int receivedLength = clientChannel.read(buffer);
			byte[] receivedData = Arrays.copyOf(buffer.array(), receivedLength);
			long serverReceiveLength = Utils.bytes2Int(receivedData);
			if (serverReceiveLength != inputBytes.length) {
				LogUtil.log("!!!! client sent length:" + inputBytes.length + ",  server received length:"
						+ serverReceiveLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return measurData.getBytes().length;
	}

	public static void main(String[] args) throws Exception {
		NIOTransfer np = new NIOTransfer();
		np.clientChannel = SocketChannel.open();
		np.clientChannel.connect(new InetSocketAddress("localhost", 9999));
		np.transfer("==================");
	}
}
