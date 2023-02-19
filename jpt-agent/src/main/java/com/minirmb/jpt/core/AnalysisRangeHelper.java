package com.minirmb.jpt.core;

import com.minirmb.jpt.common.*;
import com.minirmb.jpt.tools.JPTLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.stream.Stream;

/**
 * 
 * the priority of exclude is higher than include.
 * 
 * @author Spooner
 *
 */
public class AnalysisRangeHelper {

	public static AnalysisRange GetAnalysisRange( ) throws IOException {
		ByteBuffer icBuffer = null;
		JPTLogger.log("***** getAnalysisRange......");
		try (SocketChannel clientChannel = SocketChannel.open()) {
			boolean connected = clientChannel.connect(new InetSocketAddress(ClientConfig.GetServerIp(), ClientConfig.GetServerPort()));
			if (connected) {
				clientChannel.write(ByteBuffer.wrap(TracerFlag.GetAnalysisRangePrefix.getBytes()));
				icBuffer = ByteBuffer.allocate(2000);
				int serverReceivedLength = clientChannel.read(icBuffer);
				JPTLogger.log("***** getAnalysisRange length : " + serverReceivedLength );
			}
		} catch (IOException e) {
			String errorLog = "*** Error when get config from " + ClientConfig.GetServerIp() + ":" + ClientConfig.GetServerPort();
			JPTLogger.log(errorLog);
			throw new IOException( errorLog , e);
		}

		String analysisConfigStr = new String(icBuffer.array());
		JPTLogger.log("*****AnalysisRange : \n" + analysisConfigStr );
		return Parse( analysisConfigStr );
	}

	private static AnalysisRange Parse(String rangeStr) {
		AnalysisRange result = new AnalysisRange();
		result.setExclude( new ArrayList<>() );
		result.setInclude( new ArrayList<>() );

		Stream.of(rangeStr.split("\n")).filter(line -> line.trim().length() > 0).forEach(line -> {
			String[] lineEle = line.split("::");
			if (lineEle.length > 1) {
				String content = lineEle[1].trim();
				AnalysisRangeItem ici = AnalysisRangeItem.parse(content);
				switch (lineEle[0].trim()) {
				case "exclude":
					result.getExclude().add(ici);
					break;
				case "include":
					result.getInclude().add(ici);
					break;
				default:

				}
			}
		});

		return result;
	}
}
