package com.minirmb.jps.core.utils;

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

import com.minirmb.jps.common.Utils;

/**
 * 
 * 使用这个类的原因是java.util.logging.Logger没有行数。
 * 
 * 日志会写到 $user_home/.jps 目录下.
 * 
 * 是否写入日志, 要在些代码时通过 LogConfig.saveDataToLocal 来判断. 
 * 
 * 不记录要发送到远程的数据。
 * 
 * @author WeiHuaXu
 *
 */
public class LogUtil {
	private static Logger logger;
	private final static int logSize = 10 * 1024 * 1024;
	private final static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

	private static Logger getLogger() {
		if (null == logger) {
			logger = Logger.getLogger("jds");
			logger.setLevel(Level.ALL);
			logger.setUseParentHandlers(false);

			// create dirs
			String logPath = Utils.GetFolderInUsehome() + File.separator + "log";
			File logPathDir = new File(logPath);
			if (!logPathDir.exists()) {
				logPathDir.mkdirs();
			}

			// init log
			String logFile = logPath + File.separator + "jds.log";
			try {
				Formatter logFormater = new Formatter() {
					@Override
					public synchronized String format(LogRecord record) {
						return "\n" + record.getMessage();
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

			logger.info(DateFormat.format(new Date()) + " log inited...");
		}
		return logger;
	}

	public static void log(Object obj) {
		StringBuilder builder = new StringBuilder();
		builder.append(DateFormat.format(new Date()) + " ");
		builder.append(Thread.currentThread().getStackTrace()[2] + " ");
		builder.append(obj);
		LogUtil.getLogger().info(builder.toString());
	}

	public static void log(Object... objs) {
		StringBuilder builder = new StringBuilder();
		builder.append(DateFormat.format(new Date()) + " ");
		builder.append(Thread.currentThread().getStackTrace()[2] + " ");
		for (Object o : objs) {
			builder.append(String.valueOf(o));
		}
		LogUtil.getLogger().info(builder.toString());
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			LogUtil.log("===========111122211", " ert", " ds---fsdf", " ===");
			LogUtil.log("===========211122212", " ert", " ds---fsdf", " ===");
			LogUtil.log("===========311122213", " ert", " ds---fsdf", " ===");
		}
	}
}
