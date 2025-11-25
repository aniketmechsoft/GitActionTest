package com.uiFramework.Genesis.ios.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Page Object Model class representing the "Available" page in the iOS Genesis
 * App.
 * <p>
 * This class contains all iOS-specific locators and reusable methods to
 * interact with UI elements on the "Available" screen.
 * </p>
 * 
 * Author: Swapnil Shingare
 */
public class AvailablePage extends CommonClass {

	AppiumDriver driver;
	WebDriverWait wait;

	/**
	 * Constructor initializes the driver, WebDriverWait, and PageFactory for iOS
	 * element mapping.
	 *
	 * @param driver The AppiumDriver instance controlling the iOS session.
	 */
	public AvailablePage(AppiumDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// =======================
	// iOS Element Locators
	// =======================
	private By lbol = By.xpath("(//XCUIElementTypeStaticText)[3]");
	private By consignee = By.xpath("(//XCUIElementTypeStaticText)[6]");
	private By consigneeCntact = By.xpath("(//XCUIElementTypeStaticText)[8]");
	private By custName = By.xpath("(//XCUIElementTypeStaticText)[10]");
	private By wt = By.xpath("(//XCUIElementTypeStaticText)[15]");
	private By delLocation = By.xpath("(//XCUIElementTypeStaticText)[17]");
	private By conReqDate = By.xpath("(//XCUIElementTypeStaticText)[19]");
	private By delConfirmation = By.xpath("(//XCUIElementTypeButton[@name=\"DELIVERY CONFIRMATION\"])[1]");

	// Locators from Order Details section
	private By noOfPieceOnDetails = By.xpath("(//XCUIElementTypeStaticText)[5]");
	private By wtOnDetails = By.xpath("(//XCUIElementTypeStaticText)[10]");
	private By consiNameOnDetails = By.xpath("(//XCUIElementTypeStaticText)[12]");
	private By consignCntctOnDetails = By.xpath("(//XCUIElementTypeStaticText)[14]");
	private By custOrdNoOnDetails = By.xpath("(//XCUIElementTypeStaticText)[16]");
	private By ordProcessOnDetails = By.xpath("(//XCUIElementTypeStaticText)[18]");
	private By actOrdPiecesOnDetails = By.xpath("(//XCUIElementTypeStaticText)[20]");
	private By litOrdPiecesOnDetails = By.xpath("(//XCUIElementTypeStaticText)[22]");

	// Input fields and buttons for "Mark Received" workflow
	private By damageChkbox = By.xpath("(//XCUIElementTypeSwitch[@value='0'])[1]");
	private By recOrd = By.xpath("//XCUIElementTypeOther[@name=\"Genesis App\"]/XCUIElementTypeTextField[1]");
	private By recLit = By.xpath("(//XCUIElementTypeTextField[@value='0'])[1]");
	private By damageOrd = By.xpath("//XCUIElementTypeOther[@name='Genesis App']/XCUIElementTypeTextField[3]");
	private By damageLit = By.xpath("//XCUIElementTypeOther[@name='Genesis App']/XCUIElementTypeTextField[4]");
	private By cancelBtn = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");
	private By confirmBtn = By.xpath("//XCUIElementTypeButton[@name='CONFIRM']");
	private By markRec = By.xpath("//XCUIElementTypeStaticText[@name='Mark received']");

	// ======================================
	// Getter Methods (Text Extraction)
	// ======================================

	/** Fetches the Load Bill of Lading (L-BOL) text. */
	public String getLBOL() {
		return getText(driver, lbol);
	}

	/** Fetches the Consignee name. */
	public String getConsignee() {
		return getText(driver, consignee);
	}

	/** Fetches the Consignee contact number. */
	public String getConsigneeContact() {
		return getText(driver, consigneeCntact);
	}

	/** Fetches the Customer name. */
	public String getCustName() {
		return getText(driver, custName);
	}

	/** Fetches the total weight text. */
	public String getWt() {
		return getText(driver, wt);
	}

	/** Fetches the Delivery Location text. */
	public String getDelieveryLocation() {
		return getText(driver, delLocation);
	}

	/** Fetches the Scheduled Delivery Date text. */
	public String getScheduledDate() {
		return getText(driver, conReqDate);
	}

	/** Fetches the Delivery Confirmation text/button label. */
	public String getDelieveryDate() {
		return getText(driver, delConfirmation);
	}

	/** Fetches number of pieces from the details section. */
	public String getNoOfPieceOnDetails() {
		return getText(driver, noOfPieceOnDetails);
	}

	/** Fetches the weight value from the details section. */
	public String getwtOnDetails() {
		return getText(driver, wtOnDetails);
	}

	/** Fetches the Consignee name from the details view. */
	public String getConsignNameOnDetails() {
		return getText(driver, consiNameOnDetails);
	}

	/** Fetches the Consignee contact number from details view. */
	public String getConsignCntctOnDetails() {
		return getText(driver, consignCntctOnDetails);
	}

	/** Fetches the Customer Order Number from details view. */
	public String getCustOrdNoOnDetails() {
		return getText(driver, custOrdNoOnDetails);
	}

	/** Fetches the Order Processing status from details view. */
	public String getOrdProcessOnDetails() {
		return getText(driver, ordProcessOnDetails);
	}

	/** Fetches the Actual Ordered Pieces count. */
	public String getActOrdPiecesOnDetails() {
		return getText(driver, actOrdPiecesOnDetails);
	}

	/** Fetches the LIT Ordered Pieces count. */
	public String getLitOrdPiecesOnDetails() {
		return getText(driver, litOrdPiecesOnDetails);
	}

	// ======================================
	// Click Actions
	// ======================================

	/** Clicks on the "Damage" checkbox. */
	public void clickDamageCheckbox() {
		click(damageChkbox);
	}

	/** Clicks on the "Cancel" button. */
	public void clickCancelBtn() {
		click(cancelBtn);
	}

	/** Clicks on the "Confirm" button. */
	public void clickConfirmBtn() {
		click(confirmBtn);
	}

	/** Clicks on the "Mark Received" text/button. */
	public void clickMarkReceived() {
		click(markRec);
	}

	// ======================================
	// Data Entry Methods
	// ======================================

	/** Enters received order pieces count. */
	public void enterRecOrdPieces(String pieces) {
		click(recOrd);
		enterValue(recOrd, pieces);
	}

	/** Enters received LIT pieces count. */
	public void enterLitOrdPieces(String pieces) {
		click(recLit);
		enterValue(recLit, pieces);
	}

	/** Enters damaged order pieces count. */
	public void enterDamageOrdPieces(String pieces) {
		click(damageOrd);
		enterValue(damageOrd, pieces);
	}

	/** Enters damaged LIT pieces count. */
	public void enterLitDamagePieces(String pieces) {
		click(damageLit);
		enterValue(damageLit, pieces);
	}
}
