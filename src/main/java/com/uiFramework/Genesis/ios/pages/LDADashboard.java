package com.uiFramework.Genesis.ios.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.time.Duration;

public class LDADashboard extends CommonClass {
	
	IOSDriver driver;
	WebDriverWait wait;
	
	/**
	 * Constructor to initialize the LDADashboard page.
	 * Sets up driver, PageFactory, and WebDriverWait.
	 *
	 * @param driver - Instance of IOSDriver for interacting with the iOS app
	 */
	public LDADashboard(IOSDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// ---------------- Locators ---------------- //
	private By pieceCountPendingElement = By.xpath("//XCUIElementTypeStaticText[@name=\"Available\"]");
	private By pieceCountPendingCount = By.xpath("(//XCUIElementTypeStaticText)[4]");

	private By availableElement = By.xpath("//*[@name='Available' and @label='Available' and @value='Available']");
	private By availableElementCount = By.xpath("(//XCUIElementTypeStaticText)[6]");

	private By scheduledElement = By.xpath("//*[@name='Scheduled' and @label='Scheduled' and @value='Scheduled']");
	private By scheduledElementCount = By.xpath("(//XCUIElementTypeStaticText)[8]");

	private By deliveredElement = By.xpath("//XCUIElementTypeStaticText[@name='Delivered']");
	private By deliveredElementCount = By.xpath("(//XCUIElementTypeStaticText)[10]");

	// ---------------- Actions ---------------- //

	/**
	 * Clicks on the "Piece Count Pending" element on the dashboard.
	 */
	public void clickPieceCountPending() {
		click(pieceCountPendingElement);
	}

	/**
	 * Clicks on the "Available" element on the dashboard.
	 */
	public void clickAvailable() {
		click(availableElement);
	}

	/**
	 * Clicks on the "Scheduled" element on the dashboard.
	 */
	public void clickScheduled() {
		click(scheduledElement);
	}

	/**
	 * Clicks on the "Delivered" element on the dashboard.
	 */
	public void clickDelivered() {
		click(deliveredElement);
	}

	// ---------------- Get Methods ---------------- //

	/**
	 * Fetches the text value of the "Piece Count Pending" count.
	 *
	 * @return String representing the pending piece count
	 */
	public String getPieceCountPending() {
		return getText(driver, pieceCountPendingCount);
	}

	/**
	 * Fetches the text value of the "Available" count.
	 *
	 * @return String representing the available count
	 */
	public String getAvailableCount() {
		return getText(driver, availableElementCount);
	}

	/**
	 * Fetches the text value of the "Scheduled" count.
	 *
	 * @return String representing the scheduled count
	 */
	public String getScheduledCount() {
		return getText(driver, scheduledElementCount);
	}

	/**
	 * Fetches the text value of the "Delivered" count.
	 *
	 * @return String representing the delivered count
	 */
	public String getDeliveredCount() {
		return getText(driver, deliveredElementCount);
	}
}
