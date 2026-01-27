package com.uiFramework.Genesis.base;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.net.URL;
import com.uiFramework.Genesis.android.pages.LoginPage;
import com.uiFramework.Genesis.ios.pages.HomePage;
import com.uiFramework.Genesis.utils.AppiumUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;

/**
 * BaseTest class serves as the foundation for all Appium test classes. It
 * handles platform detection, driver setup, and lifecycle management for both
 * Android and iOS platforms.
 *
 * Author: Swapnil Shingare
 */
public class BaseTest extends AppiumUtils {

	/** Enum to define supported mobile platforms. */
	public enum Platform {
		ANDROID, IOS
	}

	// Shared driver and Appium service instances
	// protected IOSDriver driver;
	protected AppiumDriver driver;
	protected AppiumDriverLocalService service;

	// Commonly used page objects
	protected LoginPage loginPage;
	protected HomePage homePage;

	/**
	 * Initializes and configures the Appium environment before test execution.
	 * 
	 * @param platformParam Optional parameter (from TestNG or system property)
	 *                      indicating the platform ("ios" or "android").
	 * @throws Exception if setup fails or configuration is missing. platformParam
	 *                   -- commented
	 */
	@Parameters({ "platform" })
	@BeforeClass(alwaysRun = true)
	public void configureAppium(@Optional String platformParam) throws Exception {
		// Check if platform is web — if so, skip Appium setup entirely
		String platformType = System.getProperty("platform", "Android");
		if (platformType == null || platformType.equalsIgnoreCase("web")) {
			System.out.println("Skipping Appium setup since platform=web");
			throw new SkipException("Skipping Appium setup for web run");
		}

		// Identify which platform (iOS or Android) to use
		Platform resolvedPlatform = resolvePlatform(platformType);

		// Load Appium server configuration
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\appiumConfig.properties");) {
			prop.load(fis);
		}

		// Fetch IP address and port (with system property override support)
		String ipAddress = System.getProperty("ipAddress", prop.getProperty("ipAddress", "127.0.0.1"));
		int port = Integer.parseInt(prop.getProperty("port", "4723"));

		// Start the Appium server programmatically
		service = startAppiumServer(ipAddress, port);

		// Launch the platform-specific setup
		switch (resolvedPlatform) {
		case IOS:
			setupIOS();
			break;
		case ANDROID:
			setupAndroid();
			break;
		default:
			throw new SkipException("Unknown platform: " + resolvedPlatform);
		}
	}

	/**
	 * Determines which platform to run tests on, based on system property, TestNG
	 * parameter, or default OS.
	 *
	 * @param platformParam Platform passed from TestNG suite.
	 * @return Platform enum (IOS or ANDROID)
	 */
	private Platform resolvePlatform(String platformParam) {
		String fromSysProp = System.getProperty("platform");
		String raw = (fromSysProp != null ? fromSysProp : platformParam);

		if (raw == null || raw.isBlank()) {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("mac"))
				return Platform.IOS;
			return Platform.ANDROID;
		}

		String normalized = raw.trim().toLowerCase();
		switch (normalized) {
		case "ios":
			return Platform.IOS;
		case "android":
			return Platform.ANDROID;
		default:
			throw new SkipException("Unsupported platform value: " + raw);
		}
	}

	/**
	 * Performs iOS-specific driver setup using XCUITest. Loads iOS configuration
	 * values from a properties file and initializes the IOSDriver instance.
	 *
	 * @throws Exception if configuration or driver initialization fails.
	 */
	private void setupIOS() throws Exception {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/configfile/iosConfig.properties")) {
			prop.load(fis);
		}

		XCUITestOptions options = new XCUITestOptions();
		options.setDeviceName(prop.getProperty("deviceName"));
		options.setUdid(prop.getProperty("udid"));
		options.setPlatformVersion(prop.getProperty("platformVersion"));
		options.setWdaLaunchTimeout(Duration.ofSeconds(Long.parseLong(prop.getProperty("wdaLaunchTimeout"))));
		options.setCapability("bundleId", prop.getProperty("bundleId"));

		// iOS driver init commented for now
		// driver = new IOSDriver(service.getUrl(), options);
		// driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
		// Long.parseLong(prop.getProperty("implicitWait"))));
		// homePage = new HomePage((IOSDriver) driver);
	}

	/**
	 * Performs Android-specific driver setup using UiAutomator2. Loads Android
	 * configuration values from a properties file and initializes the Android
	 * driver session.
	 *
	 * @throws Exception if configuration or driver initialization fails.
	 */
	private void setupAndroid() throws Exception {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
		        System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\androidConfig.properties")) {
		    prop.load(fis);
		}

		UiAutomator2Options options = new UiAutomator2Options();
		options.setDeviceName(prop.getProperty("androidDeviceName"));
//		options.setAppPackage(prop.getProperty("androidAppPackage"));
//		options.setAppActivity(prop.getProperty("androidAppActivity"));
		options.setApp("C:\\Users\\EMP256\\Downloads\\dev (1).apk");

		// Uncomment the below section when ready to run Android tests
		driver = new AndroidDriver(new URL(prop.getProperty("androidAppiumServer")), options);
		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Long.parseLong(prop.getProperty("androidImplicitWait"))));

		System.out.println("Android session started on " + prop.getProperty("androidDeviceName"));
	}

	/**
	 * Cleans up test resources after all tests are executed. Ensures that both
	 * driver and Appium service are stopped properly.
	 */
//    @AfterClass(alwaysRun = true)
	public void tearDown() {
		try {
			if (driver != null)
				driver.quit();
		} catch (Exception ignored) {
		}
		try {
			if (service != null && service.isRunning())
				service.stop();
		} catch (Exception ignored) {
		}
	}
}
