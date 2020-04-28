package com.minirmb.jds.common;

public interface SnapshotFlag {
	public static final String FlagForMethodIn=" in";
	public static final String FlagForMethodOut="out";	
	public static final String FlagForItemSpliter=",";
	public static final String FlagForLineBreaker = "\n";
	public static final String PrefixForBaseData="++ ";
	public static final String PrefixForSysInfo=">>SysInfo ";
	/**
	 * 在关闭jvm时会发送这个命令。
	 */
	public static final String PrefixForDisconnect=FlagForLineBreaker +">>Disconnect ";	
}
