package com.uiFramework.Genesis.android.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class LDADashboard {
	
	AppiumDriver driver;

	public LDADashboard(AppiumDriver driver) {
		// super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this); 
	}

	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Piece Count Pending']")
	private WebElement pieceCountPendingElement;
	
	@AndroidFindBy(xpath = "//android.view.View/android.widget.Image/android.widget.TextView[2]")
	private WebElement pieceCountPendingCount;

	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Available']")
	private WebElement availableElement;
	
	@AndroidFindBy(xpath = "(//android.view.View/android.widget.Image/android.widget.TextView[2])[2]")
	private WebElement availableElementCount;


	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Scheduled']")
	private WebElement scheduledElement;
	
	@AndroidFindBy(xpath = "(//android.view.View/android.widget.Image/android.widget.TextView[2])[3]")
	private WebElement scheduledElementCount;

	
	@AndroidFindBy(xpath = "//android.widget.TextView[@text='Delivered']")
	private WebElement deliveredElement;
	
	@AndroidFindBy(xpath = "(//android.view.View/android.widget.Image/android.widget.TextView[2])[4]")
	private WebElement deliveredElementCount;
	
	public void clickPieceCountPending() {
	    try {
	        pieceCountPendingElement.click();
	        System.out.println("✅ Clicked on Piece Count Pending element");
	    } catch (Exception e) {
	        System.err.println("❌ Failed to click Piece Count Pending: " + e.getMessage());
	    }
	}

	public void clickAvailable() {
	    try {
	        availableElement.click();
	        System.out.println("✅ Clicked on Available element");
	    } catch (Exception e) {
	        System.err.println("❌ Failed to click Available: " + e.getMessage());
	    }
	}

	public void clickScheduled() {
	    try {
	        scheduledElement.click();
	        System.out.println("✅ Clicked on Scheduled element");
	    } catch (Exception e) {
	        System.err.println("❌ Failed to click Scheduled: " + e.getMessage());
	    }
	}

	public void clickDelivered() {
	    try {
	        deliveredElement.click();
	        System.out.println("✅ Clicked on Delivered element");
	    } catch (Exception e) {
	        System.err.println("❌ Failed to click Delivered: " + e.getMessage());
	    }
	}




}
