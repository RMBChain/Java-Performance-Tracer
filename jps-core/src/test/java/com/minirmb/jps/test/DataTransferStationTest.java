package com.minirmb.jps.test;

import com.minirmb.jps.core.collect.DataTransferStation;
import com.minirmb.jps.core.config.RootConfig;
import com.minirmb.jps.core.utils.LogUtil;

public class DataTransferStationTest {

	public static void main(String[] args) throws Exception {
		RootConfig.Init("/Users/spooner/Desktop/OpenSource/github.com/RMBChain/Java-Performance-Tracer/jps-core-tester/workspace");
		DataTransferStation.Init(RootConfig.GetInstance());

		DataTransferStation.sendData( new StringBuilder("asdfsadf 1"));
		Thread.sleep(1000);
		DataTransferStation.sendData(new StringBuilder("asdfsadf 2"));
		Thread.sleep(1000);
		DataTransferStation.sendData(new StringBuilder("asdfsadf 3"));
		Thread.sleep(1000);
		DataTransferStation.sendData(new StringBuilder("asdfsadf 4"));
		Thread.sleep(1000);
		DataTransferStation.sendData(new StringBuilder("asdfsadf 5"));
		Thread.sleep(1000);
		//DataTransferStation.sendData(SnapshotFlag.PrefixForDisconnect);
		Thread.sleep(1000);
		LogUtil.log("+++++++++++++++++++++++++++");
		System.exit(0);
	}
}
