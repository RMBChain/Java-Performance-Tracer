package com.minirmb.jds.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {

	public static long bytesToLong(byte[] value) {
		long ret = 0;
		for (int i = 0; i < value.length; i++) {
			ret += (long) (value[value.length - i - 1] & 0xFF) << (long) (i * 8);
		}
		return ret;
	}

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

	public static String padding(int data, int len) {
		return padding(String.valueOf(data), len, "0");
	}

	public static String padding(String data, int len) {
		return padding(data, len, " ");
	}

	public static String padding(String data, int maxLen, String paddingChar) {
		StringBuilder result = new StringBuilder();
		for (int i = data.length()+1; i <= maxLen; i++) {
			result.append(paddingChar);
		}
		result.append(data);
		return result.toString();
	}

	public static String generate(String base, int count) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i <= count; i++) {
			result.append(base);
		}
		return result.toString();
	}
	
	public static void CheckFileExistWithException(String path) {
		CheckFileExistExistWithException(new File(path));
	}

	public static void CheckFileExistExistWithException(File file) {
		if (null == file) {
			throw new IllegalArgumentException("File not exist!!!");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException(file.getAbsolutePath() + " not exist!!!");
		}
	}

	public static boolean CheckFileExist(String path) {
		return CheckFileExistExist(new File(path));
	}

	public static boolean CheckFileExistExist(File file) {
		return null != file && file.exists();
	}

	public static File createTempDirectory() throws IOException {
		final File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		}
		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		}
		return temp;
	}

	public static byte[] Int2Bytes(int integer) {
		byte[] bytes = new byte[4];
		bytes[3] = (byte) (integer >> 24);
		bytes[2] = (byte) (integer >> 16);
		bytes[1] = (byte) (integer >> 8);
		bytes[0] = (byte) integer;
		return bytes;
	}
	
	public static String GetFolderInUsehome() {
		return System.getProperties().getProperty("user.home") + File.separator + ".jds" + File.separator;
	}
}
