package com.minirmb.jpt.common;

import static com.minirmb.jpt.common.TracerFlag.*;

public class AnalysisLog implements IData {

	private String tracerId;

	private String className;

	private String methodName;

	private String methodDesc;

	private String signature;

	private Long timestamp;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public AnalysisLog( ){
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public static AnalysisLog parse(String data) {
		AnalysisLog heartBeat = null;
		if( data.trim().length() > 0 ) {
			heartBeat = new AnalysisLog();
			String[] items = data.split(TracerFlag.ItemSplitter);
			heartBeat.setTracerId( items[1].trim() );
			heartBeat.setClassName( items[2].trim() );
			heartBeat.setMethodName( items[3].trim() );
			heartBeat.setMethodDesc( items[4].trim() );
			heartBeat.setSignature( items[5].trim() );
			heartBeat.setTimestamp( Long.parseLong( items[6].trim()) );
		}
		return heartBeat;
	}

	@Override
	public byte[] toBytes() {
		StringBuilder builder = new StringBuilder();
		builder.append( AnalysisLog );
		builder.append( ItemSplitter).append( tracerId );
		builder.append( ItemSplitter).append( className );
		builder.append( ItemSplitter).append( methodName );
		builder.append( ItemSplitter).append( methodDesc );
		builder.append( ItemSplitter).append( signature );
		builder.append( ItemSplitter).append( timestamp );
		builder.append( LineBreaker );
		return builder.toString().getBytes();
	}
}
