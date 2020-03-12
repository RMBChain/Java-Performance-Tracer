package com.minirmb.jds.test;

import com.minirmb.jds.client.collect.DataTransferStation;
import com.minirmb.jds.client.config.RootConfig;
import com.minirmb.jds.client.utils.LogUtil;

public class TransferTest {

	public static void main(String[] args) throws Exception {
		RootConfig.Init("C:\\_minirmb_\\JavaDynamicSnapshot_workspace\\jds\\jds-test\\config");
		DataTransferStation.Init(RootConfig.GetInstance());

		DataTransferStation.sendData("asdfsadf 1");
		Thread.sleep(1000);
		DataTransferStation.sendData("asdfsadf 2");
		Thread.sleep(10000);
		DataTransferStation.sendData("asdfsadf 3");
		Thread.sleep(10000);
		DataTransferStation.sendData("asdfsadf 4");
		Thread.sleep(10000);
		DataTransferStation.sendData("asdfsadf 5");
		Thread.sleep(10000);
		DataTransferStation.sendData("asdfsadf 6");
		LogUtil.log("exit exit exit exit exit exit exit exit ");
	}
}
