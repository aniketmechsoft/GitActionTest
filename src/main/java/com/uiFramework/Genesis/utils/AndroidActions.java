package com.uiFramework.Genesis.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

public class AndroidActions extends AppiumUtils {

	protected AndroidDriver driver;

	public AndroidActions(AndroidDriver driver) {

		this.driver = driver;
	}

	public void longPressAction(WebElement ele) {
		((JavascriptExecutor) driver).executeScript("mobile: longClickGesture",
				ImmutableMap.of("elementId", ((RemoteWebElement) ele).getId(), "duration", 4000));
	}

	public void scrollToEndAction() {
		boolean canScrollMore;
		do {
			canScrollMore = (Boolean) ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap
					.of("left", 100, "top", 100, "width", 200, "height", 200, "direction", "down", "percent", 3.0

					));
		} while (canScrollMore);
	}

	public void partialScroll() {
		{
			((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of("left", 100, "top",
					100, "width", 200, "height", 200, "direction", "down", "percent", 3.0

			));
		}
	}

	public void scrollToText(String text) {

		driver.findElement(AppiumBy
				.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(text(\"" + text + "\"));"));
	}

	public void scrollToAccessibilityId(String accessibilityId) {
		driver.findElement(AppiumBy
				.androidUIAutomator("new UiScrollable(new UiSelector()).scrollIntoView(new UiSelector().description(\""
						+ accessibilityId + "\"));"));
	}

	public void swipeAction(WebElement ele, String direction) {
		((JavascriptExecutor) driver).executeScript("mobile: swipeGesture",
				ImmutableMap.of("elementId", ((RemoteWebElement) ele).getId(),

						"direction", direction, "percent", 0.75));

	}
	
	public void enterText(By locator, String text) {
		WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
		element.clear();
		element.sendKeys(text);
	}

	public void enterTextWithAction(By locator, String text) {
		try {
			WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.elementToBeClickable(locator));
			element.click(); // Ensure focus
			element.clear();
			element.sendKeys(text);
			driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));

		} catch (InvalidElementStateException e1) {
			System.out.println("Element not interactable, using Actions fallback.");
			WebElement element = driver.findElement(locator);
			element.click();
			element.clear();
			//new Actions(driver).sendKeys(element, text);
			driver.pressKey(new KeyEvent(AndroidKey.DIGIT_0));
		}
	}

	public String getText(By locator) {
		WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return element.getText().trim();
	}

	public String getTextOnPresence(By locator) {
		WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.getText().trim();
	}
	
	public void enterTextWithPressKey(By locator) {
		try {
			WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.elementToBeClickable(locator));
			element.click(); // Ensure focus
			element.clear();
			element.sendKeys("2");
			driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));

		} catch (InvalidElementStateException e1) {
			System.out.println("Element not interactable, using Actions fallback.");
			WebElement element = driver.findElement(locator);
			element.click();
			element.clear();
			//new Actions(driver).sendKeys(element, text);
			driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));
		}
	}


}
