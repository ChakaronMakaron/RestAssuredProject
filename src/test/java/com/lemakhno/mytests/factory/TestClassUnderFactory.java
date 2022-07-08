package com.lemakhno.mytests.factory;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ MyListener.class })
public class TestClassUnderFactory {
    
    private int invocations;

    public TestClassUnderFactory(int invocations) {
        this.invocations = invocations;
    }

    @Test
    public void test1() {
        for (int i = 0; i < invocations; i++) {
            System.out.println(">> test1() invocation #" + (i + 1));
        }
    }
}


