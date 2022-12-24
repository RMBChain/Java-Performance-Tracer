package com.minirmb.jpt.test;

import com.minirmb.jpt.core.TransformEntry;

public class Test {

	public static void main(String[] args) {
		Package ee = TransformEntry.class.getPackage();
		System.out.println(ee.getName());
		System.out.println(TransformEntry.class.getName());
		System.out.println(TransformEntry.class.getPackage().getName().replace(".", "/"));

		sendData("1","1",1,"1","1",null);
		
		
		
	}
	

	public static void sendData(Object... data) {
		StringBuilder builder = new StringBuilder();
		for(Object s : data) {
			builder.append(String.valueOf(s));
		}
		builder.append("\n");
		System.out.println(builder.toString());
	}

}
