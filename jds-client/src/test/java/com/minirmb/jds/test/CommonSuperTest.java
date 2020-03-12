package com.minirmb.jds.test;

import java.io.IOException;

import com.minirmb.jds.client.config.CommonSuper;
import com.minirmb.jds.client.config.RootConfig;

public class CommonSuperTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		RootConfig.Init("C:\\_minirmb_\\PerformanceMeasurer_workspace\\PerformanceMeasurer_Core\\config");
		CommonSuper.Init(RootConfig.GetInstance().getCommonSuperConfigFile());
		System.out.println( CommonSuper.GetInstance().getCommonSuper("org/eclipse/core/runtime/Status", "java/net/URL"));
	}
}
