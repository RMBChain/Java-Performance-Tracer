package com.thirdpart.jds.test.xxx;

public class TestEntry {

	public String sayHello0(String memo) {
		System.out.println("now in " + this.getClass());
		return "rrrrr";
	}

	public final int sayHello1() {
		System.out.println("now in " + this.getClass());
		try {
			new TestEntry().sayHello2();
		} catch (Exception e1) {

		}
		return 999;
	}

	public int sayHello2() throws Exception {
		System.out.println("now in " + this.getClass());
		new TestEntry().sayHello3();
		return 999;
	}

	public void sayHello3() throws Exception {
		System.out.println("now in " + this.getClass());
		com.thirdpart.jds.test.xxx.tp1.AAAA3.main(null);
		Thread.sleep(1000);
		throw new Exception();
	}
}
