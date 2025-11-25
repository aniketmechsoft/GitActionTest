package com.uiFramework.Genesis.LDAPages;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class ScheduleDeliveryPage {

	WebDriver driver = null;
	OrderPage op = null;
	CommonPage cp;
	CommonMethods cm = null;
	WaitHelper wt;

	public ScheduleDeliveryPage(WebDriver driver) {
		this.driver = driver;
		this.cp = new CommonPage(driver);
		cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		op = new OrderPage(driver);

	}

	private By scheduleDelivery = By.xpath("//i[@title='Schedule Delivery']");
	private By manualscheduleDelivery = By.xpath("//i[@title='Manual Schedule Delivery']");
	private By updateschedelBtn = By.xpath("//*[@title='Click to update schedule delivery']");
	private By enterlbolno = By.xpath("//*[@placeholder='Enter LBOL No']");
	private By datebtn = By.xpath("(//*[@aria-label='Choose Date'])[2]");
	private By dateSel = By.xpath("//td[@data-p-today='true']");
	private By scheddate = By.xpath("(//*[@placeholder='Sched. Delivery Date & Time'])[2]");
	private By acceptbtn = By.xpath("//*[@aria-label='Accept']");
	private By selectcheckbox = By.xpath("//tr//td//*[@class='p-checkbox p-component']");
	private By searchcontain = By.xpath("//*[@placeholder='Contains...']");
	private By selectlbol = By.xpath("//*[@id='LBOL']");
	private By searchDrpDwn1 = By.xpath("//*[@class='p-multiselect-filter-container']/input");
	private By selfirstelement = By.xpath("//*[@class='p-multiselect-items p-component']");
	private By closedropdown = By.xpath("//*[@data-pc-section='closeicon']");
	private By addnote = By.xpath("//*[@placeholder='Enter Add Note']");
	private By viewbtn = By.xpath("//*[@title='Click to view']");
	private By closeicon = By.xpath("//*[@data-testid='CloseIcon']");
	private By selectstatus = By.xpath("//*[@id='status']");

	/**
	 * Clicks on "Manual Schedule Delivery" button after waiting for loaders.
	 */
	public void clickManualScheDel() {
	    cp.waitForLoaderToDisappear();
	    wt.waitToClick(manualscheduleDelivery, 10);
	    cp.waitForLoaderToDisappear();
	}

	/**
	 * Clicks on "Schedule Delivery" button after waiting for loaders.
	 */
	public void clickScheduleDel() {
	    cp.waitForLoaderToDisappear();
	    wt.waitToClick(scheduleDelivery, 10);
	    cp.waitForLoaderToDisappear();
	}

	/**
	 * Clicks on "Update Schedule Delivery" button.
	 */
	public void clickUpdateScheDel() {
		cp.waitForPopupToDisappear();
	    wt.waitToClick(updateschedelBtn, 10);
	}

	/**
	 * Enters the given Lbol number into the input field.
	 * 
	 * @param lbol The Lbol number to enter.
	 */
	public void enterLbolNo(String lbol) {
	    cp.clickAndSendKeys(enterlbolno, lbol);
	}

	/**
	 * Selects a manual schedule date using the date picker popup.
	 */
	public void selectManualScheDate() throws InterruptedException {
	    cp.waitForPopupToDisappear();
	    wt.waitToClick(datebtn, 10);
	    Thread.sleep(1000);
	    wt.waitToClick(dateSel, 10);
	    wt.waitToClick(enterlbolno, 10);
	}

	/**
	 * Enters the provided delivery date manually into the input field.
	 * 
	 * @param date The date string to enter.
	 */
	public void enterDateManually(String date) {
	    cp.clickAndSendKeys(scheddate, date);
	}

	/**
	 * Clicks on "Accept" button if available, otherwise skips the test.
	 */
	public void clickOnAcceptedBtnIfPresent() {
		cp.waitForPopupToDisappear();
	    if (driver.findElements(acceptbtn).isEmpty()) {
	        throw new SkipException("Accept button not available, skipping test.");
	    }
	    wt.waitToClick(acceptbtn, 10);
	}

	/**
	 * Selects an Lbol checkbox from the list.
	 */
	public void selectLbol() {
	    wt.waitToClick(selectcheckbox, 10);
	}

	/**
	 * Searches for the given Lbol number in dropdown and selects it.
	 * 
	 * @param lbol The Lbol number to search for.
	 */
	public void searchlbol(String lbol) {
		cp.waitForPopupToDisappear();
	    wt.waitToClick(selectlbol, 10);
	    System.out.println("send lbol "+ lbol);
	    cp.clickAndSendKeys(searchDrpDwn1, lbol);
	   // driver.findElement(searchDrpDwn1).clear();
	   // driver.findElement(searchDrpDwn1).click();
	   // driver.findElement(searchDrpDwn1).sendKeys(lbol);
	    wt.waitToClick(selfirstelement, 10);
	    wt.waitToClick(closedropdown, 10);
	}
	
	/**
	 * This method is used to select a status from the status dropdown based on the
	 * value passed as parameter.
	 * 
	 * @param status the status value to be selected
	 */
	public void selectStatus() {
		cp.waitForPopupToDisappear();
		wt.waitToClick(selectstatus, 10);
		wt.waitToClick(selfirstelement, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Chooses schedule dates for the first three date fields using date picker.
	 */
	public void chooseSchedDates() throws InterruptedException {
		cp.waitForPopupToDisappear();
	    for (int i = 1; i <= 3; i++) {
	        wt.waitToClick(By.xpath("(//*[@aria-label='Choose Date'])[" + i + "]"), 10);
	        Thread.sleep(1000);
	        wt.waitToClick(dateSel, 10);
	        wt.waitToClick(searchcontain, 5);
	        wt.waitToClick(searchcontain, 5);
	    }
	}

	/**
	 * Enters random future dates in the first three 
	 * "Scheduled Delivery Date & Time" fields.
	 * @throws InterruptedException 
	 */
	public void schedDeliveryDates() throws InterruptedException {
//		cp.waitForPopupToDisappear();
//		 wt.waitToClick(By.xpath("(//*[@aria-label='Choose Date'])[1]"), 10);
//	     Thread.sleep(1000);
//	     wt.waitToClick(dateSel, 10);
//	     wt.waitToClick(searchcontain, 5);
//	     wt.waitToClick(searchcontain, 5);
//		
		for (int i = 1; i <= 3; i++) {
		    wt.waitToClick(By.xpath("(//*[@aria-label='Choose Date'])[" + i + "]"), 10);
		    Thread.sleep(1000);
		    int date = getRanNo();

		    By randate = By.xpath("//*[@aria-label='" + date + "']");

		    wt.waitToClick(randate, 10);

		    wt.waitToClick(searchcontain, 5);
		    wt.waitToClick(searchcontain, 5);
		
//	        By field = By.xpath("(//*[@placeholder='Scheduled Delivery Date & Time'])[" + i + "]");
//	        String date =getRanFutDate();
//	        System.out.println("date "+ date);
//	        Thread.sleep(2000);
//	        cp.clickAndSendKeys(field, date);
	    }
		
		
	}
	public int getRanNo() {
	    return (int) (Math.random() * 28) + 1; // 1 to 28
	}


	/**
	 * Generates a random future date within 30 days in the format 
	 * "MM/dd/yyyy hh:mm a".
	 * 
	 * @return Random future date as String.
	 */
	public String getRanFutDate() {
	    return java.time.LocalDateTime.now()
	            .plusDays((long) (Math.random() * 30) + 1)
	            .withHour((int) (Math.random() * 12) + 12)
	            .withMinute((int) (Math.random() * 60))
	            .format(java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"));
	}

	/**
	 * Checks if a date is available in the first row, 8th column of the table.
	 * 
	 * @return true if a date is displayed, otherwise false.
	 */
	public boolean isDateAvailable() {
	    List<WebElement> dates = driver.findElements(By.xpath("//table/tbody/tr[1]/td[8]//span"));
	    return !dates.isEmpty();
	}

	/**
	 * Adds a note into the note field if available.
	 * 
	 * @param note The note text to add.
	 */
	public void addNote(String note) {
	    try {
	        cp.clickAndSendKeys(addnote, note);
	    } catch (Exception e) {
	        // ignored
	    }
	}

	/**
	 * Checks if any note is displayed in the note section.
	 * 
	 * @return true if a note is displayed, otherwise false.
	 */
	public boolean isNoteDisplayed() {
	    List<WebElement> notes = driver.findElements(By.xpath("//*[@class='labelBoxK__labelData']/span"));
	    return !notes.isEmpty();
	}

	public void clickOnViewBtn() {
		wt.waitToClick(viewbtn, 10);
	}
	
	/**
	 * Checks if the schedule history popup is displayed by verifying 
	 * the close button. If displayed, it clicks the close button.
	 *
	 * @return true if the popup is displayed, false otherwise.
	 */
	public boolean isSchedHistoryDisplayed() {
	    try {
	        WebElement closeBtn = driver.findElement(closeicon);
	        if (closeBtn.isDisplayed()) {
	            wt.waitToClick(closeicon, 10);
	            return true;
	        }
	    } catch (NoSuchElementException e) {
	        return false;
	    }
	    return false;
	}


}
