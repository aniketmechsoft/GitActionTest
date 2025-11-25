package com.uiFramework.Genesis.web.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.helper.WaitHelper;

public class OrderPage {
	WebDriver driver = null;
	CommonPage cp;
	DropDownHelper DrpDwn;
	WaitHelper wt;
	JavaScriptHelper js;
	public static String MLDA;
	public static String MGLOrderno;
	public String Noofpieces;
	public String Actualwt;
	public String MWarehouse;
	public String Litnoofpieces;
	public String Litactualwt;
	public static String MCustomer;
	public String MPicklocation;
	public String MConsignee;
	public String MCusorderNo;
	SoftAssert sAssert = new SoftAssert();
	private WebDriverWait wait;
	private String enteredPieces;
	protected static final Logger logger = Logger.getLogger(OrderPage.class.getName());

	// ======== Cross-browser helpers ========
	private boolean isSafari() {
		try {
			String name = ((HasCapabilities) driver).getCapabilities().getBrowserName();
			return name != null && name.toLowerCase().contains("safari");
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isMac() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}

	private void scrollIntoView(WebElement el) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
		} catch (Exception ignored) {}
	}

	private void jsClick(WebElement el) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
	}

	/** Safari-safe, resilient click helper */
	private void safeClick(By locator) {
		WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
		scrollIntoView(el);
		try {
			if (isSafari()) {
				jsClick(el); // Safari prefers JS click on many UIs
			} else {
				el.click();
			}
		} catch (ElementNotInteractableException e) {
			jsClick(el);
		} catch (Exception e) {
			try { jsClick(el); } catch (Exception ignored) {}
		}
	}

	/** Clear then type (CMD/CTRL+A) with JS fallback for stubborn fields */
	private void clearAndType(By locator, String text) {
		WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		scrollIntoView(el);
		try {
			el.click();
			if (isMac()) {
				el.sendKeys(Keys.chord(Keys.COMMAND, "a"));
			} else {
				el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			}
			el.sendKeys(Keys.BACK_SPACE);
		} catch (Exception ignored) {
			try { el.clear(); } catch (Exception ignore2) {}
		}
		try {
			el.sendKeys(text);
		} catch (Exception e) {
			((JavascriptExecutor) driver)
					.executeScript("arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input'));",
							el, text);
		}
	}

	public OrderPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		this.cp = new CommonPage(driver);
		this.wt = new WaitHelper(driver);
		this.DrpDwn = new DropDownHelper(driver);
		this.js = new JavaScriptHelper(driver);
	}

	/*
	 * Locators for Order Creation
	 */
	private By fetchtextforredirect = By.xpath("//*[@id='panel1bh-header']/div[1]/div/div/div[1]/h6");
	private By confirm = By.id("ConfirmTenderButton");
	private By searchwarehouse = By.xpath("(//*[.='Select Warehouse'])[4]");
	private By list = By
			.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li");
	private By clist = By.xpath("//div[contains(@class,'d-flex align-items-center mt-1 pitem ')]");
	private By searchcustomer = By.xpath("//*[@id='customer']/span");
	private By selectconsignee = By.xpath("//*[@id='consignee']");
	private By backbtn = By.xpath("//button[text()='back']");
	private By ordersource = By.xpath("//div[@id='orderSource']");
	private By pickupadd = By.xpath("//*[@id='pickupLocation']/span");
	private By selectordertype = By.xpath("(//*[@id='orderType']/*)[4]");
	private By distibutor = By.xpath("//input[@id='distributor']");
	private By localcarrier = By.xpath("//div[@id='deliveringLocalCarrier']/span");
	private By boltype = By.xpath("//input[@id='customerBOLType']");
	private By qastatus = By.xpath("//input[@id='qaStatus']");
	private By enterordernum = By.xpath("//input[@placeholder='Enter Customer Order No']");
	private By enterordernumlist = By.id("customer");
	private By entersapordernum = By.xpath("//input[@placeholder='Enter SAP Order Number']");
	private By enterparentordernum = By.xpath("//input[@placeholder='Enter Parent Order']");
	private By enetrwavenum = By.xpath("//input[@placeholder='Enter Wave Number']");
	private By addnote = By.xpath("//textarea[@placeholder='Enter Add Note']");
	private By orderType = By.xpath("//div[text()='Order Type is required.']");
	private By fetchaddress1 = By.xpath("//*[@id='address1']");
	private By address2 = By.xpath("//*[@id='address2']");
	private By address3 = By.xpath("//*[@id='address3']");
	private By fetchstate = By.xpath("//*[@id='state']/span");
	private By fetchcity = By.xpath("//*[@id='city']");
	private By citypresent = By.xpath("//input[@id='city']");
	private By fetchzipcode = By.xpath("//input[@id='zip']");
	private By ratedweight = By.xpath("//input[@placeholder='Rated Weight']");
	private By ratedlitweight = By.xpath("(//input[contains(@placeholder,'Literature Rated Weight')])[1]");
	private By enteractweight = By.xpath("//input[@placeholder='Enter Actual Weight']");
	private By setcontain = By.xpath("//input[@id='setToContain']");
	private By enetrdimweight = By.xpath("//input[@placeholder='Enter Dimensional Weight']");
	private By enetrpieces = By.xpath("//input[@placeholder='Enter Number Of Pieces']");
	private By enterLitweight = By.xpath("//input[@placeholder='Enter Literature Actual Weight']");
	private By enetrLitdimweight = By.xpath("//input[@placeholder='Enter Literature Dimensional Weight']");
	private By enetrLitpieces = By.xpath("//input[@placeholder='Enter Literature Number Of Pieces']");
	private By custaccess = By.xpath("//button[@title='Click to add customer accessorial']");
	private By addcarr = By.xpath("//button[@title='Click to add carrier accessorial']");
	private By custaccesstext = By.xpath("//span[text()='Customer Accessorial is required.']");
	private By carrieraccesstext = By.xpath("//span[text()='Carrier Accessorial is required.']");
	private By pricetext = By.xpath("//span[text()='Price is required.']");
	private By selectcustaccess = By.xpath("//span[normalize-space()='Select Customer Accessorial']");
	private By selectcarrtaccess = By.xpath("//span[normalize-space()='Select Carrier Accessorial']");
	private By selectcarrtaccesstype = By.xpath("//span[normalize-space()='Select Carrier Accessorial Type']");
	private By input = By.xpath("//input[@class='p-dropdown-filter p-inputtext p-component']");
	private By enterprice = By.xpath("//input[@placeholder='Enter Price ($)']");
	private By enterreason = By.id("reason");
	private By saveaccess = By.cssSelector("button[title='Click to save data'][type='button']");
	private By delete = By.xpath("//*[name()='svg' and @data-testid='DeleteIcon']");
	private By editbutton = By.xpath("(//*[name()='svg' and @data-testid='EditIcon'])[2]");
	private By editbuttoncarr = By.xpath("(//button[@aria-label='edit'])[3]");
	private By deletecarr = By
			.xpath("(//*[name()='svg'][@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4'])[12]");
	By searchContain = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By yesbutton = By.xpath(
			"//div[@class='MuiDialog-root LoginForm MuiModal-root css-126xj0f']//button[@type='button'][normalize-space()='Yes']");

	private By selectOrderType = By.xpath("//div[@id='orderType']");
	private By comment = By.xpath("//button[contains(@class, 'MuiButtonBase-root') and @title='Click to add comment']");
	private By typetext = By.xpath("//span[text()='Type is required.']");
	private By entercomment = By.xpath("//textarea[@placeholder='Enter Comment']");
	private By editcomment = By.xpath("(//button[@title='Click to edit'])[2]");
	private By subjecttext = By.xpath("//span[text()='Subject is required.']");
	private By entersubject = By.xpath("//input[@placeholder='Enter Subject']");
	private By closecommnet = By.xpath("//button[@title='Click to close comment']");
	private By deletecomment = By.xpath("//button[@title='Click to delete']");
	private By selecttype = By.xpath("//span[normalize-space()='Select Type']");
	private By addfile = By.xpath("//button[@title='Click to add file']");
	private By downloadfile = By.cssSelector("button[aria-label='view'] svg");
	private By deletefile = By.xpath("//button[@id='orderFilesDeleteBtn']");
	private By documnet = By.xpath("//input[@placeholder='Enter Document Name']");
	private By orderstatus = By.xpath("//input[@name='orderStatus']");
	private By yescomment = By.xpath("(//button[@id='confmSubmit'])[6]");
	private By doctext = By.xpath("//span[text()='Document Name is required.']");
	private By filetext = By.xpath("//span[text()='File is required.']");
	public By yes = By
			.xpath("//div[@class='MuiDialog-root LoginForm MuiModal-root css-126xj0f']//button[@id='confmSubmit']");
	private By no = By
			.xpath("//div[@class='MuiDialog-root LoginForm MuiModal-root css-126xj0f']//button[@id='confmNo']");

	/**** Locators for order Listing ***/
	private By tenderorder = By.id("confirmTenderSelectedTitle");
	private By validationmessageforact = By.xpath(
			"(//div[@class='MuiGrid-root MuiGrid-container css-1d3bbye']//div[@class='MuiGrid-root MuiGrid-grid-xs-12 css-nwhc3l']//p//div)[1]");
	private By validationmessageforlit = By.xpath(
			"(//div[@class='MuiGrid-root MuiGrid-container css-1d3bbye']//div[@class='MuiGrid-root MuiGrid-grid-xs-12 css-nwhc3l']//p//div)[2]");
	private By ok = By.xpath("//button[@title='Click to ok']");
	private By edit = By.xpath("//button[@title='Click to edit']");
	private By deleteorder = By.xpath("//button[@title='Click to delete']");
	private By ordersource1 = By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[3]");
	private By distributor = By.xpath("//span[normalize-space()='Select Distributor']");
	private By firstOptionCheckbox = By.xpath("(//input[@aria-invalid='false'])[2]");
	private By secondOptionCheckbox = By.xpath("(//input[@aria-invalid='false'])[3]");
	/**** Locators for filters ***/
	private By fetchordertext = By.xpath("(//td[contains(normalize-space(.),'GL-')])[1]");
	private By fetchcustordertext = By.xpath("(//*[@id='panel1bh-content']/div//div[2]/table/tbody/tr[1]/td[8])[1]");
	private By fetchwavetext = By.xpath("(//*[@id='panel1bh-content']/div//div[2]/table/tbody/tr[1]/td[14])[1]");
	private By selectconsigneeforlist = By.xpath("//div[contains(text(),'Select Consignee')]");
	private By searchlocalcarrier = By.xpath("//*[@id='deliveringLocalCarrier']/span");
	// getorderdetails xpath
	public By getwarehouse = By.xpath("//div[@id='warehouse']");
	public By getglorder = By.xpath("//input[@placeholder='Genesis Order Number']");
	public By getcustomer = By.xpath("//div[@id='customer']");
	private By getpickuplocation = By.xpath("//*[@id='pickupLocation']/span");
	private By getconsignee = By.xpath("//input[@id='consignee']");
	private By getcustomerorderno = By.xpath("//input[@placeholder='Enter Customer Order No']");
	private By getlda = By.xpath("//div[@id='deliveringLocalCarrier']");
	private By getnoofpieces = By.xpath("//input[@id='noOfPieces']");
	private By getactualwt = By.xpath("//input[@id='actualWeight']");
	private By getlitnoofoieces = By.xpath("//input[@id='literatureNoOfPieces']");
	private By getlitactualwt = By.xpath("//input[@id='literatureActualWeight']");
	// listing page
	private By searchorder = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By searchstatus = By.xpath("(//input[@placeholder='Contains...'])[14]");
	private By clearfilter = By.xpath("//*[@class='p-icon' and @data-pc-section='filterclearicon']");
	private By loader = By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']");
	private By getlicustomer = By.xpath("(//table//tbody//tr[2]/td[3])[2]");
	private By getlicustomerorderno = By.xpath("(//table//tbody//tr[2]/td[4])[2]");
	private By getliwarehouse = By.xpath("(//table//tbody//tr[2]/td[1])[2]");
	private By getactulpieces = By.xpath("//table//tbody//tr/td[6]");
	private By gettotalweight = By.xpath("//table//tbody//tr/td[7]");
	private By orderpage = By.xpath("//a[@href='/order']");

	/**
	 * This method is used to fetch text from order page
	 */
	public String fetchText() { return cp.getMandatoryText(fetchtextforredirect); }

	/** Safari-safe confirm click */
	public void confirm() { safeClick(confirm); }

	public void testFieldErrorMessages(String actionType) {
		cp.waitForLoaderToDisappear();
		if (actionType.equalsIgnoreCase("save")) {
			cp.save();
		} else if (actionType.equalsIgnoreCase("confirm")) {
			confirm();
		}
		List<String> fields = new ArrayList<>(Arrays.asList("warehousetext", "customerText", "consigneeText",
				"customerordText", "localcarrText", "address1Text", "stateText", "cityText", "zipText"));
		if (actionType.equalsIgnoreCase("confirm")) {
			fields.add("orderTypeText");
			fields.add("pickupText");
		}
		for (String field : fields) {
			try {
				String expectedMessage = ReadJsonData.getNestedValue(field, "expected");
				String message = ReadJsonData.getNestedValue(field, "message");
				String fieldXpath = "//*[contains(text(), '" + expectedMessage + "')]";
				List<WebElement> elements = driver.findElements(By.xpath(fieldXpath));
				if (!elements.isEmpty()) {
					String actualMessage = elements.get(0).getText().trim();
					Assert.assertEquals(actualMessage, expectedMessage, message);
				} else {
					logger.warning("No error message displayed for field: " + field + " (Action: " + actionType + ")");
				}
			} catch (RuntimeException e) {
				logger.log(Level.SEVERE, "Error processing field " + field, e);
			}
		}
	}

	public void selectAnyWarehouseOrSkip() {
		cp.waitForLoaderToDisappear();
		safeClick(searchwarehouse);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(list));
		List<WebElement> warehouses = driver.findElements(list);
		if (warehouses.isEmpty()) {
			throw new SkipException("No warehouses available in the dropdown.");
		}
		warehouses.get(0).click();
		cp.waitForLoaderToDisappear();
	}

	public WebElement distributorFiled() {
		logger.info(cp.getMandatoryText(ordersource));
		return driver.findElement(distibutor);
	}

	public void localcarrier(String carrierName) throws InterruptedException {
		logger.info("Selecting local carrier: " + carrierName);
		WebElement SearchLDA = driver.findElement(searchlocalcarrier);
		cp.selectSearchableDropdown(SearchLDA, carrierName);
	}

	public String getPickupLocationText() { return driver.findElement(pickupadd).getText(); }

	public void enterCustOrdNum(String orderNumber) {
		cp.waitForLoaderToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(enterordernum));
		clearAndType(enterordernum, orderNumber);
	}

	public void enterSapOrdNum(String sapordernum) {
		try { clearAndType(entersapordernum, sapordernum); }
		catch (Exception e) { logger.severe("Failed to enter SAP Order Number: " + e.getMessage()); }
	}

	public void enterParentOrdNum(String parentordernum) {
		try { clearAndType(enterparentordernum, parentordernum); }
		catch (Exception e) { logger.severe("Failed to enter Parent Order Number: " + e.getMessage()); }
	}

	public void enterWaveNum(String wavenum) {
		try { clearAndType(enetrwavenum, wavenum); }
		catch (Exception e) { logger.severe("Failed to enter Wave Number: " + e.getMessage()); }
	}

	public void AddNote(String note) {
		try { clearAndType(addnote, note); }
		catch (Exception e) { logger.severe("Failed to add note: " + e.getMessage()); }
	}

	public String getAdrees1(String Address) {
		String address = cp.getMandatoryText(fetchaddress1);
		if (address.isEmpty()) { driver.findElement(fetchaddress1).sendKeys(Address); }
		return cp.getMandatoryText(fetchaddress1);
	}
	public String getAdrees2() { return cp.getMandatoryText(address2); }
	public String getAdrees3() { return cp.getMandatoryText(address3); }

	public String state(String st) {
		String state = cp.getMandatoryText(fetchstate);
		if (state.isEmpty()) {
			WebElement selectdropdown = driver.findElement(fetchstate);
			cp.selectSearchableDropdown(selectdropdown, st);
		}
		return cp.getMandatoryText(fetchstate);
	}

	public String getCity(String city) {
		String City = cp.getAttributeValue(citypresent, "value");
		if (City.isEmpty()) { driver.findElement(fetchaddress1).sendKeys(city); }
		return cp.getAttributeValue(citypresent, "value");
	}

	public String getZipcode(String zip) {
		String zipcode = cp.getAttributeValue(fetchzipcode, "value");
		if (zipcode.isEmpty()) { driver.findElement(fetchaddress1).sendKeys(zip); }
		return cp.getAttributeValue(fetchzipcode, "value");
	}

	public String orderTypeText() { return cp.getMandatoryText(orderType); }

	public void selectFirstOrderType() {
		cp.waitForLoaderToDisappear();
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(selectordertype));
		dropdown.click();
		By options = By.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//li[not(contains(@class,'p-disabled'))]");
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(options));
		List<WebElement> orderTypes = driver.findElements(options);
		if (orderTypes.isEmpty()) throw new SkipException("No order types available.");
		orderTypes.get(0).click();
		cp.waitForLoaderToDisappear();
	}

	public void enterActWeight(String actWeight) {
		logger.info("Entering Actual Weight: " + actWeight);
		wait.until(ExpectedConditions.elementToBeClickable(enteractweight));
		clearAndType(enteractweight, actWeight);
	}

	public void enterDimWeight(String dimWeight) { clearAndType(enetrdimweight, dimWeight); }
	public void enterPieces(String pieces) { clearAndType(enetrpieces, pieces); }
	public void enterLitWeight(String litWeight) { clearAndType(enterLitweight, litWeight); }
	public void enterLitDimWeight(String litDimWeight) { clearAndType(enetrLitdimweight, litDimWeight); }
	public void enterLitPieces(String litPieces) { clearAndType(enetrLitpieces, litPieces); }

	public String getLocalcarrierText() { return cp.getMandatoryText(localcarrier); }

	public void verifySetContainFieldBasedOnBOLType(String settocontain) {
		String boltType = cp.getAttributeValue(boltype, "value");
		WebElement setContainField = driver.findElement(setcontain);
		if ("PDMA Repacks".equals(boltType)) {
			setContainField.sendKeys(settocontain);
			Assert.assertTrue(setContainField.isEnabled(), "SetContain field should be editable for PDMA Repacks");
		} else {
			Assert.assertFalse(setContainField.isEnabled(), "SetContain field should NOT be editable for non-PDMA Repacks");
		}
	}

	public String qaStatus() { return cp.getAttributeValue(qastatus, "value"); }

	public String getPodCheckboxValue() {
		WebElement checkbox = driver.findElement(By.name("podReceived"));
		return checkbox.getAttribute("value");
	}

	public String ratedWeight() { return cp.getAttributeValue(ratedweight, "value"); }
	public String ratedLitweight() { return cp.getAttributeValue(ratedlitweight, "value"); }

	public boolean clickAndVerifyAccessButton() {
		logger.info("Verifying if accessorial button is displayed after save");
		cp.save();
		try {
			WebElement accessoriesButton = wait.until(ExpectedConditions.visibilityOfElementLocated(custaccess));
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", accessoriesButton);
			return accessoriesButton.isDisplayed();
		} catch (TimeoutException e) {
			logger.warning("Accessorial button not found in time: " + e.getMessage());
			return false;
		}
	}

	public void clickAccessorialButton() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		logger.info("Attempting to click the accessorial button");
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(custaccess));
			safeClick(custaccess);
			logger.info("Clicked on Accessorial button successfully.");
		} catch (Exception e) {
			logger.info("Unable to click Accessorial button");
		}
	}

	public String addAccessorial() {
		logger.info("Attempting to add accessorial with empty values to capture validation messages");
		safeClick(saveaccess);
		String accessorialText = driver.findElement(custaccesstext).getText();
		String priceText = driver.findElement(pricetext).getText();
		return accessorialText + " | " + priceText;
	}

	public void editFirstorder() { safeClick(edit); }

	public void saveCustAccess() {
	//	try {
			safeClick(selectcustaccess);
			WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(input));
			searchBox.sendKeys(Keys.ARROW_DOWN);
			searchBox.sendKeys(Keys.ENTER);
			safeClick(saveaccess);
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
//		} finally {
//			cp.clickToClosePopUp();
//		}
	}

	public void editCustAccess(String price2) {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		safeClick(editbutton);
		clearAndType(enterprice, price2);
		safeClick(saveaccess);
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	public void deleteCustAccess() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		cp.waitForLoaderToDisappear();
		safeClick(delete);
		safeClick(yesbutton);
	}

	public void saveCustAccessAfterDelete() {
//		try {
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
			safeClick(custaccess);
			safeClick(selectcustaccess);
			WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(input));
			searchBox.sendKeys(Keys.ARROW_DOWN);
			searchBox.sendKeys(Keys.ENTER);
			safeClick(saveaccess);
//		} finally {
//			cp.clickToClosePopUp();
//		}
	}

	public String addCarrAccessorial() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		safeClick(addcarr);
		try {
			safeClick(saveaccess);
		} catch (ElementNotInteractableException e) {
			WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveaccess));
			jsClick(saveBtn);
		}
		String accessorialText = wait.until(ExpectedConditions.visibilityOfElementLocated(carrieraccesstext)).getText();
		String priceText = wait.until(ExpectedConditions.visibilityOfElementLocated(pricetext)).getText();
		return accessorialText + " | " + priceText;
	}

	public void saveCarrAccess(String price, String text) {
	//	try {
			safeClick(selectcarrtaccess);
			WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(input));
			searchInput.sendKeys(Keys.ARROW_DOWN);
			searchInput.sendKeys(Keys.ENTER);
			safeClick(selectcarrtaccesstype);
			WebElement searchInputType = wait.until(ExpectedConditions.presenceOfElementLocated(input));
			searchInputType.sendKeys(Keys.ARROW_DOWN);
			searchInputType.sendKeys(Keys.ENTER);
			clearAndType(enterprice, price);
			clearAndType(enterreason, text);
			safeClick(saveaccess);
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
//		} finally {
//			cp.clickToClosePopUp();
//		}
	}

	public void editCarrAccess(String price2) {
//		try {
			safeClick(editbuttoncarr);
			clearAndType(enterprice, price2);
			safeClick(saveaccess);
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
//		} finally {
//			cp.clickToClosePopUp();
//		}
	}

	public void deleteCarrAccess() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		safeClick(deletecarr);
		safeClick(yesbutton);
	}

	public void saveCarrAccessAfterDelete(String price, String text) {
	//	try {
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
			safeClick(addcarr);
			safeClick(selectcarrtaccess);
			WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(input));
			searchInput.sendKeys(Keys.ARROW_DOWN);
			searchInput.sendKeys(Keys.ENTER);
			safeClick(selectcarrtaccesstype);
			WebElement searchInputType = wait.until(ExpectedConditions.presenceOfElementLocated(input));
			searchInputType.sendKeys(Keys.ARROW_DOWN);
			searchInputType.sendKeys(Keys.ENTER);
			clearAndType(enterprice, price);
			clearAndType(enterreason, text);
			safeClick(saveaccess);
//		} finally {
//			cp.clickToClosePopUp();
//		}
	}

	public String addComment() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		safeClick(comment);
		safeClick(saveaccess);
		String accessorialText = wait.until(ExpectedConditions.visibilityOfElementLocated(typetext)).getText();
		String priceText = wait.until(ExpectedConditions.visibilityOfElementLocated(subjecttext)).getText();
		return accessorialText + " | " + priceText;
	}

	public void saveComment(String commentType, String subject, String comments) {
	//	try {
			safeClick(selecttype);
			driver.findElement(By.xpath("//*[@class='p-dropdown-item-label']")).click();
			clearAndType(entersubject, subject);
			clearAndType(entercomment, comments);
			safeClick(saveaccess);
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
//		} finally {
//			cp.clickToClosePopUp();
//		}
	}

	public void editComment(String newSubject) {
		safeClick(editcomment);
		clearAndType(entersubject, newSubject);
		safeClick(closecommnet);
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
	}

	public void deleteComment() throws InterruptedException {
		cp.waitForPopupToDisappear();
		cp.waitForLoaderToDisappear();
		Thread.sleep(3000);
		safeClick(deletecomment);
		safeClick(yescomment);
	}

	public String addFile() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		safeClick(addfile);
		safeClick(saveaccess);
		String docText = wait.until(ExpectedConditions.visibilityOfElementLocated(doctext)).getText();
		String fileReq = wait.until(ExpectedConditions.visibilityOfElementLocated(filetext)).getText();
		return docText + " | " + fileReq;
	}

	public String savefile(String file) {
		try {
			WebElement docField = driver.findElement(documnet);
			docField.click();
			docField.sendKeys(file);

			By fileInputLocator = By.xpath("//input[@type='file']");
			WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(fileInputLocator));
			scrollIntoView(fileInput);

			// Cross-platform path (works on Mac/Safari and Windows/Chrome)
			String filePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "attachments", "demo.txt").toString();
			fileInput.sendKeys(filePath);

			safeClick(saveaccess);

			WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@role='alert']/div[contains(text(),'File save successfully')]")));
			return successMessage.getText().contains("File save successfully")
					? "File saved successfully"
					: "File not saved";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during file upload", e);
			return "Error during file upload: " + e.getMessage();
//		} finally {
//			cp.clickToClosePopUp();
		}
	}

	public void deleteFile() throws InterruptedException {
		try {
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
			safeClick(deletefile);
			safeClick(yesbutton);
		} finally {
			cp.clickToClosePopUp();
		}
	}

	public String verifyOrderStatus() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		return driver.findElement(orderstatus).getAttribute("value");
	}

	public String verifyBarcodeAfterConfirmation(String value) throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		clearAndType(enteractweight, value);
		WebElement piecesField = driver.findElement(enetrpieces);
		piecesField.click();
		piecesField.clear();
		piecesField.sendKeys(value);
		String enteredPieces = piecesField.getAttribute("value");

		safeClick(confirm);
		try {
			WebElement secondPopup = new WebDriverWait(driver, Duration.ofSeconds(3)).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("(//h2[@class='confirm__title pt-1 pb-3'])[2]")));
			safeClick(By.xpath("(//*[@id='confmSubmit'])[2]"));
			safeClick(yes);
		} catch (TimeoutException e) {
			safeClick(yes);
		} finally {
			cp.clickToClosePopUp();
		}

		WebElement barcodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='10']")));
		String barcodeValue = barcodeElement.getText().trim();
		sAssert.assertEquals(barcodeValue, enteredPieces, "Barcode does not match entered pieces!");
		sAssert.assertAll();
		return barcodeValue;
	}

	public String getMGLOrderno() { return MGLOrderno; }

	public void getOrderData() throws InterruptedException {
		try {
			cp.waitForLoaderToDisappear();
			MGLOrderno = driver.findElement(getglorder).getAttribute("value");
			MWarehouse = cp.getAttributeValue(getwarehouse, "valuelabel");
			MCustomer = cp.getAttributeValue(getcustomer, "valuelabel");
			MConsignee = cp.getAttributeValue(getconsignee, "value");
			MCusorderNo = cp.getAttributeValue(getcustomerorderno, "value");
			MLDA = cp.getAttributeValue(getlda, "valuelabel");
			Noofpieces = cp.getAttributeValue(getnoofpieces, "value");
			Actualwt = cp.getAttributeValue(getactualwt, "value");
			Litnoofpieces = cp.getAttributeValue(getlitnoofoieces, "value");
			Litactualwt = cp.getAttributeValue(getlitactualwt, "value");
		} catch (Exception e) {
			System.out.println("Error while fetching order details.");
		}
		WebElement back = driver.findElement(backbtn);
		js.scrollIntoView(back);
		Thread.sleep(1000);
		wt.waitToClick(backbtn, 10);
	}

	public void landingOrderListing() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		try {
			cp.clickBackBtn();
			cp.waitForLoaderToDisappear();
			List<WebElement> searchSections = driver.findElements(cp.searchsection);
			if (searchSections.isEmpty() || !searchSections.get(0).isDisplayed()) {
				cp.waitForLoaderToDisappear();
				wt.waitToClick(orderpage, 4);
				cp.waitForLoaderToDisappear();
			}
		} catch (Exception e) {
			logger.info("Error occurred while landing: " + e.getMessage());
		}
	}

	public void serachorder() {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchorder, MGLOrderno);
	}

	public String getCustomer() { return cp.getMandatoryText(getlicustomer); }
	public String getWarehouse() { return cp.getMandatoryText(getliwarehouse); }
	public String getCustomerOrder() { return cp.getMandatoryText(getlicustomerorderno); }
	public String getActulwt() { return cp.getMandatoryText(gettotalweight); }

	public String getActualpieces() {
		String value = cp.getMandatoryText(getactulpieces);
		return value.split("/")[1].trim();
	}

	public void checkStatusCountOneByone() {
		cp.waitForLoaderToDisappear();
		try {
			driver.findElement(clearfilter).click();
			wt.waitForElement(loader, 4);
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("Not present filter data");
		}
		List<WebElement> countList = driver
				.findElements(By.xpath("//span[@class='MuiChip-label MuiChip-labelSmall css-1pjtbja']"));
		List<String> allStatuses = new ArrayList<>();
		for (WebElement el : countList) { allStatuses.add(el.getText().trim()); }

		for (String data : allStatuses) {
			if (data.contains(":")) {
				String[] parts = data.split(":");
				String labelStatus = parts[0].trim();
				String countValue = parts[1].trim();
				serchStatus(labelStatus);

				String pageCounttrim = driver.findElement(By.xpath("(//*[@class='v-sected spanTag'])[1]")).getText();
				String pageCount = pageCounttrim.replaceAll(".*Total Records\\s*:\\s*(\\d+).*", "$1");

				Assert.assertEquals(pageCount, countValue, "status for" + labelStatus + "not match");
				driver.findElement(clearfilter).click();
				try { wt.waitForElement(loader, 10); } catch (Exception e) {}
				cp.waitForLoaderToDisappear();
			}
		}
	}

	public void serchStatus(String status) {
		cp.waitForLoaderToDisappear();
		driver.findElement(searchstatus).clear();
		driver.findElement(searchstatus).sendKeys(status);
		try {
			wt.waitForElement(loader, 4);
			cp.waitForLoaderToDisappear();
		} catch (Exception e) {
			logger.info("element not found on listing for order no. loader");
		}
	}

	public String verifyNoBarcodeAfterConfirmation(String value) throws InterruptedException {
		enterActWeight(value);
		WebElement piecesField = driver.findElement(enetrpieces);
		piecesField.click();
		piecesField.clear();
		piecesField.sendKeys(value);
		safeClick(confirm);
		try {
			WebElement secondPopup = new WebDriverWait(driver, Duration.ofSeconds(3)).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("(//h2[@class='confirm__title pt-1 pb-3'])[2]")));
			cp.waitAndClickWithJS(By.xpath("(//*[@id='confmSubmit'])[2]"), 10);
			cp.waitAndClickWithJS(no, 5);
		} catch (TimeoutException e) {
			cp.waitAndClickWithJS(no, 5);
			logger.info("Not click on barcode no Pop up");
		} finally {
			cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[2]");
		}
		WebElement barcodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//div//h6[text()='Record not found.'][1]")));
		String norecord = barcodeElement.getText().trim();
		Assert.assertNotEquals(norecord, enteredPieces, "Barcode is generated on No Popup..!");
		return norecord;
	}

	public void orderConfirmfromListing(String ordernum) {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		safeClick(backbtn);
		cp.waitForLoaderToDisappear();
		safeClick(firstOptionCheckbox);
		safeClick(tenderorder);
		safeClick(yes);
		try { safeClick(yes); } catch (Exception e) { logger.info("Set to contain pop not displyed."); }
		cp.waitAndClickWithJS(ok, 10);
	}

	public void clickOk() { try { cp.waitAndClickWithJS(ok, 10); } catch (Exception ignored) {} }

	public boolean isErrorMessagePresent(String expectedMessage) {
		List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'" + expectedMessage + "')]"));
		return !elements.isEmpty() && elements.get(0).isDisplayed();
	}

	public void deleteOrder() {
		try {
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
			safeClick(deleteorder);
			safeClick(yes);
		} finally {
			cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[2]");
		}
	}

	public void onConfirmFieldDisable() {
		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
		try {
			safeClick(edit);
			cp.waitForLoaderToDisappear();
			WebElement warehouseField = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-p-disabled='true']")));
			WebElement confirmButton = driver.findElement(confirm);
			boolean isDisabled = confirmButton.getAttribute("disabled") != null;
			if (!warehouseField.isDisplayed()) {
				logger.warning("Warehouse field is NOT disabled!");
			}
			sAssert.assertTrue(isDisabled, "Confirm / Tender button should be disabled.");
			sAssert.assertTrue(warehouseField.isDisplayed(), "Warehouse field should be disabled but is not.");
			sAssert.assertAll();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during warehouse field verification", e);
		}
	}

	public void cityDropdown() throws Throwable {
		String city = getRandomCity();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement cityField = wait.until(ExpectedConditions.elementToBeClickable(citypresent));
			cityField.click();
			cityField.clear();
			cityField.clear();
			cityField.sendKeys(city);
			cp.save();
			cp.waitForLoaderToDisappear();
			cp.waitForPopupToDisappear();
			wt.waitForElement(citypresent, 10);
			js.scrollIntoView(cityField);
			String enteredCity = cityField.getAttribute("value").trim();
			if (!enteredCity.endsWith(city)) {
				throw new AssertionError("City value mismatch after saving and reopening.");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during city dropdown interaction", e);
		}
	}

	public static String getRandomCity() {
		String[] cities = { "New York", "Los Angeles", "Chicago", "Houston", "Phoenix" };
		return cities[new Random().nextInt(cities.length)];
	}

	public void enetrpieces(String value) throws Throwable {
		clearAndType(enteractweight, value);
		WebElement piecesField = driver.findElement(enetrpieces);
		piecesField.click();
		piecesField.clear();
		piecesField.sendKeys(value);
		enteredPieces = piecesField.getAttribute("value");
		cp.save();
	}

	public void verifyBarcodeAfterConfirmationOnListing() throws InterruptedException {
		logger.info("Verifying barcode after confirming order for order number: ");
		cp.waitForLoaderToDisappear();
		safeClick(firstOptionCheckbox);
		safeClick(tenderorder);
		safeClick(yes);
		try { safeClick(yes); } finally { cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[2]"); }
		cp.waitForLoaderToDisappear();
		safeClick(edit);
		cp.waitForLoaderToDisappear();
	}

	public void untickBarcodeGenertion() {
		driver.findElement(By.xpath("(//input[@class='PrivateSwitchBase-input css-1m9pwf3'])[1]")).click();
		logger.info("barcode untick for order no." + cp.getMandatoryText(By.xpath("//table/tbody/tr/td[4]")));
	}

	public String getGLOrderNo() { return cp.getAttributeValue(getglorder, "value"); }

	public boolean isPrintButtonDisplayed() {
		List<WebElement> printButtons = driver.findElements(By.xpath("//button[@title='Click to print']"));
		return !printButtons.isEmpty() && printButtons.get(0).isDisplayed();
	}

	public void verifyBarcodeIfDisplayed() {
		if (isPrintButtonDisplayed()) {
			WebElement barcodeElement = wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table/tbody/tr[last()]/td[2])[1]")));
			String barcodeValue = barcodeElement.getText().trim();
			Assert.assertEquals(barcodeValue, enteredPieces, "Barcode count does not match entered pieces!");
		} else {
			logger.info("Print button not displayed. Skipping barcode verification.");
		}
	}

	public void confirmTwoOrders() throws Throwable {
		try {
			safeClick(firstOptionCheckbox);
			safeClick(secondOptionCheckbox);
			safeClick(tenderorder);
			safeClick(yes);
			safeClick(yes);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during order confirmation", e);
		}
	}

	public void searchAndValidateWaveNum() throws InterruptedException {
		cp.clickClearButton();
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			WebElement wave = wait2.until(ExpectedConditions.visibilityOfElementLocated(fetchwavetext));
			String orderText = wave.getText();
			WebElement waveField = wait.until(ExpectedConditions.visibilityOfElementLocated(enetrwavenum));
			waveField.clear();
			waveField.sendKeys(orderText);
			cp.Search();
			cp.waitForLoaderToDisappear();
			WebElement listedOrderElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fetchwavetext));
			String listedWaveText = listedOrderElement.getText();
			if (!listedWaveText.equals(orderText)) {
				logger.warning("Verification Failed: Expected " + orderText + " but found " + listedWaveText);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during wave number validation", e);
		}
		cp.waitForPopupToDisappear();
		cp.clickClearButton();
	}

	public boolean isConsigneeDropdownEmpty1() {
		try {
			safeClick(selectconsignee);
			List<WebElement> options = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//ul[contains(@class,'p-dropdown-items')]/li")));
			boolean isEmpty = options.size() == 1
					&& options.get(0).getText().trim().equalsIgnoreCase("No available options");
			return isEmpty;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while checking if consignee dropdown is empty", e);
			return false;
		}
	}

	public boolean isConsigneeDropdownEmpty() {
		try {
			safeClick(selectconsignee);
			List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.xpath("//ul[contains(@class,'p-dropdown-items') or contains(@id,'pr_id_739_list')]/li")));
			return options.size() <= 1;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while checking if consignee dropdown is empty", e);
			return false;
		}
	}

	public void searchAndValidateOrderNumber() throws InterruptedException {
		cp.clickClearButton();
		cp.searchAndValidateOrderNumber(fetchordertext);
		cp.waitForPopupToDisappear();
	}

	public void searchAndValidateCustomerOrderNumber() throws InterruptedException {
		cp.searchAndValidateCustomerOrderNumber(fetchcustordertext);
		cp.waitForPopupToDisappear();
	}

	public void orderSource(String dist) {
		try {
			cp.waitForLoaderToDisappear();
			safeClick(ordersource);
			driver.findElement(input).sendKeys(Keys.ARROW_DOWN);
			driver.findElement(input).sendKeys(Keys.ENTER);
			logger.info("Order source selected: " + dist);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during selecting order source", e);
		}
	}

	public void selectAnyDistributorOrSkip() {
		cp.waitForLoaderToDisappear();
		safeClick(distributor);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(list));
		List<WebElement> distributors = driver.findElements(list);
		if (distributors.isEmpty()) throw new SkipException("No distributors available in the dropdown.");
		distributors.get(0).click();
		cp.waitForLoaderToDisappear();
	}

	public void selectAnyValidCustomerOrFail() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		for (int i = 0; i < 25; i++) {
			WebElement selectcustomer = wait.until(ExpectedConditions.elementToBeClickable(searchcustomer));
			selectcustomer.click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(list));
			wt.waitForElementClickable(list, 10);
			List<WebElement> customerslist = driver.findElements(list);
			if (customerslist.isEmpty()) { logger.info("No customer found"); }
			WebElement customer = customerslist.get(i);
			customer.click();
			cp.waitForLoaderToDisappear();
			try {
				safeClick(selectconsignee);
				List<WebElement> options = driver.findElements(By.xpath("//*[text()='No available options']"));
				if (!options.isEmpty() && options.get(0).isDisplayed()) { continue; }
			} catch (Exception ignored) {}
			driver.findElement(By.xpath("//input[@placeholder='Enter Customer Order No']")).click();
			MPicklocation = driver.findElement(pickupadd).getText().trim();
			if (MPicklocation.isEmpty()) { continue; }
			try {
				safeClick(selectOrderType);
				WebElement optionsOrderT = driver.findElement(By.xpath("//*[text() ='No available options']"));
				if (optionsOrderT.isDisplayed()) { continue; }
			} catch (Exception ignored) {}
			return;
		}
		cp.waitForLoaderToDisappear();
		Assert.fail("No valid customer found.");
	}

	public boolean isConsigneeDropdownPopulated() {
		try {
			safeClick(selectconsignee);
			List<WebElement> options = wait.until(ExpectedConditions
					.presenceOfAllElementsLocatedBy(By.xpath("//ul[contains(@class,'p-dropdown-items')]/li")));
			return options.size() > 0;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while checking if consignee dropdown is populated", e);
			return false;
		}
	}

	public void selectAnyConsigneeOrSkip() {
		cp.waitForLoaderToDisappear();
		safeClick(selectconsignee);
		wait.until(driver -> driver.findElements(clist).size() > 1);
		driver.findElement(By.xpath("//input[@placeholder='Enter Customer Order No']")).click();
		safeClick(selectconsignee);
		List<WebElement> consignee = driver.findElements(clist);
		if (consignee.size() == 1) {
			throw new SkipException("No Consignee available in the dropdown.");
		}
		try {
			driver.findElements(clist).get(0).click();
		} catch (StaleElementReferenceException e) {
			safeClick(selectconsignee);
			driver.findElements(clist).get(0).click();
		}
		cp.waitForLoaderToDisappear();
	}

	public void orderCreation(String ordernumber) throws InterruptedException {
		cp.createOrder();
		cp.waitForLoaderToDisappear();
		selectAnyWarehouseOrSkip();
		selectAnyValidCustomerOrFail();
		selectAnyConsigneeOrSkip();
		enterCustOrdNum(ordernumber);
		Thread.sleep(1000);
		selectFirstOrderType();
	}

	public void orderCreationforDuplicateCustomer(String ordernumber) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.createOrder();
		cp.waitForLoaderToDisappear();
		selectAnyWarehouseOrSkip();
		driver.findElement(searchcustomer).click();
		WebElement search = driver.findElement(input);
		cp.selectSearchableDropdown(search, MCustomer);
		selectAnyConsigneeOrSkip();
		enterCustOrdNum(ordernumber);
		selectFirstOrderType();
	}

	public void checkColoumFilterForOrderListing() throws InterruptedException, IOException {
		cp.waitForLoaderToDisappear();
		String commonPath = Paths.get(System.getProperty("user.dir"), "src","main","resources","configfile","Locators.JSON").toString();
		String jsonContent = new String(Files.readAllBytes(Paths.get(commonPath)));
		JSONObject root = new JSONObject(jsonContent);
		String pageName = "orderPage";

		if (root.has("pages") && root.getJSONObject("pages").has(pageName)) {
			JSONObject pageData = root.getJSONObject("pages").getJSONObject(pageName);
			JSONArray filters = pageData.getJSONArray("columns");

			for (int i = 0; i < filters.length(); i++) {
				JSONObject filter = filters.getJSONObject(i);

				String firstRowXPath = filter.getString("firstRowData");
				String filterIconXPath = filter.getString("filterIcon");
				String filterInputXPath = filter.getString("filterInput");
				String tableRowsXPath = filter.getString("tableData");
				int columnIndex = filter.getInt("columnIndex");
				String method = filter.getString("validationType");

				if (method.equals("column")) {
					cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				} else if (method.equals("lda")) {
					cp.validateLDAColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				} else if (method.equals("date")) {
					cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
				}
			}
		} else {
			System.out.println("Page not found: " + pageName);
		}
	}

	public void paginationTest() { cp.paginationTest(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'GL-')]"), 2); }
	
	public void saveCommentAfterDelete(String commentType, String subject, String comments) {
	    try {
	        cp.waitForLoaderToDisappear();
	        cp.waitForPopupToDisappear();

	        // Open the comment dialog
	        safeClick(comment);

	        // Select Type (choose the passed type if available, else first option)
	        safeClick(selecttype);
	        By desiredType = By.xpath("//*[contains(@class,'p-dropdown-item-label') and normalize-space()='" + commentType + "']");
	        try {
	            safeClick(desiredType);
	        } catch (Exception e) {
	            // Fallback: first visible option
	            safeClick(By.xpath("(//*[contains(@class,'p-dropdown-item-label')])[1]"));
	        }

	        // Fill Subject & Comment
	        clearAndType(entersubject, subject);
	        clearAndType(entercomment, comments);

	        // Save
	        safeClick(saveaccess);
	        cp.waitForLoaderToDisappear();
	        cp.waitForPopupToDisappear();

	        logger.info("Comment re-saved successfully");
	    } finally {
	        cp.clickToClosePopUp();
	    }
	}

}
