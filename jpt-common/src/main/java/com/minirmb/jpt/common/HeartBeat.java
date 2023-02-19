package com.minirmb.jpt.common;

import static com.minirmb.jpt.common.TracerFlag.*;

public class HeartBeat implements IData {

	private String tracerId;

	private Long timestamp;

	private String hostName;

	private String ip;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public HeartBeat(){
	}

	public HeartBeat( String tracerId, Long timestamp, String hostName, String ip ){
		this.tracerId = tracerId;
		this.timestamp = timestamp;
		this.hostName = hostName;
		this.ip = ip;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getTracerId() {
		return tracerId;
	}

	public void setTracerId(String tracerId) {
		this.tracerId = tracerId;
	}

	public static HeartBeat parse(String data) {
		HeartBeat heartBeat = null;
		if( data.trim().length() > 0 ) {
			heartBeat = new HeartBeat();
			String[] items = data.split(TracerFlag.ItemSplitter);
			heartBeat.setTracerId( items[1].trim() );
			heartBeat.setTimestamp( Long.parseLong( items[2].trim()) );
			heartBeat.setHostName( items[3].trim() );
			heartBeat.setIp( items[4].trim() );
		}
		return heartBeat;
	}

	@Override
	public byte[] toBytes() {
		StringBuilder builder = new StringBuilder();
		builder.append( HeartBeat );
		builder.append( ItemSplitter).append( tracerId );
		builder.append( ItemSplitter).append( timestamp );
		builder.append( ItemSplitter).append( hostName );
		builder.append( ItemSplitter).append( ip );
		builder.append( LineBreaker );
		return builder.toString().getBytes();
	}
}
