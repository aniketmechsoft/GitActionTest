package com.uiFramework.Genesis.ios.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

/**
 * Page Object Model (POM) class representing the Login Screen
 * of the Genesis iOS mobile application.
 *
 * <p>
 * This class encapsulates all locators and interactions related
 * to user login actions, including entering credentials and
 * performing the sign-in click. Designed to work specifically
 * with iOS automation using Appium and XCUITest framework.
 * </p>
 * 
 * Author: Swapnil Shingare
 */
public class LoginPage {

    // Instance of IOSDriver used to interact with the iOS app
    IOSDriver driver;

    /**
     * Constructor initializes driver instance and decorates
     * elements using AppiumFieldDecorator for iOS interactions.
     *
     * @param driver Active iOSDriver instance connected to the Appium session
     */
    public LoginPage(IOSDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ======================================
    // Locator Definitions
    // ======================================

    By usernameEntryField = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeTextField'");
    By passwordLocator = MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeSecureTextField'");
    By signInButtonLocator = MobileBy.iOSNsPredicateString("name == 'SIGN IN'");

    // ======================================
    // Action Methods
    // ======================================

    /**
     * Enters the provided username into the username field.
     * <p>
     * Clicks the username input field to focus, then types
     * the given username string.
     * </p>
     *
     * @param username The username to be entered.
     */
    public void enterUsername(String username) {
        click(usernameEntryField);
        enterValue(usernameEntryField, username);
    }

    /**
     * Enters the provided password into the password field.
     * <p>
     * Clicks the password field to activate it, then types
     * the provided password securely.
     * </p>
     *
     * @param password The password to be entered.
     */
    public void enterPassword(String password) {
        click(passwordLocator);
        enterValue(passwordLocator, password);
    }

    /**
     * Clicks on the "SIGN IN" button to initiate login.
     */
    public void clickSignIn() {
        click(signInButtonLocator);
    }

    // ==================================================
    // Generic Utility Methods (Click, GetText, SendKeys)
    // ==================================================

    /**
     * Waits until the given element is clickable and performs a click.
     * <p>
     * Includes one retry mechanism in case of transient interaction failures
     * such as element not being interactable momentarily.
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
            // Retry once in case of temporary visibility or timing issues
            WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
            next.click();
        }
    }

    /**
     * Waits for the given iOS element to become visible and retrieves its text.
     *
     * @param driver     The active AppiumDriver instance.
     * @param iosLocator The locator of the target iOS element.
     * @return The text extracted from the visible element.
     */
    public String getText(AppiumDriver driver, By iosLocator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(iosLocator));
        String textValue = element.getText();
        System.out.println("iOS Element Text: " + textValue);
        return textValue;
    }

    /**
     * Clears any pre-existing text and inputs a new value into the specified field.
     * <p>
     * Uses explicit wait to ensure visibility before interacting with the element.
     * </p>
     *
     * @param ele   Locator of the input text field.
     * @param value The value to be typed into the text field.
     */
    public void enterValue(By ele, String value) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
            element.clear(); // Ensure no old text remains
            element.sendKeys(value);
            System.out.println("Entered text: " + value);
        } catch (Exception e) {
            System.err.println("Failed to enter text: " + e.getMessage());
        }
    }
}
