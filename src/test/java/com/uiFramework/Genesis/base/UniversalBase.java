package com.uiFramework.Genesis.base;

import com.aventstack.extentreports.ExtentReports;
import com.uiFramework.Genesis.utils.ExtentManager;
import org.openqa.selenium.WebDriver;
import io.appium.java_client.AppiumDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterSuite;

public class UniversalBase {
	
	protected static ExtentReports extent;
    protected static WebDriver webDriver;
    protected static AppiumDriver appDriver;
    protected static String platform;

    @BeforeTest(alwaysRun = true)
    public void setupSuite(ITestContext context) {
        platform = context.getCurrentXmlTest().getParameter("platform");
        if (platform == null) platform = "web";

        extent = ExtentManager.getInstance(platform);

        context.setAttribute("platform", platform);
        context.setAttribute("extent", extent);
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        System.out.println("Flushing Extent report for platform: " + platform);
        if (extent != null) {
            extent.flush();
        }
    }

}
