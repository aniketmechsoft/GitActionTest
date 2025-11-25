package com.uiFramework.Genesis.ios.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class CommonClass {
	
	AppiumDriver driver;
	WebDriverWait wait;

	public CommonClass(AppiumDriver driver) {
		// super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this); 
	}
	
	// ==================================================
	// Generic Utility Methods (Click, GetText, SendKeys)
	// ==================================================

	/**
	 * Waits until an element is clickable, then performs a click action.
	 * <p>
	 * Includes a retry in case of a transient element interaction failure
	 * (common in iOS animations or rendering delays).
	 * </p>
	 *
	 * @param ele The locator of the element to be clicked.
	 */
	public void click(By ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		try {
			WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
			next.click();
		} catch (Exception e) {
			// Retry once if the first attempt fails
			WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
			next.click();
		}
	}

	/**
	 * Waits for visibility of the given iOS element and extracts its text.
	 *
	 * @param driver     The active AppiumDriver instance.
	 * @param iosLocator The locator for the target iOS element.
	 * @return The visible text extracted from the element.
	 */
	public String getText(AppiumDriver driver, By iosLocator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(iosLocator));
		String textValue = element.getText();
		System.out.println("iOS Element Text: " + textValue);
		return textValue;
	}
	
	/**
	 * Waits until the input field is visible and enters the provided text.
	 * Clears existing value before sending new input.
	 *
	 * @param ele   The locator of the input field element.
	 * @param value The text value to be entered.
	 */
	public void enterValue(By ele, String value) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
			element.clear(); // Clear any pre-filled data
			element.sendKeys(value);
			System.out.println("Entered text: " + value);
		} catch (Exception e) {
			System.err.println("Failed to enter text: " + e.getMessage());
		}
	}
	
	/** 
	 * Waits until the truck loader disappears from the screen.
	 * Useful for ensuring the page or action is fully loaded before continuing.
	 */
	public void waitForLoaderToDisappear() {
	    By loader = By.xpath("//android.widget.Image[@text='truck']");
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	    try {
	        // Wait for the loader to become invisible
	        wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
	        System.out.println("✅ Loader disappeared successfully.");
	    } catch (TimeoutException e) {
	        System.out.println("⚠️ Loader still visible after timeout.");
	    }
	}



}
