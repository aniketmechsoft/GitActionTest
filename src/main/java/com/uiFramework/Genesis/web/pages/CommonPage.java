package com.uiFramework.Genesis.web.pages;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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
	private By warehouse = By.xpath("(//*[.='Select Warehouse'])[3]");
	private By customer = By.xpath("(//*[.='Select Customer'])[3]");
	private By distributor = By.xpath("(//*[.='Select Distributor'])[3]");
	private By consignee = By.xpath("(//*[.='Select Consignee'])[3]");
	private By selectLDA = By.xpath("(//*[.='Select LDA'])[3]");
	private By selectLTL = By.xpath("(//*[.='Select LTL'])[3]");
	private By selectUser = By.xpath("(//*[.='Select Users'])[3]");
	private By selectCreationMode = By.xpath("(//*[.='Select Creation Mode'])[3]");
	private By selectstatus = By.xpath("(//*[.='Select Status'])[3]");
	private By fromdate = By.xpath("//input[@placeholder='Select From Date']");
	private By todate = By.xpath("//input[@placeholder='Select To Date']");
	private By selectpickup = By.xpath("(//*[.='Select Pickup Location'])[3]");
	private By selectdrop = By.xpath("(//*[.='Select Drop Location'])[3]");
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
	private By closePopup = By.xpath("(//*[@data-testid='HighlightOffIcon'])[8]");
	private By palletNo = By.xpath("//*[@placeholder='Enter Order No']");
	private By truckNo = By.xpath("//*[@placeholder='Enter Truck No']");
	private By truckName = By.xpath("//*[@placeholder='Enter Truck Name']");
	private By orderNo = By.xpath("//*[@placeholder='Enter Order No']");
	private By custOrderNo = By.xpath("//*[@placeholder='Enter Customer Order No']");
	private By paginationDrpDwn = By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/div/div[3]");
	private By paginationCount = By.xpath("//*[@id='genesis']/div[3]/div/ul/li[4]");
	private By CreatOrder = By.xpath("//*[@title='Click to create new order']");
	private static final String NUMERIC_SANITIZER_REGEX = "[^0-9.-]";
	private static final int DECIMAL_PRECISION = 2;
	
	
	
	private String downloadDir = System.getProperty("user.dir") + "\\downloadedFiles";
	private By PDFBtn = By.xpath("//*[@title='Export to PDF']");
	private By excelBtn = By.xpath("//*[@title='Export to Excel']");

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

	public void max_pagination() {
		wt.waitToClick(paginationDrpDwn, DEFAULT_TIMEOUT);
		wt.waitToClick(paginationCount, DEFAULT_TIMEOUT);
	}

	public void enterTruckName(String trkName) {
		this.clickAndSendKeys(truckName, trkName);
	}

	public void enterPalletNo(String paltNo) {
		this.clickAndSendKeys(palletNo, paltNo);
	}

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

	public void searchClick() {
		waitForLoaderToDisappear();
		wt.waitToClick(searchsection, DEFAULT_TIMEOUT);
	}

	public void clickBackBtn() {
		waitForLoaderToDisappear();
		waitForPopupToDisappear();
		wt.waitToClick(backbtn, DEFAULT_TIMEOUT);
	}

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

	public String getMandatoryText(By locator) {
		WebElement el = driver.findElement(locator);
		String text = el.getText();

		// Fallback for Safari, especially for input fields
		if (text == null || text.trim().isEmpty()) {
			text = el.getAttribute("value");
		}

		return text == null ? "" : text.trim();
	}

	public String getAttributeValue(By element, String attributeName) {
		wt.waitForElement(element, DEFAULT_TIMEOUT);
		return driver.findElement(element).getAttribute(attributeName);
	}

	public void selectSearchableDropdown(WebElement dropdownTrigger, String valueToSelect) {
		dropdownTrigger.click();
		DrpDwn.DrpDwnValueSel(dropdownTrigger, valueToSelect);
	}

//    public void DrpDwnValueSel(By input, String DrpDwnValue) {
//        driver.findElement(input).click();
//        driver.findElement(input).clear();
//        driver.findElement(input).sendKeys(DrpDwnValue);
//        driver.findElement(input).sendKeys(Keys.ARROW_DOWN);
//        driver.findElement(input).sendKeys(Keys.ENTER);
//    }

	public void DrpDwnValueSel(By input, String DrpDwnValue) {
		WebElement dropdown = driver.findElement(input);

		// Scroll into view (Safari needs this)
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);

		dropdown.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		List<WebElement> options = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//ul//li[contains(text(),'" + DrpDwnValue + "')]")));

		boolean optionSelected = false;
		for (WebElement option : options) {
			if (option.getText().equalsIgnoreCase(DrpDwnValue)) {
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

	public void clickAndSendKeys(By locator, String text) {
		driver.findElement(locator).click();
		driver.findElement(locator).clear();
		driver.findElement(locator).sendKeys(text);
	}

	public void clickAndSendKeys1(By locator, String text) {
		driver.findElement(locator).click();
		driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
		driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
		driver.findElement(locator).sendKeys(text);
	}

	public String captureToastMessage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

	public boolean toastMsgReceivedSuccessfully() {
		boolean flag = false;
		try {
			wt.waitForElement(toastlement, DEFAULT_TIMEOUT);
			flag = true;
		} catch (Exception e) {
		}
		return flag;
	}

	public void moveToElementAndClick1(By locator) throws InterruptedException {
		Actions actions = new Actions(driver);
		WebElement element = driver.findElement(locator);
		actions.moveToElement(element).perform();
		logger.info("Element clicked after scroll: " + locator.toString());
	}

	public void moveToElementAndClick(By locator) {
		// more robust: try Actions then fall back
		try {
			actionsClick(locator);
		} catch (Exception e) {
			logger.info("Fallback click used for: " + locator);
		}
	}

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

	public void selectMultipleConsigneesByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		driver.findElement(consignee).click();
		for (Integer index : indices) {
			try {
				String consigneeXPath = "(//li[contains(@class, 'p-multiselect-item')])[" + index + "]";
				WebElement consigneeElement = driver.findElement(By.xpath(consigneeXPath));
				consigneeElement.click();
			} catch (Exception e) {
				logger.severe("Could not select consignee at index: " + index + ", Error: " + e.getMessage());
			}
		}
	}

	public void Search() {
		wt.waitToClick(searchbtn, DEFAULT_TIMEOUT);
		this.waitForLoaderToDisappear();
	}

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

	public void selectMultipleWarehouseByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(warehouse, indices, "Warehouse");
	}

	public void selectMultipleLDAByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectLDA, indices, "LDA");
	}

	public void selectMultipleDistributorByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(distributor, indices, "Distributor");
	}

	public void selectMultipleCustomerByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(customer, indices, "Customer");
	}

	public void selectMultiplePickuprByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectpickup, indices, "Pickup");
	}

	public void selectMultipleLTLByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectLTL, indices, "LTL");
	}

	public void selectMultipleUsersByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectUser, indices, "User");
	}

	public void selectMultipleCreationModeByIndex(List<Integer> indices) throws InterruptedException {
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectCreationMode, indices, "Creation Mode");
	}

	public void selectMultiplestatusByIndex(List<Integer> indices) throws InterruptedException {
		clickClearButton();
		waitForLoaderToDisappear();
		selectMultipleByIndex(selectstatus, indices, "Status");
	}

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
		for (int i = 1; i <= Math.min(10, rows.size()); i++) {
			try {
				String cellXPath = String.format("//*[@id='panel1bh-content']//table/tbody/tr[%d]/td[%d]", i, index);
				List<WebElement> cellElements = driver.findElements(By.xpath(cellXPath));

				if (!cellElements.isEmpty()) {
					String cellText = cellElements.get(0).getText().trim();
					if (expectedValues.contains(cellText)) {
						// match
					} else {
						// mismatch logging if needed
					}
				}
			} catch (Exception e) {
				logger.severe("Error retrieving text for Row " + i + ": " + e.getMessage());
			}
		}
	}

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

	public void searchAndValidateDateByColumn(int columnIndex) throws InterruptedException {
		clickClearButton();
		searchAndValidate(By.xpath(String.format("//*[@id='panel1bh-content']//table/tbody/tr[1]/td[%d]", columnIndex)),
				fromdate, "From Date");
	}

	public void searchAndValidateToDateByColumn(int columnIndex) throws InterruptedException {
		searchAndValidate(By.xpath(String.format("//*[@id='panel1bh-content']//table/tbody/tr[1]/td[%d]", columnIndex)),
				todate, "To Date");
	}

	public void searchAndValidateOrderNumber(By fetchOrderTextLocator) throws InterruptedException {
		searchAndValidate(fetchOrderTextLocator, ordernum, "Order Number");
	}

	public void searchAndValidateCustomerOrderNumber(By fetchCustomerOrderTextLocator) throws InterruptedException {
		searchAndValidate(fetchCustomerOrderTextLocator, entercustordernum, "Customer Order Number");
	}

	public void searchAndValidatePalletNo(By fetchPalletTextLocator) throws InterruptedException {
		searchAndValidate(fetchPalletTextLocator, enterPalletNoLocator, "Pallet Number");
	}

	public void searchAndValidateTruckNo(By fetchPalletTextLocator) throws InterruptedException {
		searchAndValidate(fetchPalletTextLocator, enterTruckNoLocator, "Truck Number");
	}

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

	public void validateColumnFilters(String columnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex) throws InterruptedException {
		validateColumnFiltersGeneric(columnXPath, filterIconXPath, filterInputXPath, tableDataXPath, columnIndex,
				false);
	}

	public void validateLDAColumnFilters(String columnXPath, String filterIconXPath, String filterInputXPath,
			String tableDataXPath, int columnIndex) throws InterruptedException {
		validateColumnFiltersGeneric(columnXPath, filterIconXPath, filterInputXPath, tableDataXPath, columnIndex, true);
	}

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

	private String extractDateOnly(String dateTimeStr) {
		return (dateTimeStr == null || dateTimeStr.isEmpty()) ? "" : dateTimeStr.split(" ")[0];
	}

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

	public void waitForPopupToDisappear() {
		By popupLocator = By.xpath("//div[@role='alert']//div[2]");
		List<WebElement> popups = driver.findElements(popupLocator);
		if (!popups.isEmpty() && popups.get(0).isDisplayed()) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(popupLocator));
			logger.info("Toast popup appeared and disappeared.");
		} else {
			logger.info("No popup appeared. Continuing...");
		}
	}

	public void createOrder() throws InterruptedException {
		waitForPopupToDisappear();
		waitForLoaderToDisappear();
		wt.waitToClick(CreatOrder, DEFAULT_TIMEOUT);
	}

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

	private boolean isNoRecordsFound() {
		return driver.findElements(By.xpath("//td[contains(text(),'Record not found.')]")).size() > 0;
	}

	public void save() {
		wt.waitToClick(save, DEFAULT_TIMEOUT);
	}

	public void closeIcon() {
		try {
			wt.waitToClick(closepopupIcon, DEFAULT_TIMEOUT);
		} catch (Exception e) {
			logger.info("No filter dropdown to close or already closed.");
		}
	}

	public List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {
		String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonContent,
				new com.fasterxml.jackson.core.type.TypeReference<List<HashMap<String, String>>>() {
				});
	}

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

	private void clickIcon(String iconXPath) {
		WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(iconXPath)));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", icon);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
	}

	private String extractTextFromIcons(String iconXPath1, String iconXPath2, String popupTextXPath) {
		clickIcon(iconXPath1);
		clickIcon(iconXPath2);
		WebElement popupTextElement = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(popupTextXPath)));
		return popupTextElement.getText().trim();
	}

	private void closePopupIcons() {
		WebElement closeIcon2 = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath2));
		closeIcon2.click();
		WebElement closeIcon = wait.until(ExpectedConditions.elementToBeClickable(closeIconXPath));
		closeIcon.click();
	}

	private void enterTextInField(String inputFieldXPath, String text) {
		WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(inputFieldXPath)));
		inputField.clear();
		inputField.sendKeys(text);
	}

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
	 */
	public void searchColoumFilter(By locator, String input) {
		WebElement element = driver.findElement(locator);
		element.sendKeys(input == null ? "" : input);
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

	public void searchSection() {
		waitForLoaderToDisappear();
		safeClick(searchsection);
	}

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

	public void clickToClosePopUp() {
		try {
			wt.waitToClick(closePopup, 3);
		} catch (Exception e) {
			logger.info("HighlightOffIcon not found or not clickable: " + e.getMessage());
		}
	}

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
	
	public void clickToExpPDF() {
		waitForPopupToDisappear();
		wt.waitToClick(PDFBtn, 10);
	}
	
	public void clickToExpExcel() {
		waitForPopupToDisappear();
		wt.waitToClick(excelBtn, 10);
	}

}
