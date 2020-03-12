package com.minirmb.jds.client.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.minirmb.jds.common.Utils;

public class LogUtil {

	private static Logger logger;
	private static int logSize = 10 * 1024 * 1024;

	private static Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger("PerformanceMeasurer");
			logger.setLevel(Level.ALL);
			
			//create dirs
			String logPath = Utils.GetFolderInUsehome()+ File.separator + "log";
			File logPathDir = new File(logPath);
			if( !logPathDir.exists() ) {
				logPathDir.mkdirs();
			}
			
			//init log
			String logFile = logPath + File.separator + "jds.log";
			try {
				Formatter logFormater = new Formatter() {
					@Override
					public synchronized String format(LogRecord record) {
						return record.getMessage();
					}
				};
				
				FileHandler fileHandler = new FileHandler(logFile, logSize, 9, true);
				fileHandler.setLevel(Level.ALL);
				fileHandler.setFormatter(logFormater);
				logger.addHandler(fileHandler);
				
				ConsoleHandler consoleHandler = new ConsoleHandler();
				consoleHandler.setLevel(Level.ALL);
				consoleHandler.setFormatter(logFormater);
				logger.addHandler(consoleHandler);
			} catch (SecurityException | IOException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Can not init log!!!");
			}

			logger.info("\nlog inited...");
		}
		return logger;
	}

	private static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

	public static void log(Object obj) {
		StringBuilder builder = new StringBuilder();
		builder.append("\n" + DateFormat.format(new Date()) + " ");
		builder.append(Thread.currentThread().getStackTrace()[2] + " ");
		builder.append(obj);
		LogUtil.getLogger().info(builder.toString());
	}

	public static void log(Object... objs) {
		StringBuilder builder = new StringBuilder();
		builder.append("\n" + DateFormat.format(new Date()) + " ");
		builder.append(Thread.currentThread().getStackTrace()[2] + " ");
		for (Object o : objs) {
			builder.append(String.valueOf(o));
		}
		LogUtil.getLogger().info(builder.toString());
	}

	public static void main(String[] args) {
		LogUtil.log("===========111122211", "ert", "ds---fsdf", "===");
	}
}
