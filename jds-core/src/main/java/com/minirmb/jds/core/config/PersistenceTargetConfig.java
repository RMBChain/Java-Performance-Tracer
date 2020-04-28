package com.minirmb.jds.core.config;

import java.util.Properties;

public class PersistenceTargetConfig {

	private boolean saveDataToLocal = true;

	private boolean saveDataToRemote = true;

	public PersistenceTargetConfig(Properties properties) {
		saveDataToLocal = Boolean.valueOf(properties.getProperty("persistenceTarget.saveDataToLocal", "false"));
		saveDataToRemote = Boolean.valueOf(properties.getProperty("persistenceTarget.saveDataToRemote", "false"));
	}

	public boolean isSaveDataToLocal() {
		return saveDataToLocal;
	}

	public boolean isSaveDataToRemote() {
		return saveDataToRemote;
	}

	@Override
	public String toString() {
		return "*LogConfig [saveDataToLocal=" + saveDataToLocal + ", saveDataToRemote=" + saveDataToRemote + "]";
	}

}
