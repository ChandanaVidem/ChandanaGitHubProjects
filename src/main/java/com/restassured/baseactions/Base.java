package com.restassured.baseactions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.asserts.SoftAssert;

public class Base {
    public static ExtentReports extent;
    public static ExtentTest extentTest;
    public static ExtentTest node;

    @BeforeSuite(alwaysRun = true)
    public void startReport() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/TestReport.html");
        extent.attachReporter(spark);
    }

    @BeforeTest
    public void createReportsForTests(ITestContext context) {
        extentTest = extent.createTest(context.getName());
    }

    @BeforeMethod
    public void getAnnotationDetails(Method m, ITestContext context) {
        //extentTest.assignAuthor(context.getCurrentXmlTest().getParameter("author"));
        extentTest.assignCategory(m.getAnnotation(Test.class).groups());
        node = extentTest.createNode(m.getAnnotation(Test.class).testName());
    }

    @AfterMethod
    public void getTestStatus(Method m, ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.fail("Test Failed");
        } else {
            extentTest.pass(m.getName() + " is passed");
        }
    }

    @AfterSuite
    public void endReport() {
        extent.flush();
    }
}
