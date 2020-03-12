package com.minirmb.jds.client.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.minirmb.jds.client.utils.LogUtil;

public class InjectRangeConfig {
	private static InjectRangeConfig instance;

	private Set<String> excludedClasses = new HashSet<>();// priority 1
	private Set<String> excludedPackages = new HashSet<>();// priority 1
	private Set<String> includedClasses = new HashSet<>();// priority 2
	private Set<String> includedPackages = new HashSet<>();// priority 2

	public static InjectRangeConfig GetInstance() {
		return instance;
	}

	public static synchronized void Init(String cfgFile) throws IOException {
		 
		LogUtil.log("Read InjectRangeConfig file:" + cfgFile);

		List<String> allLine = new ArrayList<String>();		
		try (FileReader fr = new FileReader(cfgFile); BufferedReader bf = new BufferedReader(fr);) {
			String str;
			while ((str = bf.readLine()) != null) {
				allLine.add(str.trim());
			}
		} catch (IOException e) {
			throw e;
		}

		InjectRangeConfig tf = new InjectRangeConfig();

		for (String line : allLine) {
			String[] lineEle = line.split("=");
			if (lineEle.length > 1) {
				switch (lineEle[0].trim()) {
				case "ExcludedClass":
					tf.excludedClasses.add(lineEle[1].trim().replace(".", "/"));
					break;
				case "ExcludedPackage":
					tf.excludedPackages.add(lineEle[1].trim().replace(".", "/"));
					break;
				case "IncludedClass":
					tf.includedClasses.add(lineEle[1].trim().replace(".", "/"));
					break;
				case "IncludedPackage":
					tf.includedPackages.add(lineEle[1].trim().replace(".", "/"));
					break;
				default:

				}
			}
		}
		instance = tf;
	}

	/**
	 * 
	 * @param className className 
	 * @return boolean boolean
	 */
	public boolean shouldInject(String className) {
		boolean result = false;
		if (!excludedClasses.contains(className)) {
			LogUtil.log("after excludedClasses. className:", className );
			if (!isSubPackage(excludedPackages, className)) {
				LogUtil.log("after isSubPackage excludedPackages. className:", className, ", result:", result );
				if (includedClasses.contains(className)) {			    	
					result = true;
					LogUtil.log("after includedClasses. className:", className, ", result:", result);
				} else if (isSubPackage(includedPackages, className)) {
					result = true;
					LogUtil.log("after isSubPackage . className:", className, ", result:", result);
				}
			}
		}
		LogUtil.log("className:", className, ", result:", result);
		
		return result;
	}

	private boolean isSubPackage(Set<String> packages, String package2) {
		boolean result = false;
		for (String pk : packages) {
			if (package2.startsWith(pk)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public void print() throws IOException {
		StringBuilder builer = new StringBuilder();
		builer.append("=excludedClasses");
		for(String ss : excludedClasses) {
			builer.append("\n " + ss );	
		}
		builer.append("\n=excludedPackage");
		for(String ss : excludedPackages) {
			builer.append("\n " + ss );	
		}
		builer.append("\n=includedClass");
		for(String ss : includedClasses) {
			builer.append("\n " + ss );	
		}
		builer.append("\n=includedPackage");
		for(String ss : includedPackages) {
			builer.append("\n " + ss );	
		}
		LogUtil.log("\n" + builer + "\n");
	}
}
