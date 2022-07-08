package com.lemakhno.mytests.factory;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class MyListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("########## FAIL ##########");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(result.getName());
        System.out.println(result.getMethod());
        System.out.println(result.getInstanceName());
        System.out.println("########## SUCCESS ##########");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("########## SKIP ##########");
    }
}


