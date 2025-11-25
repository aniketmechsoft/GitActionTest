package com.uiFramework.Genesis.utils;

import org.testng.*;
import com.aventstack.extentreports.*;

public class UniversalListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
        // Grab platform from testng.xml parameter or system property
        String platform = suite.getParameter("platform");
        if (platform == null || platform.trim().isEmpty()) {
            platform = System.getProperty("platform", "web"); // Default fallback
        }
        extent = ExtentManager.getInstance(platform);
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        testThread.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().pass("Test passed ✅");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testThread.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().skip(result.getThrowable());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}
}


