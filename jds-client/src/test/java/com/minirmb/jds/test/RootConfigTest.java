package com.minirmb.jds.test;

import java.io.IOException;

import com.minirmb.jds.client.config.RootConfig;

public class RootConfigTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		RootConfig.Init("C:\\_minirmb_\\PerformanceMeasurer_workspace\\PerformanceMeasurer_Core\\config");
		RootConfig ir = RootConfig.GetInstance();
		
		System.out.println("=log_visitClass:" + ir.isLogVisitClass());
		System.out.println("=log_visitMethod:" + ir.isLogVisitMethod());
		System.out.println("=log_onMethodEnter:" + ir.isLogOnMethodEnter());
		System.out.println("=log_onMethodExit:" + ir.isLogOnMethodExit());
		System.out.println("=log_visitMaxs:" + ir.isLogVisitMaxs());
		System.out.println("=log_onFinally:" + ir.isLogOnFinally());
		
		System.out.println("=log_workFolder:" + ir.getWorkFolder());
		System.out.println("=log_commonSuperConfigFile:" + ir.getCommonSuperConfigFile());
		System.out.println("=log_injectRangeConfigFile:" + ir.getInjectRangeConfigFile());
		
	}
}
