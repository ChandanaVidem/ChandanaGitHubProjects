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

    @BeforeSuite(alwaysRun = true)
    public void startReport() {
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("TestReport.html");
        extent.attachReporter(spark);

    }

    @BeforeTest
    public void createReportsForTests(ITestContext context) {
        extentTest = extent.createTest(context.getName());
        extentTest.assignAuthor(context.getCurrentXmlTest().getParameter("author"));
    }

    @BeforeMethod
    public void getAnnotationDetails(Method m, ITestResult result)
    {
       extentTest.assignCategory(m.getAnnotation(Test.class).groups());

    }
    @AfterMethod
    public void getTestStatus(Method m, ITestResult result) {
        if (result.getStatus()==ITestResult.FAILURE)
        {
            extentTest.fail("Test Failed");
        }
    }

    @AfterSuite
    public void endReport() {
        extent.flush();

    }
}
