package com.lemakhno.mytests.factory;

import org.testng.annotations.Factory;

public class MyFactory {
    
    @Factory
    public Object[] myLittleFactory() {
        
        Object[] result = new Object[3];

        for (int i = 0; i < 3; i++) {
            result[i] = new TestClassUnderFactory((i + 1) * 3);
        }

        return result;
    }
}
