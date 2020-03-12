package com.minirmb.jds.receiver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableAsync
@Slf4j
public class NIOServer implements ApplicationContextAware {

	@Async
	public void start() {
		log.info("NIOServer starting...");
		int serverPort = getPort();
		log.info("Using port:" + serverPort);
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(serverPort));
			open(serverSocketChannel);
			log.error("NIOServer error!!!");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		log.info("NIOServer shutdown!");
	}

	private int getPort() {
		Map<String, String> env = System.getenv();
		return Integer.parseInt(env.getOrDefault("jds_client_receiver_port", "8091"));
	}
	private void open(ServerSocketChannel serverSocketChannel) throws IOException {
		log.info("NIOServer listening...");
		try (Selector selector = Selector.open()) {
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			log.info("NIOServer listening port:" + getPort());
			while (selector.select() > 0) {
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey next = iterator.next();
					if (next.isAcceptable()) {
						SocketChannel accept = serverSocketChannel.accept();
						if (accept != null) {
							NIOClientHandleI clientHandler = (NIOClientHandleI)applicationContext.getBean("NIOClientHandle");
							clientHandler.startRun(accept);
							log.info("New connect establisted.");
						}
						iterator.remove();
					}
				} 
			}
			log.error("NIOServer error!!!");
		}
	}
	
    private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
