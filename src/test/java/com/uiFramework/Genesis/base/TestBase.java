package com.uiFramework.Genesis.base;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.uiFramework.Genesis.browserConfiguration.BrowserFactory;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.CaptureScreen;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.utils.ExtentManager;

public class TestBase extends UniversalBase{
	public static WebDriver driver;
	public static ExtentReports extent;
	public static ExtentTest test;
	public CommonMethods cmObj = new CommonMethods(null);
	CaptureScreen screenObj = new CaptureScreen();
	protected static ReadJsonData jsonData;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception {
		if (!"web".equalsIgnoreCase(System.getProperty("platform", "web"))) {
	        throw new SkipException("Skipping web setup for non-web platform");
	    }
		
		extent = ExtentManager.getInstance("Web");
		
		String platform = System.getProperty("platform", "web");
		extent = ExtentManager.getInstance(platform);
		System.out.println("Extent report initialized for platform: " + platform);

	
		String browserName = "chrome";
		
		driver = BrowserFactory.getDriver(browserName);
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		test = extent.createTest(getClass().getSimpleName());
		jsonData = new ReadJsonData();

		if (!browserName.equalsIgnoreCase("safari")) {
		    driver.get("https://devwebdave.azurewebsites.net/");
		}
		
		cmObj.loginToApplication(driver, "Jordan", "GL083654");

	}

	@AfterSuite(alwaysRun = true)
	public void tearDown() {
		// driver.close();
		System.out.println("Flushing Extent report for platform: " + platform);
		extent.flush();

	}

}
