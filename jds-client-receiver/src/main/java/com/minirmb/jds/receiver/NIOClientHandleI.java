package com.minirmb.jds.receiver;

import java.nio.channels.SocketChannel;

public interface NIOClientHandleI {
	public void startRun(SocketChannel clientChannel);
}
