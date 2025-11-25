package com.uiFramework.Genesis.ios.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Page Object Model (POM) class representing the "Piece Count Pending" screen in the Genesis iOS App.
 * 
 * <p>This class provides reusable methods to interact with and retrieve data from
 * the iOS screen elements during IPA automation testing using Appium.</p>
 * 
 * Author: Swapnil Shingare
 */
public class PieceCountPendingPage extends CommonClass{

	AppiumDriver driver;

	/**
	 * Constructor initializes the AppiumDriver instance and
	 * decorates all elements using AppiumFieldDecorator.
	 *
	 * @param driver Active AppiumDriver instance for the iOS app session.
	 */
	public PieceCountPendingPage(AppiumDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	// ======================================
	// Locator Definitions
	// ======================================

	// Summary card fields
	private By searchManifest = By.xpath("//android.widget.EditText");
	private By manifest = By.xpath("(//XCUIElementTypeStaticText)[3]");
	private By consignee = By.xpath("(//XCUIElementTypeStaticText)[5]");
	private By createdDate = By.xpath("(//XCUIElementTypeStaticText)[7]");
	private By trkingNo = By.xpath("(//XCUIElementTypeStaticText)[10]");
	private By eta = By.xpath("(//XCUIElementTypeStaticText)[11]");


	// Locators under "Order Details" section
	private By manifestOnDetails = By.xpath("(//XCUIElementTypeStaticText)[4]");
	private By createdDateOnDetails = By.xpath("(//XCUIElementTypeStaticText)[6]");
	private By palletOnDetails = By.xpath("(//XCUIElementTypeStaticText)[9]");
	private By custOrdNoOnDetails = By.xpath("(//XCUIElementTypeStaticText)[11]");
	private By ordProcessOnDetails = By.xpath("(//XCUIElementTypeStaticText)[13]");
	private By custNameOnDetails = By.xpath("(//XCUIElementTypeStaticText)[15]");
	private By consigneeOnDetails = By.xpath("(//XCUIElementTypeStaticText)[17]");
	private By delLocationOnDetails = By.xpath("(//XCUIElementTypeStaticText)[19]");

	// Input fields and buttons for "Mark Received" workflow
	private By recOrd = By.xpath("//XCUIElementTypeOther[@name=\"Genesis App\"]/XCUIElementTypeTextField[1]");
	private By recLit = By.xpath("(//XCUIElementTypeTextField[@value='0'])[1]");
	private By cancelBtn = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");
	private By confirmBtn = By.xpath("//XCUIElementTypeButton[@name='CONFIRM']");

	// ======================================
	// Getter Methods (Text Extraction)
	// ======================================

	/** 
	 * Retrieves the Manifest (Load Bill of Lading) number from the summary section.
	 * @return Manifest number text.
	 */
	public String getManifest() { return getText(driver, manifest); }

	/** 
	 * Retrieves the Consignee name from the summary section.
	 * @return Consignee name text.
	 */
	public String getConsignee() { return getText(driver, consignee); }

	/** 
	 * Retrieves the Created Date of the shipment from the summary section.
	 * @return Created date text.
	 */
	public String getCreateddate() { return getText(driver, createdDate); }

	/** 
	 * Retrieves the Tracking Number from the summary section.
	 * @return Tracking number text.
	 */
	public String getTrkingNo() { return getText(driver, trkingNo); }

	/** 
	 * Retrieves the Estimated Time of Arrival (ETA) from the summary section.
	 * @return ETA text.
	 */
	public String getETA() { return getText(driver, eta); }

	/** 
	 * Retrieves the Manifest value displayed on the Order Details screen.
	 * @return Manifest text from details view.
	 */
	public String getManifestOnDetails() { return getText(driver, manifestOnDetails); }

	/** 
	 * Retrieves the Created Date from the Order Details screen.
	 * @return Created date text from details view.
	 */
	public String getCreatedDateOnDetails() { return getText(driver, createdDateOnDetails); }

	/** 
	 * Retrieves the Pallet information from the Order Details section.
	 * @return Pallet text value.
	 */
	public String getPalletOnDetails() { return getText(driver, palletOnDetails); }

	/** 
	 * Retrieves the Customer Order Number from the Order Details section.
	 * @return Customer Order Number text.
	 */
	public String getCustOrdNoOnDetails() { return getText(driver, custOrdNoOnDetails); }

	/** 
	 * Retrieves the Order Process status from the Order Details section.
	 * @return Order Process status text.
	 */
	public String getOrdProcessOnDetails() { return getText(driver, ordProcessOnDetails); }

	/** 
	 * Retrieves the Customer Name from the Order Details section.
	 * @return Customer Name text.
	 */
	public String getCustNameOnDetails() { return getText(driver, custNameOnDetails); }

	/** 
	 * Retrieves the Consignee Name displayed in the Order Details section.
	 * @return Consignee text from details view.
	 */
	public String getConsigneeOnDetails() { return getText(driver, consigneeOnDetails); }

	/** 
	 * Retrieves the Delivery Location from the Order Details section.
	 * @return Delivery Location text.
	 */
	public String getDelLocationOnDetails() { return getText(driver, delLocationOnDetails); }

	// ======================================
	// Click Actions
	// ======================================

	/** Clicks the "Cancel" button in the Mark Received dialog. */
	public void clickCancelBtn() { click(cancelBtn); }

	/** Clicks the "Confirm" button in the Mark Received dialog. */
	public void clickConfirmBtn() { click(confirmBtn); }

	// ======================================
	// Data Entry Methods
	// ======================================

	/**
	 * Clicks and enters the received order pieces count.
	 * @param pieces Number of pieces to be entered.
	 */
	public void enterRecOrdPieces(String pieces) {
		click(recOrd);
		enterValue(recOrd, pieces);
	}

	/**
	 * Clicks and enters the received LIT (Line Item Tracking) count.
	 * @param pieces Number of LIT pieces to be entered.
	 */
	public void enterLitOrdPieces(String pieces) {
		click(recLit);
		enterValue(recLit, pieces);
	}
	
	public void enterManifest(String manifest) {
		click(searchManifest);
		enterValue(searchManifest, manifest);
	}
}


