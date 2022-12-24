package com.minirmb.jpt.core.utils;

import com.minirmb.jpt.core.TransformEntry;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 
 * the priority of exclude is higher than include.
 * 
 * @author WeiHuaXu
 *
 */
public class InjectConfig {

	private Set<String> excludedClasses = new HashSet<>();// priority 1
	private Set<String> excludedPackages = new HashSet<>();// priority 1
	private Set<String> includedClasses = new HashSet<>();// priority 2
	private Set<String> includedPackages = new HashSet<>();// priority 2

	public Set<String> getExcludedClasses() {
		return excludedClasses;
	}

	public Set<String> getExcludedPackages() {
		return excludedPackages;
	}

	public Set<String> getIncludedClasses() {
		return includedClasses;
	}

	public Set<String> getIncludedPackages() {
		return includedPackages;
	}

	public static synchronized InjectConfig Parse(String ic) throws IOException {
		InjectConfig instance = new InjectConfig();
		// injectRangeConfigFile
		Stream.of(ic.split("\n")).filter(line-> line.trim().length() > 0).forEach( line ->{
			String[] lineEle = line.split("=");
			if (lineEle.length > 1) {
				String content = lineEle[1].trim().replace(".", "/");
				switch (lineEle[0].trim()) {
					case "excludedClass":
						instance.excludedClasses.add(content);
						break;
					case "excludedPackage":
						instance.excludedPackages.add(content);
						break;
					case "includedClass":
						instance.includedClasses.add(content);
						break;
					case "includedPackage":
						instance.includedPackages.add(content);
						break;
					default:

				}
			}
		});

		instance.excludedPackages.addAll(Arrays.asList(new String[] {"java","jdk","sun","javax"}));
		instance.excludedPackages.add(TransformEntry.class.getPackage().getName().replace(".", "/"));
		return instance;
	}

	@Override
	public String toString() {
		return "*InjectRangeConfig [excludedClasses=" + excludedClasses + ", excludedPackages=" + excludedPackages
				+ ", includedClasses=" + includedClasses + ", includedPackages=" + includedPackages + "]";
	}
}
