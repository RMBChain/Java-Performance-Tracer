package com.minirmb.jds.receiver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.minirmb.jds.common.SnapshotFlag;
import com.minirmb.jds.common.Utils;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableAsync
@Slf4j
public class NIOServer {

	@Autowired
	private DBTransferService dbTransfer;

	public int socketChannelId = 100001;
	public int bufferSize = 2048;

	@Async
	public void start() {
		int serverPort = getPort();
		log.info(NIOServer.class.getName() + " starting...", " Using port:" + serverPort);
		try {
			// 创建通道和选择器
			ServerSocketChannel socketChannel = ServerSocketChannel.open();
			Selector serverSocketChannelSelector = Selector.open();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverPort);
			socketChannel.socket().bind(inetSocketAddress);
			// 设置通道非阻塞 绑定选择器
			socketChannel.configureBlocking(false);
			socketChannel.register(serverSocketChannelSelector, SelectionKey.OP_ACCEPT).attach(socketChannelId++);
			log.info(NIOServer.class.getName() + " started .... port:" + serverPort);
			listener(serverSocketChannelSelector);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(NIOServer.class.getName() + " shutdown!", e);
		}
		log.error(NIOServer.class.getName() + " shutdown!");
	}

	private void listener(Selector serverSocketChannelSelector) throws IOException {
		while (true) {
			serverSocketChannelSelector.select(); // 阻塞 直到有就绪事件为止
			Set<SelectionKey> readySelectionKey = serverSocketChannelSelector.selectedKeys();
			Iterator<SelectionKey> it = readySelectionKey.iterator();
			while (it.hasNext()) {
				SelectionKey selectionKey = it.next();
				// 判断是哪个事件
				if ( selectionKey.isConnectable()) {
					log.trace("socketChannelId : " + selectionKey.attachment());
				}
				if ( selectionKey.isAcceptable()) {// 客户请求连接
					handleAccept(selectionKey);
				}
				if ( selectionKey.isReadable()) {// 读数据
					handleRead(selectionKey);
				}				
				it.remove();
			}
		}
	}

	private int getPort() {
		Map<String, String> env = System.getenv();
		return Integer.parseInt(env.getOrDefault("jds_client_receiver_port", "8091"));
	}

	private void handleRead(SelectionKey selectionKey) throws IOException {
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
		int count = 0;
		// 从channel中读取的数据长度为零时，不再循环取数据.
		boolean shouldContinue = true;
		// 从channel中取数据时发生异常，或接收的数据中有 SnapshotFlag.PrefixForDisconnect时，此值为true
		boolean shouldCloseSocketChannel = false;
		while (shouldContinue && !shouldCloseSocketChannel) {
			int read = -2;
			byteBuffer.clear();
			try {
				read = socketChannel.read(byteBuffer);
			} catch (IOException e) {
				e.printStackTrace();
				// 如果发生错误，则表示连接已经不可用了。需要关闭 SocketChannel
				shouldCloseSocketChannel = true;
			}
			if (read > 0) {
				count += read;
				String data = new String(Arrays.copyOf(byteBuffer.array(), read));
				dbTransfer.transfer(data);
				socketChannel.write(ByteBuffer.wrap(Utils.Int2Bytes(read)));
				if (data.contains(SnapshotFlag.PrefixForDisconnect)) {
					shouldCloseSocketChannel = true;
					shouldContinue = false;
					log.info("Connection will be closed by client. socketChannelId : " + selectionKey.attachment());
				}
			} else if (read == -1) {
				shouldCloseSocketChannel = true;
				log.debug("Connection unavaliabled socketChannelId : " + selectionKey.attachment() + ", read count : " + read);
			} else if (read == 0) {
				shouldContinue = false;
			}else {
				shouldContinue = false;
			}

			if (shouldCloseSocketChannel) {
				closeSocketChannel(selectionKey);
			}
		}
		byteBuffer.clear();
		log.debug("socketChannelId : " + selectionKey.attachment() + ", read count : " + count);
	}

	private void closeSocketChannel(SelectionKey selectionKey) {
		log.info("***close socketChannel socketChannelId : " + selectionKey.attachment());
		selectionKey.cancel();
		try {
			selectionKey.channel().close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void handleAccept(SelectionKey selectionKey) throws IOException {
		// 获取通道 接受连接,设置非阻塞模式（必须），同时需要注册 读写数据的事件，这样有消息触发时才能捕获
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		SelectionKey key = socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
		key.attach(socketChannelId++);
		log.info("socketChannelId : " + selectionKey.attachment() + " accepted. Remote: " 	+ socketChannel.getRemoteAddress());
	}
}
