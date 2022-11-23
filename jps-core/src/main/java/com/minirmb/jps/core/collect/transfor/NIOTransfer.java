package com.minirmb.jps.core.collect.transfor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Map;

import com.minirmb.jps.common.Utils;
import com.minirmb.jps.core.collect.TransferI;
import com.minirmb.jps.core.config.RootConfig;
import com.minirmb.jps.core.utils.LogUtil;

public class NIOTransfer implements TransferI{

	private SocketChannel clientChannel;

	public void init(RootConfig rootConfig) throws IOException {
		Map<String, String> env = System.getenv();
		String serverIp = env.getOrDefault("jds_client_receiver_host", "localhost");
		int serverPort = Integer.parseInt(env.getOrDefault("jds_client_receiver_port", "8091"));
		System.out.println("!!! jds_client_receiver_host : " + serverIp);
		System.out.println("!!! jds_client_receiver_port : " + serverPort);
		clientChannel = SocketChannel.open();
		clientChannel.connect(new InetSocketAddress(serverIp, serverPort));
	}

	@Override
	public long transfer(StringBuilder measurData) throws IOException {

		byte[] inputBytes = measurData.toString().getBytes();

		clientChannel.write(ByteBuffer.wrap(inputBytes));

		ByteBuffer buffer = ByteBuffer.allocate(8);
		int receivedLength = clientChannel.read(buffer);
		byte[] receivedData = Arrays.copyOf(buffer.array(), receivedLength);
		long serverReceiveLength = Utils.bytes2Int(receivedData);
		if (serverReceiveLength != inputBytes.length) {
			LogUtil.log("!!!! client sent length:" + inputBytes.length + ",  server received length:"
					+ serverReceiveLength);
		}

		return inputBytes.length;
	}

	@Override
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
	
	public static void main(String[] args) throws Exception {
		NIOTransfer np = new NIOTransfer();
		np.clientChannel = SocketChannel.open();
		np.clientChannel.connect(new InetSocketAddress("localhost", 8091));
		np.transfer(new StringBuilder("=================="));
	}
}
