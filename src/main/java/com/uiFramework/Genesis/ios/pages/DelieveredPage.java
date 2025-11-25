package com.uiFramework.Genesis.ios.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Page Object Model class representing the "Delievered" page in the iOS Genesis App.
 * <p>
 * This class contains all iOS-specific locators and reusable methods 
 * to interact with UI elements on the "Delievered" screen.
 * </p>
 * 
 * Author: Swapnil Shingare
 */

public class DelieveredPage extends CommonClass{

	// iOS driver instance
	IOSDriver driver;

	// Explicit wait for element synchronization
	WebDriverWait wait;

	// Constructor initializes driver and page factory
	public DelieveredPage(IOSDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// =====================================
	// Element Locators (XCUIElementTypeStaticText)
	// =====================================

	private By lbol = By.xpath("(//XCUIElementTypeStaticText)[3]");
	private By podUpload = By.xpath("(//XCUIElementTypeStaticText)[4]");
	private By consignee = By.xpath("(//XCUIElementTypeStaticText)[6]");
	private By consigneeCntact = By.xpath("(//XCUIElementTypeStaticText)[8]");
	private By custName = By.xpath("(//XCUIElementTypeStaticText)[10]");
	private By wt = By.xpath("(//XCUIElementTypeStaticText)[15]");
	private By delLocation = By.xpath("(//XCUIElementTypeStaticText)[17]");
	private By scheduledDate = By.xpath("(//XCUIElementTypeStaticText)[19]");
	private By delDate = By.xpath("(//XCUIElementTypeStaticText)[21]");

	// =====================================
	// Common Actions
	// =====================================

	/**
	 * Clicks on the Delivered element.
	 * Waits until the element is clickable and then performs the click.
	 */
	public void clickDelivered() {
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(lbol));
			element.click();
			System.out.println("Clicked on Delivered element");
		} catch (Exception e) {
			System.err.println("Failed to click Delivered: " + e.getMessage());
		}
	}

	// =====================================
	// Getter Methods for Each UI Element
	// =====================================

	/** Returns the L-BOL (Load Bill of Lading) text. */
	public String getLBOL() {
		return getText(driver, lbol);
	}

	/** Returns the Consignee (receiver) name text. */
	public String getConsignee() {
		return getText(driver, consignee);
	}

	/** Returns the Consignee Contact number text. */
	public String getConsigneeContact() {
		return getText(driver, consigneeCntact);
	}

	/** Returns the Customer name text. */
	public String getCustName() {
		return getText(driver, custName);
	}

	/** Returns the Weight text value. */
	public String getWt() {
		return getText(driver, wt);
	}

	/** Returns the Delivery Location text. */
	public String getDelieveryLocation() {
		return getText(driver, delLocation);
	}

	/** Returns the Scheduled Delivery Date text. */
	public String getScheduledDate() {
		return getText(driver, scheduledDate);
	}

	/** Returns the Actual Delivery Date text. */
	public String getDelieveryDate() {
		return getText(driver, delDate);
	}

	// =====================================
	// POD (Proof of Delivery) Action
	// =====================================

	/**
	 * Clicks on the POD Upload option.
	 * Typically used to open the upload screen for proof of delivery.
	 */
	public void clickPopUpload() {
		click(podUpload);
	}
}
