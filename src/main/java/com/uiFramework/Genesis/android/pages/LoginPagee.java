package com.uiFramework.Genesis.android.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import com.google.common.collect.ImmutableMap;
import com.uiFramework.Genesis.utils.AndroidActions;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPagee extends AndroidActions {

	AndroidDriver driver;

	public LoginPagee(AndroidDriver driver) {
		super(driver);
		this.driver = driver;	
		PageFactory.initElements(new AppiumFieldDecorator(driver), this); 
	}

	@AndroidFindBy(accessibility = "Agree and Continue")
	private WebElement agreeAndContinue;

	@AndroidFindBy(xpath = "//android.widget.Button[@text='Next']")
	private WebElement next;

	@AndroidFindBy(accessibility = "Login")
	private WebElement loginText;

	@AndroidFindBy(xpath = "//android.widget.Button[@content-desc='Select Country US +1']")
	private WebElement countryCodeDrpDwn;

	@AndroidFindBy(xpath = "//*[@class='android.widget.EditText']")
	private WebElement phoneNumber;

	@AndroidFindBy(xpath = "(//*[@class='android.widget.EditText'])[2]")
	private WebElement password;

	@AndroidFindBy(xpath = "(//*[@class='android.widget.Button'])[3]")
	private WebElement login;

	@AndroidFindBy(id = "com.android.packageinstaller:id/permission_allow_button")
	private WebElement acceptPopup;

	@AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
	private WebElement popup;

	@AndroidFindBy(xpath = "//android.widget.ImageView[@content-desc=\"IN +91\"]")
	private WebElement india;

	@AndroidFindBy(xpath = "//*[@class='android.widget.CheckBox']")
	private WebElement checkbox;

	@AndroidFindBy(id = "com.android.packageinstaller:id/permission_allow_button")
	private WebElement allowOnPopup;

	@AndroidFindBy(id = "com.android.packageinstaller:id/permission_deny_button")
	private WebElement denyOnPopup;

	public void allow() throws InterruptedException {
		Thread.sleep(5000);
		popup.click();
		Thread.sleep(2000);
	}

	public void denyPermission() throws InterruptedException {
		Thread.sleep(2000);
		denyOnPopup.click();
		Thread.sleep(2000);
	}

	public void name() throws InterruptedException {
		if (checkbox.isDisplayed()) {
			Thread.sleep(2000);
			checkbox.click();
			Thread.sleep(2000);
			denyOnPopup.click();
			Thread.sleep(2000);
		} else {
			Thread.sleep(2000);
			denyOnPopup.click();
			Thread.sleep(2000);
		}
	}

	public void clickAgreenContinue() throws InterruptedException {
		agreeAndContinue.click();
		Thread.sleep(2000);
	}

	public void clickNext() throws InterruptedException {
		next.click();
		Thread.sleep(2000);
	}

	public void loginText() throws InterruptedException {
		loginText.click();
		Thread.sleep(2000);
	}

	public void acceptPopup() throws InterruptedException {
		acceptPopup.click();
		Thread.sleep(2000);
	}

	public void selectCountry() throws InterruptedException {
		String country = "ID +62";

		driver.pressKey(new KeyEvent(AndroidKey.TAB));
		// driver.pressKey(new KeyEvent(AndroidKey.TAB));
		driver.pressKey(new KeyEvent(AndroidKey.ENTER));
		Thread.sleep(2000);

		int countryCount = driver.findElements(By.xpath("//android.widget.ImageView")).size();
		System.out.println(countryCount);

		for (int i = 0; i < countryCount; i++) {
			String countryName = driver.findElements(By.xpath("//android.widget.ImageView")).get(i)
					.getAttribute("content-desc");
			System.out.println("issssssssssss" + countryName);

			if (countryName.equalsIgnoreCase(country)) {
				driver.findElements(By.xpath("//android.widget.ImageView")).get(i).click();
			}
			
		}
	}

	public void SetPhoneNumber(String phoneNo) {
		phoneNumber.click();
		phoneNumber.sendKeys(phoneNo);
		driver.hideKeyboard();
	}

	public void setPassword(String pass) {
		password.click();
		password.sendKeys(pass);
		driver.hideKeyboard();
	}
	

	public void login() {
		login.click();
	}
	
	@AndroidFindBy (xpath = "//android.widget.ScrollView/android.widget.EditText[2]/android.view.View")
	WebElement regularForm;
	
	@AndroidFindBy (xpath = "//android.widget.ScrollView/android.widget.EditText[2]/android.view.View")
	WebElement encryptedForm;

	public void setActivity() {
		((JavascriptExecutor) driver).executeScript("mobile:startActivity",
				ImmutableMap.of("Intent", "com.mechsoft.contact_mngmt/.MainActivity"));
	}
	@AndroidFindBy (id = "com.android.permissioncontroller:id/permission_allow_foreground_only_button")
	private WebElement permissionPopup;
	
	public void accPermissionPopup() {
		try {
			Thread.sleep(1000);
			permissionPopup.click();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	


}
