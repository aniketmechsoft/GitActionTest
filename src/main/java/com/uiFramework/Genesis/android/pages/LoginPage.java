package com.uiFramework.Genesis.android.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPage {

	//IOSDriver driver;
	AndroidDriver driver;

	public LoginPage(AndroidDriver driver) {
		super();
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this); //

	}

	@AndroidFindBy(xpath = "//android.widget.EditText[@resource-id='userName']")
	private WebElement usernameEntryField;

	@AndroidFindBy(xpath = "//android.widget.EditText[@resource-id='password']")
	private WebElement passwordField;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='SIGN IN']")
	private WebElement signInButton;
//	
//	@AndroidFindBy(xpath = "//android.widget.Button[@text='Next']")
//	private WebElement nextButton;
	
//	@AndroidFindBy(xpath = "//android.widget.Button[@text='Login Now']")
//	private WebElement loginNowButton;

	public WebElement getUsernameField() {
		return usernameEntryField;
	}

	public WebElement getPasswordField() {
		return passwordField;
	}

	public WebElement getSignInButton() {
		return signInButton;
	}

	public void performLogin(LoginPage loginPage, String username, String password) {
		try {
			WebElement userField = loginPage.getUsernameField();
			userField.click();
			userField.clear();
			userField.sendKeys(username);

			WebElement passField = loginPage.getPasswordField();
			passField.click();
			passField.clear();
			passField.sendKeys(password);

			WebElement signInBtn = loginPage.getSignInButton();
			signInBtn.click();

			System.out.println("Login executed successfully with username: " + username);
		} catch (Exception e) {
			System.err.println("Login flow failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
//	public void clickNext() throws InterruptedException {
//
//		Thread.sleep(2000);
//		nextButton.click();
//		
//	}
	
    private final By nextButton = MobileBy.xpath("//android.widget.Button[@text='Next']");
    
    public void clickNext() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement next = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            next.click();
        } catch (Exception e) {
            // Retry once in case of stale or slow UI
            WebElement next = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            next.click();
        }
    }
    
    private final By loginNowButton = MobileBy.xpath("//android.widget.Button[@text='Login Now']");

    public void clickLoginNow() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            WebElement next = wait.until(ExpectedConditions.elementToBeClickable(loginNowButton));
            next.click();
        } catch (Exception e) {
            WebElement next = wait.until(ExpectedConditions.elementToBeClickable(loginNowButton));
            next.click();
        }
    }
    
    public void allow() {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    try {
	        WebElement popupBtn = wait.until(
	            ExpectedConditions.elementToBeClickable(popup));
	        popupBtn.click();
	        System.out.println("✅ Permission popup allowed.");
	    } catch (Exception e) {
	        System.out.println("⚠️ Popup not found or not clickable: " + e.getMessage());
	    }
	}
    
    public boolean isDisplayedAndroidToast() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toast = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//android.view.View[@resource-id='root']/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.TextView")
                )
            );
            return toast.isDisplayed();
        } catch (Exception e) {
            System.out.println("Toast not displayed: " + e.getMessage());
            return false;
        }
    }
    
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
    private WebElement popup;
}
