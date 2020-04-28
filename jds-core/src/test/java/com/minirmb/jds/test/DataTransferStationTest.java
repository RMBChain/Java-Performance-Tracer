package com.minirmb.jds.test;

import com.minirmb.jds.core.collect.DataTransferStation;
import com.minirmb.jds.core.config.RootConfig;
import com.minirmb.jds.core.utils.LogUtil;

public class DataTransferStationTest {

	public static void main(String[] args) throws Exception {
		RootConfig.Init("C:\\_minirmb_\\JavaDynamicSnapshot_workspace\\jds\\jds-core-tester\\workspace");
		DataTransferStation.Init(RootConfig.GetInstance());

		DataTransferStation.sendData("asdfsadf 1");
		Thread.sleep(1000);
		DataTransferStation.sendData("asdfsadf 2");
		Thread.sleep(1000);
		DataTransferStation.sendData("asdfsadf 3");
		Thread.sleep(1000);
		DataTransferStation.sendData("asdfsadf 4");
		Thread.sleep(1000);
		DataTransferStation.sendData("asdfsadf 5");
		Thread.sleep(1000);
		//DataTransferStation.sendData(SnapshotFlag.PrefixForDisconnect);
		Thread.sleep(1000);
		LogUtil.log("+++++++++++++++++++++++++++");
		System.exit(0);
	}
}
