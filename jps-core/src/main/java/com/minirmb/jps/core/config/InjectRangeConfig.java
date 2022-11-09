package com.minirmb.jps.core.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * the priority of exclude is higher than include.
 * 
 * @author WeiHuaXu
 *
 */
public class InjectRangeConfig {

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

	public InjectRangeConfig(List<String> configLines) {
		for (String line : configLines) {
			String[] lineEle = line.split("=");
			if (lineEle.length > 1) {
				String content = lineEle[1].trim().replace(".", "/");
				switch (lineEle[0].trim()) {
				case "excludedClass":
					excludedClasses.add(content);
					break;
				case "excludedPackage":
					excludedPackages.add(content);
					break;
				case "includedClass":
					includedClasses.add(content);
					break;
				case "includedPackage":
					includedPackages.add(content);
					break;
				default:

				}
			}
		}
	}

	@Override
	public String toString() {
		return "*InjectRangeConfig [excludedClasses=" + excludedClasses + ", excludedPackages=" + excludedPackages
				+ ", includedClasses=" + includedClasses + ", includedPackages=" + includedPackages + "]";
	}

}
