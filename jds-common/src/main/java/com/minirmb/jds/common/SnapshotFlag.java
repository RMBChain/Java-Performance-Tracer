package com.minirmb.jds.common;

public interface SnapshotFlag {
	public static final String PrefixForBaseData="++ ";
	public static final String PrefixForLoadClass=">>LoadClass ";
	public static final String PrefixForRoundClass=">>AdviseClass ";
	public static final String PrefixForSysInfo=">>SysInfo ";
	
	public static final String FlagForMethodIn=" in";
	public static final String FlagForMethodOut="out";	
	
	public static final String FlagForItemSpliter=",";
}
