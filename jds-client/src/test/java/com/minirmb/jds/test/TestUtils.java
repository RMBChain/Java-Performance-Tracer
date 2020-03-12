package com.minirmb.jds.test;

import com.minirmb.jds.common.Utils;

public class TestUtils {

	public static void main(String[] args) {
		System.out.println(Utils.padding("1", 5));
		System.out.println(Utils.padding("2", 5));
		System.out.println(Utils.padding("3", 5));
		System.out.println(Utils.padding("10", 5));
		System.out.println(Utils.padding("11", 5));
		System.out.println(Utils.padding("123", 5));
		System.out.println(Utils.padding("231", 5));
		System.out.println(Utils.padding("431", 5));
		System.out.println(Utils.padding("6661", 5));
		System.out.println(Utils.padding("33331", 5));
	}
}
