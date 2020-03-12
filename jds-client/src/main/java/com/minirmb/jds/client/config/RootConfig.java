package com.minirmb.jds.client.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.minirmb.jds.client.utils.LogUtil;

public class RootConfig {
	private static RootConfig instance = new RootConfig();

	private String workFolder;
	private Properties rootProperties;

	//	private String logSerialToDir;
	private String commonSuperConfigFile;
	private String injectRangeConfigFile;

	private boolean logVisitClass = false;
	private boolean logVisitMethod = false;
	private boolean logOnMethodEnter = false;
	private boolean logOnMethodExit = false;
	private boolean logVisitMaxs = false;
	private boolean logOnFinally = false;

	private boolean logTrace = false;

	public Properties getRootProperties() {
		return rootProperties;
	}

	public boolean isLogTrace() {
		return logTrace;
	}

	public String getWorkFolder() {
		return workFolder;
	}

	public String getCommonSuperConfigFile() {
		return commonSuperConfigFile;
	}

	public String getInjectRangeConfigFile() {
		return injectRangeConfigFile;
	}

	public boolean isLogVisitClass() {
		return logVisitClass;
	}

	public boolean isLogVisitMethod() {
		return logVisitMethod;
	}

	public boolean isLogOnMethodEnter() {
		return logOnMethodEnter;
	}

	public boolean isLogOnMethodExit() {
		return logOnMethodExit;
	}

	public boolean isLogVisitMaxs() {
		return logVisitMaxs;
	}

	public boolean isLogOnFinally() {
		return logOnFinally;
	}

	public static RootConfig GetInstance() {
		return instance;
	}

	public static synchronized void Init(String configFolder) throws IOException, ClassNotFoundException {
		LogUtil.log("RootConfig workFolder:", configFolder);
		
		instance = InitRootConfig(configFolder);

		// injectRangeConfigFile
		instance.injectRangeConfigFile = instance.workFolder + File.separator + "injectRange.properties";
		if (!new File(instance.injectRangeConfigFile).exists()) {
			LogUtil.log(instance.injectRangeConfigFile + " not exist!!!");
		}

		// commonSuperConfigFile
		instance.commonSuperConfigFile = instance.workFolder + File.separator + "commonSuper.properties";
		if (!new File(instance.commonSuperConfigFile).exists()) {
			LogUtil.log(instance.commonSuperConfigFile + " not exist!!!");
		}
	}
	
	private static RootConfig InitRootConfig(String configFolder) throws IOException, ClassNotFoundException {
		LogUtil.log("RootConfig workFolder:", configFolder);
		if (!new File(configFolder).exists()) {
			LogUtil.log(configFolder, " not exist!!!");
			throw new IllegalArgumentException(configFolder + " not exist!!!");
		}

		RootConfig cfg = new RootConfig();
		cfg.workFolder = configFolder;

		// config.properties
		String cfgFile = configFolder + File.separator + "config.properties";
		if (new File(cfgFile).exists()) {
			Properties prop = new Properties();
			try (InputStream is = new FileInputStream(cfgFile)) {
				prop.load(is);
				cfg.rootProperties=prop;
				cfg.logVisitClass = Boolean.valueOf(prop.getProperty("persistence.timeNode.visitClass", "false"));
				cfg.logVisitMethod = Boolean.valueOf(prop.getProperty("persistence.timeNode.visitMethod", "false"));
				cfg.logOnMethodEnter = Boolean.valueOf(prop.getProperty("persistence.timeNode.onMethodEnter", "false"));
				cfg.logOnMethodExit = Boolean.valueOf(prop.getProperty("persistence.timeNode.onMethodExit", "false"));
				cfg.logVisitMaxs = Boolean.valueOf(prop.getProperty("persistence.timeNode.visitMaxs", "false"));
				cfg.logOnFinally = Boolean.valueOf(prop.getProperty("persistence.timeNode.onFinally", "false"));

				cfg.logTrace = Boolean.valueOf(prop.getProperty("log.trace", "false"));
			}
		} else {
			throw new IllegalArgumentException(cfgFile + " not exist!!!");
		}

		return cfg;
	}


}
