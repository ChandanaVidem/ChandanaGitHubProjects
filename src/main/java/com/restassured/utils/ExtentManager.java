package com.restassured.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ExtentManager {

        private static ExtentReports extent;

        public static synchronized ExtentReports createInstance() {
            if (extent == null) {
                ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent-report.html");
                extent = new ExtentReports();
                extent.attachReporter(htmlReporter);
            }
            return extent;
        }
    }

