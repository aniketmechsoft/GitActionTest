//package com.uiFramework.Genesis.testbase;
//
//import java.io.File;
//
//import org.testng.ITestContext;
//import org.testng.ITestListener;
//import org.testng.ITestResult;
//
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.Status;
//import com.uiFramework.Genesis.helper.getScreenShot.CaptureScreen;
//
//public class UniversalListener implements ITestListener {
//
//	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
//	CaptureScreen screenObj = new CaptureScreen();
//
//	@Override
//	public void onTestStart(ITestResult result) {
//		ExtentTest test = extent.createTest(result.getMethod().getMethodName());
//		extentTest.set(test);
//		test.log(Status.INFO, result.getMethod().getMethodName() + " started");
//	}
//
//	@Override
//	public void onTestSuccess(ITestResult result) {
//		extentTest.get().log(Status.PASS, result.getMethod().getMethodName() + " passed");
//	}
//
//	
//	@Override
//	public void onTestFailure(ITestResult result) {
//	    extentTest.get().log(Status.FAIL, result.getThrowable());
//	    String imagePath = screenObj.getScreenShot(result.getName(), driver); // if this does NOT throw IOException
//	    extentTest.get().addScreenCaptureFromPath(imagePath); // this throws IOException
//	}
//
//
//	@Override
//	public void onTestSkipped(ITestResult result) {
//	    ExtentTest test = extentTest.get();
//	    if (test == null) {
//	        test = extent.createTest(result.getMethod().getMethodName());
//	        extentTest.set(test);
//	    }
//	    test.log(Status.SKIP, result.getMethod().getMethodName() + " skipped");
//	    if (result.getThrowable() != null) {
//	        test.log(Status.SKIP, result.getThrowable());
//	    }
//	}
//
//
//	@Override
//	public void onStart(ITestContext context) {
//	    // Clean up existing screenshots before the run starts
//	    File screenshotDir = new File(System.getProperty("user.dir") + "/src/main/resources/screenShots/");
//	    if (screenshotDir.exists()) {
//	        for (File file : screenshotDir.listFiles()) {
//	            if (file.isFile()) {
//	                file.delete(); // delete individual file
//	            }
//	        }
//	    }
//	    System.out.println("Old screenshots cleared before test execution!");
//	}
//
//
//	@Override
//	public void onFinish(ITestContext context) {
//		extent.flush();
//	}
//
//}
