package com.minirmb.jds.receiver;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.minirmb.jds.common.Utils;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@EnableAsync
@Slf4j
public class NIOClientHandle implements NIOClientHandleI {
	@Autowired
	private DBTransferService dbTransfer;

	@Async
	public void startRun(SocketChannel clientChannel) {
		boolean flag = true;
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024);	    
		
		try (SocketChannel channel = clientChannel) {
			while (flag) {
				byteBuffer.clear();
				int read = channel.read(byteBuffer);
				if( read<=0) {
					continue;
				}

				dbTransfer.transfer(Arrays.copyOf(byteBuffer.array(), read));
				channel.write(ByteBuffer.wrap(Utils.Int2Bytes(read)));		        
				byteBuffer.clear();
				byteBuffer.flip();

			}
		} catch (Exception e) {			
			e.printStackTrace();
			log.error(e.getMessage());
		}
		log.info("NIOServer: client closed");
	}
}
