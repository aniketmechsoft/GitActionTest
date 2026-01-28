package com.uiFramework.Genesis.web.pages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;

import java.util.logging.Level;

public class CommonPage {
	WebDriver driver;
	public WebDriverWait wait;
	WaitHelper wt;
	JavaScriptHelper js;
	DropDownHelper DrpDwn;
	CommonMethods cm;
	public static String toastMsg;
	private static final Logger logger = Logger.getLogger(CommonPage.class.getName());
	private static final int DEFAULT_TIMEOUT = 30;

	public CommonPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.DrpDwn = new DropDownHelper(driver);
		this.js = new JavaScriptHelper(driver);
	}

	/**** Locators for filters ***/
	private By backbtn = By.xpath("//button[text()='back']");
	public By searchbtn = By.xpath("//button[@title='Click to search data']");
	private By closepopupIcon = By.xpath("//*[name()='svg'][@class='p-icon p-multiselect-close-icon'])[1]");
	private By toastlement = By.xpath("//div[@role='alert']//div[2]");
	private By loader = By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']");
	private By ordernum = By.xpath("//input[@placeholder='Enter Order No']");
	private By entercustordernum = By.xpath("//input[@placeholder='Enter Customer Order No']");
	private By warehouse = By.xpath("(//*[.='Select Warehouse'])[4]");
	private By customer = By.xpath("(//*[.='Select Customer'])[3]");
	private By distributor = By.xpath("(//*[.='Select Distributor'])[4]");
	private By consignee = By.xpath("//*[@placeholder='Select Consignee']");
	private By selectLDA = By.xpath("(//*[.='Select LDA'])[4]");
	private By selectLTL = By.xpath("(//*[.='Select LTL'])[4]");
	private By selectUser = By.xpath("(//*[.='Select Users'])[4]");
	private By selectCreationMode = By.xpath("(//*[.='Select Creation Mode'])[4]");
	private By selectstatus = By.xpath("(//*[.='Select Status'])[3]");
	private By fromdate = By.xpath("//input[@placeholder='Select From Date']");
	private By todate = By.xpath("//input[@placeholder='Select To Date']");
	private By selectpickup = By.xpath("(//*[.='Select Pickup Location'])[4]");
	private By selectdrop = By.xpath("(//*[.='Select Drop Location'])[4]");
	private By selectDoctype = By.xpath("(//*[.='Select Document Type'])[3]");
	private By closeIconXPath2 = By.xpath("(//*[@data-testid='CloseIcon'])[2]");
	public By searchsection = By.xpath("//h6[normalize-space()='Search Section']");
	private By orders = By.xpath("//i[@title='Orders']");
	private By orderpage = By.xpath("//a[@href='/order']");
	private By clearbtn = By.xpath("//button/*[.='clear']");
	private By ordstatusLocator = By
			.xpath("//*[@id='panel1bh-content']/div/div//div/div/div[2]/table/tbody/tr[1]/td[19]");
	private By closeIconXPath = By.xpath(
			"//div[@class='MuiPaper-root MuiPaper-elevation MuiPaper-rounded MuiPaper-elevation8 MuiPopover-paper css-1pmi0tv']//div//button[@type='button']//*[name()='svg']");
	private By enterPalletNoLocator = By.xpath("//input[@placeholder='Enter Pallet No']");
	private By enterTruckNoLocator = By.xpath("//input[@placeholder='Enter Truck No']");
	private By enterTruckNameLocator = By.xpath("//input[@placeholder='Enter Truck Name']");
	private By save = By.xpath("//button[@title='Click to save data']");
	private By dropdown = By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[1]");
	private By dropdownOptions = By.xpath("//span[@class='p-dropdown-item-label']");
	private By multiselectItems = By.xpath("//li[@class='p-multiselect-item']");
	private By closePickupPopup = By.xpath("//button[@class='p-multiselect-close p-link']");
	private By closePopup = By.xpath("(//*[@data-testid='HighlightOffIcon'])[9]");
	private By palletNo = By.xpath("//*[@placeholder='Enter Order No']");
	private By truckNo = By.xpath("//*[@placeholder='Enter Truck No']");
	private By truckName = By.xpath("//*[@placeholder='Enter Truck Name']");
	private By orderNo = By.xpath("//*[@placeholder='Enter Order No']");
	private By custOrderNo = By.xpath("//*[@placeholder='Enter Customer Order No']");
	private By paginationDrpDwn = By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div[1]/div[2]/select");
	private By paginationCount = By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div[1]/div[2]/select/option[4]");
	private By CreatOrder = By.xpath("//*[@title='Click to create new order']");
	private By errorToast = By.xpath(
			"//div[@class='Toastify__progress-bar Toastify__progress-bar--animated Toastify__progress-bar-theme--light Toastify__progress-bar--error']");
	private static final String NUMERIC_SANITIZER_REGEX = "[^0-9.-]";
	private static final int DECIMAL_PRECISION = 2;

	private String downloadDir = System.getProperty("user.dir")+ File.separator + "target"
																+ File.separator + "downloads";
	private By PDFBtn = By.xpath("//*[@title='Export to PDF']");
	private By excelBtn = By.xpath("//*[@title='Export to Excel']");
	private By palletMenu = By.xpath("//i[@title='Pallet']");
	private By barMenu = By.xpath("//i[@title='Barcode Printing']");
	private By inboundpalletMenu = By.xpath("//a[@href='/pallet/inbound-pallet']");
	private By outboundpalletMenu = By.xpath("//a[@href='/pallet/outbound-pallet']");
	By truckMenu = By.xpath("//i[@title='Truck']");
	private By inboundTruckMenu = By.xpath("//a[@href='/truck/inbound-truck']");
	private By obtTruckMenu = By.xpath("//a[@href='/truck/outbound-truck']");
	private By docMenu = By.xpath("//i[@title='Documents']");
	private By docGenMenu = By.xpath("//a[@href='/documents/document-queue']");
	private By splitPodMenu = By.xpath("//a[@href='/documents/split-pod']");
	private By GenerateDocMenu = By.xpath("//a[@href='/documents/generated-documents']");
	private By selectPrintStatus = By.xpath("(//*[.='Select Print Status'])[4]");
	private By qaMenu = By.xpath("//i[@title='QA']");
	private By inboundqaMenu = By.xpath("//a[@href='/qa/inbound-qa']");
	private By tracking = By.xpath("//i[@title='Tracking & ETA']");
	private By outboundQAMenu = By.xpath("//a[@href='/qa/outbound-qa']");
	private By consigneeQAMenu = By.xpath("//a[@href='/qa/consignee-qa']");
	private By trackingMenu = By.xpath("//a[@href='/outbound-truck/update-tracking-no-eta']");
	private By reconsignMenu = By.xpath("//i[@title='Reconsignment']");
	private By returnMenu = By.xpath("//i[@title='Return']");
	private By returnsubmenu = By.xpath("//a[@href='/return']");
	private By reconsign = By.xpath("//a[@href='/reconsignment']");
	private By originconsignee = By.xpath("//*[@placeholder='Select Original Consignee']");
	private By rectoconsignee = By.xpath("//*[@placeholder='Select Reconsigned To Consignee']");
	private By pickupconsignee = By.xpath("//*[@placeholder='Select Pickup Consignee']");
	private By originLDA = By.xpath("(//*[.='Select Original LDA'])[4]");
	private By newLDA = By.xpath("(//*[.='Select New LDA'])[4]");
	private By pickupLDA = By.xpath("(//*[.='Select Pickup LDA'])[4]");
	private By closeOutMenu = By.xpath("//i[@title='Closeout']");
	private By subMenu = By.xpath("//a[@href='/closeout']");
	 By getOrderStatus = By.xpath("(//table/tbody/tr[2])[2]//td[15]");
	private By searchfirstcoloum = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By palletWt = By.xpath("//*[@placeholder='Enter Pallet Weight']");
	
	/*
	 * ------------------------------ Cross-browser click utilities
	 * ------------------------------
	 */
	private void scrollIntoViewCenter(WebElement el) {
		try {
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].scrollIntoView({block:'center',inline:'nearest'});", el);
		} catch (Exception ignored) {
		}
	}

	/** Try normal click, fall back to scroll+click, then JS click. Safari-safe. */
	private void safeClick(By locator) {
		WebElement el = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
				.until(ExpectedConditions.elementToBeClickable(locator));
		try {
			el.click();
		} catch (ElementClickInterceptedException | StaleElementReferenceException e) {
			try {
				scrollIntoViewCenter(el);
				el.click();
			} catch (Exception ex2) {
				try {
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
				} catch (Exception ex3) {
					logger.log(Level.SEVERE, "JS click failed on: " + locator, ex3);
					throw ex3;
				}
			}
		}
	}

	/**
	 * Try Actions move+click; if it flakes (Safari), fall back to safeClick(JS).
	 */
	private void actionsClick(By locator) {
		WebElement el = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
				.until(ExpectedConditions.elementToBeClickable(locator));
		try {
			new Actions(driver).moveToElement(el).click().perform();
		} catch (Exception e) {
			// fallback for Safari/WebKit overlays
			try {
				scrollIntoViewCenter(el);
				el.click();
			} catch (Exception ex2) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
			}
		}
	}

	/*
	 * ------------------------------ Your original public API
	 * ------------------------------
	 */

	public void enterTruckNo(String trkNo) {
		this.clickAndSendKeys(truckNo, trkNo);
	}
	
	public void max_pagination() throws InterruptedException {
		wt.waitToClick(paginationDrpDwn, 10);
		Thread.sleep(500);
		wt.waitToClick(paginationCount, 10);
	}
	/**
	 * This method used to click on truck name and enter truck name
	 * @param trkName
	 */
	public void enterTruckName(String trkName) {
		this.clickAndSendKeys(truckName, trkName);
	}
	
	/**
	 * This method used to click on pallet no and enter pallet name
	 * @param paltNo
	 */
	public void enterPalletNo(String paltNo) {
		this.clickAndSendKeys(palletNo, paltNo);
	}
	
	/**
	 * This method used to click on order no and enter order no
	 * @param paltNo
	 */
	public void enterOrderNo(String ordNo) {
		this.clickAndSendKeys(orderNo, ordNo);
	}

	public void enterCustOrdNo(String custOrdNo) {
		this.clickAndSendKeys(custOrderNo, custOrdNo);
	}

	/** Kept same signature; now uses Safari-friendly click fallback. */
	public void clickElement(By locator) {
		try {
			safeClick(locator);
		} catch (Exception e) {
			logger.log(Level.SEVERE, " Failed to click element: " + locator, e);
		}
	}
	
	/**
	 * This method used to click on search section
	 */
	public void searchClick() {
		waitForLoaderToDisappear();
		wt.waitToClick(searchsection, DEFAULT_TIMEOUT);
	}
	
	/**
	 * This method used to click on Back button
	 */
	public void clickBackBtn() {
		waitForLoaderToDisappear();
		waitForPopupToDisappear();
		wt.waitToClickWithAction(backbtn, DEFAULT_TIMEOUT);
	}

	/**
	 * This method used to clikc on clear button
	 */
	public void clickClearButton() {
		try {
			waitForLoaderToDisappear();
			wt.waitToClick(clearbtn, DEFAULT_TIMEOUT);
			waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("Clear button not available to click");
		}
	}

//    public String getMandatoryText(By locator) {
//        return driver.findElement(locator).getText().trim();
//    }
	
	/**
	 * This method used to click on element and get value
	 * @param locator
	 * @return
	 */
	public String getMandatoryText(By locator) {
		WebElement el = driver.findElement(locator);
		String text = el.getText();

		// Fallback for Safari, especially for input fields
		if (text == null || text.trim().isEmpty()) {
			text = el.getAttribute("value");
		}

		return text == null ? "" : text.trim();
	}
	
	/**
	 * This method used to click on element and and get valued
	 * @param element
	 * @param attributeName
	 * @return
	 */
	public String getAttributeValue(By element, String attributeName) {
		wt.waitForElement(element, DEFAULT_TIMEOUT);
		return driver.findElement(element).getAttribute(attributeName);
	}
	
	/**
	 * This method used to click on drop down and select enter text in search field
	 * @param dropdownTrigger
	 * @param valueToSelect
	 */
	public void selectSearchableDropdown(WebElement dropdownTrigger, String valueToSelect) {
		dropdownTrigger.click();
		DrpDwn.DrpDwnValueSel(dropdownTrigger, valueToSelect);
	}
	
	/**
	 * This method used to click enetr value and select value by arrow down
	 * @param input
	 * @param DrpDwnValue
	 */
	public void DrpDwnValueSelArrowDw(By input, String DrpDwnValue) {
		driver.findElement(input).click();
		driver.findElement(input).clear();
		driver.findElement(input).sendKeys(DrpDwnValue);
		driver.findElement(input).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(input).sendKeys(Keys.ENTER);
	}
	
	/**
	 * This methods used to enter value and select first value from drop down
	 * @param input
	 * @param DrpDwnValue
	 * @throws InterruptedException 
	 */
	public void DrpDwnValueSel(By input, String DrpDwnValue) {
		WebElement dropdown = driver.findElement(input);

		// Scroll into view (Safari needs this)
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);

		dropdown.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		List<WebElement> options = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//li[.//text()[contains(., '" + DrpDwnValue + "')]]")));
		
		boolean optionSelected = false;
		for (WebElement option : options) {
			
			if (option.getText().toLowerCase().contains(DrpDwnValue.toLowerCase())) {
				// Click the matching option directly
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
				optionSelected = true;
				break;
			}
		}
		
		if (!optionSelected) {
			throw new NoSuchElementException("Dropdown option not found: " + DrpDwnValue);
		}
	}
	
	/**
	 * This method used to click on locator and enter text
	 */
	public void clickAndSendKeys(By locator, String text) {
		driver.findElement(locator).click();
		driver.findElement(locator).clear();
		driver.findElement(locator).sendKeys(text);
	}
	
	/**
	 * This method clicks on a WebElement and enters text inside it.
	 */
	public void clickAndSendKeysEle(WebElement element, String text) {
	    element.click();
	    element.clear();
	    element.sendKeys(text);
	}
	/**
	 * This method used to click on locator clear filed using back space then send keys
	 * @param locator
	 * @param text
	 */
	public void clickAndSendKeys1(By locator, String text) {
		driver.findElement(locator).click();
		driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
		driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
		driver.findElement(locator).sendKeys(text);
	}
	
	/**
	 * This method used to capture text from toast message
	 * @return
	 */
	public String captureToastMessage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			WebElement toastMessage = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div[2]")));
			String message = toastMessage.getText();
			logger.info("Captured toast message: " + message);
			return message;
		} catch (Exception e) {
			logger.severe("Error capturing toast message: " + e.getMessage());
			return "Toast message Failed";
		}
	}
	
	/**
	 * This method used to return true is toast displayed
	 * @return
	 */
	public boolean toastMsgReceivedSuccessfully() {
		boolean flag = false;
		try {
			wt.waitForElement(toastlement, DEFAULT_TIMEOUT);
			flag = true;
		} catch (Exception e) {
		}
		return flag;
	}
	
	/**
	 * This method used to move to element and click
	 * @param locator
	 * @throws InterruptedException
	 */
	public void moveToElementAndClick1(By locator) throws InterruptedException {
		Actions actions = new Actions(driver);
		WebElement element = driver.findElement(locator);
		actions.moveToElement(element).perform();
		logger.info("Element clicked after scroll: " + locator.toString());
	}
	
	/**
	 * This method used to move to element and click
	 * @param locator
	 */
	public void moveToElementAndClick(By locator) {
		// more robust: try Actions then fall back
		try {
			actionsClick(locator);
		} catch (Exception e) {
			logger.info("Fallback click used for: " + locator);
		}
	}
	
	//toastMsg=cp.captureToastMessage(); //used of toast
	
	/**
	 * This method used to return message from toast
	 * @return
	 */
	public String getToastMessage() {
		return toastMsg;
	}
	
	/**
	 * This method used to Wait until loader is disable
	 */
	public void waitForLoaderToDisappear() throws TimeoutException {
		try {
			wait.until(driver -> {
				try {
					WebElement loader = driver
							.findElement(By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']"));
					boolean isDisplayed = loader.isDisplayed();
					return !isDisplayed;
				} catch (NoSuchElementException e) {
					return true;
				}
			});
			logger.info("Loader has disappeared..");
		} catch (TimeoutException e) {
			logger.info("Timeout: Loader did not disappear within " + DEFAULT_TIMEOUT + " seconds");
			throw e;
		}
	}

	/**
	 * This method used to select multiple To consignee
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleConsigneesByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		driver.findElement(consignee).click();
		for (Integer index : indices) {
			try {
				String consigneeXPath = "(//li[contains(@class, 'p-autocomplete-item')])[" + index + "]";
				WebElement consigneeElement = driver.findElement(By.xpath(consigneeXPath));
				Thread.sleep(500);
				consigneeElement.click();
			} catch (Exception e) {
				logger.severe("Could not select consignee at index: " + index + ", Error: " + e.getMessage());
			}
		}
	}

	/**
	 * This method used to select multiple origin consignee on reconsignemnt page
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleOrgConsigneesByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		driver.findElement(originconsignee).click();
		for (Integer index : indices) {
			try {
				String consigneeXPath = "(//li[contains(@class, 'p-autocomplete-item')])[" + index + "]";
				WebElement consigneeElement = driver.findElement(By.xpath(consigneeXPath));
				Thread.sleep(500);
				consigneeElement.click();
			} catch (Exception e) {
				logger.severe("Could not select consignee at index: " + index + ", Error: " + e.getMessage());
			}
		}
	}

	/**
	 * This method used to select multiple To consignee on reconsignemnt page
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleToConsigneesByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		driver.findElement(rectoconsignee).click();
		for (Integer index : indices) {
			try {
				String consigneeXPath = "(//li[contains(@class, 'p-autocomplete-item')])[" + index + "]";
				WebElement consigneeElement = driver.findElement(By.xpath(consigneeXPath));
				Thread.sleep(500);
				consigneeElement.click();
			} catch (Exception e) {
				logger.severe("Could not select consignee at index: " + index + ", Error: " + e.getMessage());
			}
		}
	}

	/**
	 * This method used to select multiple pickup consignee on closeout page
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultiplePickupConsigneesByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		driver.findElement(pickupconsignee).click();
		for (Integer index : indices) {
			try {
				String consigneeXPath = "(//li[contains(@class, 'p-autocomplete-item')])[" + index + "]";
				WebElement consigneeElement = driver.findElement(By.xpath(consigneeXPath));
				Thread.sleep(500);
				consigneeElement.click();
			} catch (Exception e) {
				logger.severe("Could not select consignee at index: " + index + ", Error: " + e.getMessage());
			}
		}
	}
	/**
	 * This method used to click on search button and wait for loader 
	 */
	public void Search() {
		wt.waitToClick(searchbtn, DEFAULT_TIMEOUT);
		this.waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to select multiple value from drop down
	 * @param dropdownLocator
	 * @param indices
	 * @param elementName
	 * @throws InterruptedException
	 */
	public void selectMultipleByIndex(By dropdownLocator, List<Integer> indices, String elementName)
			throws InterruptedException {
		driver.findElement(dropdownLocator).click();
		for (Integer index : indices) {
			try {
				String itemXPath = "(//li[contains(@class, 'p-multiselect-item')])[" + index + "]";
				WebElement itemElement = driver.findElement(By.xpath(itemXPath));
				itemElement.click();
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.severe("Could not select " + elementName + " at index: " + index + " due to: " + e.getMessage());
			}
		}
	}
	
	/**
	 * This method used to select multiple warehouse from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleWarehouseByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(warehouse, indices, "Warehouse");
	}
	
	/**
	 * This method used to select multiple LDA from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleLDAByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectLDA, indices, "LDA");
	}
	/**
	 * This method used to select multiple Distributor from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleDistributorByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(distributor, indices, "Distributor");
	}
	
	/**
	 * This method used to select multiple customer from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleCustomerByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(customer, indices, "Customer");
	}
	
	/**
	 * This method used to select multiple pickup from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultiplePickuprByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectpickup, indices, "Pickup");
	}
	
	/**
	 * This method used to select multiple LTL from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleLTLByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectLTL, indices, "LTL");
	}
	
	/**
	 * This method used to select multiple user from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleUsersByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectUser, indices, "User");
	}
	
	/**
	 * This method used to select multiple Creation mode from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleCreationModeByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectCreationMode, indices, "Creation Mode");
	}
	
	/**
	 *  This method used to select multiple Status from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultiplestatusByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectstatus, indices, "Status");
	}
	
	/**
	 *  This method used to select multiple Document type from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleDocumentTypeByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectDoctype, indices, "Document Type");
	}
	
	/**
	 *  This method used to select multiple  print Status from list
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleDocPrintStatusByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectPrintStatus, indices, "Print Status");
	}

	/**
	 * This method is used to select multiple origin LDA
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleOriginLDAByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(originLDA, indices, "Origin LDA");
	}

	/**
	 * This method is used to select multiple New LDA
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleNewLDAByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(newLDA, indices, "New LDA");
	}

	/**
	 * This method is used to select multiple pickup LDA for closeout
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultiplePickupLDAByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(pickupLDA, indices, "New LDA");
	}
	
	/**
	 * This method used to fetch data those are select in drop down
	 * @return
	 */
	public List<String> fetchSelectedData() {
		List<String> selectedData = new ArrayList<>();
		try {
			String xpath = "//li[contains(@class, 'p-multiselect-item')]//div[@data-p-highlight='true']/ancestor::li//span";
			List<WebElement> elements = driver.findElements(By.xpath(xpath));
			for (WebElement element : elements) {
				String text = element.getText().trim();
				selectedData.add(text);
			}
		} catch (Exception e) {
			logger.severe("Error retrieving selected data: " + e.getMessage());
		}
		return selectedData;
	}
	
	/**
	 * This method used to get selected consignee list from drop down
	 * @return
	 */
	public List<String> fetchSelectedDataForConsignee() {
		List<String> selectedData = new ArrayList<>();
		try {
			String xpath = "//div[contains(@class, 'p-checkbox p-component')]//div[@data-p-highlight='true']/ancestor::li//span[@class='ml-2 flex-grow-1']";
			List<WebElement> elements = driver.findElements(By.xpath(xpath));
			for (WebElement element : elements) {
				String text = element.getText().trim();
				selectedData.add(text);
			}
		} catch (Exception e) {
			logger.severe("Error retrieving selected data: " + e.getMessage());
		}
		return selectedData;
	}
	
	/**
	 * This method used to verify select data and search data on grid
	 * @param index
	 * @throws InterruptedException
	 */
	public void validateDataInGrid(int index) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		List<String> expectedValues = fetchSelectedData();

		try {
			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//*[name()='svg'][@class='p-icon p-multiselect-close-icon'])[1]")));
			closeIcon.click();
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
		Search();
		waitForLoaderToDisappear();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='panel1bh-content']//table/tbody")));
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));

		if (rows.isEmpty()) {
			logger.warning("No records found after applying filter.");
			return;
		}

		System.out.println("Column name " + driver
				.findElement(By.xpath(
						"//*[@id='panel1bh-content']//thead//tr[1]//th[" + index + "]//*[@class='p-column-title']"))
				.getText().trim());

		for (int i = 1; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = String.format("//*[@id='panel1bh-content']//table/tbody/tr[%d]/td[%d]", i, index);
				List<WebElement> cellElements = driver.findElements(By.xpath(cellXPath));

//				if (!cellElements.isEmpty()) {
//					String cellText = cellElements.get(0).getText().trim();
//					if (expectedValues.contains(cellText)) {
//						// match
//					} else {
//						// mismatch logging if needed
//					}
//				}
				if (!cellElements.isEmpty()) {
					String cellText = cellElements.get(0).getText().trim();

					boolean matchFound = expectedValues.stream().anyMatch(ev -> ev.contains(cellText));

					System.out.println("Actual cell value: " + cellText);
					Assert.assertTrue(matchFound, "Row " + i + " mismatch. Actual: '" + cellText
							+ "' | Expected partial match from: " + expectedValues);
				}

			} catch (Exception e) {
				logger.severe("Error retrieving text for Row " + i + ": " + e.getMessage());
			}
		}
	}
	
	/**
	 * This method used to check selected data and grid data check on grid 
	 * and this method for dynamic grid
	 * @param index
	 * @throws InterruptedException
	 */
	public void validateDataInGridForScroll(int index) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		List<String> expectedValues = fetchSelectedData();

		try {
			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//*[name()='svg'][@class='p-icon p-multiselect-close-icon'])[1]")));
			closeIcon.click();
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
		Search();
		waitForLoaderToDisappear();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='panel1bh-content']//table/tbody")));
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));

		if (rows.isEmpty()) {
			logger.warning("No records found after applying filter.");
			return;
		}

		System.out.println("column name " + driver
				.findElement(By.xpath(
						"//*[@class='scrollable-pane']//thead//tr[1]//th[" + index + "]//*[@class='p-column-title']"))
				.getText().trim());

		for (int i = 2; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = String.format("//*[@class='scrollable-pane']//table/tbody/tr[%d]/td[%d]", i, index);
				List<WebElement> cellElements = driver.findElements(By.xpath(cellXPath));

				if (!cellElements.isEmpty()) {
					String cellText = cellElements.get(0).getText().trim();

					boolean matchFound = expectedValues.stream().anyMatch(ev -> ev.contains(cellText));

					System.out.println("Actual cell value: " + cellText);
					Assert.assertTrue(matchFound, "Row " + i + " mismatch. Actual: '" + cellText
							+ "' | Expected partial match from: " + expectedValues);

				} else {
					System.out.println("No record found Foe filter " + index);
				}

			} catch (Exception e) {
				logger.severe("Error retrieving text for Row " + i + ": " + e.getMessage());
			}
		}
	}
	
	/**
	 * This method check data filter contains same value for drop location
	 * @param index
	 * @throws InterruptedException
	 */
	public void validateDataInGridForScrollForDropLoc(int index) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		List<String> expectedValues = fetchSelectedData();

		try {
			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//*[name()='svg'][@class='p-icon p-multiselect-close-icon'])[1]")));
			closeIcon.click();
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
		Search();
		waitForLoaderToDisappear();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='panel1bh-content']//table/tbody")));
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));

		if (rows.isEmpty()) {
			logger.warning("No records found after applying filter.");
			return;
		}

		System.out.println("column name " + driver
				.findElement(By.xpath(
						"//*[@class='scrollable-pane']//thead//tr[1]//th[" + index + "]//*[@class='p-column-title']"))
				.getText().trim());

		for (int i = 2; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = String.format("//*[@class='scrollable-pane']//table/tbody/tr[%d]/td[%d]", i, index);
				List<WebElement> cellElements = driver.findElements(By.xpath(cellXPath));

				if (!cellElements.isEmpty()) {
					String cellText = cellElements.get(0).getText().trim();

					boolean matchFound = expectedValues.stream()
							.anyMatch(expected -> cellText.toLowerCase().contains(expected.toLowerCase()));

					System.out.println("Actual cell value: " + cellText);
					Assert.assertTrue(matchFound, "Row " + i + " mismatch. Actual: '" + cellText
							+ "' | Expected partial match from: " + expectedValues);

				} else {
					System.out.println("No record found Foe filter " + index);
				}

			} catch (Exception e) {
				logger.severe("Error retrieving text for Row " + i + ": " + e.getMessage());
			}
		}
	}
	
	/**
	 * This method used to get data from consignee List and verify data on grid
	 */
	public void validateDataInGridForScrollForConsignee(int index) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		List<String> expectedValues = fetchSelectedDataForConsignee();

		try {
			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//*[name()='svg'][@class='p-icon p-multiselect-close-icon'])[1]")));
			closeIcon.click();
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
		Search();
		waitForLoaderToDisappear();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='panel1bh-content']//table/tbody")));
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));

		if (rows.isEmpty()) {
			logger.warning("No records found after applying filter.");
			return;
		}

		for (int i = 2; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = String.format("//*[@class='scrollable-pane']//table/tbody/tr[%d]/td[%d]", i, index);
				List<WebElement> cellElements = driver.findElements(By.xpath(cellXPath));

				if (!cellElements.isEmpty()) {
					String cellText = cellElements.get(0).getText().trim();

					boolean matchFound = expectedValues.stream().anyMatch(ev -> ev.contains(cellText));

					Assert.assertTrue(matchFound, "Row " + i + " mismatch. Actual: '" + cellText
							+ "' | Expected partial match from: " + expectedValues);

				} else {
					Assert.fail("Cell not found at row " + i);
				}

			} catch (Exception e) {
				logger.severe("Error retrieving text for Row " + i + ": " + e.getMessage());
			}
		}

	}
	
	/**
	 * This method used to check filter data for consignee for fixed grid
	 * @param index
	 * @throws InterruptedException
	 */
	public void validateDataInGridForConsignee(int index) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		List<String> expectedValues = fetchSelectedDataForConsignee();

		try {
			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("(//*[name()='svg'][@class='p-icon p-multiselect-close-icon'])[1]")));
			closeIcon.click();
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
		Search();
		waitForLoaderToDisappear();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='panel1bh-content']//table/tbody")));
		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));

		if (rows.isEmpty()) {
			logger.warning("No records found after applying filter.");
			return;
		}
		for (int i = 1; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = String.format("//*[@id='panel1bh-content']//table/tbody/tr[%d]/td[%d]", i, index);
				List<WebElement> cellElements = driver.findElements(By.xpath(cellXPath));

//				if (!cellElements.isEmpty()) {
//					String cellText = cellElements.get(0).getText().trim();
//					if (expectedValues.contains(cellText)) {
//						// match
//					} else {
//						// mismatch logging if needed
//					}
//				}
				if (!cellElements.isEmpty()) {
					String cellText = cellElements.get(0).getText().trim();

					boolean matchFound = expectedValues.stream().anyMatch(ev -> ev.contains(cellText));

					System.out.println("Actual cell value: " + cellText);
					Assert.assertTrue(matchFound, "Row " + i + " mismatch. Actual: '" + cellText
							+ "' | Expected partial match from: " + expectedValues);
				}

			} catch (Exception e) {
				logger.severe("Error retrieving text for Row " + i + ": " + e.getMessage());
			}
		}
	}
	
	/**
	 * This method used to search and check data in grid 
	 * this for text field data
	 * @param fetchValueLocator
	 * @param inputFieldLocator
	 * @param logEntityName
	 * @throws InterruptedException
	 */
	public void searchAndValidate(By fetchValueLocator, By inputFieldLocator, String logEntityName)
			throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement valueElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fetchValueLocator));
		String expectedValue = valueElement.getText().trim();
		logger.info("Extracted " + logEntityName + ": " + expectedValue);
		WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(inputFieldLocator));
		inputField.clear();
		inputField.sendKeys(expectedValue);
		Search();
		waitForLoaderToDisappear();
		WebElement resultElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fetchValueLocator));
		String actualValue = resultElement.getText().trim();
		if (expectedValue.equals(actualValue)) {
			logger.info(logEntityName + " match confirmed.");
		} else {
			logger.warning(logEntityName + " mismatch: Expected " + expectedValue + ", but found " + actualValue);
		}
	}
	
	/**
	 * Clears previous filters and searches for a date in a specific column using the "From Date" value.
	 * @param columnIndex Index of the column to search
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void searchAndValidateDateByColumn(int columnIndex) throws InterruptedException {
		clickClearButton();
		searchAndValidate(By.xpath(String.format("//*[@id='panel1bh-content']//table/tbody/tr[2]/td[%d]", columnIndex)),
				fromdate, "From Date");
	}
	
	/**
	 * Searches for a "To Date" value in a specific column and validates it.
	 * @param columnIndex Index of the column to search
	 * @throws InterruptedException if waiting is interrupted/td[%d]
	 */
	public void searchAndValidateToDateByColumn(int columnIndex) throws InterruptedException {
		searchAndValidate(By.xpath(String.format("//*[@class='scrollable-pane']//tr[2]//td[%d]", columnIndex)),
				todate, "To Date");
	}
	
	/**
	 * Searches for an order number in the table and validates it.
	 * @param fetchOrderTextLocator Locator for the order number cell
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void searchAndValidateOrderNumber(By fetchOrderTextLocator) throws InterruptedException {
		searchAndValidate(fetchOrderTextLocator, ordernum, "Order Number");
	}

	/**
	 * Searches and validates the customer order number in the table.
	 * @param fetchCustomerOrderTextLocator Locator for the customer order number cell
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void searchAndValidateCustomerOrderNumber(By fetchCustomerOrderTextLocator) throws InterruptedException {
		searchAndValidate(fetchCustomerOrderTextLocator, entercustordernum, "Customer Order Number");
	}
	
	/**
	 * Searches for a pallet number in the table and validates it.
	 * @param fetchPalletTextLocator Locator for the pallet number cell
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void searchAndValidatePalletNo(By fetchPalletTextLocator) throws InterruptedException {
		searchAndValidate(fetchPalletTextLocator, enterPalletNoLocator, "Pallet Number");
	}
	
	/**
	 * Searches for a truck number in the table and validates it.
	 * @param fetchPalletTextLocator Locator for the truck number cell
	 * @throws InterruptedException if waiting is interrupted
	 *
	public void searchAndValidateTruckNo(By fetchPalletTextLocator) throws InterruptedException {
		searchAndValidate(fetchPalletTextLocator, enterTruckNoLocator, "Truck Number");
	}
	
	/**
	 * Searches for a truck name in the table and validates it.
	 * @param fetchPalletTextLocator Locator for the truck name cell
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void searchAndValidateTruckName(By fetchPalletTextLocator) throws InterruptedException {
		searchAndValidate(fetchPalletTextLocator, enterTruckNameLocator, "Truck Name");
	}

	public void validateColumnFiltersGeneric(String columnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex, boolean digitsOnly) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		String rawText = driver.findElement(By.xpath(columnXPath)).getText();
		String firstRowData = digitsOnly ? rawText.replaceAll("[^0-9]", "") : rawText;

		if (firstRowData.isEmpty()) {
			logger.warning("No data found in the first row. Skipping validation.");
			return;
		}

		String[] conditions = { "Starts with", "Ends with", "Equals", "Contains", "Not contains", "Not equals" };
		String lastUsedFilterValue = "";

		for (String condition : conditions) {
			WebElement filterIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(filterIconXPath)));
			try {
				js.scrollIntoView(filterIcon);
				filterIcon.click();
			} catch (Exception e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterIcon);
			}
			waitForLoaderToDisappear();
			selectFilterCondition(condition);

			WebElement filterInput = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterInputXPath)));
			filterInput.clear();
			filterInput.sendKeys(firstRowData);
			waitForLoaderToDisappear();

			lastUsedFilterValue = firstRowData;
			if (isNoRecordsFound()) {
				logger.warning("No records found for filter condition '" + condition + "'. Skipping.");
				continue;
			}

			List<WebElement> rows = driver.findElements(By.xpath(tableDataXPath));
			for (int i = 1; i <= Math.min(10, rows.size()); i++) {
				try {
					WebElement cellElement = driver
							.findElement(By.xpath(tableDataXPath + "[" + i + "]/td[" + columnIndex + "]"));
					String cellText = digitsOnly ? cellElement.getText().replaceAll("[^0-9]", "")
							: cellElement.getText();
					validateCondition(condition, firstRowData, cellText);
				} catch (Exception e) {
					logger.warning("Error reading Row " + i + ": " + e.getMessage());
				}
			}
		}

		resetFilter(filterIconXPath, filterInputXPath);
		validateValueExists(tableDataXPath, columnIndex, lastUsedFilterValue, digitsOnly);
	}
	
	/**
	 * Validates column filters for string/text values using multiple conditions.
	 * @param columnXPath XPath of the first row of the column
	 * @param filterIconXPath XPath of the filter icon
	 * @param filterInputXPath XPath of the filter input field
	 * @param tableDataXPath XPath of the table rows
	 * @param columnIndex Index of the column to validate
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void validateColumnFilters(String columnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex) throws InterruptedException {
		validateColumnFiltersGeneric(columnXPath, filterIconXPath, filterInputXPath, tableDataXPath, columnIndex,
				false);
	}
	
	/**
	 * Validates column filters for numeric/LDA values by removing non-digit characters.
	 * @param columnXPath XPath of the first row of the column
	 * @param filterIconXPath XPath of the filter icon
	 * @param filterInputXPath XPath of the filter input field
	 * @param tableDataXPath XPath of the table rows
	 * @param columnIndex Index of the column to validate
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void validateLDAColumnFilters(String columnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex) throws InterruptedException {
		validateColumnFiltersGeneric(columnXPath, filterIconXPath, filterInputXPath, tableDataXPath, columnIndex, true);
	}
	
	/**
	 * Validates a string cell value against a filter value based on the specified condition.
	 * @param condition Comparison type (e.g., "Starts with", "Contains", "Equals")
	 * @param filterValue Value used for filtering
	 * @param cellValue Value from the cell to validate
	 * @return true if the condition is satisfied, otherwise false
	 */
	private boolean validateCondition(String condition, String filterValue, String cellValue) {
		switch (condition) {
		case "Starts with":
			return cellValue.startsWith(filterValue);
		case "Contains":
			return cellValue.contains(filterValue);
		case "Not contains":
			return !cellValue.contains(filterValue);
		case "Ends with":
			return cellValue.endsWith(filterValue);
		case "Equals":
			return cellValue.equals(filterValue);
		case "Not equals":
			return !cellValue.equals(filterValue);
		case "No Filter":
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Selects a filter condition from the filter dropdown list.
	 * @param condition The filter condition to select (e.g., "Date is", "No Filter")
	 */

	private void selectFilterCondition(String condition) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			String xpath = "//ul[@class='p-column-filter-row-items']";
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

			String conditionXPath = xpath + "/li[normalize-space(text())='" + condition + "']";
			WebElement conditionElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(conditionXPath)));
			try {
				conditionElement.click();
			} catch (Exception e) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", conditionElement);
			}
		} catch (Exception e) {
			logger.severe("Error selecting filter condition: " + e.getMessage());
		}
	}
	/**
	 * Validates date filter functionality on a column by applying multiple conditions
	 * and verifying that the filtered rows match the expected date criteria.
	 * @param dateColumnXPath XPath of the first row in the date column
	 * @param filterIconXPath XPath of the filter icon
	 * @param filterInputXPath XPath of the filter input field
	 * @param tableDataXPath XPath of the table rows
	 * @param columnIndex Index of the date column in the table
	 * @throws InterruptedException if waiting is interrupted
	 */
	public void validateDateFilters(String dateColumnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex) throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		String firstRowDate = driver.findElement(By.xpath(dateColumnXPath)).getText();
		String[] conditions = { "Date is", "Date is not", "Date is before", "Date is after", "No Filter" };

		for (String condition : conditions) {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(filterIconXPath))).click();
				waitForLoaderToDisappear();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(filterInputXPath)));
				selectFilterCondition(condition);

				if (!applyDateInput(filterInputXPath, firstRowDate)) {
					logger.warning("Failed to apply filter input for condition: " + condition);
					continue;
				}

				if (driver.findElements(By.xpath("//td[contains(text(),'Record not found.')]")).size() > 0) {
					logger.warning("No records found for condition '" + condition + "'. Skipping validation.");
					continue;
				}

				validateFilteredRows(tableDataXPath, columnIndex, condition, firstRowDate);

			} catch (Exception e) {
				logger.severe("Unexpected error with condition '" + condition + "': " + e.getMessage());
			}
		}
	}

	/**
	 * Enters a date value into the specified input field, retrying up to 3 times if needed,
	 * and ensures the value is correctly set.
	 * @param inputXPath XPath of the date input field
	 * @param dateValue Date value to enter
	 * @return true if the value was successfully applied, otherwise false
	 */
	private boolean applyDateInput(String inputXPath, String dateValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputXPath)));
				wait.until(ExpectedConditions.elementToBeClickable(input));

				input.clear();
				waitForLoaderToDisappear();
				input.sendKeys(dateValue);
				waitForLoaderToDisappear();

				String entered = input.getAttribute("value");
				if (entered == null || !entered.contains(dateValue.split(" ")[0])) {
					((JavascriptExecutor) driver).executeScript(
							"arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));", input,
							dateValue.split(" ")[0]);
					wait.until(ExpectedConditions.attributeToBeNotEmpty(input, "value"));
				}
				waitForLoaderToDisappear();
				return true;
			} catch (StaleElementReferenceException se) {
				logger.warning("StaleElementReferenceException (attempt " + attempt + ")");
			} catch (Exception e) {
				logger.warning("Error on attempt " + attempt + ": " + e.getMessage());
			}
		}
		return false;
	}
	
	/**
	 * Validates the first 10 rows of a column against a date filter condition.
	 * Logs a warning if no rows match the specified condition.
	 * @param dataXPath XPath of the table rows
	 * @param colIndex Index of the column to validate
	 * @param condition Date comparison condition (e.g., "Date is", "Date is before")
	 * @param inputDate Date value to compare against
	 */

	private void validateFilteredRows(String dataXPath, int colIndex, String condition, String inputDate) {
		List<WebElement> rows = driver.findElements(By.xpath(dataXPath));
		boolean foundMatch = false;

		for (int i = 1; i <= Math.min(10, rows.size()); i++) {
			try {
				WebElement cell = driver.findElement(By.xpath(dataXPath + "[" + i + "]/td[" + colIndex + "]"));
				String cellText = cell.getText();

				if (validatedateCondition(condition, inputDate, cellText)) {
					foundMatch = true;
				}
			} catch (Exception e) {
				logger.warning("Error reading row " + i + ": " + e.getMessage());
			}
		}

		if (!foundMatch) {
			logger.warning("No matching rows found for condition: " + condition);
		}
	}
	
	/**
	 * Extracts only the date part (before the first space) from a date-time string.
	 * @param dateTimeStr Date-time string
	 * @return Date portion of the string, or empty string if input is null/empty
	 */
	private String extractDateOnly(String dateTimeStr) {
		return (dateTimeStr == null || dateTimeStr.isEmpty()) ? "" : dateTimeStr.split(" ")[0];
	}
	
	/**
	 * Validates a date comparison between the input date and the cell date
	 * based on the specified condition.
	 * @param condition Comparison type (e.g., "Date is", "Date is before")
	 * @param inputDateTime Input date as a string
	 * @param cellDateTime Cell date as a string
	 * @return true if the condition is satisfied, otherwise false
	 */
	private boolean validatedateCondition(String condition, String inputDateTime, String cellDateTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date inputDate = sdf.parse(extractDateOnly(inputDateTime));
			Date cellDate = sdf.parse(extractDateOnly(cellDateTime));

			switch (condition) {
			case "Date is":
				return cellDate.equals(inputDate);
			case "Date is not":
				return !cellDate.equals(inputDate);
			case "Date is before":
				return cellDate.before(inputDate);
			case "Date is after":
				return cellDate.after(inputDate);
			case "No Filter":
				return true;
			default:
				return false;
			}
		} catch (Exception e) {
			logger.warning("Date parsing failed: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Navigates to the Order page from the menu after waiting for the loader to disappear.
	 */
	public void orderpageMenulanding() {
		waitForLoaderToDisappear();
		// Use robust click instead of raw Actions chain
		safeClick(orders);
		safeClick(orderpage);
	}

	public void waitForPopupToDisappear1() {
		try {
			WebElement toastMsgPopup = driver.findElement(By.xpath("//div[@role='alert']//div[2]"));
			wt.invisibility(toastMsgPopup, DEFAULT_TIMEOUT);
		} catch (TimeoutException e) {
			logger.warning("Popup did not disappear within the timeout.");
		}
	}
	
	/**		
	  * show error 'wait for element but no show' failed method after not genereate barcode
	  */
	
	
	/**
	 * Waits for a toast popup to disappear if it appears on the page.
	 */
	public void waitForPopupToDisappear() {
		By popupLocator = By.xpath("//div[@role='alert']");

	    List<WebElement> popups = driver.findElements(popupLocator);

	    // If popup is not present OR not displayed → continue immediately
	    if (popups.isEmpty() || !popups.get(0).isDisplayed()) {
	        logger.info("Popup not displayed. Continuing execution.");
	        return;
	    }

	    // Popup is displayed → wait until it disappears
	    try {
	        wait.until(ExpectedConditions.invisibilityOf(popups.get(0)));
	        logger.info("Popup was displayed and has disappeared.");
	    } catch (TimeoutException e) {
	        logger.info("Popup did not disappear within timeout. Continuing execution.");
	    }
	    
//		By popupLocator = By.xpath("//div[@role='alert']//div[2]");
//		List<WebElement> popups = driver.findElements(popupLocator);
//		if (!popups.isEmpty() && popups.get(0).isDisplayed()) {
//			wait.until(ExpectedConditions.invisibilityOfElementLocated(popupLocator));
//			logger.info("Toast popup appeared and disappeared.");
//		} else {
//			logger.info("No popup appeared. Continuing...");
//		}
	}
	/**
	 * Waits for popups and loaders to disappear, then clicks the Create Order button.
	 * @throws InterruptedException if the waiting is interrupted
	 */
	public void createOrder() throws InterruptedException {
		waitForPopupToDisappear();
		waitForLoaderToDisappear();
		wt.waitToClick(CreatOrder, DEFAULT_TIMEOUT);
	}
	
	/**
	 * Checks whether an element is present on the page.
	 * @param locator Locator of the element to check
	 * @return true if the element exists, otherwise false
	 */
	public boolean isElementPresent(By locator) {
		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
			boolean exists = !driver.findElements(locator).isEmpty();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			return exists;
		} catch (Exception e) {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			return false;
		}
	}
	
	/**
	 * Retrieves and returns the order status text from the list.
	 * @return Order status text, or an error message if not visible
	 */
	public String verifyOrderStatusOnList() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ordstatusLocator));
			return statusElement.getText().trim();
		} catch (TimeoutException e) {
			logger.severe(" Timeout: Status element not visible.");
			return "Status element not visible.";
		} catch (Exception e) {
			logger.severe(" Exception while verifying order status: " + e.getMessage());
			return "Error fetching status";
		}
	}
	
	/**
	 * Selects multiple dropdown options based on their index positions.
	 * @param indices List of option indices to select
	 */
	public void selectMultipleDropByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		driver.findElement(selectdrop).click();
		for (Integer index : indices) {
			try {
				String dropXPath = "(//li[contains(@class, 'p-multiselect-item')])[" + index + "]";
				WebElement dropElement = driver.findElement(By.xpath(dropXPath));
				dropElement.click();
			} catch (Exception e) {
				logger.warning("Could not select drop at index: " + index + " due to: " + e.getMessage());
			}
		}
	}
	
	/**
	 * Extracts text from an info icon popup, inputs it into a search field,
	 * performs a search, and verifies the popup value matches the originally extracted text.
	 * @param xpathMap Map containing XPaths for the icon, popup text, and input field
	 */
	public void extractInputFromIconSearchVerify(Map<String, String> xpathMap) {
		clickClearButton();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement infoIcon = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("iconXPath"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", infoIcon);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", infoIcon);

			WebElement popupTextElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("popupTextXPath"))));
			String extractedText = popupTextElement.getText().trim();

			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath));
			closeIcon.click();

			WebElement inputField = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("inputFieldXPath"))));
			inputField.clear();
			inputField.sendKeys(extractedText);

			Search();

			WebElement infoIconAfterSearch = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("iconXPath"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", infoIconAfterSearch);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", infoIconAfterSearch);

			WebElement resultTextElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("popupTextXPath"))));
			String resultText = resultTextElement.getText().trim();

			WebElement closeIconAfter = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath));
			closeIconAfter.click();

			if (!resultText.equals(extractedText)) {
				logger.warning("Verification failed: expected '" + extractedText + "' but found '" + resultText + "'");
			}

		} catch (Exception e) {
			logger.severe("Exception in extractInputFromIconSearchVerify: " + e.getMessage());
		}
	}
	
	/**
	 * Enters the truck number into the specified input field after making it visible.
	 * @param truckNo Truck number to enter
	 * @param truckFieldLocator Locator of the truck number field
	 */
	private void enterTruckNumber(String truckNo, By truckFieldLocator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement truckField = wait.until(ExpectedConditions.visibilityOfElementLocated(truckFieldLocator));
		truckField.clear();
		truckField.sendKeys(truckNo);
	}

	public void searchAndValidateTruckNo(By fetchTruckNoLocator, By truckFieldLocator) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement truckElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fetchTruckNoLocator));
			String truckNo = truckElement.getText().trim();
			enterTruckNumber(truckNo, truckFieldLocator);
			Search();
			waitForLoaderToDisappear();
			WebElement listedTruckElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(fetchTruckNoLocator));
			String listedTruckNo = listedTruckElement.getText().trim();
			if (!listedTruckNo.equalsIgnoreCase(truckNo)) {
				// mismatch logging if needed
			}
		} catch (Exception e) {
			logger.severe("Exception in searchAndValidateTruckNo: " + e.getMessage());
		}
	}

	public void validateNumericFilters(String columnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex) throws InterruptedException {
		try {
			String rawText = driver.findElement(By.xpath(columnXPath)).getText().replaceAll(NUMERIC_SANITIZER_REGEX,
					"");
			String[] conditions = { "Equals", "Not equals", "Less than", "Less than or equal to", "Greater than",
					"Greater than or equal to" };
			for (String condition : conditions) {
				applyNumericFilter(filterIconXPath, filterInputXPath, condition, rawText);
				if (isNoRecordsFound()) {
					logger.warning("No records found for condition: '" + condition + "'");
					continue;
				}
				validateNumericFilterResults(tableDataXPath, columnIndex, condition, rawText);
			}
		} catch (Exception e) {
			logger.severe("Exception during numeric filter validation: " + e.getMessage());
		} finally {
			clearFilter(filterIconXPath, filterInputXPath);
			logger.info("Filters cleared after validation.");
		}
	}

	private void applyNumericFilter(String filterIconXPath, String filterInputXPath, String condition, String value)
			throws InterruptedException {
		WebElement filterIcon = driver.findElement(By.xpath(filterIconXPath));
		WebElement filterInput = driver.findElement(By.xpath(filterInputXPath));
		wt.waitToClickByWebelement(filterIcon, DEFAULT_TIMEOUT);
		waitForLoaderToDisappear();
		selectFilterCondition(condition);
		wt.waitToClickByWebelement(filterInput, DEFAULT_TIMEOUT);
		filterInput.clear();
		filterInput.sendKeys(value);
		waitForLoaderToDisappear();
	}

	private void validateNumericFilterResults(String tableDataXPath, int columnIndex, String condition,
			String filterValue) {
		List<WebElement> rows = driver.findElements(By.xpath(tableDataXPath));
		int validRowCount = 0;
		for (int i = 1; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = tableDataXPath + "[" + i + "]/td[" + columnIndex + "]";
				WebElement cell = driver.findElement(By.xpath(cellXPath));
				String cellText = cell.getText().replaceAll(NUMERIC_SANITIZER_REGEX, "").trim();
				if (!cellText.isEmpty()) {
					validRowCount++;
					validateNumericCondition(condition, filterValue, cellText);
				}
			} catch (Exception e) {
				logger.warning("Error reading row " + i + ": " + e.getMessage());
			}
		}
		if (validRowCount < 10) {
			logger.warning("Only " + validRowCount + " rows had valid numeric data (expected 10).");
		}
	}
	
	/**
	 * Validates a numeric comparison between the filter value and the cell value
	 * based on the specified condition.
	 * @param condition Comparison type (e.g., Equals, Less than)
	 * @param filterValue Numeric value used for filtering
	 * @param cellValue Numeric value from the grid cell
	 * @return true if the condition is satisfied, otherwise false
	 */
	private boolean validateNumericCondition(String condition, String filterValue, String cellValue) {
		try {
			double filterNum = Double.parseDouble(filterValue);
			double cellNum = Double.parseDouble(cellValue);
			switch (condition) {
			case "Equals":
				return cellNum == filterNum;
			case "Not equals":
				return cellNum != filterNum;
			case "Less than":
				return cellNum < filterNum;
			case "Less than or equal to":
				return cellNum <= filterNum;
			case "Greater than":
				return cellNum > filterNum;
			case "Greater than or equal to":
				return cellNum >= filterNum;
			default:
				return false;
			}
		} catch (NumberFormatException e) {
			logger.warning("Invalid numeric input: " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Clears the applied filter by selecting 'No Filter', removing input text,
	 * and triggering the field update.
	 * @param filterIconXPath XPath of the filter icon
	 * @param filterInputXPath XPath of the filter input field
	 */
	private void clearFilter(String filterIconXPath, String filterInputXPath) throws InterruptedException {
		try {
			WebElement filterIcon = driver.findElement(By.xpath(filterIconXPath));
			WebElement filterInput = driver.findElement(By.xpath(filterInputXPath));
			wt.waitToClickByWebelement(filterIcon, DEFAULT_TIMEOUT);
			selectFilterCondition("No Filter");
			filterInput.clear();
			filterInput.sendKeys(Keys.TAB);
		} catch (Exception e) {
			logger.severe("Exception while clearing filter: " + e.getMessage());
		}
		waitForLoaderToDisappear();
	}
	
	/**
	 * Checks whether the grid shows a 'Record not found.' message.
	 * @return true if no records are found, otherwise false
	 */
	private boolean isNoRecordsFound() {
		return driver.findElements(By.xpath("//td[contains(text(),'Record not found.')]")).size() > 0;
	}
	
	/**
	 * Clicks the Save button after waiting for it to be clickable.
	 */
	public void save() {
		wt.waitToClick(save, DEFAULT_TIMEOUT);
	}
	
	/**
	 * Attempts to close the popup filter icon if present.
	 */
	public void closeIcon() {
		try {
			wt.waitToClick(closepopupIcon, DEFAULT_TIMEOUT);
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
	}

//	public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {
//		String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
//		ObjectMapper mapper = new ObjectMapper();
//		return mapper.readValue(jsonContent,
//				new com.fasterxml.jackson.core.type.TypeReference<List<HashMap<String, String>>>() {
//				});
//	}
	/**
	 * Validates that a field contains the correct numeric value extracted from an alphanumeric input,
	 * formatted to the defined decimal precision.
	 * @param fieldLocator Locator of the input field
	 * @param inputValue Provided alphanumeric input
	 * @param fieldName Name of the field for assertion messages
	 */
	public void validateAlphaNumericValue(By fieldLocator, String inputValue, String fieldName) {
		try {
			wait.until(ExpectedConditions.attributeToBeNotEmpty(driver.findElement(fieldLocator), "value"));
			String actualValue = driver.findElement(fieldLocator).getAttribute("value").trim();
			String expected = inputValue.replaceAll(NUMERIC_SANITIZER_REGEX, "");
			if (expected.isEmpty()) {
				throw new AssertionError(fieldName + " input has no numeric part.");
			}
			String expectedFormatted = String.format("%." + DECIMAL_PRECISION + "f", Double.parseDouble(expected));
			String actualFormatted = String.format("%." + DECIMAL_PRECISION + "f", Double.parseDouble(actualValue));
			if (!expectedFormatted.equals(actualFormatted)) {
				throw new AssertionError(fieldName + " value mismatch. Expected: " + expectedFormatted + ", but got: "
						+ actualFormatted);
			}
		} catch (Exception e) {
			logger.severe("Validation failed for " + fieldName + ": " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Scrolls to the icon and clicks it using JavaScript executor.
	 * @param iconXPath XPath of the icon to click
	 */
	private void clickIcon(String iconXPath) {
		WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(iconXPath)));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", icon);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
	}
	
	/**
	 * Extracts text by clicking two icons and reading the popup content.
	 * @param iconXPath1 First icon XPath
	 * @param iconXPath2 Second icon XPath
	 * @param popupTextXPath XPath of popup text element
	 */
	private String extractTextFromIcons(String iconXPath1, String iconXPath2, String popupTextXPath) {
		clickIcon(iconXPath1);
		clickIcon(iconXPath2);
		WebElement popupTextElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(popupTextXPath)));
		return popupTextElement.getText().trim();
	}
	
	/**
	 * Closes the popup by clicking the close icons in sequence.
	 */
	private void closePopupIcons() {
		WebElement closeIcon2 = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath2));
		closeIcon2.click();
		WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath));
		closeIcon.click();
	}
	
	/**
	 * Enters the provided text into the input field after clearing existing value.
	 * @param inputFieldXPath XPath of the input field
	 * @param text Text to enter
	 */
	private void enterTextInField(String inputFieldXPath, String text) {
		WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputFieldXPath)));
		inputField.clear();
		inputField.sendKeys(text);
	}
	
	/**
	 * This method extracts text from icons, inputs it into a field,
	 * performs a search, and verifies the result matches the extracted text.
	 * @param xpathMap Map containing required XPaths for icons and fields
	 */
	public void extractInputAndVerifyFromIcons(Map<String, String> xpathMap) {
		try {
			String extractedText = extractTextFromIcons(xpathMap.get("iconXPath"), xpathMap.get("iconXPath2"),
					xpathMap.get("popupTextXPath"));
			closePopupIcons();
			enterTextInField(xpathMap.get("inputFieldXPath"), extractedText);
			Search();
			String resultText = extractTextFromIcons(xpathMap.get("iconXPath"), xpathMap.get("iconXPath2"),
					xpathMap.get("popupTextXPath"));
			closePopupIcons();
			Assert.assertEquals(resultText, extractedText,
					"Verification failed: expected '" + extractedText + "' but found '" + resultText + "'");
		} catch (Exception e) {
			logger.severe("Exception in extractInputAndVerifyFromIcons: " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * This method used to wait and click with java script executor
	 * @param locator
	 * @param timeoutInSeconds
	 */
	public void waitAndClickWithJS(By locator, int timeoutInSeconds) {
		WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
		WebElement element = customWait.until(ExpectedConditions.elementToBeClickable(locator));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

//    public void searchColoumFilter(By element, String data) {
//        driver.findElement(element).clear();
//        driver.findElement(element).sendKeys(data);
//        try {
//            wt.waitForElement(loader, 4);
//            waitForLoaderToDisappear();
//        } catch (Exception e) {
//            logger.info("Showing error while searching coloum filter");
//        }
//    }

	/**
	 * Safari & Chrome-friendly sendKeys method using By locator. Converts null
	 * input to empty string automatically.
	 * @throws InterruptedException 
	 */
	public void searchColoumFilter(By locator, String input) {
		WebElement element = driver.findElement(locator);
		element.clear();
		element.sendKeys(input == null ? "" : input);
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitForLoaderToDisappear();
	}

	public void extractInputFromIconSearchVerifyTwoIcons(Map<String, String> xpathMap) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement infoIcon = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("iconXPath"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", infoIcon);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", infoIcon);

			WebElement infoIcon2 = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("iconXPath2"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", infoIcon2);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", infoIcon2);

			WebElement popupTextElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("popupTextXPath"))));
			String extractedText = popupTextElement.getText().trim();

			WebElement closeIcon2 = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath2));
			closeIcon2.click();

			WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath));
			closeIcon.click();

			WebElement inputField = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("inputFieldXPath"))));
			inputField.clear();
			inputField.sendKeys(extractedText);

			Search();

			WebElement infoIconAfterSearch = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("iconXPath"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", infoIconAfterSearch);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", infoIconAfterSearch);

			WebElement infoIconAfterSearch2 = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath(xpathMap.get("iconXPath2"))));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", infoIconAfterSearch2);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", infoIconAfterSearch2);

			WebElement resultTextElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpathMap.get("popupTextXPath"))));
			String resultText = resultTextElement.getText().trim();

			WebElement closeIconafter2 = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath2));
			closeIconafter2.click();

			WebElement closeIconAfter = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath));
			closeIconAfter.click();

			if (!resultText.equals(extractedText)) {
				logger.warning("Verification failed: expected '" + extractedText + "' but found '" + resultText + "'");
			}

		} catch (Exception e) {
			logger.severe("Exception in extractInputFromIconSearchVerify: " + e.getMessage());
		}
	}
	
	/**
	 * This method used to click on search section
	 */
	public void searchSection() {
		waitForLoaderToDisappear();
		safeClick(searchsection);
	}
	
	/**
	 * This method used to check pick from drop down after change should check data change 
	 * on depended drop down
	 */
	public void pickFrom() {
		clickClearButton();
		try {
			driver.findElement(dropdown).click();
			List<WebElement> options = driver.findElements(dropdownOptions);
			for (int i = 0; i < Math.min(3, options.size()); i++) {
				options.get(i).click();
				driver.findElement(selectpickup).click();

				List<WebElement> items = driver.findElements(multiselectItems);
				for (int j = 0; j < Math.min(5, items.size()); j++) {
					logger.info("[" + (i + 1) + "] " + items.get(j).getText());
				}
				driver.findElement(closePickupPopup).click();
				driver.findElement(dropdown).click();
				options = driver.findElements(dropdownOptions);
			}
		} catch (Exception e) {
			logger.info("In Pick From filter somthing wrong");
		}
		clickClearButton();
	}
	
	/**
	 * This method will check that pagination page sized and data in girs should match
	 * @param itemsOnpage
	 * @param j
	 */
	public void paginationTest(By itemsOnpage, int j) {
		List<WebElement> pageNumbers = driver.findElements(By.xpath("//span[@class='p-paginator-pages']/button"));
		int totalPages = pageNumbers.size();

		for (int i = 0; i < 5 && i < totalPages; i++) {
			pageNumbers = driver.findElements(By.xpath("//span[@class='p-paginator-pages']//button"));
			pageNumbers.get(i).click();
			waitForLoaderToDisappear();
			String paginationSizeStr = getMandatoryText(
					By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[" + j + "]"));
			List<WebElement> itemsOnPage = driver.findElements(itemsOnpage);
			String itemSizeStr = String.valueOf(itemsOnPage.size());

			try {
				int paginationSize = Integer.parseInt(paginationSizeStr);
				int itemSize = Integer.parseInt(itemSizeStr);

				if (itemSize > paginationSize) {
					logger.info("Item count on page " + (i + 1) + " is less than expected pagination size.");
					Assert.assertEquals(itemSizeStr, paginationSizeStr, "Mismatch on page " + (i + 1));
				}
			} catch (NumberFormatException e) {
				logger.info("Failed to parse itemSize or paginationSize to integer: " + e.getMessage());
			}

			WebElement activePage = driver.findElement(
					By.xpath("//span[@class='p-paginator-pages']/button[contains(@class, 'p-highlight')]"));
			logger.info("Currently active page: " + activePage.getText());
		}
	}
	/**
	 * This method used to close pop up
	 */
	public void clickToClosePopUp() {
		try {
			wt.waitToClick(closePopup, 3);
		} catch (Exception e) {
			logger.info("HighlightOffIcon not found or not clickable: " + e.getMessage());
		}
	}
	
	/**
	 * This method used to close pop up if present
	 * @param closeBtnXpath
	 */
	public void closePopupIfPresent(String closeBtnXpath) {
		try {
			WebElement closeBtn = driver.findElement(By.xpath(closeBtnXpath));
			if (closeBtn.isDisplayed() && closeBtn.isEnabled()) {
				closeBtn.click();
				waitForPopupToDisappear();
			}
		} catch (NoSuchElementException e) {
			logger.info("No popup to close");
		} catch (StaleElementReferenceException e) {
			logger.info("Popup element became stale, could not close: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Failed to close popup: " + e.getMessage());
		}
	}

	// Add inside CommonPage

	/** Resets the active column filter to "No Filter" and clears the input. */
	private void resetFilter(String filterIconXPath, String filterInputXPath) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement filterIcon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(filterIconXPath)));
		// open the filter menu
		try {
			filterIcon.click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterIcon);
		}
		waitForLoaderToDisappear();

		// select "No Filter"
		selectFilterCondition("No Filter");

		// clear the input if present
		try {
			WebElement filterInput = driver.findElement(By.xpath(filterInputXPath));
			filterInput.clear();
			waitForLoaderToDisappear();
			filterInput.sendKeys(Keys.TAB); // commit/blur
		} catch (Exception e) {
			logger.info("Filter input not visible or already cleared.");
		}
		waitForLoaderToDisappear();
	}

	/**
	 * Verifies the table still contains a row whose cell text includes the given
	 * value.
	 */
	private void validateValueExists(String tableDataXPath, int columnIndex, String value, boolean digitsOnly) {
		if (isNoRecordsFound()) {
			logger.warning("No records found after removing filter.");
			return;
		}

		List<WebElement> rows = driver.findElements(By.xpath(tableDataXPath));
		for (int i = 1; i <= rows.size(); i++) {
			List<WebElement> cells = driver
					.findElements(By.xpath(tableDataXPath + "[" + i + "]/td[" + columnIndex + "]"));
			if (cells.isEmpty())
				continue;

			String cellText = digitsOnly ? cells.get(0).getText().replaceAll("[^0-9]", "") : cells.get(0).getText();

			if (cellText.contains(value)) {
				// found it; success
				return;
			}
		}
		logger.warning("Value '" + value + "' not found after clearing filter.");
	}

	/**
	 * This methods is used to check not record is displayed or not
	 * 
	 * @return
	 */
	public boolean checkElementNorecord() {
		List<WebElement> norecords = driver.findElements(By.xpath("//tr[@class='p-datatable-emptymessage']"));
		return !norecords.isEmpty() && norecords.get(0).isDisplayed();
	}

	/**
	 * This method checks whether a file exists at the given download location. If
	 * the file is found, it deletes the existing file to ensure a clean state for
	 * the next download.
	 */
	public void deleteExistingFiles() {
		File dir = new File(downloadDir);
		if (dir.isDirectory()) {
			Arrays.stream(dir
					.listFiles((d, name) -> name.endsWith(".xlsx") || name.endsWith(".xls") || name.endsWith(".pdf")))
					.forEach(file -> {
						System.out.println("Deleted file: " + file.getName());
						file.delete();
					});
		}
	}

	/**
	 * This method waits for a file to be downloaded in the specified directory. If
	 * a new file appears within the timeout, it returns true; otherwise, false.
	 *
	 * @return true if file is downloaded successfully, false otherwise
	 * @throws InterruptedException
	 */
	public boolean isFileDownloaded() throws InterruptedException {

		File dir = new File(downloadDir);
		File[] filesBefore = dir.listFiles();
		int initialCount = filesBefore != null ? filesBefore.length : 0;
		int timeoutSec = 30;
		int waited = 0;

		while (waited < timeoutSec) {
			Thread.sleep(1000);
			File[] filesAfter = dir.listFiles();

			if (filesAfter != null && filesAfter.length > initialCount) {
				System.out.println("File downloaded: " + filesAfter[filesAfter.length - 1].getName());
				return true;
			}
			waited++;
		}
		System.out.println("No file downloaded within timeout.");
		return false;
	}
	
	/**
	 * This method used to click on export pdf btn
	 */
	public void clickToExpPDF() {
		waitForPopupToDisappear();
		wt.waitToClick(PDFBtn, 10);
	}
	
	/**
	 * This method used to click on export excel btn
	 */
	public void clickToExpExcel() {
		waitForPopupToDisappear();
		wt.waitToClick(excelBtn, 10);
	}
	
	/**
	 * This method used to check is displayed invalid toast or not
	 * @return
	 */
	public boolean isInvalidToastDisplayed() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		try {
			WebElement progressBar = wait.until(ExpectedConditions.visibilityOfElementLocated(errorToast));

			if (progressBar.isDisplayed()) {
				WebElement toastMessage = driver.findElement(By.xpath("//div[@role='alert']//div[2]"));
				System.out.println("Captured error toast: " + toastMessage.getText());
				return true;
			}
		} catch (Exception e) {
			logger.info("No error: toast Not displayed.");
		}
		return false;
	}
	
	/**
	 * This method used to check extension of the file should be match 
	 * @param expectedExtension
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isFileDownloadWithExtension(String expectedExtension) throws InterruptedException {
		File dir = new File(downloadDir);
		ensureDownloadDirExists(dir);

		File[] filesBefore = dir.listFiles();
		int initialCount = filesBefore != null ? filesBefore.length : 0;
		int timeoutSec = 50;
		int waited = 0;

		while (waited < timeoutSec) {
			Thread.sleep(1000);
			File[] filesAfter = dir.listFiles();

			if (filesAfter != null && filesAfter.length > initialCount) {
				// Skip if partial download files are still present
				boolean hasPartial = Arrays.stream(filesAfter)
						.anyMatch(f -> f.getName().endsWith(".crdownload") || f.getName().endsWith(".part"));
				if (hasPartial) {
					waited++;
					continue;
				}

				// Find the most recently modified file
				File latestFile = Arrays.stream(filesAfter).max(Comparator.comparingLong(File::lastModified))
						.orElse(null);

				if (latestFile != null) {
					String fileName = latestFile.getName();
					logger.info("File downloaded: " + fileName);

					// Check file extension
					if (fileName.toLowerCase().endsWith(expectedExtension.toLowerCase())) {
						logger.info("File extension matches expected: " + expectedExtension);
						return true;
					} else {
						logger.warning(
								"File extension mismatch. Expected: " + expectedExtension + ", Found: " + fileName);
						return false;
					}
				}
			}
			waited++;
		}
		logger.info("No file downloaded within timeout.");
		return false;
	}
	
	/**
	 * Ensures that the specified download directory exists, creating it if necessary.
	 * @param dir Directory to check or create
	 */
	private void ensureDownloadDirExists(File dir) {
		if (!dir.exists()) {
			boolean ok = dir.mkdirs();
			if (!ok)
				logger.warning("Could not create download directory: " + dir.getAbsolutePath());
		}
	}
	
	/**
	 * This method used to open inbound pallet menu page
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public void inboundMenu() throws TimeoutException {
		waitForLoaderToDisappear();
		wt.waitToClick(barMenu, 10);
		wt.waitToClick(palletMenu, 10);
		safeClick(inboundpalletMenu);
		waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to click on outbound pallet menu
	 * @throws TimeoutException
	 */
	public void outboundMenu() throws TimeoutException {
		waitForLoaderToDisappear();
		wt.waitToClick(barMenu, 10);
		wt.waitToClick(palletMenu, 10);
		safeClick(outboundpalletMenu);
		waitForLoaderToDisappear();
	}

	/**
	 * Click on truck menu for inbound truck
	 */
	public void inboundtruckMenu() throws TimeoutException {
		waitForLoaderToDisappear();
		safeClick(barMenu);
		safeClick(truckMenu);
		safeClick(inboundTruckMenu);
		waitForLoaderToDisappear();
	}

	/**
	 * Click on truck menu for outbound truck
	 */
	public void outboundtruckMenu() throws TimeoutException {
		waitForLoaderToDisappear();
		safeClick(barMenu);
		safeClick(truckMenu);
		safeClick(obtTruckMenu);
		waitForLoaderToDisappear();
	}

	/**
	 * Opens Documents → Document Queue.
	 */
	public void docQueueMenu() throws TimeoutException, InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(docMenu, 10);
		Thread.sleep(500); // brief stabilization for nested menu
		wt.waitToClick(docGenMenu, 10);
		waitForLoaderToDisappear();
	}

	/**
	 * Clicks on the "Document Generation" menu.
	 * 
	 * @throws InterruptedException
	 */
	public void clickDocGen() throws InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(GenerateDocMenu, 10);
		waitForLoaderToDisappear();
	}

	/**
	 * Clicks on the "Split POD" menu.
	 */
	public void ClickSplitPod() {
		wt.waitToClick(splitPodMenu, 10);
		waitForLoaderToDisappear();
	}

	/**
	 * Open QA menu
	 */
	public void inboundQaMenu() throws TimeoutException, InterruptedException {
		waitForLoaderToDisappear();
		safeClick(docMenu);
		safeClick(qaMenu);
		Thread.sleep(300);
		safeClick(inboundqaMenu);
		waitForLoaderToDisappear();
	}

	/**
	 * Navigate to Outbound QA.
	 */
	public void outboundQAMenu() throws TimeoutException, InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(tracking, 10);
		Thread.sleep(1000);
		wt.waitToClick(qaMenu, 10);
		Thread.sleep(2000);
		wt.waitToClick(outboundQAMenu, 10);
		waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to select consignee QA menu
	 * @throws InterruptedException 
	 */
	public void consigneeQAMenu() throws InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(tracking, 10);
		Thread.sleep(1000);
		wt.waitToClick(qaMenu, 10);
		wt.waitToClick(consigneeQAMenu, 10);
		waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to open tracking update page
	 * @throws InterruptedException
	 */
	public void trackingMenu() throws InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(tracking, 20);
		Thread.sleep(1000);
		wt.waitToClick(trackingMenu, 10);
		waitForLoaderToDisappear();
	}
	
	
	/**
	 * This method used to open reconsignment page
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public void reconsignMenu() throws TimeoutException, InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(reconsignMenu, 20);
		Thread.sleep(2000);
		wt.waitToClick(reconsign, 10);
		waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to open return page
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public void returnMenu() throws InterruptedException {
		waitForLoaderToDisappear();
		wt.waitToClick(returnMenu, 20);
		Thread.sleep(2000);
		wt.waitToClick(returnsubmenu, 10);
		waitForLoaderToDisappear();
	}

	/**
	 * This method used to open closeout menu
	 * 
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public void closeOutMenu() throws TimeoutException, InterruptedException {
		waitForLoaderToDisappear();
		waitForPopupToDisappear();
		wt.waitToClickWithAction(reconsignMenu, 10);
		Thread.sleep(1500);
		wt.waitToClickWithAction(closeOutMenu, 10);
		wt.waitToClick(subMenu, 10);
		waitForLoaderToDisappear();
	}
	
	public String getOrderStatus(String order) throws InterruptedException {
		waitForLoaderToDisappear();
		searchColoumFilter(searchfirstcoloum, order);
		Thread.sleep(100);
		return getMandatoryText(getOrderStatus);
	}
	
	public void searchAndValidateTruckNo(By fetchPalletTextLocator) throws InterruptedException {
		searchAndValidate(fetchPalletTextLocator, enterTruckNoLocator, "Truck Number");
	}
	
	/**
	 * This method used to enter pallet wt into truck
	 * @param wt
	 */
	public void addpalletWt(String wt) {
		clickAndSendKeys(palletWt, wt);
	}
	
	/**
	 * This method used to get pallet wt from edit truck
	 * @return
	 */
	public String getPalletWt() {
		return getAttributeValue(palletWt, "value");
	}
	/**
	 * Scrolls through and validates grid data dynamically based on the grid type.
	 * Works for both frozen and scrollable panes.
	 *
	 * @param classname Either "frozen-pane" or "scrollable-pane" — determines which grid section to operate on.
	 * @throws InterruptedException if thread sleep is interrupted.
	 */
	//frozen-pane
	//scrollable-pane
	public void verifyColumnFilter(String classname) throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    List<WebElement> initialCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@class='" + classname + "']//tbody//tr[2]//td")));

	    int columnCount = initialCells.size();

	    for (int i = 1; i <= columnCount; i++) {
	    	
	        String cellXPath = "//*[@class='" + classname + "']//tbody//tr[2]//td[" + i + "]";
	        WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cellXPath)));

	        String text = cell.getText().trim();
	        Thread.sleep(300);

	        if (text.isEmpty()) continue;

	        System.out.println("Cell " + i + " value: " + text);

	        String filterXPath = "//*[@class='" + classname + "']//thead//tr[2]//th[" + i + "]//input";
	        List<WebElement> filters = driver.findElements(By.xpath(filterXPath));

	        if (filters.isEmpty() || !filters.get(0).isDisplayed()) {
	            System.out.println("No visible filter for column " + i);
	            continue;
	        }

	        WebElement filter = driver.findElement(By.xpath(filterXPath));
	        filter.clear();

	        // Get header title fresh
	        String headerTitle = driver.findElement(
	        	    By.xpath("//*[@class='scrollable-pane']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']")
	        	).getText().trim();

	        // Special case: LDA column
	        if ("LDA".equals(headerTitle)) {
	            text = text.contains("-") ? text.split("-")[0].trim() : text.trim();
	            if (!text.matches("\\d+")) continue; // Skip non-numeric
	        }

	        // Special case: QA Status column
	        if ("QA Status".equals(headerTitle)) {
	            text = text.split("\n")[0].trim();
	        }

	        System.out.println("Filtering with: " + text);

	        filter.sendKeys(text);
	        Thread.sleep(1500);
	        waitForLoaderToDisappear();

	        // Re-locate the cell after filtering (fresh lookup)
	        WebElement filteredCell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cellXPath)));
	        String filteredText = filteredCell.getText().trim();

	        System.out.println("After filter, cell[" + i + "] text: " + filteredText);

	        Assert.assertTrue(filteredText.contains(text),
	                "Column filter not matching — expected to contain: " + text + " but got: " + filteredText);
	    }
	}

	/**
	 * Scrolls through and validates grid data dynamically based on the grid type.
	 * Works for fix grid pattern
	 *
	 * @throws InterruptedException if thread sleep is interrupted.
	 */
	//This method for Remaining for optimization pages
	public void verifyColumnFilterForFixGrid() throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
	    List<WebElement> gridCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@id='panel1bh-content']//tbody//tr[1]//td")
	        ));
	    
	    int columnCount = gridCells.size();

	    for (int i = 1; i <= columnCount; i++) {

	        // Always re-locate fresh to avoid stale
	        String cellXPath = "//*[@id='panel1bh-content']//tbody//tr[1]//td[" + i + "]";
	        WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cellXPath)));

	        Thread.sleep(500);
	        String text = cell.getText().trim();

	        if (text.isEmpty()) continue;

	        System.out.println("Cell " + i + " value: " + text);

	        String filterXPath = "//*[@id='panel1bh-content']//thead//tr[2]//th[" + i + "]//input";
	        List<WebElement> filters = driver.findElements(By.xpath(filterXPath));

	        if (filters.isEmpty() || !filters.get(0).isDisplayed()) {
	            System.out.println("No visible filter for column " + i);
	            continue;
	        }

	        WebElement filter = filters.get(0);
	        filter.clear();
	        
	        String header = driver.findElement(By.xpath(
	                "//*[@id='panel1bh-content']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']")
	        ).getText().trim();
	        
	        if ("LDA".equals(header) || "Original LDA".equals(header) ||"New LDA".equals(header)) {

	            text = text.contains("-") ? text.split("-")[0].trim() : text.trim();
	            // only get numeric values; skip others
	            if (!text.matches("\\d+")) {
	                continue;
	            }
	        }
	        
	      //Check column label is QA Status then Spite text
	        if ("QA Status".equals(driver.findElement(By.xpath(
	    	        "//*[@id='panel1bh-content']//thead//tr[1]//th[" + i + "]//*[@class='p-column-title']"))
	    	        .getText().trim())) {
	    	        text = text.split("\n")[0].trim();
	 
	    	    }    
	        System.out.println("Filter apply "+text);

	        filter.sendKeys(text);
	        Thread.sleep(2000) ;
	        waitForLoaderToDisappear();

	        String filteredText = driver.findElement(By.xpath(cellXPath)).getText().trim();
	        System.out.println("After filter, cell[" + i + "] text: " + filteredText);

	        Assert.assertTrue(filteredText.contains(text),
	                "Column filter not matching — expected to contain: " + text + " but got: " + filteredText);
	    }
	}
	
	/** Return total entries count from paginator  from LDA portal and od grid*/
	public int getTotalEntriesCount(WebDriver driver) {
	    waitForLoaderToDisappear();

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    WebElement paginatorText = wait.until(
	            ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//span[contains(@class,'p-paginator-current')]")));

	    String text = paginatorText.getText(); // Showing 1 to 10 of 13 entries

	    String countStr = text.replaceAll(".*of\\s+(\\d+)\\s+entries.*", "$1");

	   System.out.println("Total count: " + countStr);
	    return Integer.parseInt(countStr);
	}

}
