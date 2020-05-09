package com.minirmb.jds.test;

import com.minirmb.jds.core.collect.DataTransferStation;
import com.minirmb.jds.core.config.RootConfig;
import com.minirmb.jds.core.utils.LogUtil;
import com.sun.org.apache.xpath.internal.operations.String;

public class DataTransferStationTest {

	public static void main(String[] args) throws Exception {
		RootConfig.Init("C:\\_minirmb_\\JavaDynamicSnapshot_workspace\\jds\\jds-core-tester\\workspace");
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
