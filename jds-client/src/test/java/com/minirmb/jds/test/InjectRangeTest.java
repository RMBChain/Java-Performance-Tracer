package com.minirmb.jds.test;

import java.io.IOException;

import com.minirmb.jds.client.config.InjectRangeConfig;
import com.minirmb.jds.client.config.RootConfig;

public class InjectRangeTest {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		RootConfig.Init("C:\\_minirmb_\\PerformanceMeasurer_workspace\\PerformanceMeasurer_Core\\config");
		InjectRangeConfig.Init(RootConfig.GetInstance().getInjectRangeConfigFile());
		InjectRangeConfig.GetInstance().print();
		InjectRangeConfig.GetInstance().shouldInject("com/jds/performance/measurer/AAAA1");
		
	}
}
