package com.uiFramework.Genesis.ios.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Represents the "Scheduled" page in the Genesis iOS application.
 * <p>
 * This class encapsulates all UI element locators and actions related to
 * Scheduled orders, including fetching delivery details, interacting with
 * checkboxes, and performing CRUD-like UI operations.
 * </p>
 * 
 * <p>
 * Design follows Page Object Model (POM) with reusability and maintainability
 * in mind. All interactions are wrapped in robust waits to handle iOS
 * animations and dynamic loads.
 * </p>
 * 
 * Author: Swapnil Shingare
 */
public class ScheduledPage extends CommonClass {

	AppiumDriver driver;

	/**
	 * Constructor to initialize the Scheduled Page with an existing AppiumDriver instance.
	 * Also initializes all elements via AppiumFieldDecorator.
	 *
	 * @param driver The active AppiumDriver instance.
	 */
	public ScheduledPage(AppiumDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	// =======================
	// iOS Element Locators
	// =======================
	private By lbol = By.xpath("(//XCUIElementTypeStaticText)[3]");
	private By histortBtn = By.xpath("(//XCUIElementTypeStaticText[@name='History'])");
	private By consignee = By.xpath("(//XCUIElementTypeStaticText)[6]");
	private By consigneeCntact = By.xpath("(//XCUIElementTypeStaticText)[8]");
	private By custName = By.xpath("(//XCUIElementTypeStaticText)[10]");
	private By wt = By.xpath("(//XCUIElementTypeStaticText)[15]");
	private By delLocation = By.xpath("(//XCUIElementTypeStaticText)[17]");
	private By scheduledDate1 = By.xpath("(//XCUIElementTypeStaticText)[19]");
	private By scheduledDate2 = By.xpath("(//XCUIElementTypeStaticText)[20]");
	private By scheduledDate3 = By.xpath("(//XCUIElementTypeStaticText)[21]");
	private By reqDate = By.xpath("(//XCUIElementTypeStaticText)[23]");
	private By reqDateCheckbox = By.xpath("//XCUIElementTypeOther[@name='SCHEDULED, tab panel']/XCUIElementTypeOther[18]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther");
	private By searchLBOL = By.xpath("(//XCUIElementTypeTextField)[1]");
	private By setScheduledDate1 = By.xpath("(//XCUIElementTypeTextField)[2]");
	private By setScheduledDate2 = By.xpath("(//XCUIElementTypeTextField)[3]");
	private By setScheduledDate3 = By.xpath("(//XCUIElementTypeTextField)[4]");
	private By updateBtn = By.xpath("(//XCUIElementTypeButton[@name='UPDATE'])");
	private By acceptScheduledBtn = By.xpath("//XCUIElementTypeButton[@name='ACCEPT SCHEDULED']");
	private By delConfirmationBtn = By.xpath("(//XCUIElementTypeButton[@name='DELIVERY CONFIRMATION'])");
	private By closeOnHistoryPopup = By.xpath("(//XCUIElementTypeOther[@name=\"Vertical scroll bar, 2 pages Horizontal scroll bar, 2 pages Horizontal scroll bar, 1 page Vertical scroll bar, 11 pages Vertical scroll bar, 8 pages Horizontal scroll bar, 1 page Vertical scroll bar, 1 page Horizontal scroll bar, 1 page\"])[6]/XCUIElementTypeWebView/XCUIElementTypeWebView/XCUIElementTypeWebView/XCUIElementTypeOther/XCUIElementTypeOther");
	private By reqDateOnPopup = By.xpath("(//XCUIElementTypeStaticText)[last() - 4]");

	// ======================================
	// Getter Methods (Text Extraction)
	// ======================================

	/** Fetches the Load Bill of Lading (L-BOL) text. */
	public String getLBOL() { return getText(driver, lbol); }

	/** Fetches the Consignee name. */
	public String getConsignee() { return getText(driver, consignee); }

	/** Fetches the Consignee contact number. */
	public String getConsigneeContact() { return getText(driver, consigneeCntact); }

	/** Fetches the Customer name. */
	public String getCustName() { return getText(driver, custName); }

	/** Fetches the total weight text. */
	public String getWt() { return getText(driver, wt); }

	/** Fetches the Delivery Location text. */
	public String getDeliveryLocation() { return getText(driver, delLocation); }

	/** Fetches the Scheduled Delivery Date text. */
	public String getScheduledDeliveryDate() { return getText(driver, scheduledDate1); }

	/** Fetches the actual Delivery Date text. */
	public String getDeliveryDate() { return getText(driver, scheduledDate2); }

	/** Fetches the number of pieces from the details section. */
	public String getNoOfPiecesOnDetails() { return getText(driver, scheduledDate3); }

	/** Fetches the weight value from the details section. */
	public String getWeightOnDetails() { return getText(driver, reqDate); }

	/** Fetches the Consignee Name displayed on the details view popup. */
	public String getConsigneeNameOnDetails() { return getText(driver, reqDateOnPopup); }

	/** Fetches the Required Delivery Date displayed on the popup. */
	public String getReqDateOnPopup() { return getText(driver, reqDateOnPopup); }

	// ======================================
	// Click Actions
	// ======================================

	/** Clicks the "Update" button. */
	public void clickUpdateBtn() { click(updateBtn); }

	/** Clicks the "Accept Scheduled" button. */
	public void clickAcceptScheduleBtn() { click(acceptScheduledBtn); }

	/** Clicks the "Delivery Confirmation" button. */
	public void clickDeliveryConfirmBtn() { click(delConfirmationBtn); }

	/** Closes the History popup window. */
	public void clickCloseOnHistoryPopup() { click(closeOnHistoryPopup); }

	/** Toggles the Required Date checkbox. */
	public void clickReqDateCheckbox() { click(reqDateCheckbox); }

	/** Clicks on the History button. */
	public void clickHistoryBtn() { click(histortBtn); }

	/** Alternative method to close the History popup. */
	public void closeHistoryPopup() { click(closeOnHistoryPopup); }

	// ======================================
	// Data Entry Methods
	// ======================================

	/** Searches for a specific L-BOL number by entering it in the search field. */
	public void searchLBOL(String pieces) {
		click(searchLBOL);
		enterValue(searchLBOL, pieces);
	}

	/** Enters the first scheduled delivery date. */
	public void setScheduledDate1(String pieces) {
		click(setScheduledDate1);
		enterValue(setScheduledDate1, pieces);
	}

	/** Enters the second scheduled delivery date. */
	public void setScheduledDate2(String pieces) {
		click(setScheduledDate2);
		enterValue(setScheduledDate2, pieces);
	}

	/** Enters the third scheduled delivery date. */
	public void setScheduledDate3(String pieces) {
		click(setScheduledDate3);
		enterValue(setScheduledDate3, pieces);
	}

}
