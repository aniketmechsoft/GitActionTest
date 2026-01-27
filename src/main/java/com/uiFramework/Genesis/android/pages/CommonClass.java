package com.uiFramework.Genesis.android.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.HasOnScreenKeyboard;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class CommonClass {

	AndroidDriver driver;
	WebDriverWait wait;

	public CommonClass(AndroidDriver driver) {
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
	 * Includes a retry in case of a transient element interaction failure (common
	 * in iOS animations or rendering delays).
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
	 * Waits until the input field is visible and enters the provided text. Clears
	 * existing value before sending new input.
	 *
	 * @param ele   The locator of the input field element.
	 * @param value The text value to be entered.
	 * @throws InterruptedException 
	 */
//	public void enterValue(By ele, String value) {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
//			element.clear(); // Clear any pre-filled data
//			element.sendKeys(value);
//			System.out.println("Entered text: " + value);
//		} catch (Exception e) {
//			System.err.println("Failed to enter text: " + e.getMessage());
//		}
//	}
	
	public void enterValue(By ele, String value) {
	    try {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
	        element.clear(); // Clear any pre-filled data
	        element.sendKeys(value);
	          driver.hideKeyboard();
	        // --- Hide keyboard if visible ---
	        try {
	            if (driver instanceof HasOnScreenKeyboard) {
	                if (((HasOnScreenKeyboard) driver).isKeyboardShown()) {
	                    driver.hideKeyboard();
	                    System.out.println("Keyboard hidden successfully.");
	                }
	            }
	        } catch (Exception innerEx) {
	            System.out.println("Keyboard not visible or failed to hide: " + innerEx.getMessage());
	        }

	        System.out.println("Entered text: " + value);

	    } catch (Exception e) {
	        System.err.println("Failed to enter text: " + e.getMessage());
	    }
	}

	public void assertElementPresent(By elementLocator) {
	    try {
	   //     By elementLocator = By.xpath("//android.view.View[@resource-id='childLayout']//android.widget.TextView[3]");

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));

	        Assert.assertTrue(element.isDisplayed(), 
	            "Element is not displayed: " + elementLocator);

	        System.out.println("Element is present and visible: " + elementLocator);

	    } catch (Exception e) {
	        System.err.println("Failed to verify element presence: " + e.getMessage());
	        Assert.fail("Element not found or not visible: " + e.getMessage());
	    }
	}
	
	/**
	 * This method used to close keyboard
	 */
	public void closeKeyboard() {
	    try {
	        driver.hideKeyboard();
	    } catch (Exception e) {
	        driver.pressKey(new KeyEvent(AndroidKey.BACK));
	    }
	}



}
