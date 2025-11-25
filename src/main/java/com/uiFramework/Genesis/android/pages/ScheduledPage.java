package com.uiFramework.Genesis.android.pages;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

//public class ScheduledPage extends AndroidActions {
//	
//	public ScheduledPage(AndroidDriver driver) {
//		super(driver);
//		this.driver = driver;	
//		PageFactory.initElements(new AppiumFieldDecorator(driver), this); 
//	}
//	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//	
//	private By scheduledtab_Android = By.xpath("//android.view.View[@resource-id='swipeable-tabs-tab-3']");
//	private By first_cal_icon_Android = By.xpath("(//android.widget.EditText)[2]");
//	private By second_cal_icon_Android = By.xpath("(//android.widget.EditText)[3]");
//	private By third_cal_icon_Android = By.xpath("(//android.widget.EditText)[4]");
//	private By updateBtn_Android = By.xpath("//android.widget.Button[@text='UPDATE']");
//	private By acceptSchedBtn_Android = By.xpath("//android.widget.Button[@text='ACCEPT SCHEDULED']");
//	private By delConfBtn_Android = By.xpath("//android.widget.Button[@text='DELIVERY CONFIRMATION']");
//	private By clearDateBtn = By.xpath("//android.view.View[@resource-id='childLayout']/android.view.View[2]/android.widget.TextView[1]");
//	private By historyBtn = By.xpath("(//android.widget.TextView[@text='History'])[1]");
//	private By closehistory = By.xpath("//android.webkit.WebView[@text='Genesis App']/android.view.View");
//	private By searchlbol1 = MobileBy.AndroidUIAutomator("new UiSelector().className('android.view.View').instance(10)");
//	private By searchlbol =  By.xpath("(//android.widget.EditText)[1]");
//	private By getlbol = By.xpath("//android.widget.TextView[@text='LBOL']/following-sibling::android.widget.TextView");
//	private By norecordfound = By.xpath("//android.widget.TextView[@text='No Record Found']");
//	private By lastDate = By.xpath("(//android.widget.TextView[@text='Scheduled Date & Time']/following-sibling::android.widget.TextView)[3]");
//	private By dateLocator = AppiumBy.xpath(
//	            "//android.view.View[contains(@text,'/') and (contains(@text,'AM') or contains(@text,'PM'))]");
//	private By backBtn=By.xpath("//android.widget.Button");
//	private By confirmBtn=By.xpath("//android.widget.Button[@text='CONFIRM']");
//	private By cancelBtn=By.xpath("//android.widget.Button[@text='CANCEL']");
//	private By clearDelDate=By.xpath("//android.view.View[contains(@text,'Delivered Date & Time')]/android.view.View/android.widget.TextView[1]");
//	private By enterDelDate = By.xpath("//android.view.View[contains(@text,'Delivered Date & Time')]//android.widget.EditText");
//    private By yesBtn=By.xpath("//android.widget.Button[@text='Yes']");
//    private By noBtn=By.xpath("//android.widget.Button[translate(@text, 'no', 'NO')='NO']");
//	private By enterRecpieces = By.xpath("//android.view.View[contains(@resource-id, 'GL-')]/android.widget.EditText[1]");
//
//	/**
//	 * This method used to click on schedule tab
//	 */
//	public void clickOnScheduledTab() {
////		WebElement scheduled = wait.until(ExpectedConditions.elementToBeClickable(scheduledtab_Android));
////		scheduled.click();
//		click(scheduledtab_Android);
//		//driver.findElement(scheduledtab_Android).click();	
//	}
//	
//	public void click(By ele) {
//		//WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//	//	try {
//			WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
//			next.click();
//		//} catch (Exception e) {
//		//	WebElement next = wait.until(ExpectedConditions.elementToBeClickable(ele));
//		//	next.click();
//		//}
//	}
//	
//	/**
//	 * This method is used to enter date in to the field to pass element date
//	 * @param ele
//	 * @param date
//	 * @throws InterruptedException 
//	 */
//	public void enterSchedDate(By ele, String date) throws InterruptedException {
//		WebElement scheddate = wait.until(ExpectedConditions.elementToBeClickable(ele));
//		scheddate.clear();
//		scheddate.sendKeys(date);
//		
//	}
//	
//	/**
//	 * This method will return current date and time 
//	 * @return
//	 */
//	public static String getCurrentDateTime() {
//	    Calendar c = Calendar.getInstance();
//	    int m = c.get(Calendar.MINUTE);
//	    c.set(Calendar.MINUTE, (m < 15) ? 0 : (m < 45) ? 30 : 0);
//	    if (m >= 45) c.add(Calendar.HOUR_OF_DAY, 1);
//	    c.set(Calendar.SECOND, 0);
//	    return new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(c.getTime());
//	}
//
//	/**
//	 * Returns a future date and time by adding specified days to the current date,
//	 * rounding minutes to the nearest 0 or 30, and resetting seconds to 0.
//	 *
//	 * @param daysToAdd Number of days to add.
//	 * @return Formatted future date and time as "MM/dd/yyyy hh:mm aa".
//	 */
//	public static String getFutureDateTime(int daysToAdd) {
//	    Calendar c = Calendar.getInstance();
//	    c.add(Calendar.DAY_OF_MONTH, daysToAdd);
//	    int m = c.get(Calendar.MINUTE);
//	    c.set(Calendar.MINUTE, (m < 15) ? 0 : (m < 45) ? 30 : 0);
//	    if (m >= 45) c.add(Calendar.HOUR_OF_DAY, 1);
//	    c.set(Calendar.SECOND, 0);
//	    return new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(c.getTime());
//	}
//	
//	/**
//	 * Enters the given date for a specified row, clicks the calendar icon,
//	 * selects the current time, waits briefly, and closes the date picker.
//	 *
//	 * @param rowNumber The row number (1, 2, or 3) to enter the date for.
//	 * @param date The date to enter in the row.
//	 * @throws InterruptedException If the thread sleep is interrupted.
//	 */
////	public void enterDateForRow(int rowNumber, String date) throws InterruptedException {
////	    By calIcon;
////	    switch (rowNumber) {
////	        case 1 -> calIcon = first_cal_icon_Android;
////	        case 2 -> calIcon = second_cal_icon_Android;
////	        case 3 -> calIcon = third_cal_icon_Android;
////	        default -> throw new IllegalArgumentException("Invalid row number: " + rowNumber);
////	    }
////
////	    enterSchedDate(calIcon, date);
////	    click(calIcon);
////	    clickCurrentTime();
////	    Thread.sleep(900);
////	    driver.pressKey(new KeyEvent(AndroidKey.BACK));
////	}
//
////	public void enterDateForfirstRow(String date) throws InterruptedException {
////		enterSchedDate(first_cal_icon_Android, date);
////		click(first_cal_icon_Android);
////		clickCurrentTime();
////		Thread.sleep(900);	
////		driver.pressKey(new KeyEvent(AndroidKey.BACK));
////	
////	}
////	
////	public void enterDateForSecondRow(String date) throws InterruptedException {
////		enterSchedDate(second_cal_icon_Android, date);
////		click(second_cal_icon_Android);
////		clickCurrentTime();
////		Thread.sleep(900);
////		driver.pressKey(new KeyEvent(AndroidKey.BACK));
////	}
////	
////	public void enterDateForThirdRow(String date) throws InterruptedException {
////		enterSchedDate(third_cal_icon_Android, date);
////		click(third_cal_icon_Android);
////		clickCurrentTime();
////		Thread.sleep(900);
////		driver.pressKey(new KeyEvent(AndroidKey.BACK));
////	}
//	
//	/**
//	 * This method is used to click on history btn
//	 */
//	public void clickonHistoryBtn() {
//		click(historyBtn);
//	}
//	
//	/**
//	 * Clears dates for rows 2 to 4 by clicking the respective clear buttons.
//	 */
//	public void clearDates() {
//	    for (int i = 2; i < 5; i++) {
//	        By clearDateBtn = By.xpath("//android.view.View[@resource-id='childLayout']/android.view.View[" + i + "]/android.widget.TextView[1]");
//	        click(clearDateBtn);
//	    }
//	}
//	
//	/**
//	 * This method is used to click on update btn
//	 */
//	public void clickonUpdate() {
//		click(updateBtn_Android);
//	}
//	
//	/**
//	 * This method is used to click on accept scheduled btn
//	 */
//	public void clickonAcceptSched() {
//		click(acceptSchedBtn_Android);
//	}
//	
//	/**
//	 * This method is used to Delivery confirmation button
//	 */
//	public void clickDeliveryConfirm() {
//		click(delConfBtn_Android);
//	}
//	
//	/**
//	 * This method is used to search LBOL into the search section
//	 * @param LBOL
//	 */
//	public void searchLBOL(String LBOL) {
//		//driver.findElement(searchlbol).sendKeys(LBOL);
//		enterText(searchlbol, LBOL);
//	}
//	
//	/**
//	 * This method used to get LBOL from page
//	 */
//	public String getLBOL() {
//		return getText(getlbol);
//	}
//	
//	/**
//	 * Checks if the "No Record Found" element is displayed on the screen.
//	 *
//	 * @return true if the element exists and is visible, false otherwise.
//	 */
//	public boolean isDisplayedNoRecord() {
//	    List<WebElement> elements = driver.findElements(norecordfound);
//	    return !elements.isEmpty() && elements.get(0).isDisplayed();
//	}
//	
////	public String captureAndroidToast() {
////	    
////	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
////	        WebElement toast = wait.until(
////	        ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.Toast")));
////	        String message = toast.getAttribute("value");
////	        if (message == null || message.isEmpty()) {
////	            message = toast.getAttribute("name");
////	        }
////	        return message;
//////	    } catch (Exception e) {
//////	        System.out.println("Toast message not found: " + e.getMessage());
//////	        return null;
//////	    }
//		
//		
////	}
//	
//	/**
//	 * Captures an Android toast message containing the text "User not found".
//	 * Waits up to 3 seconds for the toast to appear and returns its message.
//	 *
//	 * @return The toast message if found, otherwise null.
//	 */
//	public String captureAndroidToast() {
//	    try {
//	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//	        WebElement toast = wait.until(
//	            ExpectedConditions.presenceOfElementLocated(
//	                By.xpath("//*[contains(@text,'User not found')]")
//	            )
//	        );
//
//	        String message = toast.getAttribute("name");
//	        if (message == null || message.isEmpty()) {
//	            message = toast.getAttribute("text");
//	        }
//	        System.out.println("Toast captured: " + message);
//	        return message;
//	    } catch (Exception e) {
//	        System.out.println("Toast not found: " + e.getMessage());
//	        return null;
//	    }
//	}
//	
//	/**
//	 * Selects the current time in a time picker by rounding minutes to the nearest 0 or 30.
//	 * If minutes are 45 or above, the hour is incremented. Waits up to 5 seconds for the
//	 * time element to be clickable before clicking it.
//	 */
//	public void clickCurrentTime() {
//	    try {
//	        Calendar c = Calendar.getInstance();
//	        int m = c.get(Calendar.MINUTE);
//	        c.set(Calendar.MINUTE, (m < 15) ? 0 : (m < 45) ? 30 : 0);
//	        if (m >= 45) c.add(Calendar.HOUR_OF_DAY, 1);
//
//	        String currentTime = new SimpleDateFormat("h:mm a").format(c.getTime());
//	        System.out.println("Looking for current time: " + currentTime);
//
//	        By timeLocator = By.xpath("//android.widget.ListView//android.view.View[@text='" + currentTime + "']");
//	        WebElement timeElement = new WebDriverWait(driver, Duration.ofSeconds(5))
//	                .until(ExpectedConditions.elementToBeClickable(timeLocator));
//
//	        timeElement.click();
//	        System.out.println("Clicked current time: " + currentTime);
//
//	    } catch (Exception e) {
//	        System.err.println("❌ Failed to click current time: " + e.getMessage());
//	    }
//	}
//	
//	/**
//	 * Checks if the date field identified by 'lastDate' is blank.
//	 *
//	 * @return true if the date field exists and is empty, false otherwise.
//	 */
//	public boolean isDateBlank() {
//	    try {
//	        WebElement dateElem = driver.findElement(lastDate);
//	        if (dateElem.getText().trim().isEmpty()) {
//	            return true;
//	        } else {
//	            return false;
//	        }
//	    } catch (NoSuchElementException e) {
//	        System.out.println("Date element not found: " + e.getMessage());
//	        return false;
//	    }
//	}
//	
//	/**
//	 * This method used to get Scheduled date
//	 * @return
//	 */
//	public String getSchedDate() {
//		return getText(lastDate);
//	}
//	
//	/**
//	 * Checks if the date element identified by 'dateLocator' is present and visible on the page.
//	 *
//	 * @return true if the element exists and is displayed, false otherwise.
//	 */
//	public boolean isDatePresent() {
//	    try {
//	        WebElement date = driver.findElement(dateLocator);
//
//	        if (date.isDisplayed()) {
//	            System.out.println("Date element is present: " + date.getText());
//	            return true;
//	        } else {
//	            System.out.println("Date element found but not visible.");
//	            return false;
//	        }
//
//	    } catch (NoSuchElementException e) {
//	        System.out.println("❌ No date element found on the page!");
//	        return false;
//	    }
//	}
//	
//	/**
//	 * Scrolls the screen down in 5 partial increments, pausing briefly between each scroll.
//	 *
//	 * @throws InterruptedException if the thread sleep is interrupted.
//	 */
//	public void scrollToBottom() throws InterruptedException {
//		for (int i = 1; i <= 5; i++) {
//		    partialScroll();
//		    System.out.println("Scrolled " + i + " time(s)");
//		    Thread.sleep(700);
//		}
//	}
//	/**
//	 * This method used to click on click history button
//	 */
//	public void closeHistory() {
//		click(closehistory);
//	}
//	
//	/**
//	 * Scrolls through the delivery list to find an LBOL entry with an enabled "Received Pieces" field.
//	 * Attempts up to 10 scroll iterations, performing incremental partial scrolls in each iteration.
//	 * Clicks the delivery confirmation button in each iteration and checks if the input field is enabled.
//	 * If enabled, stops scrolling; otherwise, navigates back and continues searching.
//	 *
//	 * @throws InterruptedException if the thread sleep is interrupted.
//	 */
//	public void checkLBOLAvailableForDelivery() throws InterruptedException {
//		for (int i = 1; i <= 10; i++) {
//		    System.out.println("Scroll attempt: " + i);
//		    
//		    for (int j = 1; j <= i; j++) {
//		        partialScroll();
//		        System.out.println("→ partialScroll() called " + j + " time(s) in iteration " + i);
//		        Thread.sleep(800);
//		    }
//
//		    click(delConfBtn_Android);
//		    WebElement RecPieces = driver.findElement(enterRecpieces);
//
//		    if (RecPieces.isEnabled()) {
//		    	System.out.println("EditText is enabled → breaking loop!");
//			    break; 
//			    
//		    } else {
//		    	back();
//		        System.out.println("EditText is disabled → going back and continuing search...");
//		    }
//		}
//	}
//	
//	/**
//	 * This method used to click on back button
//	 */
//	public void back() {
//		click(backBtn);
//	}
//	
//	/**
//	 * THis method used to click on confirm order 
//	 */
//	public void confirmOrder() {
//		click(confirmBtn);
//		try {
//			driver.findElement(yesBtn).click();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
//	/**
//	 * This method used to click on cancel button
//	 */
//	public void cancelOrder() {
//		click(cancelBtn);
//	}
//	
//	/**
//	 * This method used to click on clear button for clear delivery date
//	 */
//	public void clearDelDate() {
//		click(clearDelDate);
//	}
//	
//	/**
//	 * This method use to get current date date time 
//	 * @return
//	 */
//	public String getCurrDateTime() {
//	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
//	    return sdf.format(new Date());
//	}
//	
//	/**
//	 * Enters the current date and time into the delivery date field.
//	 *
//	 * @throws InterruptedException if the scheduling method uses Thread.sleep internally.
//	 */
//	public void enterDelDate() throws InterruptedException {
//		enterSchedDate(enterDelDate, getCurrDateTime());
//		driver.findElement(enterDelDate).click();
//		driver.findElement(By.xpath("//android.widget.Image")).click();
//	}
//	
//	/**
//	 * Retrieves text from a popup by locating the first TextView element.
//	 *
//	 * @return The text from the popup, or an empty string if not found.
//	 */
//	public String  getTextFromPopup() {
//		return getText(By.xpath("//android.widget.TextView"));
//	}
//	
//	/**
//	 * This method used to click on NO button from pop up
//	 */
//	public void clickOnNo() {
//		click(noBtn);
//	}
//	
//	/**
//	 * This method used to click on YES button from pop up
//	 */
//	public void clickOnYes() {
//		click(yesBtn);
//	}
//	
//	/**
//	 * Checks if the Confirm button is both visible and enabled.
//	 *
//	 * @return true if the Confirm button is displayed and enabled, false otherwise.
//	 */
//	public boolean isDispalyedConfrimBtn() {
//		WebElement confirm = driver.findElement(confirmBtn);
//		if (confirm.isDisplayed() && confirm.isEnabled()) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
//	By markReceivedBtn = By.xpath("//android.view.View[contains(@text, 'Mark received')]");
//	By damageChekBox = By.xpath("//android.widget.CheckBox");
//	By damagePopumMsg = By.xpath("//android.widget.TextView[contains(@text, 'Are you sure to mark')]");
//	By enterdampieces = By.xpath("//android.widget.TextView[contains(@text, 'Damage - Order')]/following-sibling::android.widget.EditText[1]");
//	By enterdamLitpieces = By.xpath("//android.widget.TextView[contains(@text, 'Damage - Literature')]/following-sibling::android.widget.EditText[1]");
//	By getActpieces = By.xpath("//android.widget.TextView[@text='Actual Order Pieces']/following-sibling::android.widget.TextView[1]");
//	By getLitpieces = By.xpath("//android.widget.TextView[@text='Actual Literature Pieces']/following-sibling::android.widget.TextView[1]");
//	By piecesExeedMsg = By.xpath("//android.widget.TextView[contains(@text, 'entered pieces exceeds')]");
//	
//	/**
//	 * Clicks the "Mark Received" button.
//	 */
//	public void markRecieved() {
//		click(markReceivedBtn);
//	}
//	
//	/**
//	 * Clicks the "Damage" checkbox.
//	 */
//	public void damageCheckBox() {
//		click(damageChekBox);
//	}
//	
//	/**
//	 * Retrieves the damage confirmation message from the popup.
//	 *
//	 * @return The text of the damage confirmation message.
//	 */
//	public String getDamageConfiMsg() {
//		return getText(damagePopumMsg);
//	}
//	
//	/**
//	 * Checks if the damage order field is displayed.
//	 *
//	 * @return true if the field exists, false otherwise.
//	 */
//	public boolean isDamageOrdFieldDisplayed() {
//	    return !driver.findElements(enterdampieces).isEmpty();
//	}
//
//	/**
//	 * Retrieves the current "Actual Pieces" count, increments it by 1, and returns it as a string.
//	 *
//	 * @return The incremented "Actual Pieces" count.
//	 */
//	public String getMoreDamageActPc() {
//	    //String actpcText = getTextOnPresence(getActpieces);
//		String actpcText = driver.findElement(getActpieces).getText();
//	    int newCount = Integer.parseInt(actpcText) + 1;
//	    System.out.println("more Act pc "+newCount );
//	    return String.valueOf(newCount);
//	}
//	
//	/**
//	 * Retrieves the current "Liter Pieces" count, increments it by 1, and returns it as a string.
//	 *
//	 * @return The incremented "Liter Pieces" count.
//	 */
//	public String getMoreDamageLitPc() {
//	   // String actpcText = getTextOnPresence(getLitpieces);
//	    String actpcText = driver.findElement(getLitpieces).getText();
//	    int newCount = Integer.parseInt(actpcText) + 1;
//	    return String.valueOf(newCount);
//	}
//	
////	public void enterTextWithAction(String text) {
////	    try {
////	        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
////	                .until(ExpectedConditions.elementToBeClickable(enterdampieces));
////
////	        element.click(); // Ensure focus
////	        element.clear();
////	        element.sendKeys(text);
////
////	    }catch (InvalidElementStateException e1) {
////	        System.out.println("Element not interactable, using Actions fallback.");
////	        WebElement element = driver.findElement(enterdampieces);
////	        element.click();
////	        element.clear();
////	        new Actions(driver).sendKeys(element, text).perform();
////	    }
////	}
//	
//	/**
//	 * Enters the given number of damaged pieces into the corresponding input field.
//	 *
//	 * @param pieces The number of damaged pieces to enter.
//	 */
//	public void enterDamagePieces(String pieces) {
//		enterTextWithAction(enterdampieces,pieces);
//		
//	}
//	
//	/**
//	 * Enters the given number of damaged liter pieces into the corresponding input field.
//	 *
//	 * @param pieces The number of damaged liter pieces to enter.
//	 */
//	public void enterDamageLitPieces(String pieces) {
//		enterTextWithAction(enterdamLitpieces,pieces);
//	}
//	
//	/**
//	 * Retrieves the "Actual Pieces" count and returns half of it as a string.
//	 *
//	 * @return Half of the actual pieces count.
//	 */
//	public String getHalfDamagePc() {
//	   // String actpcText = getTextOnPresence(getActpieces);
//		String actpcText = driver.findElement(getActpieces).getText();
//	    int actpc = Integer.parseInt(actpcText);
//	    int halfCount = actpc / 2;
//	    return String.valueOf(halfCount);
//	}
//	
//	
//	public String getHalfDamageLitPc() {
//	  //  String actpcText = getTextOnPresence(getLitpieces);
//	    String actpcText = driver.findElement(getLitpieces).getText();
//	    int actpc = Integer.parseInt(actpcText);
//	    int halfCount = actpc / 2;
//	    return String.valueOf(halfCount);
//	}
//	
//	/**
//	 * Retrieves the "Liter Pieces" count and returns half of it as a string.
//	 *
//	 * @return Half of the liter pieces count.
//	 */
//	public boolean isExceedPiecesMsgDisplayed() {
//	    try {
//	        return driver.findElement(piecesExeedMsg).isDisplayed();
//	    } catch (NoSuchElementException e) {
//	        return false;
//	    }
//	}
//	By getOrderId =By.xpath("");
//	List<String> orderedIds = new ArrayList<>();
//	public void enterReceivedForOrders() {
////		List<WebElement> orderBlocks = driver.findElements(By.xpath("//android.view.View[contains(@resource-id, 'GL')]"));
////		for (WebElement el : orderBlocks) {
////		    String orderId = el.getAttribute("resource-id");
////		    orderedIds.add(orderId);
////		    System.out.println("Order found: " + orderId);
////		}
//		
//		
//		
//
//	}
//	
//	public void fillReceivedPiecesDynamically() throws InterruptedException {
//	    Set<String> processedIds = new HashSet<>();
//	    boolean moreToScroll = true;
//
//	    while (moreToScroll) {
//	        // 1️⃣ Get all currently visible orders
//	        List<WebElement> orderBlocks = driver.findElements(
//	            By.xpath("//android.view.View[contains(@resource-id,'GL-')]")
//	           
//	        );
//	        System.out.println("1");
//	        System.out.println("count or "+ orderBlocks.size());
//	        for (WebElement order : orderBlocks) {
//	            String resourceId = order.getAttribute("resource-id");  
//
//	            // Skip already processed ones
//	            if (processedIds.contains(resourceId)) continue;
//	            System.out.println("2");
//	           // try {
//	                // 2️⃣ Get Actual Order Pieces
//	                WebElement actualPiecesElem = order.findElement(
//	                    By.xpath(".//android.widget.TextView[@text='Actual Order Pieces']/following-sibling::android.widget.TextView[1]")
//	                );
//	                System.out.println("3");
//	                String actualPieces = actualPiecesElem.getText().trim();
//	                System.out.println("act pc "+actualPieces);
//	                // 3️⃣ Get Received Order EditText
//	                partialScroll();
//	                System.out.println("4");
//	                WebElement receivedEdit = order.findElement(
//	                    By.xpath(".//android.widget.TextView[@text='Received Order']/following-sibling::android.widget.EditText")
//	                );
//	                System.out.println("5");
//
//	                // 4️⃣ Enter the same number
//	                receivedEdit.click();
//	                receivedEdit.clear();
//	                //receivedEdit.sendKeys(actualPieces);
//	                new Actions(driver).sendKeys(receivedEdit, actualPieces).perform();
//	                System.out.println("✅ " + resourceId + " → Entered: " + actualPieces);
//
//	                // Mark processed
//	                processedIds.add(resourceId);
//
//	                Thread.sleep(500);
//	         //   } catch (Exception e) {
//	          //      System.out.println("⚠️ Unable to process " + resourceId + ": " + e.getMessage());
//	          //  }
//	        }
//	        System.out.println("6");
//	        // 5️⃣ Scroll down and check if new GL ids appear
//	        int beforeCount = processedIds.size();
//	        System.out.println("Before count "+beforeCount);
//	        partialScroll(); // your existing method
//	        Thread.sleep(1000);
//
//	        List<WebElement> afterScroll = driver.findElements(
//	            By.xpath("//android.view.View[contains(@resource-id,'GL-')]")
//	        );
//	        System.out.println("7");
//	        boolean newFound = afterScroll.stream()
//	            .anyMatch(e -> !processedIds.contains(e.getAttribute("resource-id")));
//
//	        // 6️⃣ Stop if no new elements were found after scroll
//	        if (!newFound || processedIds.size() == beforeCount) {
//	            moreToScroll = false;
//	            System.out.println("8");
//	            System.out.println("✅ All orders processed.");
//	        }
//	    }
//	}
//}
