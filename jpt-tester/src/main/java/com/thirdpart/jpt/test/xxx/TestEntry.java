package com.thirdpart.jpt.test.xxx;

import com.thirdpart.jpt.test.xxx.tp1.AAAA3;

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
        AAAA3.main(null);
        Thread.sleep(1000);
        throw new Exception();
    }

    public void sayHello4() {
        for (int i = 0; i < 10000; i++) {
            sayHello5();
        }
    }
    public void sayHello5() {
    }
}
