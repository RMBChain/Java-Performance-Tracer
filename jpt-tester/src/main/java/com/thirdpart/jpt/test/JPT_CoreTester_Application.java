package com.thirdpart.jpt.test;

import com.thirdpart.jpt.test.xxx.TestEntry;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JPT_CoreTester_Application {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Begin............. " + JPT_CoreTester_Application.class);
        TestEntry te = new TestEntry();
        te.sayHello0("");

        for (int i = 0; i < 1; i++) {
            te.sayHello1();
        }

        {
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                threads.add(        new Thread(() -> {
                    te.sayHello1();
                }));
            }
            threads.forEach(t -> t.start());
        }

        te.sayHello4();

        Thread.sleep(10000);

        System.out.println("JPT_ClientDemo_Application Over!!!!!!");
        System.out.println("End " + JPT_CoreTester_Application.class);
    }
}
