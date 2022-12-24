package com.minirmb.jpt.core.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

	public static int bytes2Int(byte[] bytes) {
		int int1 = bytes[0] & 0xff;
		int int2 = (bytes[1] & 0xff) << 8;
		int int3 = (bytes[2] & 0xff) << 16;
		int int4 = (bytes[3] & 0xff) << 24;

		return int1 | int2 | int3 | int4;
	}

	public static <E extends Throwable> String ExceptionStackTraceToString(E t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw, true));
		return sw.getBuffer().toString();
	}

	public static String GetFolderInUserHome() {
		return System.getProperties().getProperty("user.home") + File.separator + ".jpt" + File.separator;
	}
}
