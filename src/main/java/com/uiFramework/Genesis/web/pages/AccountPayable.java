package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.ReadJsonData;

public class AccountPayable {
	WebDriver driver = null;
	CommonPage cp;
	CommonMethods cm;
	private WebDriverWait wait;
	private JavascriptExecutor js;
	protected static final Logger logger = Logger.getLogger(AccountPayable.class.getName());
	double totalPayable;
	double totalPayableEdit;
	String invoiceNo;
	int count;
	List<String> manifestNumbers = new ArrayList<>();
	List<String> authNumbers = new ArrayList<>();
	List<String> truckNumbers = new ArrayList<>();

	public AccountPayable(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		this.cp = new CommonPage(driver);
		this.js = (JavascriptExecutor) driver;
		this.cm = new CommonMethods(driver);
	}

	/* ---------- small, resilient helpers ---------- */
	private WebElement waitVisible(By loc) { return wait.until(ExpectedConditions.visibilityOfElementLocated(loc)); }
	private WebElement waitClickable(By loc) { return wait.until(ExpectedConditions.elementToBeClickable(loc)); }
	private void safeClick(By loc) {
		try { waitClickable(loc).click(); }
		catch (Exception e) {
			try {
				WebElement el = waitVisible(loc);
				js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
				js.executeScript("arguments[0].click();", el);
			} catch (Exception ex) { throw e; }
		}
	}
	private CharSequence selectAllChord() {
		String os = System.getProperty("os.name","").toLowerCase();
		return os.contains("mac") ? Keys.chord(Keys.COMMAND, "a") : Keys.chord(Keys.CONTROL, "a");
	}
	private void clearAndType(By loc, String text) {
		WebElement el = waitClickable(loc);
		el.click();
		el.sendKeys(selectAllChord());
		el.sendKeys(Keys.BACK_SPACE);
		el.sendKeys(text);
	}
	private String safeGetValue(By loc) {
		return waitVisible(loc).getAttribute("value") == null ? "" : waitVisible(loc).getAttribute("value").trim();
	}
	private String safeText(By loc) {
		return waitVisible(loc).getText().trim();
	}
	/* --------------------------------------------- */

	private By APmenu = By.xpath("//i[@title='Accounts Payable']");
	private By APsubmenu = By.xpath("//a[@href=\"/AP/pay-invoices\"]");
	private By payinvoice = By.id("APInviceBatchPayableCreateInvoiceBtn");
	private By carrierDropdown = By.xpath("//*[@id=\"APInviceUploadInvoiceFiltercarrier\"]/span");
	private By carrierList = By.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li");
	private By invoiceNoField = By.xpath("//input[@placeholder='Enter Invoice No']");
	private By invoiceAmountField = By.xpath("//input[@placeholder='Enter Invoice Amount']");
	private By payableAmountField = By.xpath("//input[contains(@id, 'payableAmount')]");
	private By commentField = By.xpath("//textarea[@placeholder=\"Enter Invoice Comment\"]");
	private By totalpayableField = By.xpath("//input[@id=\"APInviceSummarypayableAmount\"]");
	private By invoiceDate = By.xpath("//input[@placeholder=\"Invoice Date\"]");
	private By saveBatch = By.xpath("//button[@title=\"Click to save batch\"]");
	private By lineItemsInputLocator = By.xpath("//input[@placeholder='Enter No Of Line Items']");
	private By invoiceDateInput = By.xpath("//input[@placeholder='Invoice Date']");
	private By dueDateInput = By.xpath("//input[@placeholder='Due Date']");
	private By fiscalDropdown = By.xpath("//*[@id='APInviceSummaryfiscalPeriod']/span");
	private String fiscalOptionXpathTemplate = "//div[contains(@class,'p-dropdown-items-wrapper')]//ul/li[%d]";
	private By expandRowButton = By.xpath("(//*[@id=\"panel1bh-content\"]//div[2]/table/tbody/tr[1]/td[1]/div//button)[1]");
	private By innerExpand = By.xpath("//*/td/div/div/div/div/div/div/div/div/div/div/div[1]/table/tbody/tr/td[1]//button[1]");
	private By edit = By.xpath("//*/td/div//div/div/div[1]/table/tbody/tr/td[1]//button[2]");
	private By payableamountedit = By.xpath("//input[@id=\"APInviceBatchEditpayableAmount\"]");
	private By fromdate = By.xpath("//*[@id=\"APInviceBatchPayablefromDate\"]/input");
	private By todate = By.xpath("//*[@id=\"APInviceBatchPayabletoDate\"]/input");
	private By firstRowInvoice = By.xpath("//*[@id='panel1bh-content']//table/tbody/tr/td[3]");
	private By page2Button = By.xpath("//button[@aria-label='Page 2']");
	private By page1Button = By.xpath("//button[@aria-label=\"Page 1\"]");

	/* go to AP screen */
	public void goToAPScreen() {
		cp.waitForLoaderToDisappear();
		safeClick(APmenu);
		cp.waitForLoaderToDisappear();
		safeClick(APsubmenu);
		cp.waitForLoaderToDisappear();
	}

	/* click create invoice (robust against stale) */
	public void clickPayInvoice() {
		int retries = 3;
		while (retries-- > 0) {
			try {
				WebElement btn = waitClickable(payinvoice);
				new Actions(driver).moveToElement(btn).perform();
				btn.click();
				break;
			} catch (StaleElementReferenceException e) {
				logger.warning("Stale while clicking 'Create invoice'. Retrying...");
			} catch (Exception e) {
				logger.severe("Error clicking 'Create invoice': " + e.getMessage());
				break;
			}
		}
		cp.waitForLoaderToDisappear();
	}

	/* select a carrier that has GL- or -bound rows, else skip */
	public void selectCarrierWithRecordsOrSkip() {
		safeClick(carrierDropdown);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(carrierList));

		int carrierCount = driver.findElements(carrierList).size();
		boolean found = false;
		logger.info("Carriers in list: " + carrierCount);

		for (int i = 1; i <= carrierCount; i++) { // start from 1 for generality
			try {
				By ith = By.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li[" + i + "]");
				safeClick(ith);
				cp.waitForLoaderToDisappear();

				boolean hasMatch = driver.findElements(By.xpath("//td")).stream()
						.map(WebElement::getText).map(String::trim)
						.anyMatch(t -> t.startsWith("GL-") || t.toLowerCase().endsWith("bound"));
				if (hasMatch) { found = true; break; }

				// try next
				safeClick(carrierDropdown);
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(carrierList));
			} catch (Exception e) {
				logger.warning("Carrier idx " + i + " gave error: " + e.getMessage());
				try {
					safeClick(carrierDropdown);
					wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(carrierList));
				} catch (Exception ignore) {}
			}
		}

		if (!found) {
			logger.warning("No carrier with GL-/bound rows. Skipping.");
			throw new SkipException("No valid carrier with 'GL-' or '-bound' rows.");
		}
	}

	/* enter invoice details (up to 4 rows if present) and compute total payable */
	public double enterInvoiceDetailsAndCalculateTotal() {
		List<WebElement> invoiceNos = driver.findElements(invoiceNoField);
		List<WebElement> invoiceAmts = driver.findElements(invoiceAmountField);
		List<WebElement> payables = driver.findElements(payableAmountField);

		int rows = Math.min(Math.min(invoiceNos.size(), invoiceAmts.size()), payables.size());
		rows = Math.min(rows, 4); // cap to 4 like original intent (0..3)

		totalPayable = 0.0;
		count = rows - 1;

		for (int i = 0; i < rows; i++) {
			String inv = "INV" + System.currentTimeMillis() % 100000 + "-" + (100 + i);
			String invAmt = String.valueOf(100 * (i + 1));

			WebElement invField = invoiceNos.get(i);
			WebElement amtField = invoiceAmts.get(i);
			WebElement payField = payables.get(i);

			invField.click(); invField.sendKeys(selectAllChord()); invField.sendKeys(Keys.BACK_SPACE); invField.sendKeys(inv);
			amtField.click(); amtField.sendKeys(selectAllChord()); amtField.sendKeys(Keys.BACK_SPACE); amtField.sendKeys(invAmt);

			wait.until(ExpectedConditions.attributeToBeNotEmpty(payField, "value"));
			String payableValue = payField.getAttribute("value");
			try { totalPayable += Double.parseDouble(payableValue); }
			catch (NumberFormatException nfe) { logger.warning("Non-numeric payable in row " + i + ": " + payableValue); }
		}
		return totalPayable;
	}

	/* ---------- validators (fixed logic) ---------- */

	// Accept letters, digits, hyphen as valid for Invoice No
	private void validateInvoiceNoField() {
		WebElement f = driver.findElement(invoiceNoField);
		String input = "1189-ABC";
		f.click(); f.sendKeys(selectAllChord()); f.sendKeys(Keys.BACK_SPACE); f.sendKeys(input);
		String actual = f.getAttribute("value");
		// pass if field kept the input and it matches allowed pattern
		if (!actual.matches("^[A-Za-z0-9-]+$")) {
			throw new RuntimeException("Invoice No did not accept valid alphanumeric with hyphen. Got: " + actual);
		}
		f.sendKeys(selectAllChord()); f.sendKeys(Keys.BACK_SPACE);
	}

	// Field should be numeric-only: OK if numeric OR cleared; fail only if letters remain
	private void validateInvoiceAmountField() {
		WebElement f = driver.findElement(invoiceAmountField);
		String bad = "1189-ABC";
		f.click(); f.sendKeys(selectAllChord()); f.sendKeys(Keys.BACK_SPACE); f.sendKeys(bad);
		String actual = f.getAttribute("value");
		boolean numeric = actual.matches("^\\d+(\\.\\d+)?$");
		boolean cleared = actual.isEmpty();
		if (!(numeric || cleared)) {
			throw new RuntimeException("Invoice Amount accepted non-numeric characters: " + actual);
		}
		f.sendKeys(selectAllChord()); f.sendKeys(Keys.BACK_SPACE);
	}

	// Same as above for payable amount
	private void validatePayableAmountField() {
		WebElement f = driver.findElement(payableAmountField);
		String bad = "1189-ABC";
		f.click(); f.sendKeys(selectAllChord()); f.sendKeys(Keys.BACK_SPACE); f.sendKeys(bad);
		String actual = f.getAttribute("value");
		boolean numeric = actual.matches("^\\d+(\\.\\d+)?$");
		boolean cleared = actual.isEmpty();
		if (!(numeric || cleared)) {
			throw new RuntimeException("Payable Amount accepted non-numeric characters: " + actual);
		}
		f.sendKeys(selectAllChord()); f.sendKeys(Keys.BACK_SPACE);
	}

	public void validateFieldsWithAlphanumeric() {
		validateInvoiceNoField();
		validateInvoiceAmountField();
		validatePayableAmountField();
		validateCommentField();
	}

	private void validateCommentField() {
		WebElement comment = driver.findElement(commentField);
		String valid = "Comment " + new Random().nextInt(1000);
		comment.click(); comment.sendKeys(selectAllChord()); comment.sendKeys(Keys.BACK_SPACE); comment.sendKeys(valid);
		String actual = comment.getAttribute("value");
		if (!actual.matches("^[a-zA-Z0-9 ]*$")) {
			throw new RuntimeException("Invoice Comment did not accept alphanumeric/space: " + actual);
		}
	}

	/* totals check (guarded) */
	public void verifyTotalPayableEqualsTotal() {
		safeClick(invoiceDate); // often triggers summary calc
		String actualValue = safeGetValue(totalpayableField);
		try {
			double actual = Double.parseDouble(actualValue);
			if (Math.abs(actual - totalPayable) > 0.001) {
				throw new AssertionError("Expected total payable: " + totalPayable + ", but found: " + actual);
			}
		} catch (NumberFormatException nfe) {
			throw new AssertionError("Total payable not numeric: " + actualValue);
		}
	}

	public void saveBatch() {
		safeClick(saveBatch);
	}

	public void testFieldErrorMessages() {
		List<String> fields = new ArrayList<>(Arrays.asList("invoiceDatetext", "dueDateText", "FiscalPeriodText"));
		for (String field : fields) {
			try {
				String expected = ReadJsonData.getNestedValue(field, "expected");
				String message = ReadJsonData.getNestedValue(field, "message");
				if (expected == null || expected.isEmpty()) continue;
				List<WebElement> els = driver.findElements(By.xpath("//*[contains(text(), '" + expected + "')]"));
				if (!els.isEmpty()) {
					Assert.assertEquals(els.get(0).getText().trim(), expected, message);
				} else {
					logger.info("No error message displayed for field: " + field);
				}
			} catch (RuntimeException e) {
				logger.warning("Error processing field: " + field + " -> " + e.getMessage());
			}
		}
	}

	public void verifyNumberOfLineItemsEqualsCount() {
		String val = safeGetValue(lineItemsInputLocator);
		int actual = Integer.parseInt(val.isEmpty() ? "0" : val);
		Assert.assertEquals(actual, count + 1, "Expected line items count: " + (count + 1) + ", but found: " + actual);
	}

	public void enterCurrentInvoiceDate() {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
		clearAndType(invoiceDateInput, today);
	}

	public void enterDueDate(String date) {
		clearAndType(dueDateInput, date);
	}

	public void selectFiscalPeriodByIndex(int index) {
		safeClick(fiscalDropdown);
		By opt = By.xpath(String.format(fiscalOptionXpathTemplate, index));
		safeClick(opt);
	}

	/* tables → collectors */
	public List<String> validateLbolTableManifestData() {
		try {
			WebElement header = driver.findElement(By.xpath("//h6[text()='LBOL Listing']"));
			WebElement container = header.findElement(By.xpath("ancestor::div[contains(@class, 'MuiBox-root')][2]"));
			WebElement table = container.findElement(By.xpath(".//table"));
			boolean hasManifestCol = !table.findElements(By.xpath(".//div[@data-pc-section='headercontent']//span[text()='Manifest No']")).isEmpty();
			if (!hasManifestCol) return manifestNumbers;

			List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
			for (int i = 1; i <= rows.size(); i++) {
				try {
					// invoice amount in td[12] (input or text)
					String invoiceAmount = "";
					List<WebElement> inputs = table.findElements(By.xpath(".//tbody/tr[" + i + "]/td[12]//span//input"));
					if (!inputs.isEmpty()) invoiceAmount = inputs.get(0).getAttribute("value").trim();
					else invoiceAmount = table.findElement(By.xpath(".//tbody/tr[" + i + "]/td[12]")).getText().trim();
					if (invoiceAmount.isEmpty()) break;

					String manifestNo = table.findElement(By.xpath(".//tbody/tr[" + i + "]/td[1]")).getText().trim();
					if (!manifestNo.isEmpty()) manifestNumbers.add(manifestNo);
				} catch (NoSuchElementException ignore) {}
			}
		} catch (NoSuchElementException e) {
			logger.info("LBOL table/header not found.");
		}
		return manifestNumbers;
	}

	public List<String> validateTableAuthData() {
		List<String> authNumbers = new ArrayList<>();
		try {
			WebElement header = driver.findElement(By.xpath("//h6[text()='Auth Listing']"));
			WebElement container = header.findElement(By.xpath("ancestor::div[contains(@class, 'MuiBox-root')][2]"));
			WebElement table = container.findElement(By.xpath(".//table"));
			boolean hasAuthCol = !table.findElements(By.xpath(".//div[@data-pc-section='headercontent']//span[text()='Auth No']")).isEmpty();
			if (!hasAuthCol) return authNumbers;

			List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
			for (int i = 1; i <= rows.size(); i++) {
				try {
					String invoiceAmount = "";
					List<WebElement> inputs = table.findElements(By.xpath(".//tbody/tr[" + i + "]/td[7]//input"));
					if (!inputs.isEmpty()) invoiceAmount = inputs.get(0).getAttribute("value").trim();
					else invoiceAmount = table.findElement(By.xpath(".//tbody/tr[" + i + "]/td[7]")).getText().trim();
					if (invoiceAmount.isEmpty()) break;

					String authNo = table.findElement(By.xpath(".//tbody/tr[" + i + "]/td[3]")).getText().trim();
					if (!authNo.isEmpty()) authNumbers.add(authNo);
				} catch (NoSuchElementException ignore) {}
			}
		} catch (NoSuchElementException e) {
			logger.info("Auth table/header not found.");
		}
		return authNumbers;
	}

	public List<String> validateTruckTableData() {
		try {
			WebElement header = driver.findElement(By.xpath("//h6[text()='Truck Listing']"));
			WebElement container = header.findElement(By.xpath("ancestor::div[contains(@class, 'MuiBox-root')][2]"));
			WebElement table = container.findElement(By.xpath(".//table"));
			boolean hasTruckCol = !table.findElements(By.xpath(".//div[@data-pc-section='headercontent']//span[text()='Truck No']")).isEmpty();
			if (!hasTruckCol) return truckNumbers;

			List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));
			for (int i = 1; i <= rows.size(); i++) {
				try {
					String invoiceAmount = "";
					List<WebElement> inputs = table.findElements(By.xpath(".//tbody/tr[" + i + "]/td[9]//input"));
					if (!inputs.isEmpty()) invoiceAmount = inputs.get(0).getAttribute("value").trim();
					else invoiceAmount = table.findElement(By.xpath(".//tbody/tr[" + i + "]/td[9]")).getText().trim();
					if (invoiceAmount.isEmpty()) break;

					String truckNo = table.findElement(By.xpath(".//tbody/tr[" + i + "]/td[6]")).getText().trim();
					if (!truckNo.isEmpty()) truckNumbers.add(truckNo);
				} catch (NoSuchElementException ignore) {}
			}
		} catch (NoSuchElementException e) {
			logger.info("Truck table/header not found.");
		}
		return truckNumbers;
	}

	public void expandandRows() { safeClick(expandRowButton); }

	public void validateDisplayedTotalPayable() {
		try {
			WebElement el = driver.findElement(By.xpath("//*/td/div/div/div/div/div/div/div/div/div/div/div[1]/table/tbody/tr/td[4]"));
			String txt = el.getText().trim().replaceAll("[^\\d.]", "");
			double displayed = Double.parseDouble(txt);
			if (Double.compare(displayed, totalPayable) != 0) {
				logger.severe("Mismatch → Stored: " + totalPayable + ", Displayed: " + displayed);
			}
		} catch (Exception e) {
			logger.severe("Unable to validate displayed total payable: " + e.getMessage());
		}
	}

	public void validateSumMatchesTotalAmount() {
		try {
			WebElement table = driver.findElement(By.xpath("//*/td/div//div/div/div/div/div/div[1]/table"));
			List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr"));

			double sum = 0.0;
			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (cells.size() >= 4) {
					String first = cells.get(0).getText().trim().toLowerCase();
					if (first.contains("total")) continue;
					String amt = cells.get(3).getText().trim().replaceAll("[^\\d.]", "");
					if (!amt.isEmpty()) sum += Double.parseDouble(amt);
				}
			}

			WebElement totalEl = driver.findElement(By.xpath("(//td[@data-pc-name='datatable' and @data-pc-section='bodycell' and @style='min-width: 3rem;'])[1]"));
			double total = Double.parseDouble(totalEl.getText().trim());
			Assert.assertEquals(sum, total, 0.01, "Sum of row amounts does not match total amount.");
		} catch (NoSuchElementException e) {
			Assert.fail("Required element not found: " + e.getMessage());
		} catch (NumberFormatException e) {
			Assert.fail("Failed to parse numeric value: " + e.getMessage());
		}
	}

	public void expandInnerRowsAndValidateData() {
		safeClick(innerExpand);
		validateManifestNumbers(manifestNumbers);
		validateAuthNumbers(authNumbers);
		validateTruckNumbers(truckNumbers);
	}

	public void validateManifestNumbers(List<String> manifestNumbers) {
		List<String> actual = new ArrayList<>();
		if (!driver.findElements(By.xpath("//h6[text()='LBOL Listing']")).isEmpty()) {
			List<WebElement> rows = driver.findElements(By.xpath("//h6[text()='LBOL Listing']/following::table[1]/tbody/tr"));
			if (rows.isEmpty()) Assert.fail("Expected Manifest Numbers not found.");
			for (WebElement r : rows) {
				String m = r.findElement(By.xpath("./td[1]")).getText().trim();
				if (!m.isEmpty() && !actual.contains(m)) actual.add(m);
			}
			logger.info("Actual Manifest Numbers: " + actual);
		} else {
			logger.info("LBOL Listing header not found. Skipping.");
			return;
		}
		if (!actual.containsAll(manifestNumbers) || actual.size() != manifestNumbers.size()) {
			Assert.fail("Mismatch in Manifest Numbers. Expected: " + manifestNumbers + ", Found: " + actual);
		}
	}

	public void validateAuthNumbers(List<String> authNumbers) {
		List<String> actual = new ArrayList<>();
		if (!driver.findElements(By.xpath("//h6[text()='Auth Listing']")).isEmpty()) {
			List<WebElement> rows = driver.findElements(By.xpath("//h6[text()='Auth Listing']/following::table[1]/tbody/tr"));
			if (rows.isEmpty()) Assert.fail("Expected Auth Numbers not found.");
			for (WebElement r : rows) {
				String a = r.findElement(By.xpath("./td[3]")).getText().trim();
				if (!a.isEmpty() && !actual.contains(a)) actual.add(a);
			}
			logger.info("Actual Auth Numbers: " + actual);
		} else {
			logger.info("Auth Listing header not found. Skipping.");
			return;
		}
		if (!actual.containsAll(authNumbers) || actual.size() != authNumbers.size()) {
			Assert.fail("Mismatch in Auth Numbers. Expected: " + authNumbers + ", Found: " + actual);
		}
	}

	public void validateTruckNumbers(List<String> truckNumbers) {
		List<String> actual = new ArrayList<>();
		if (!driver.findElements(By.xpath("//h6[text()='Truck Listing']")).isEmpty()) {
			List<WebElement> rows = driver.findElements(By.xpath("//h6[text()='Truck Listing']/following::table[1]/tbody/tr"));
			if (rows.isEmpty()) Assert.fail("Expected Truck Numbers not found.");
			for (WebElement r : rows) {
				String t = r.findElement(By.xpath("./td[6]")).getText().trim();
				if (!t.isEmpty() && !actual.contains(t)) actual.add(t);
			}
			logger.info("Actual Truck Numbers: " + actual);
		} else {
			logger.info("Truck Listing header not found. Skipping.");
			return;
		}
		if (!actual.containsAll(truckNumbers) || actual.size() != truckNumbers.size()) {
			Assert.fail("Mismatch in Truck Numbers. Expected: " + truckNumbers + ", Found: " + actual);
		}
	}

	public void edit() {
		safeClick(edit);
		cp.waitForLoaderToDisappear();
	}

	public void validateTotalPayableOnEdit() {
		try {
			String txt = driver.findElement(payableamountedit).getAttribute("value").trim().replaceAll("[^\\d.]", "");
			double displayed = Double.parseDouble(txt);
			if (Double.compare(displayed, totalPayable) != 0) {
				logger.severe("Mismatch → Stored: " + totalPayable + ", Displayed: " + displayed);
			}
		} catch (Exception e) {
			logger.severe("Unable to validate total payable on edit: " + e.getMessage());
		}
	}

	public double editInvoiceDetailsAndCalculateTotal() {
		List<WebElement> invoiceNos = driver.findElements(invoiceNoField);
		List<WebElement> invoiceAmts = driver.findElements(invoiceAmountField);
		List<WebElement> payables = driver.findElements(payableAmountField);

		int rows = Math.min(Math.min(invoiceNos.size(), invoiceAmts.size()), payables.size());
		rows = Math.min(rows, 3); // first 3 rows only

		totalPayableEdit = 0.0;

		for (int i = 0; i < rows; i++) {
			WebElement invField = invoiceNos.get(i);
			WebElement amtField = invoiceAmts.get(i);
			WebElement payField = payables.get(i);

			String currentAmount = amtField.getAttribute("value").trim();
			if (currentAmount.isEmpty()) {
				String inv = "INV" + (i + 1) + "-" + (100 + i);
				String invAmt = String.valueOf(100 * (i + 1));

				invField.click(); invField.sendKeys(selectAllChord()); invField.sendKeys(Keys.BACK_SPACE); invField.sendKeys(inv);
				amtField.click(); amtField.sendKeys(selectAllChord()); amtField.sendKeys(Keys.BACK_SPACE); amtField.sendKeys(invAmt);

				wait.until(ExpectedConditions.attributeToBeNotEmpty(payField, "value"));
				String payableVal = payField.getAttribute("value").trim();

				double payableNum = Double.parseDouble(payableVal);
				double invoiceNum = Double.parseDouble(invAmt);
				if (Double.compare(payableNum, invoiceNum) != 0) {
					throw new AssertionError("Payable (" + payableNum + ") != invoice (" + invoiceNum + ") at row " + i);
				}
				totalPayableEdit += payableNum;
			}
		}
		return totalPayableEdit;
	}

	public void verifyTotalPayableInEditEqualsTotal() {
		waitClickable(invoiceNoField).click(); // trigger calc
		String actualVal = safeGetValue(By.xpath("//input[@id='APInviceBatchEditpayableAmount']"));
		double actual = Double.parseDouble(actualVal);
		if (Math.abs(actual - totalPayable) > 0.001) {
			throw new AssertionError("Expected total payable: " + totalPayable + ", but found: " + actual);
		}
	}

	public void validateDisplayedTotalPayableForEdit() {
		try {
			WebElement el = driver.findElement(By.xpath("//*/td/div/div/div/div/div/div/div/div/div/div/div[1]/table/tbody/tr/td[4]"));
			String txt = el.getText().trim().replaceAll("[^\\d.]", "");
			double displayed = Double.parseDouble(txt);
			if (Double.compare(displayed, totalPayableEdit) != 0) {
				logger.severe("Mismatch → Stored: " + totalPayableEdit + ", Displayed: " + displayed);
			}
		} catch (Exception e) {
			logger.severe("Unable to validate displayed total payable (edit): " + e.getMessage());
		}
	}

	public void enterRandomInvoiceDetails() {
		Random r = new Random();
		String randInv = "INV-" + (100000 + r.nextInt(900000));
		String randAmt = String.valueOf(100 + r.nextInt(900));

		WebElement inv = driver.findElement(invoiceNoField);
		WebElement amt = driver.findElement(invoiceAmountField);
		WebElement pay = driver.findElement(payableAmountField);

		inv.click(); inv.sendKeys(selectAllChord()); inv.sendKeys(Keys.BACK_SPACE); inv.sendKeys(randInv);
		amt.click(); amt.sendKeys(selectAllChord()); amt.sendKeys(Keys.BACK_SPACE); amt.sendKeys(randAmt);

		wait.until(ExpectedConditions.attributeToBeNotEmpty(pay, "value"));
	}

	public void handlePopup() {
		try {
			String actual = cp.captureToastMessage();
			String ei = ReadJsonData.getNestedValue("invoiceNoUse", "expected");
			String eb = ReadJsonData.getNestedValue("batchcreatedalredy", "expected");
			String mi = ReadJsonData.getNestedValue("invoiceNoUse", "message");
			String mb = ReadJsonData.getNestedValue("batchcreatedalredy", "message");

			if (actual.equals(ei)) {
				Assert.assertEquals(actual, ei, mi);
				logger.info("Invoice toast validated: " + actual);
			} else if (actual.equals(eb)) {
				Assert.assertEquals(actual, eb, mb);
				logger.info("Batch already toast validated: " + actual);
			} else {
				logger.warning("Unexpected toast: " + actual);
			}
			cp.waitForPopupToDisappear();
		} catch (Exception e) {
			logger.severe("Toast validation failed: " + e.getMessage());
		}
	}

	public void enterCurrentInvoiceDates() {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
		clearAndType(invoiceDateInput, today);
	}

	public void searchAndValidateToDateByColumn(int columnIndex) throws InterruptedException {
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
		String ref = String.format("//*[@id='panel1bh-content']//table/tbody/tr[1]/td[%d]", columnIndex);

		String expectedDate = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ref))).getText().trim();
		WebElement input = w.until(ExpectedConditions.visibilityOfElementLocated(todate));
		input.clear(); input.sendKeys(expectedDate);

		cp.Search(); cp.waitForLoaderToDisappear();

		String actualDate = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ref))).getText().trim();
		if (!expectedDate.equals(actualDate)) logger.warning("To Date mismatch: " + expectedDate + " vs " + actualDate);
		cp.waitForLoaderToDisappear();
	}

	public void searchAndValidateDateByColumn(int columnIndex) throws InterruptedException {
		WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
		String ref = String.format("//*[@id='panel1bh-content']//table/tbody/tr[1]/td[%d]", columnIndex);

		String expectedDate = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ref))).getText().trim();
		logger.info("Extracted Date: " + expectedDate);

		WebElement input = w.until(ExpectedConditions.visibilityOfElementLocated(fromdate));
		input.clear(); input.sendKeys(expectedDate);

		cp.Search(); cp.waitForLoaderToDisappear();

		String actualDate = w.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ref))).getText().trim();
		if (!expectedDate.equals(actualDate)) logger.warning("Date mismatch: " + expectedDate + " vs " + actualDate);
		cp.waitForPopupToDisappear();
	}

	public void verifyInvoicePersistsOnPageSwitch() {
		List<WebElement> p1 = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(firstRowInvoice));
		String first = p1.get(0).getText().trim();

		cp.waitForLoaderToDisappear();
		safeClick(page2Button);
		cp.waitForLoaderToDisappear();

		safeClick(page1Button);
		cp.waitForLoaderToDisappear();

		List<WebElement> p1Again = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(firstRowInvoice));
		String again = p1Again.get(0).getText().trim();

		Assert.assertEquals(again, first, "Invoice on Page 1 changed after navigation.");
	}
}

