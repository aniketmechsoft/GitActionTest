package com.uiFramework.Genesis.android.pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.uiFramework.Genesis.utils.AndroidActions;
import com.uiFramework.Genesis.web.pages.CommonPage;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Page Object Model (POM) class representing the "Piece Count Pending" screen
 * in the Genesis andrid App.
 * 
 * <p>
 * This class provides reusable methods to interact with and retrieve data from
 * the iOS screen elements during IPA automation testing using Appium.
 * </p>
 * 
 * Author: Swapnil Shingare
 */

public class PieceCountPendingPage extends CommonClass {

//	AppiumDriver driver;
	AndroidActions Action;
	/**
	 * Constructor initializes the AppiumDriver instance and decorates all elements
	 * using AppiumFieldDecorator.
	 *
	 * @param driver Active AppiumDriver instance for the iOS app session.
	 */
	public PieceCountPendingPage(AndroidDriver driver) {
		super(driver);
		this.driver = driver;
		Action=new AndroidActions(driver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
	}

	// ======================================
	// Locator Definitions
	// ======================================

	// Summary card fields
	private By searchManifest = By.xpath("//android.widget.EditText");
	private By manifest = By.xpath("//android.view.View[@text='Manifest']/following-sibling::android.view.View[1]");
	private By consignee = By.xpath("//android.view.View[@text='Manifest']/following-sibling::android.view.View[3]");
	private By createdDate = By.xpath("//android.view.View[@text='Manifest']/following-sibling::android.view.View[5]");
	private By trkingNo = By.xpath("//android.view.View[@text='ETA']/following-sibling::android.view.View[1]");
	private By eta = By.xpath("//android.view.View[@text='ETA']/following-sibling::android.view.View[2]");

	// Locators under "Order Details" section
	private By manifestOnDetails = By
			.xpath("//android.view.View[@text='Manifest']/following-sibling::android.view.View[1]");
	private By createdDateOnDetails = By
			.xpath("//android.view.View[@text='Manifest']/following-sibling::android.view.View[3]");
	private By palletOnDetails = By.xpath("//android.view.View[contains(@text, 'OBPL-')]");
	private By custOrdNoOnDetails = By.xpath("(//android.view.View)[18]");
	private By ordProcessOnDetails = By.xpath("(//android.view.View)[20]");
	private By custNameOnDetails = By.xpath("(//android.view.View)[15]");
	private By consigneeOnDetails = By.xpath("(//android.view.View)[17]");
	private By delLocationOnDetails = By.xpath("(//android.view.View)[19]");

	// Input fields and buttons for "Mark Received" workflow
	private By recOrd = By.xpath("//XCUIElementTypeOther[@name=\"Genesis App\"]/XCUIElementTypeTextField[1]");
	private By recLit = By.xpath("(//XCUIElementTypeTextField[@value='0'])[1]");
	private By cancelBtn = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");
	private By confirmBtn = By.xpath("//XCUIElementTypeButton[@name='CONFIRM']");

	private By noRecFound = By.xpath("(//android.widget.TextView)[3]");
	private By pieceCntMenu = By.xpath("//android.view.View[@resource-id=\"swipeable-tabs-tab-1\"]");
	By elementLocator = By.xpath("//android.view.View[@resource-id='childLayout']//android.widget.TextView");
	private By okBtnOnTrkingPopup = By.xpath("//android.widget.Button[@text=\"OK\"]");

	// ======================================
	// Getter Methods (Text Extraction)
	// ======================================

	/**
	 * Retrieves the Manifest (Load Bill of Lading) number from the summary section.
	 * 
	 * @return Manifest number text.
	 */
	public String getManifest() {
		return getText(driver, manifest);
	}

	/**
	 * Retrieves the Consignee name from the summary section.
	 * 
	 * @return Consignee name text.
	 */
	public String getConsignee() {
		return getText(driver, consignee);
	}

	/**
	 * Retrieves the Created Date of the shipment from the summary section.
	 * 
	 * @return Created date text.
	 */
	public String getCreateddate() {
		return getText(driver, createdDate);
	}

	/**
	 * Retrieves the Tracking Number from the summary section.
	 * 
	 * @return Tracking number text.
	 */
	public String getTrkingNo() {
		return getText(driver, trkingNo);
	}

	/**
	 * Retrieves the Estimated Time of Arrival (ETA) from the summary section.
	 * 
	 * @return ETA text.
	 */
	public String getETA() {
		return getText(driver, eta);
	}

	/**
	 * Retrieves the Manifest value displayed on the Order Details screen.
	 * 
	 * @return Manifest text from details view.
	 */
	public String getManifestOnDetails() {
		return getText(driver, manifestOnDetails);
	}

	/**
	 * Retrieves the Created Date from the Order Details screen.
	 * 
	 * @return Created date text from details view.
	 */
	public String getCreatedDateOnDetails() {
		return getText(driver, createdDateOnDetails);
	}

	/**
	 * Retrieves the Pallet information from the Order Details section.
	 * 
	 * @return Pallet text value.
	 */
	public String getPalletOnDetails() {
		return getText(driver, palletOnDetails);
	}

	/**
	 * Retrieves the Customer Order Number from the Order Details section.
	 * 
	 * @return Customer Order Number text.
	 */
	public String getCustOrdNoOnDetails() {
		return getText(driver, custOrdNoOnDetails);
	}

	/**
	 * Retrieves the Order Process status from the Order Details section.
	 * 
	 * @return Order Process status text.
	 */
	public String getOrdProcessOnDetails() {
		return getText(driver, ordProcessOnDetails);
	}

	/**
	 * Retrieves the Customer Name from the Order Details section.
	 * 
	 * @return Customer Name text.
	 */
	public String getCustNameOnDetails() {
		return getText(driver, custNameOnDetails);
	}

	/**
	 * Retrieves the Consignee Name displayed in the Order Details section.
	 * 
	 * @return Consignee text from details view.
	 */
	public String getConsigneeOnDetails() {
		return getText(driver, consigneeOnDetails);
	}

	/**
	 * Retrieves the Delivery Location from the Order Details section.
	 * 
	 * @return Delivery Location text.
	 */
	public String getDelLocationOnDetails() {
		return getText(driver, delLocationOnDetails);
	}

	/**
	 * Retrieves the Delivery Location from the Order Details section.
	 * 
	 * @return Delivery Location text.
	 */
	public String getNoRecFoundMsg() {
		return getText(driver, noRecFound);
	}

	// ======================================
	// Click Actions
	// ======================================

	/** Clicks the "Cancel" button in the Mark Received dialog. */
	public void clickCancelBtn() {
		click(cancelBtn);
	}

	/** Clicks the "Confirm" button in the Mark Received dialog. */
	public void clickConfirmBtn() {
		click(confirmBtn);
	}

	public void clickPieceCntPendingMenu() {
		click(pieceCntMenu);
	}

	// ======================================
	// Data Entry Methods
	// ======================================

	/**
	 * Clicks and enters the received order pieces count.
	 * 
	 * @param pieces Number of pieces to be entered.
	 */
	public void enterRecOrdPieces(String pieces) {
		click(recOrd);
		enterValue(recOrd, pieces);
	}

	/**
	 * Clicks and enters the received LIT (Line Item Tracking) count.
	 * 
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

	public void assertForManifestData() {
		assertElementPresent(elementLocator);
	}

	public void elementLoop(By ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By listItem = By.xpath("//android.view.View[@resource-id='childLayout']//android.widget.TextView");
		List<WebElement> elements = driver.findElements(listItem);
		System.out.println("Total clickable elements found: " + elements.size());
		boolean found = false;
		for (int i = 0; i < elements.size(); i++) {
			try {
				elements = driver.findElements(listItem);
				WebElement current = elements.get(i);
				System.out.println("Clicking element #" + (i + 1));
				current.click();
				try {
					wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(ele));
					System.out.println("Target element found after clicking item #" + (i + 1));
					found = true;
					break;
				} catch (TimeoutException te) {
					System.out.println("Target element not found on item #" + (i + 1) + ". Going back...");
					driver.pressKey(new KeyEvent(AndroidKey.BACK));
					Thread.sleep(1000);
				}

			} catch (Exception e) {
				System.err.println("Error processing item #" + (i + 1) + ": " + e.getMessage());
				driver.pressKey(new KeyEvent(AndroidKey.BACK));
			}
		}

		if (!found) {
			System.out.println("Target element not found in any of the list items.");
		} else {
			System.out.println("Required element successfully found!");
		}
	}

	public void recOrderDisableValid() {
		elementLoop(recOrd);
	}

	public void TrkingNoNotUpdatedValid() {
		elementLoop(okBtnOnTrkingPopup);
	}
	
	/*
	 * Changes after Partial manifest
	 */
	
	private By backBtn=By.xpath("//android.widget.Button");
	private By confirmBtn1=By.xpath("//android.widget.Button[@text='CONFIRM']");
	private By yesBtn=By.xpath("//android.widget.Button[@text='YES']");///android.widget.Button[@text="YES"]
    private By noBtn=By.xpath("//android.widget.Button[translate(@text, 'no', 'NO')='NO']");
    private By enterRecpieces = By.xpath("//android.view.View[contains(@resource-id, 'GL-')]/android.widget.EditText[1]");
    private By openManifest = By.xpath("");
    private By recOrdpc = By.xpath("(//android.widget.TextView[@text='Received Order'])[1]/following-sibling::android.widget.EditText[1]");
	private By recLitOrd = By.xpath("(//android.widget.TextView[@text='Received Literature'])[1]/following-sibling::android.widget.EditText");
	private By confirmbtn = By.xpath("//android.widget.Button[@text='CONFIRM']");
	private By viewdetails = By.xpath("//android.widget.Button[@text='VIEW DETAILS']");
	private By getstatus = By.xpath("//android.widget.TextView[@text='Manifest Details']/following-sibling::android.widget.TextView[1]");
    
	/**
	 * Scrolls through the delivery list to find an LBOL entry with an enabled "Received Pieces" field.
	 * Attempts up to 10 scroll iterations, performing incremental partial scrolls in each iteration.
	 * Clicks the delivery confirmation button in each iteration and checks if the input field is enabled.
	 * If enabled, stops scrolling; otherwise, navigates back and continues searching.
	 *
	 * @throws InterruptedException if the thread sleep is interrupted.
	 */
    public void checkManifestAvailableForDelivery() throws InterruptedException {
    	Thread.sleep(2500);
        List<WebElement> availableManifest =
                driver.findElements(By.xpath("//android.widget.TextView[@text='Manifest']"));
        
         System.out.println("manifest grid "+availableManifest.size());
         
        for (int i = 1; i <= availableManifest.size(); i++) {

            // Click Manifest
            click(By.xpath("(//android.widget.TextView[@text='Manifest'])[" + i + "]"));
            Thread.sleep(2500);
            try {
            	 WebElement TrackingNotUpdate =
                         driver.findElement(By.xpath("//android.widget.Button[@text='OK']"));
            	 if(TrackingNotUpdate.isDisplayed()) {
            		 TrackingNotUpdate.click();
            		 continue;
                 }
	
            	} catch (Exception e) {
	      // TODO: handle exception
            	}
              
            List<WebElement> OBPLcount =
                    driver.findElements(By.xpath("//android.view.View[@resource-id='childLayout']"));
            
            String status = driver.findElement(
                    By.xpath("//android.view.View[@resource-id='root']/android.widget.TextView[3]")
            ).getText();

            // Get manifest number
          
            System.out.println("OBPL count: " + OBPLcount.size());
            System.out.println("Status: " + status);
            
            System.out.println("iteration is "+i);
            if (OBPLcount.size() >= 2 && status.equals("QA Pending")) {
            	
            	  WebElement manifestNumber = driver.findElement(
                          By.xpath("//android.widget.TextView[@text='Manifest']/following-sibling::android.widget.TextView[1]"));
            	  String manifest=manifestNumber.getText();
            	  
                System.out.println("Found Manifest for QA: " + manifest);
                break;
            } else {
                System.out.println("Wrong manifest: " + manifest);
                back() ;
                Thread.sleep(2500);
                if(i>=2) {
                	partialScrollInsideElement(innerPage);
                	System.out.println("Scroolling..");
                }
                continue;
            }
        }
    //}

	
//		for (int i = 1; i <= 10; i++) {
//		    System.out.println("Scroll attempt: " + i);
//		    
//		    for (int j = 1; j <= i; j++) {
//		        Action.partialScroll();
//		        System.out.println("→ partialScroll() called " + j + " time(s) in iteration " + i);
//		        Thread.sleep(800);
//		    }
//
//		   // click(delConfBtn_Android);
//		    WebElement RecPieces = driver.findElement(enterRecpieces);
//
//		    if (RecPieces.isEnabled()) {
//		    	System.out.println("EditText is enabled → breaking loop!");
//			    break; 
//			    
//		    } else {
//		    	back();
//		        System.out.println("EditText is disabled → going back and continuing search...");
//		    }
//		}
	}
	
	/**
	 * This method used to click on back button
	 */
	public void back() {
		click(backBtn);
	}
	
	/**
	 * This method used to click on confirm order 
	 */
	public void confirmOrder() {
		click(confirmBtn1);
		try {
			click(yesBtn);
			//driver.findElement(yesBtn).click();
			Thread.sleep(2500);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private static final Logger logger = Logger.getLogger(CommonPage.class.getName());
	/**
	 * This method used to Wait until loader is disable
	 */
	public void waitForLoaderToDisappear() throws TimeoutException {
		try {
			 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			 wait.until(driver -> {
				try {
					WebElement loader = driver
							.findElement(By.xpath("//android.widget.TextView[@text='Loading...']"));
					boolean isDisplayed = loader.isDisplayed();
					return !isDisplayed;
				} catch (NoSuchElementException e) {
					return true;
				}
			});
			logger.info("Loader has disappeared..");
		} catch (TimeoutException e) {
			logger.info("Timeout: Loader did not disappear within..seconds");
			throw e;
		}
	}
	
	WebElement innerPage =////android.view.View[@resource-id="childLayout"]
	        driver.findElement(By.xpath("(//android.widget.TextView[@text='Manifest'])[2]"));
	
	public void partialScrollInsideElement(WebElement element) {
		try {
	    Rectangle rect = element.getRect();

	    ((JavascriptExecutor) driver).executeScript(
	        "mobile: scrollGesture", ImmutableMap.of(
	            "left", rect.getX(), "top", rect.getY(), "width", rect.getWidth(), 
	            "height", rect.getHeight(), "direction", "down", "percent", 0.5
	       )
	    );
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	/**
	 * Clicks and enters the received order pieces count.
	 * 
	 * @param pieces Number of pieces to be entered.
	 * @throws InterruptedException 
	 */
	public void enterRecOrderPieces(String pieces) throws InterruptedException {
//		click(recOrdpc);
//		enterValue(recOrdpc, pieces);
		Action.enterTextWithPressKey(recOrdpc);
	}

	/**
	 * Clicks and enters the received LIT (Line Item Tracking) count.
	 * 
	 * @param pieces Number of LIT pieces to be entered.
	 * @throws InterruptedException 
	 */
	public void enterLitOrderPieces(String pieces) throws InterruptedException {
//		click(recLitOrd);
//		enterValue(recLitOrd, pieces);
		Thread.sleep(1000);
		Action.enterTextWithPressKey(recLitOrd);
		
	}
	
	/**
	 * This method used to Click on view details
	 */
	public void viewDetails() {
		
		click(viewdetails);
	}
	
	/**
	 * This method used to get manifest status
	 */
	public String getmanifestStatus() {
		return	getText(driver, getstatus);
	}	
	
	


}
