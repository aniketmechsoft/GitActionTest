package com.uiFramework.Genesis.web.pages;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class AccountsReceivable {
	WebDriver driver = null;
	CommonMethods cm;
	WaitHelper wt;
	CommonPage cp;

	private WebDriverWait wait;
	private JavascriptExecutor js;

	public static List<String> selectedOrderNumbers = new ArrayList<>();
	public static List<Double> orderPrices = new ArrayList<>();
	public static double TotalPriceFirstOrder;
	public static double TotalPriceSecondOrder;
	public static double TotalPriceThirdOrder;
	public static double SubTotal;
	public static double TotalDiscount;
	public static double InvoiceAmount;
	public double TotalAddandDeduction;
	public String updatedOrder;
	public String customer;
	public static String InvoiceNumber;
	String fuelCharges;

	protected static final Logger logger = Logger.getLogger(AccountsReceivable.class.getName());

	List<Map<String, String>> rowDataList = new ArrayList<>();

	public AccountsReceivable(WebDriver driver) {
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.cp = new CommonPage(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		this.js = (JavascriptExecutor) driver;
	}

	/* =================== small helpers =================== */
	private void safeClick(By locator) {
		for (int i = 0; i < 2; i++) {
			try {
				wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
				return;
			} catch (StaleElementReferenceException | ElementNotInteractableException e) {
				try {
					WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
					js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
					js.executeScript("arguments[0].click();", el);
					return;
				} catch (Exception ignore) {}
			}
		}
		// last attempt (will throw if still failing)
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	private void clearAndType(By locator, String text) {
		WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
		el.click();
		el.sendKeys(selectAllChord());
		el.sendKeys(Keys.BACK_SPACE);
		el.sendKeys(text);
	}

	private CharSequence selectAllChord() {
		return System.getProperty("os.name","").toLowerCase().contains("mac")
				? Keys.chord(Keys.COMMAND, "a")
				: Keys.chord(Keys.CONTROL, "a");
	}

	private double parseMoney(String raw) {
		if (raw == null) return 0.0;
		String cleaned = raw.replaceAll("[^\\d.\\-]", "");
		if (cleaned.isEmpty() || ".".equals(cleaned) || "-".equals(cleaned)) return 0.0;
		return Double.parseDouble(cleaned);
	}

	private double getDoubleFromAttr(By locator, String attr) {
		String v = cp.getAttributeValue(locator, attr);
		return parseMoney(v);
	}

	private double getDoubleFromText(By locator) {
		String v = cp.getMandatoryText(locator);
		return parseMoney(v);
	}

	private void ensureDownloadDirExists(File dir) {
		if (!dir.exists()) {
			boolean ok = dir.mkdirs();
			if (!ok) logger.warning("Could not create download directory: " + dir.getAbsolutePath());
		}
	}
	/* ===================================================== */

	private By ARmenu = By.xpath("//i[@title='Accounts Receivable']");
	private By ARinvoicemenu = By.xpath("//a[@href='/AR/invoice']");
	private By EDIfilemenu = By.xpath("//a[@href='/AR/EDI-file-creation']");
	private By createinvoice = By.id("arInvoiceDetailsFilterCreateInvoiceBtn"); // (minor) id updated if needed later
	private By createinvoiceCompat = By.id("arInvoiceFilterCreateInvoiceBtn");  // keep original id too
	private By customerDropdown = By.xpath("//*[@id='arInvoiceDetailsFiltercustomer']");
	private By firstcustomer = By.xpath("(//*[@class='p-dropdown-item'])[1]");
	private By searchField = By.xpath("//*[@class='p-dropdown-filter p-inputtext p-component']");
	private By saveInvoicebtn = By.xpath("//*[@id='arInvoiceDetailsSaveInvoiceBtn']");
	private By fuelcharge = By.xpath("//*[@id='arInvoiceDetailsfuelSurcharge']/input");
	private By accessorialfield = By.xpath("//input[@placeholder='Enter Accessorial Price']");
	private By priceInputField = By.xpath("//input[@placeholder='Enter Price']");
	private By subtotalField = By.xpath("//*[@id='arInvoiceDetailsubTotalFooter']/input");
	private By addrowfordiscount = By.xpath("//*[@id='arInvoiceDetailsaddNewRowdiscount']");
	private By addrowfordeduction = By.xpath("//*[@id='arInvoiceDetailsaddNewRowdeduction']");
	private By amountRadioBtnfirst = By.xpath("//*[@id='arInvoiceDetailsentryFlag_1']");
	private By amountRadioBtnSecond = By.xpath("//*[@id='arInvoiceDetailsentryFlag_2']");

	// Input field locators
	private By percentInputField = By.xpath("(//input[@placeholder='Enter Percentage'])[1]");
	private By percentInputField2ndrow = By.xpath("(//input[@placeholder='Enter Percentage'])[2]");
	private By amountInputField = By.xpath("(//*[@placeholder='Enter Amount'])[1]");
	private By amountInputFieldSecond = By.xpath("(//input[@id='arInvoiceDetailsdiscountAmountTemplateBtnundefined'])[2]");
	private By totalInvoice = By.xpath("//input[@placeholder='Total Invoice Amount ($)']");
	private By nextPagination = By.xpath("//button[@class='p-paginator-next p-paginator-element p-link']");
	private By backPagination = By.xpath("(//button[@aria-label='Page 1'])[1]");
	private By firstcheckBox = By.xpath("//table/tbody/tr[1]/td[1]//input[@type='checkbox']");
	private By todate = By.xpath("//*[@id='arInvoiceDetailsFiltertoDate']/button");
	private By description = By.xpath("(//*[@placeholder='Enter Description'])[1]");
	private By description2ndrow = By.xpath("(//*[@placeholder='Enter Description'])[2]");
	private By deletdiscount = By.xpath("//table/tbody/tr[1]//button[@title='Click to delete']");
	private By totaldiscount = By.xpath("//*[@id='arInvoiceDetailstotalDiscountFooter']/input");
	private By addonDiscription = By.xpath("//span[text()='Select Description']");
	private By addonDatebtn = By.xpath("//*[@id='arInvoiceDetailsaddonDateTemplateBtnundefined']/button");
	private By addonDatebtn2ndrow = By.xpath("(//*[@id='arInvoiceDetailsaddonDateTemplateBtnundefined']/button)[2]");
	private By todaydate = By.xpath("//*[@class='p-datepicker-today']");
	private By addOnamount = By.xpath("//*[@id='arInvoiceDetailsaddOnDeductionAmountTemplateBtnundefined']/input");
	private By addOnamount2ndrow = By.xpath("(//*[@id='arInvoiceDetailsaddOnDeductionAmountTemplateBtnundefined']/input)[2]");
	private By addOnQTY = By.xpath("//*[@id='arInvoiceDetailsquantityTemplateBtnundefined']/input");
	private By addOnQTY2ndrow = By.xpath("(//*[@id='arInvoiceDetailsquantityTemplateBtnundefined']/input)[2]");
	private By firstrowtotal = By.xpath("(//*[@class='p-datatable-tbody'])[3]//tr[1]/td[7]");
	private By secondrowtotal = By.xpath("(//*[@class='p-datatable-tbody'])[3]//tr[2]/td[7]");
	private By addondeletebtn = By.xpath("//*[@id='arInvoiceDetailsaddOnActionTemplateBtnundefined']");
	private By totalAddons = By.xpath("//*[@id='arInvoiceDetailstotalAddonDeducationFooter']/input");
	private By expandInvoice = By.xpath("(//*[@title='Click to expand row'])[1]");
	private By viewButton = By.xpath("(//*[@title='Click to view'])[1]");
	private By editButton = By.xpath("(//*[@title='Click to edit'])[1]");
	private By yesBtn = By.id("confmSubmit");
	private String downloadDir = System.getProperty("user.dir") + "\\downloadedFiles";
	public By downloadDetailXlS = By.xpath("(//*[@title='Click to export excel'])[2]");
	public By downloadCoverXlS = By.xpath("(//*[@title='Click to export excel'])[1]");
	public By downloadPDF = By.xpath("//*[@title='Click to export pdf']");
	private By pendingstatus = By.xpath("//span[text()='Pending']");
	private By printedstatus = By.xpath("//span[text()='Printed']");
	private By Editstatus = By.xpath("(//*[@class='p-multiselect-label-container'])[2]");
	private By searchfirstcoloum = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By invoiceamount = By.xpath("//table/tbody/tr[1]/td[6]");
	private By discount = By.xpath("//table/tbody/tr[1]/td[7]");
	private By EDIexportbtn = By.id("arEdiexportEDIInvoiceBtn");
	private By completestatus = By.xpath("//span[text()='Completed']");
	private By closesearchlist = By.xpath("//*[@class='p-multiselect-close p-link']");

	/**
	 * Navigate to AR menu
	 */
	public void ARMenu() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(ARmenu, 10);
		Thread.sleep(2000);
	}

	/**
	 * Navigate to AR > Invoice
	 */
	public void ClickOnInvoiceMenu() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(ARinvoicemenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Navigate to AR > EDI file
	 */
	public void ClickOnEDIFileMenu() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(EDIfilemenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Total entries from paginator
	 */
	public int getTotalEntriesCount() {
		cp.waitForLoaderToDisappear();
		WebElement paginatorText = driver.findElement(By.className("p-paginator-current"));
		String text = paginatorText.getText();
		String countStr = text.replaceAll(".*of\\s+(\\d+).*", "$1");
		logger.info("Total count " + countStr);
		return Integer.parseInt(countStr);
	}

	/**
	 * Alternative total record count reader
	 */
	public int getTotalRecordCount() {
		WebElement recordElement = driver.findElement(By.xpath("//*[@class='p-paginator-current']"));
		String text = recordElement.getText().trim();
		String count = text.replaceAll(".*of\\s+(\\d+)\\s+entries.*", "$1");
		logger.info("Total count " + count);
		return Integer.parseInt(count);
	}

	/**
	 * Click Create Invoice
	 */
	public void clickOnCreateInvoicebtn() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		// support either id (seen both variants in codebase)
		if (!driver.findElements(createinvoice).isEmpty()) {
			wt.waitToClick(createinvoice, 7);
		} else {
			wt.waitToClick(createinvoiceCompat, 7);
		}
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Select a customer with >= 4 records
	 */
	public void selectCustomer() throws InterruptedException {
		int count = 0;
		WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wt.waitToClick(customerDropdown, 7);
		shortWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='p-dropdown-item']")));

		List<WebElement> options = driver.findElements(By.xpath("//*[@class='p-dropdown-item']"));
		logger.info("Dropdown options found: " + options.size());

		for (int i = 0; i < options.size(); i++) {
			WebElement option = options.get(i);
			js.executeScript("arguments[0].scrollIntoView(true);", option);
			Thread.sleep(300);
			js.executeScript("arguments[0].click();", option);

			cp.Search();
			count = getTotalRecordCount();

			if (count >= 4) {
				getCustomerName();
				logger.info("Selected customer is " + customer);
				break;
			}

			wt.waitToClick(customerDropdown, 7);
			shortWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@class='p-dropdown-item']")));
			options = driver.findElements(By.xpath("//*[@class='p-dropdown-item']"));
		}
	}

	/**
	 * Save invoice (accept confirm if shown)
	 */
	public void clickSaveInvoice() {
		cp.moveToElementAndClick(saveInvoicebtn);
		try {
			WebElement yesPopUp = driver.findElement(yesBtn);
			if (yesPopUp.isDisplayed()) {
				cp.waitAndClickWithJS(yesBtn, 3);
			}
		} catch (Exception ignore) {}
	}

	/**
	 * Mandatory message (banner)
	 */
	public String mandatemessage() {
		return cp.getMandatoryText(By.xpath("//*[@class='MuiBox-root css-fgt2c8']"));
	}

	/**
	 * Walk through pages and verify process types present
	 * (fixed: actually clicks each paginator page)
	 */
	public void verifyOrderProcess1() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		List<String> expectedTypes = Arrays.asList("Regular", "Closeout", "Reconsignment", "Return");
		Set<String> foundTypes = new HashSet<>();

		List<WebElement> pages = driver.findElements(By.xpath("//*[@class='p-paginator-page p-paginator-element p-link']"));
		for (int i = 0; i < pages.size(); i++) {
			// re-find buttons each loop (DOM refresh)
			List<WebElement> buttons = driver.findElements(By.xpath("//*[@class='p-paginator-page p-paginator-element p-link']"));
			if (i < buttons.size()) {
				js.executeScript("arguments[0].click();", buttons.get(i));
				cp.waitForLoaderToDisappear();
				Thread.sleep(500);
			}
			List<WebElement> cells = driver.findElements(By.xpath("//table/tbody/tr/td[2]"));
			for (WebElement cell : cells) {
				String orderProcess = cell.getText().trim();
				foundTypes.add(orderProcess);
			}
			if (foundTypes.containsAll(expectedTypes)) break;
		}
		for (String type : expectedTypes) {
			if (!foundTypes.contains(type)) {
				Assert.fail("FAIL: Expected order process type not found: " + type);
			}
		}
	}

	public void verifyOrderProcess() {
		cp.waitForLoaderToDisappear();
		List<String> expectedTypes = Arrays.asList("Regular", "Closeout", "Reconsignment", "Return");
		Set<String> foundTypes = new HashSet<>();

		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		for (int i = 1; i <= rows.size(); i++) {
			WebElement row = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]"));
			js.executeScript("arguments[0].scrollIntoView(true);", row);
			WebElement cell = row.findElement(By.xpath("./td[2]"));
			foundTypes.add(cell.getText().trim());
		}

		for (String type : expectedTypes) {
			if (!foundTypes.contains(type)) {
				Assert.fail("❌ FAIL: Expected order process type not found: " + type);
			}
		}
		logger.info("✅ All expected process types are present on the page.");
	}

	/**
	 * Add fuel charge
	 */
	public void addFuelCharge(String number) {
		cp.moveToElementAndClick(fuelcharge);
		clearAndType(fuelcharge, number);
		safeClick(todate); // blur to trigger calc
	}

	/**
	 * Change pagination and keep state
	 */
	public void changePagination() throws InterruptedException {
		Thread.sleep(1000);
		fuelCharges = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[13]"));
		driver.findElement(nextPagination).click();
		cp.waitForLoaderToDisappear();
		driver.findElement(backPagination).click();
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Verify fuel charge persists and checkboxes remain selected
	 */
	public boolean checkGridData() {
		List<WebElement> refreshedCheckboxes = driver.findElements(
				By.xpath("//*[@class='p-checkbox p-component p-highlight']//input[@type='checkbox']"));

		if (!fuelCharges.equals(getFuelcharges())) {
			return false;
		}
		for (WebElement cb : refreshedCheckboxes) {
			if (!cb.isSelected()) return false;
		}
		return true;
	}

	public String getFuelcharges() {
		String fc = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[13]"));
		return fc;
	}

	public void selectCheckBox() throws InterruptedException {
		cp.waitForPopupToDisappear();
		Thread.sleep(1000);
		driver.findElement(firstcheckBox).click();
	}

	/**
	 * Select a 'Regular' order and verify dim weight field enabled
	 */
	public boolean selectRegularOrder() {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		for (int i = 1; i <= rows.size(); i++) {
			String orderProcess = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[2]")).getText().trim();
			if ("Regular".equalsIgnoreCase(orderProcess)) {
				WebElement checkbox = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[1]//input[@type='checkbox']"));
				if (!checkbox.isSelected()) checkbox.click();
				logger.info("Regular Order checkbox clicked in row: " + i);
				WebElement weightInput = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]//input[@placeholder='Enter Dimensional Weight']"));
				return weightInput.isEnabled();
			}
		}
		logger.info("No Regular order process found. Skipping validation.");
		return true;
	}

	/**
	 * Select first 3 orders, ensure dim weight & price set
	 */
	public void selectMultipleOrders() {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));

		for (int i = 0; i <= 2 && i < rows.size(); i++) {
			WebElement checkbox = rows.get(i).findElement(By.xpath(".//input[@type='checkbox']"));
			if (!checkbox.isSelected()) checkbox.click();

			selectedOrderNumbers.add(cp.getMandatoryText(By.xpath("//table/tbody/tr[" + (i + 1) + "]/td[3]")));
			enterDimWeight(i);

			By priceBox = By.xpath("(//*[@placeholder='Enter Price'])[" + (i + 1) + "]");
			String price = cp.getAttributeValue(priceBox, "value");
			if ("0.00".equals(price) || price == null || price.isEmpty()) {
				WebElement enterprice = driver.findElement(priceBox);
				enterprice.clear();
				enterprice.sendKeys("45");
			}
		}
	}

	public void enterDimWeight(int i) {
		try {
			int sum = getToatlwt(i) + 5;
			String dimwt = String.valueOf(sum);

			WebElement enterdimwt = driver.findElement(By.xpath("(//*[@placeholder='Enter Dimensional Weight'])[" + (i + 1) + "]"));
			if (enterdimwt.isEnabled()) {
				enterdimwt.clear();
				enterdimwt.sendKeys(dimwt);
			}
		} catch (NoSuchElementException | ElementNotInteractableException e) {
			logger.info("Dim weight is not editable");
		}
	}

	public int getToatlwt(int i) {
		String dimwt = cp.getMandatoryText(By.xpath("//table/tbody/tr[" + (i + 1) + "]/td[10]"));
		return Integer.parseInt(dimwt.trim());
	}

	/**
	 * Verify fuel surcharge = price * j (2dp)
	 */
	public boolean verifyFuelSurcharges(double j) {
		boolean allPassed = true;
		List<WebElement> selected = driver.findElements(
				By.xpath("//*[@class='p-checkbox p-component p-highlight']//input[@type='checkbox']"));
		for (int i = 0; i <= 2 && i < selected.size(); i++) {
			double actualCharges = parseMoney(cp.getMandatoryText(By.xpath("//table/tbody/tr[" + (i + 1) + "]/td[13]")));
			double price = parseMoney(cp.getAttributeValue(By.xpath("(//*[@placeholder='Enter Price'])[" + (i + 1) + "]"), "value"));
			double expected = price * j;
			String a = String.format("%.2f", actualCharges);
			String e = String.format("%.2f", expected);
			if (!a.equals(e)) {
				logger.info("Fuel mismatch row " + (i + 1) + " Expected=" + e + " Actual=" + a);
				allPassed = false;
			}
		}
		return allPassed;
	}

	public void addAccessorialPrice() {
		List<WebElement> selected = driver.findElements(
				By.xpath("//*[@class='p-checkbox p-component p-highlight']//input[@type='checkbox']"));
		for (int i = 0; i <= 2 && i < selected.size(); i++) {
			By acc = By.xpath("(//*[@placeholder='Enter Accessorial Price'])[" + (i + 1) + "]");
			String accPrice = cp.getAttributeValue(acc, "value");
			if ("0.00".equals(accPrice) || accPrice == null || accPrice.isEmpty()) {
				WebElement enterprice = driver.findElement(acc);
				enterprice.clear();
				enterprice.sendKeys("12");
				driver.findElement(By.xpath("(//*[@placeholder='Enter Dimensional Weight'])[1]")).click();
			}
		}
	}

	public double getTotalPrice(int i) {
		return getDoubleFromText(By.xpath("//table/tbody/tr[" + i + "]/td[15]"));
	}

	public double PriceSum(int i) throws InterruptedException {
		double totalPrice = 0.0;
		totalPrice += getDoubleFromAttr(By.xpath("(//*[@placeholder='Enter Price'])[" + i + "]"), "value");
		totalPrice += getDoubleFromText(By.xpath("//table/tbody/tr[" + i + "]/td[13]"));
		Thread.sleep(500);
		totalPrice += getDoubleFromAttr(By.xpath("(//*[@placeholder='Enter Accessorial Price'])[" + i + "]"), "value");
		return totalPrice;
	}

	public double getSubTotal() {
		return getDoubleFromAttr(subtotalField, "value");
	}

	public void clickOnAddnewRowForDiscount() {
		driver.findElement(addrowfordiscount).click();
	}

	/**
	 * Was checking the 'add row' button before; now checks for either discount radio presence.
	 */
	public boolean isRadioButtonDisplayed() {
		try {
			return driver.findElement(amountRadioBtnfirst).isDisplayed() || driver.findElement(amountRadioBtnSecond).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean verifyPercentageFieldEditabel() {
		try {
			wt.waitForElement(percentInputField, 10);
			WebElement percentageField = driver.findElement(percentInputField);
			WebElement amountField = driver.findElement(By.xpath("(//input[@id='arInvoiceDetailsdiscountAmountTemplateBtnundefined'])[1]"));

			if (!percentageField.isEnabled() || percentageField.getAttribute("readonly") != null) {
				logger.info("FAIL: Percentage field is not editable.");
				return false;
			}
			if (amountField.isEnabled() && amountField.getAttribute("disabled") == null) {
				logger.info("FAIL: Amount field is editable.");
				return false;
			}
			return true;
		} catch (NoSuchElementException e) {
			logger.info("FAIL: One or both input fields not found.");
			return false;
		}
	}

	public void clickAmountRadioBtnFirst() {
		wt.waitToClick(amountRadioBtnfirst, 10);
	}

	public boolean verifyAmountEditableAndPercentageNotEditable() {
		try {
			WebElement percentageField = driver.findElement(percentInputField);
			WebElement amountField = driver.findElement(amountInputField);
			return !percentageField.isEnabled() && amountField.isEnabled();
		} catch (NoSuchElementException e) {
			logger.info("FAIL: One or both input fields not found.");
			return false;
		}
	}

	public void addDiscountAmount(String amount) {
		WebElement amountField = driver.findElement(amountInputField);
		amountField.clear();
		amountField.sendKeys(amount);
		try {
			driver.findElement(description).sendKeys("Discount in Amount");
		} catch (Exception ignore) {}
	}

	public void clickOnDeleteBtn() {
		wt.waitToClick(deletdiscount, 5);
	}

	public boolean isRecordNotFoundDisplayed() {
		try {
			WebElement message = driver.findElement(By.xpath("(//td[text()='Record not found.'])[1]"));
			return message.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public double getTotalDiscount() {
		return getDoubleFromAttr(totaldiscount, "value");
	}

	public void addPercent(String disPer) {
		wt.waitToClick(percentInputField, 2);
		driver.findElement(percentInputField).sendKeys(disPer);
		try {
			WebElement enterDiscription = driver.findElement(description);
			enterDiscription.sendKeys("Discount in percent");
		} catch (Exception ignore) {}
	}

	public double getAmountFromFirstRow() {
		return getDoubleFromAttr(amountInputField, "value");
	}

	public double calculateAmountOnPercenatage(double p) {
		SubTotal = getSubTotal();
		double discountAmt = SubTotal * (p / 100);
		return Math.round(discountAmt * 100.0) / 100.0;
	}

	public void clickAmountRadioBtnSecond() {
		wt.waitToClick(amountRadioBtnSecond, 5);
	}

	public void addDiscountAmountSecondRow(String amount) throws InterruptedException {
		Thread.sleep(1000);
		WebElement amountField = driver.findElement(amountInputFieldSecond);
		amountField.clear();
		amountField.sendKeys(amount);
		try {
			driver.findElement(description2ndrow).click();
			driver.findElement(description2ndrow).sendKeys("Discount in Amount");
		} catch (Exception ignore) {}
	}

	public double getPercentFromSecondRow() {
		return getDoubleFromAttr(percentInputField2ndrow, "value");
	}

	public double calculatePercentageFromAmount(double a) {
		SubTotal = getSubTotal();
		double percentage = (a / SubTotal) * 100;
		return Math.round(percentage * 100.0) / 100.0;
	}

	public double getAmtSumOnDiscountRow() {
		List<WebElement> rows = driver.findElements(By.xpath("//*[@placeholder='Enter Amount']"));
		double sumDiscount = 0.0;
		for (WebElement row : rows) {
			String value = row.getAttribute("value");
			if (value != null && !value.isEmpty()) {
				sumDiscount += parseMoney(value);
			}
		}
		return Math.round(sumDiscount * 100.0) / 100.0;
	}

	public double getInvoiceAmount() {
		return getDoubleFromAttr(totalInvoice, "value");
	}

	public void clickOnAddnewRowForAddOns() {
		driver.findElement(addrowfordeduction).click();
	}

	public void enterwt() {
		try {
			wt.waitForElement(By.xpath("//*[@placeholder='Enter Weight']"), 5);
			driver.findElement(By.xpath("//*[@placeholder='Enter Weight']")).sendKeys("3");
		} catch (Exception ignore) {}
	}

	public void selectDiscription() {
		cp.waitForPopupToDisappear();
		driver.findElement(addonDiscription).click();
		wt.waitToClick(firstcustomer, 5);
	}

	public void selectDate() {
		cp.waitForPopupToDisappear();
		driver.findElement(addonDatebtn).click();
		driver.findElement(todaydate).click();
	}

	public void selectDate2ndRow() {
		cp.waitForPopupToDisappear();
		driver.findElement(addonDatebtn2ndrow).click();
		driver.findElement(todaydate).click();
	}

	public void addAmountIntoAddon(String number) {
		cp.waitForPopupToDisappear();
		try {
			clearAndType(addOnamount, number);
		} catch (Exception ignore) {}
	}

	public void addAmountIntoAddon2ndRow(String number) {
		cp.waitForPopupToDisappear();
		try {
			clearAndType(addOnamount2ndrow, number);
		} catch (Exception ignore) {}
	}

	public void addQTYforAddon(String number) {
		cp.waitForPopupToDisappear();
		try {
			clearAndType(addOnQTY, number);
		} catch (Exception ignore) {}
	}

	public void addQTYforAddon2ndRow(String number) {
		cp.waitForPopupToDisappear();
		try {
			clearAndType(addOnQTY2ndrow, number);
		} catch (Exception ignore) {}
	}

	public String getQTY() {
		return cp.getAttributeValue(addOnQTY, "value");
	}

	public double getAddOnTotalFromFirstRow() {
		return getDoubleFromText(firstrowtotal);
	}

	public double calculateTotal() {
		double amount = getDoubleFromAttr(addOnamount, "value");
		double qty = getDoubleFromAttr(addOnQTY, "value");
		return Math.round((amount * qty) * 100.0) / 100.0;
	}

	public void deleteAddon() {
		driver.findElement(addondeletebtn).click();
	}

	public double getTotalAddOns() {
		return getDoubleFromAttr(totalAddons, "value");
	}

	public double calculataionOnTotal() {
		double firstRowtotal = getAddOnTotalFromFirstRow();
		double secondRowAmt = getDoubleFromText(secondrowtotal);
		double finalSum = firstRowtotal + secondRowAmt;
		return Math.round(finalSum * 100.0) / 100.0;
	}

	public double calculateTotalInvoiceAmt() {
		SubTotal = getSubTotal();
		TotalDiscount = getTotalDiscount();
		TotalAddandDeduction = getTotalAddOns();
		double TotalInvoiceSum = SubTotal - TotalDiscount + TotalAddandDeduction;
		return Math.round(TotalInvoiceSum * 100.0) / 100.0;
	}

	public String getInvoiceNumber() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[2]"));
	}

	public void clickOnExpandBtn() {
		wt.waitToClick(expandInvoice, 10);
	}

	public boolean CheckGLOrdersOnAvailable() {
		List<WebElement> Glorder = driver.findElements(By.xpath("//td[starts-with(text(),'GL-')]"));
		for (WebElement cell : Glorder) {
			String order = cell.getText().trim();
			if (!selectedOrderNumbers.contains(order)) {
				logger.info("Missing order: " + order);
				return false;
			}
		}
		return true;
	}

	public void clickOnviewBtn() {
		cp.waitForLoaderToDisappear();
		driver.findElement(viewButton).click();
		cp.waitForLoaderToDisappear();
	}

	public boolean isSaveInvoiceButtonDisabled() {
		WebElement saveButton = driver.findElement(saveInvoicebtn);
		return !saveButton.isEnabled();
	}

	public void clickOnEditInvoiceBtn() {
		cp.waitForLoaderToDisappear();
		driver.findElement(editButton).click();
		cp.waitForLoaderToDisappear();
	}

	public boolean isCustomerndSearchBtnDisabled() {
		WebElement customer = driver.findElement(By.xpath("//*[@id='arInvoiceDetailsFiltercustomer']//input[@disabled]"));
		WebElement search = driver.findElement(cp.searchbtn);
		return !customer.isEnabled() && !search.isEnabled();
	}

	public boolean checkGLOrdersAreProperlySelected() {
		List<WebElement> glOrderHighlight = driver.findElements(
				By.xpath("//div[contains(@class,'p-checkbox p-component p-highlight')]/ancestor::tr"));
		Set<String> highlightedOrders = new HashSet<>();
		for (WebElement row : glOrderHighlight) {
			try {
				WebElement orderCell = row.findElement(By.xpath(".//td[starts-with(text(), 'GL-')]"));
				highlightedOrders.add(orderCell.getText().trim());
			} catch (Exception ignore) {}
		}
		for (String selected : selectedOrderNumbers) {
			if (!highlightedOrders.contains(selected)) {
				logger.info("Missing highlighted order: " + selected);
				return false;
			}
		}
		return true;
	}

	public boolean checkFiedlShouldBeEditabel() {
		WebElement fuelCharge = driver.findElement(fuelcharge);
		WebElement price = driver.findElement(priceInputField);
		WebElement accePrice = driver.findElement(accessorialfield);
		return fuelCharge.isEnabled() && price.isEnabled() && accePrice.isEnabled();
	}

	/* capture & remove specific order in edit */
	double orderTotalAmt;

	public void RemovedFirstorder(int i) throws InterruptedException {
		cp.waitForPopupToDisappear();
		Thread.sleep(500);
		updatedOrder = cp.getMandatoryText(By.xpath("//table/tbody/tr[" + i + "]/td[3]"));
		// FIX: click the i-th row checkbox (was always first row)
		By ithCheckbox = By.xpath("//table/tbody/tr[" + i + "]/td[1]//input[@type='checkbox']");
		driver.findElement(ithCheckbox).click();
		orderTotalAmt = getTotalPrice(i);
	}

	public double minusAmountfromTotal() {
		double result = SubTotal - orderTotalAmt;
		return Math.round(result * 100.0) / 100.0;
	}

	public double getPercentage() {
		return !driver.findElement(amountInputField).isEnabled()
				? getDoubleFromAttr(percentInputField, "value")
				: 0.0;
	}

	public void getCustomerName() {
		customer = cp.getMandatoryText(By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[1]"));
	}

	public void selectEditInvoiceCustomer() {
		wt.waitToClick(customerDropdown, 7);
		cp.DrpDwnValueSel(searchField, customer);
	}

	public boolean checkOrderAvailability() {
		List<WebElement> glOrderCells = driver.findElements(By.xpath("//td[starts-with(text(), 'GL-')]"));
		for (WebElement cell : glOrderCells) {
			String orderText = cell.getText().trim();
			if (orderText.equalsIgnoreCase(updatedOrder)) return true;
		}
		return false;
	}

	public void selectOneOrderAndGetAmt() {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		for (WebElement row : rows) {
			try {
				WebElement checkbox = row.findElement(By.xpath(".//input[@class='p-checkbox-input']"));
				if (!checkbox.isSelected()) {
					checkbox.click();
					Thread.sleep(200);
					WebElement glCell = row.findElement(By.xpath(".//td[15]"));
					String glOrderTotalAmt = glCell.getText().trim();
					logger.info("Selected GL Order Amount: " + glOrderTotalAmt);
					break;
				}
			} catch (Exception ignore) {}
		}
	}

	public double getSumOfSelectedOrderAmounts() {
		double total = 0.0;
		selectedOrderNumbers.clear();
		List<WebElement> highlightedRows = driver.findElements(
				By.xpath("//div[contains(@class,'p-checkbox p-component p-highlight')]/ancestor::tr"));
		for (WebElement row : highlightedRows) {
			try {
				String order = row.findElement(By.xpath("./td[3]")).getText().trim();
				selectedOrderNumbers.add(order);
				String amountText = row.findElement(By.xpath("./td[15]")).getText().trim();
				total += parseMoney(amountText);
			} catch (Exception ignore) {}
		}
		return Math.round(total * 100.0) / 100.0;
	}

	public void addPercentforSecondRow(String disPer) {
		driver.findElement(percentInputField2ndrow).sendKeys(disPer);
		driver.findElement(description2ndrow).sendKeys("Discount in percent");
	}

	public boolean areSelectedOrdersAbsentInGrid() {
		List<WebElement> glOrderCells = driver.findElements(By.xpath("//td[starts-with(text(), 'GL-')]"));
		Set<String> gridOrders = new HashSet<>();
		for (WebElement cell : glOrderCells) {
			String orderText = cell.getText().trim();
			if (!orderText.isEmpty()) gridOrders.add(orderText);
		}
		for (String selectedOrder : selectedOrderNumbers) {
			if (gridOrders.contains(selectedOrder)) {
				logger.info("FAIL: Selected order still present in grid: " + selectedOrder);
				return false;
			}
		}
		return true;
	}

	public void exportInvoiceDetailsToXLS() {
		wt.waitToClick(By.xpath("(//*[@title='Click to export excel'])[2]"), 5);
		InvoiceNumber = getInvoiceNumber(); // store it
	}

	public void deleteExistingFiles() {
		File dir = new File(downloadDir);
		ensureDownloadDirExists(dir);
		if (dir.isDirectory()) {
			File[] toDel = dir.listFiles((d, name) -> name.endsWith(".xlsx") || name.endsWith(".xls") || name.endsWith(".pdf"));
			if (toDel != null) {
				for (File f : toDel) {
					logger.info("Deleted file: " + f.getName());
					f.delete();
				}
			}
		}
	}

	public boolean isFileDownloaded() throws InterruptedException {
		File dir = new File(downloadDir);
		ensureDownloadDirExists(dir);

		File[] filesBefore = dir.listFiles();
		int initialCount = filesBefore != null ? filesBefore.length : 0;
		int timeoutSec = 30;
		int waited = 0;

		while (waited < timeoutSec) {
			Thread.sleep(1000);
			File[] filesAfter = dir.listFiles();
			if (filesAfter != null && filesAfter.length > initialCount) {
				// ignore temp partials
				boolean hasPartial = Arrays.stream(filesAfter).anyMatch(f -> f.getName().endsWith(".crdownload") || f.getName().endsWith(".part"));
				if (hasPartial) { waited++; continue; }
				logger.info("File downloaded: " + filesAfter[filesAfter.length - 1].getName());
				return true;
			}
			waited++;
		}
		logger.info("No file downloaded within timeout.");
		return false;
	}

	public void exportDetailXLSfile() {
		wt.waitToClickWithAction(downloadDetailXlS, 10);
	}

	public void exportcoverXLSfile() {
		wt.waitToClickWithAction(downloadCoverXlS, 10);
	}

	public void exportPDFfile() {
		wt.waitToClickWithAction(downloadPDF, 10);
	}

	public void assignAmountForEDICheck() {
		List<WebElement> priceInputs = driver.findElements(By.xpath("//input[@placeholder='Enter Price']"));
		for (WebElement priceInput : priceInputs) {
			double price = parseMoney(priceInput.getAttribute("value"));
			orderPrices.add(price);
			logger.info("Order Price: " + price);
		}
	}

	/* -------------------- EDI page methods -------------------- */

	public String getDefaultStatus() {
		return cp.getMandatoryText(Editstatus);
	}

	public void seacrhInvoiceNumber(String InvoiceNo) {
		cp.searchColoumFilter(searchfirstcoloum, InvoiceNo);
	}

	public String getInvoiceNumberFromEDI() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[3]"));
	}

	public double getEDIInvAmount() {
		return getDoubleFromText(invoiceamount);
	}

	public double getEDIInvDiscount() {
		return getDoubleFromText(discount);
	}

	public boolean checkPricesAvailableOnEDIPage() {
		try {
			List<WebElement> priceCells = driver.findElements(
					By.xpath("//table/tbody/tr[td[1][starts-with(text(), 'GL-')]]/td[6]"));
			for (WebElement cell : priceCells) {
				double price = parseMoney(cell.getText().trim());
				if (!orderPrices.contains(price)) {
					logger.info("Price not found in orderPrices: " + price);
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public double calculateTotalPriceSum() {
		double sumOfTotal = 0.0;
		try {
			List<WebElement> priceCells = driver.findElements(
					By.xpath("//table/tbody/tr[td[1][starts-with(text(), 'GL-')]]/td[9]"));
			for (WebElement cell : priceCells) {
				sumOfTotal += parseMoney(cell.getText().trim());
			}
		} catch (Exception e) {
			logger.info("Error while calculating total price");
		}
		return Math.round(sumOfTotal * 100.0) / 100.0;
	}

	public void selectFirstInvoice() {
		driver.findElement(By.xpath("//table/tbody/tr[1]/td[2]")).click();
	}

	public void clickOnEdiFileExportBtn() {
		wt.waitToClick(EDIexportbtn, 10);
	}

	public void selectCompleteStatus() {
		wt.waitToClick(Editstatus, 20);
		wt.waitToClick(completestatus, 10);
		wt.waitToClick(closesearchlist, 10);
	}

	public String getExportStatus() {
		return cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[8]"));
	}

	/**
	 * FIX: return true if the Edit button IS present (previously returned inverse)
	 */
	public boolean isEditButtonDisplayed() {
		List<WebElement> elements = driver.findElements(By.xpath("(//*[@title='Click to edit'])[1]"));
		return !elements.isEmpty();
	}

	public void selectPrintedStatus() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(Editstatus, 10);
		wt.waitToClick(pendingstatus, 10);
		wt.waitToClick(printedstatus, 10);
		wt.waitToClick(closesearchlist, 10);
	}

	public void checkAndSelectTwoInvoices() {
		List<WebElement> elements = driver.findElements(By.xpath("//table/tbody/tr"));
		if (elements.size() >= 2) {
			for (int i = 1; i <= 2; i++) {
				WebElement checkbox = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[2]//input[@type='checkbox']"));
				if (!checkbox.isSelected()) checkbox.click();
			}
		} else {
			throw new SkipException("Less than two invoice rows present, skipping test.");
		}
	}

	public void paginationOnListingPageforEDIPage() {
		cp.paginationTest(By.xpath("(//*[@title='Click to expand row'])[1]"), 1);
		cp.waitForLoaderToDisappear();
	}
}
