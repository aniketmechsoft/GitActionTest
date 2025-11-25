package com.uiFramework.Genesis.android.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.touch.offset.PointOption;

public class AvailablePage extends CommonClass {

	AndroidDriver driver;

	public AvailablePage(AndroidDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this); //

	}

	private By availableMenu = By.xpath("//android.view.View[@resource-id=\"swipeable-tabs-tab-2\"]");
	private By lbol_Android = By.xpath("(//android.widget.TextView)[4]");
	private By history_Android = By.xpath("(//android.widget.TextView)[5]");
	private By consignee_Android = By.xpath("(//android.widget.TextView)[7]");
	private By consignCntact_Android = By.xpath("(//android.widget.TextView)[9]");
	private By custName_Android = By.xpath("(//android.widget.TextView)[11]");
	private By wt_Android = By.xpath("(//android.widget.TextView)[13]");
	private By location_Android = By.xpath("(//android.widget.TextView)[15]");
	private By DatenTime_Android = By.xpath("(//android.widget.TextView)[17]");
	private By delConfirmationBtn = By.xpath("(//android.widget.Button[@text=\"DELIVERY CONFIRMATION\"])[1]");
	private By yesBtn = By.xpath("//android.widget.Button[@text=\"Yes\"]");
	private By damageCheckbox = By.xpath("//android.widget.TextView[@text=\"Damage\"]");
	
	private By actOrdPieces = By.xpath("(//android.widget.TextView)[18]");


	private By recOrd = By.xpath("//android.widget.EditText[1]");
	private By recLit = By.xpath("//android.widget.EditText[2]");
	private By damageOrd = By.xpath("//android.widget.TextView[@text='Damage - Order']/following-sibling::android.widget.EditText[1]");
	private By damageLit = By.xpath("//android.widget.TextView[@text='Damage - Literature']/following-sibling::android.widget.EditText[1]");
	private By cancelBtn = By.xpath("//android.widget.Button[@text=\"CANCEL\"]");
	private By confirmBtn = By.xpath("//android.widget.Button[@text=\"CONFIRM\"]");

	public String getText(AppiumDriver driver, By ele) {
		WebElement element;
		String platformName = driver.getCapabilities().getCapability("platformName").toString().toLowerCase();

		if (platformName.contains("android")) {
			element = driver.findElement(ele);
		} else {
			element = driver.findElement(ele);

		}

		String manifestText = element.getText();
		System.out.println("Manifest Number Text: " + manifestText);
		return manifestText;
	}

	public void click(By ele) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		try {
			WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
			next.click();
		} catch (Exception e) {
			WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
			next.click();
		}
	}

	public String getLBOLNoText() {
		return getText(driver, lbol_Android);
	}

	public String getConsignNameText() {
		return getText(driver, consignee_Android);
	}

	public String getConsignCntactText() {
		return getText(driver, consignCntact_Android);
	}

	public String getCustomerNameText() {
		return getText(driver, custName_Android);
	}

	public String getWtText() {
		return getText(driver, wt_Android);
	}

	public String getDelieveryLocationText() {
		return getText(driver, location_Android);
	}

	public String getReqDateTimeText() {
		return getText(driver, DatenTime_Android);
	}
	
	public String getActOrdPiecesText() {
		return getText(driver, actOrdPieces);
	}

	public void clickHistoryBtn() {
		click(history_Android);
	}

	public void clickDelConfirmationBtn() {
		click(delConfirmationBtn);
	}

	public void clickAvailableMenu() {
		click(availableMenu);
	}

	public void clickDamageCheckbox() {
		click(damageCheckbox);
	}

	public void clickYesBtn() {
		click(yesBtn);
	}
	
	public void clickCancelBtn() {
		click(cancelBtn);
	}
	
	public void clickConfirmBtn() {
		click(confirmBtn);
	}

	private By lbo = By.xpath("//android.view.View[@resource-id='childLayout']/android.widget.TextView[2]");

	public String getlboText() {
		return getText(driver, lbo);
	}

	By scheduledHistory = By.xpath("//android.view.View[@text='Scheduled History No Data Found']");

	public boolean isVisible(AndroidDriver driver, By elementLocator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
			return driver.findElement(elementLocator).isDisplayed();
		} catch (TimeoutException e) {
			return false;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public Boolean isScheduledHistoryNoDataFoundVisible() {
		return isVisible(driver, scheduledHistory);
	}

	public Boolean isDamagedOrderVisible() {
		return isVisible(driver, damageOrd);
	}
	
	public Boolean isDamagedLitVisible() {
		return isVisible(driver, damageLit);
	}

	public void backBtn() {
		driver.pressKey(new KeyEvent(AndroidKey.BACK));
	}

	public void clickMarkRecBtn() {
		WebElement element = driver
				.findElement(By.xpath("//android.view.View[@text='Mark received (Same as Order Qty.)']"));
		Point location = element.getLocation();
		Dimension size = element.getSize();
		int centerX = location.getX() + size.getWidth() / 2;
		int centerY = location.getY() + size.getHeight() / 2;
		int x = centerX - 100;
		int y = centerY;
		TouchAction<?> action = new TouchAction<>(driver);
		action.tap(PointOption.point(x, y)).perform();
		System.out.println("Clicked on 'Mark received' successfully!");
	}

	/** Enters received order pieces count. */
	public void enterRecOrdPieces(String pieces) {
		click(recOrd);
		enterValue(recOrd, pieces);
	}

	/** Enters received LIT pieces count. */
	public void enterLitOrdPieces(String pieces) {
		click(recLit);
		enterValue(recLit, pieces);
	}

	/** Enters damaged order pieces count. */
	public void enterDamageOrdPieces(String pieces) {
		click(damageOrd);
		enterValue(damageOrd, pieces);
	}

	/** Enters damaged LIT pieces count. */
	public void enterLitDamagePieces(String pieces) {
		click(damageLit);
		enterValue(damageLit, pieces);
	}
	
//	public void enterLitDamagePieces(String pieces) {
//		click(damageLit);
//		enterValue(damageLit, pieces);
//	}
	
	public String getAttributeValue(By locator, String attributeName) {
	    try {
	        WebElement element = driver.findElement(locator);
	        String attrValue = element.getAttribute(attributeName);
	        System.out.println("Attribute '" + attributeName + "' value: " + attrValue);
	        return attrValue;
	    } catch (Exception e) {
	        System.out.println("Unable to fetch attribute '" + attributeName + "' for locator: " + locator.toString());
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public int getRecOrder() {
		return Integer.parseInt(getAttributeValue(damageOrd, "text").trim());
		
	}
	

}
