package com.uiFramework.Genesis.android.pages;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Page Object Model class representing the "Delievered" page in the android
 * Genesis App.
 * <p>
 * This class contains all iOS-specific locators and reusable methods to
 * interact with UI elements on the "Delievered" screen.
 * </p>
 * 
 * Author: Swapnil Shingare
 */

public class DelieveredPage extends CommonClass {

	// AppiumDriver driver;

	public DelieveredPage(AndroidDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);

	}

	// =====================================
	// Element Locators
	// =====================================

	private By lbol = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[1]");
	private By podUpload = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[2]");
	private By consignee = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[4]");
	private By consigneeCntact = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[6]");
	private By custName = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[8]");
	private By wt = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[10]");
	private By delLocation = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[12]");
	private By scheduledDate = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[14]");
	private By delDate = By.xpath("//android.view.View[@text='LBOL']/following-sibling::android.view.View[16]");
	private By PODSuccessfullyUpload = By.xpath("//android.widget.TextView[@text=\"POD uploaded successfully!\"]");
	private By okBtn = By.xpath("//android.widget.TextView[@text=\"POD uploaded successfully!\"]");


	// =====================================
	// Common Actions
	// =====================================

	/**
	 * Clicks on the Delivered element. Waits until the element is clickable and
	 * then performs the click.
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
	
	public String getPODUploadSuccessfullyMsg() {
		return getText(driver, PODSuccessfullyUpload);
	}

	// =====================================
	// POD (Proof of Delivery) Action
	// =====================================

	/**
	 * Clicks on the POD Upload option. Typically used to open the upload screen for
	 * proof of delivery.
	 */
	public void clickPopUpload() {
		click(podUpload);
	}

	private By searchLBOL = By.xpath("//android.widget.EditText");
	private By delMenu = By.xpath("//android.view.View[@resource-id=\"swipeable-tabs-tab-4\"]");
	private By saveBtn = By.xpath("//android.widget.EditText");	

	public void enterLbol(String val) {
		click(searchLBOL);
		enterValue(searchLBOL, val);
	}

	public void clickDelMenu() {
		click(delMenu);

	}
	
	public void clickSaveBtn() {
		click(saveBtn);
	}
	
	public void clickOkBtn() {
		click(okBtn);
	}
	
	public void uploadPOD() throws IOException {
		driver.pushFile("/sdcard/Download/Screenshot_20251028-183348.png", new File("C:\\Users\\Admin\\Documents\\Screenshot_20251028-183348.png"));
        driver.findElement(By.xpath("//android.view.View[@text='POD Upload']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text='Downloads']")).click();
        driver.findElement(By.xpath("//android.widget.TextView[@text='sample.pdf']")).click();
        System.out.println("File uploaded successfully!");
	}

}
