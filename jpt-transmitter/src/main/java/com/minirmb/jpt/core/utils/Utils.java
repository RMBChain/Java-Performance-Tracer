package com.minirmb.jpt.core.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

	public static <E extends Throwable> String ExceptionStackTraceToString(E t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw, true));
		return sw.getBuffer().toString();
	}

	public static String GetFolderInUserHome() {
		return System.getProperties().getProperty("user.home") + File.separator + ".jpt" + File.separator;
	}
}
