package com.minirmb.jpt.common;

import java.util.Base64;
import java.util.regex.Pattern;

public class AnalysisRangeItem {
	private String packageName;
	private String className;
	private String methodName;
	private Boolean enabled;

	public AnalysisRangeItem(){
	}

	public AnalysisRangeItem(String packageName, String className, String methodName, Boolean enabled){
		this.packageName=packageName;
		this.className=className;
		this.methodName=methodName;
		this.enabled=enabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public StringBuilder toStringBuilder(){
		Base64.Encoder encoder = Base64.getEncoder();
		StringBuilder builder = new StringBuilder();
		if( null != packageName){
			builder.append( "packageName:" + encoder.encodeToString( packageName.getBytes()) + ",");
		}

		if( null != className){
			builder.append( "className:" + encoder.encodeToString( className.getBytes()) + ",");
		}

		if( null != methodName){
			builder.append( "methodName:" + encoder.encodeToString( methodName.getBytes()) + ",");
		}
		return builder;
	}

	public static AnalysisRangeItem parse(String line){
		AnalysisRangeItem result = new AnalysisRangeItem();
		Base64.Decoder decoder = Base64.getDecoder();
		String[] lines =line.split(",");

		for(String item : lines ){
			String[] kv = item.split(":");
			if( kv[0].equals("packageName")){
				byte[] de =  decoder.decode(kv[1].getBytes());
				result.setPackageName( new String(de) );
			}

			if( kv[0].equals("className")){
				byte[] de =  decoder.decode(kv[1].getBytes());
				result.setClassName( new String(de));
			}

			if( kv[0].equals("methodName")){
				byte[] de =  decoder.decode(kv[1].getBytes());
				result.setMethodName( new String(de));
			}
		}
		return result;
	}

	public boolean isPackageInRange(String packageName){
		String pn = this.packageName.startsWith("*") ? "." + this.packageName : this.packageName;
		return Pattern.matches(pn, packageName);
	}

	public boolean isClassInRange(String className){
		String cn = this.className.startsWith("*") ? "." + this.className : this.className;
		return Pattern.matches(cn, className);
	}

	public boolean isMethodInRange(String methodName){
		String mn = this.methodName.startsWith("*") ? "." + this.methodName : this.methodName;
		return Pattern.matches(mn, methodName);
	}
}
