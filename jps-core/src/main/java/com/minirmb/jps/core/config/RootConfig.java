package com.minirmb.jps.core.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RootConfig {
	private static RootConfig instance = new RootConfig();

	private InjectRangeConfig injectRange = null;
	private PersistenceTargetConfig logConfig = null;
	
	private RootConfig() {
	}

	public static RootConfig GetInstance() {
		return instance;
	}

	public InjectRangeConfig getInjectRangeConfig() {
		return this.injectRange;
	}

	public PersistenceTargetConfig getLogConfig() {
		return logConfig;
	}
	
	public static synchronized void Init(String workspace) throws IOException {
		// 检查配置文件目录是否存在
		if (!new File(workspace).exists()) {
			throw new IllegalArgumentException(workspace + " not exist!!!");
		}

		// injectRangeConfigFile
		String injectRangeConfigFile = workspace + File.separator + "injectRangeConfig.properties";			
		try (BufferedReader bf = new BufferedReader(new FileReader(injectRangeConfigFile));) {
			List<String> allLines = new ArrayList<String>();			
			String str;
			while ((str = bf.readLine()) != null) {
				allLines.add(str.trim());
			}
			instance.injectRange = new InjectRangeConfig(allLines);	
		}
		
		// 加载切片时的日志配置
		String logConfigFile= workspace + File.separator + "persistenceTargetConfig.properties";
		try (InputStream is = new FileInputStream(logConfigFile)) {
			Properties properties = new Properties();
			properties.load(is);
			instance.logConfig = new PersistenceTargetConfig(properties);
		}
	}
}
