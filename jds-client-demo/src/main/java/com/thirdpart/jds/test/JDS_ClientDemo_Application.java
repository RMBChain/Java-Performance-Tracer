package com.thirdpart.jds.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thirdpart.jds.test.xxx.TestEntry;
import com.thirdpart.jds.test.xxx.tp3.AAAA2;

@SpringBootApplication
public class JDS_ClientDemo_Application {

	public static void main(String[] args) {
		System.out.println("Begin............. " + JDS_ClientDemo_Application.class);
		TestEntry te = new TestEntry();
		te.sayHello1();
		try {
			te.sayHello2();
		} catch (Exception e) {
			e.printStackTrace();
		}

		AAAA2.main(null);

		new Thread(() -> {
			com.thirdpart.jds.test.xxx.tp3.AAAA2.main(null);
		}).start();

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		System.exit(0);
		System.out.println("JDS_ClientDemo_Application Over!!!!!!");
		System.out.println("End " + JDS_ClientDemo_Application.class);
	}
}
