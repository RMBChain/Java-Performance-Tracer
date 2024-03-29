package com.minirmb.jpt.collect;

class InfoInThread {
	protected String hostName;
	protected String ip;
	protected String tracerId; // 每次JVM启动为一个snapshot
	protected String bundleId; // 标注每个 OSGI 的 bundle。
	protected long serial = 0;// logEnter和logExit被调用的序列。
	protected int hierarchy = 0;// 当前被调用的方法所在的层级。in 和 out 是相同的。
	protected long threadId = 0;// 当前线程id
	protected long[] methodIdCache = new long[20];// 用于存放methodId（指向serial）,是FILO
}
