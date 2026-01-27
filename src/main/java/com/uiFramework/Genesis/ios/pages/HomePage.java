package com.uiFramework.Genesis.ios.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.uiFramework.Genesis.utils.IOSActions;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class HomePage extends IOSActions{

	
	IOSDriver driver;

	
	public HomePage(IOSDriver driver)
	{
		super(driver);
		this.driver =driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this); //
		
	}
	
	//driver.findElement(AppiumBy.accessibilityId("Alert Views")).click();
	@iOSXCUITFindBy(accessibility="Alert Views")
	private WebElement alertViews;
	
	public AlertViewsOldBase selectAlertViews()
	{
		alertViews.click();
		return new AlertViewsOldBase(driver);
	}
	
	
	
}
