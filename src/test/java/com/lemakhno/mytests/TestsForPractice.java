package com.lemakhno.mytests;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestsForPractice {
    
    @Test(groups = "parametrized")
    @Parameters({"name"})
    public void parametrizedTest(String name) {

        System.out.println(">>> Passed this name: " + name);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertAll();
    }

    @Test
    public void test1(){
        System.out.println("TEST 1");
        Assert.assertTrue(true);
    }

    @Test
    public void test2(){
        System.out.println("TEST 2");
        Assert.assertTrue(true);
    }

    @Test(retryAnalyzer = MyRetryAnalyzer.class)
    public void test3(){
        System.out.println("TEST 3");
        Assert.assertTrue(false);
    }

    @Test(retryAnalyzer = MyRetryAnalyzer.class)
    public void test4(){
        System.out.println("TEST 4");
        Assert.assertTrue(false);
    }
}


