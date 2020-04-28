package com.minirmb.jds.test;

import java.io.IOException;

import com.minirmb.jds.core.config.RootConfig;

public class RootConfigTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		RootConfig.Init("C:\\_minirmb_\\JavaDynamicSnapshot_workspace\\jds\\jds-core-tester\\workspace");
		RootConfig rootConfig = RootConfig.GetInstance();
		
		System.out.println( rootConfig.getLogConfig() );
		System.out.println( rootConfig.getInjectRangeConfig() );
		
	}
}
