package com.lemakhno.mytests;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MyRetryAnalyzer implements IRetryAnalyzer {

    private int maxRetries = 3;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult arg0) {
        
        if (retryCount < maxRetries) {
            retryCount++;
            return true;
        }

        return false;
    }
}
