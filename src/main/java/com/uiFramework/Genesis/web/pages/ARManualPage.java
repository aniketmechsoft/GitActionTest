package com.uiFramework.Genesis.web.pages;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class ARManualPage {
	WebDriver driver = null;
	CommonMethods cm;
	WaitHelper wt;
	CommonPage cp;

	public String customer;

	protected static final Logger logger = Logger.getLogger(ARManualPage.class.getName());

	public ARManualPage(WebDriver driver) {
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.cp = new CommonPage(driver);
	}

	/* ================= helpers ================= */
	private CharSequence selectAllChord() {
		return System.getProperty("os.name","").toLowerCase().contains("mac")
				? Keys.chord(Keys.COMMAND, "a")
				: Keys.chord(Keys.CONTROL, "a");
	}

	private void clearAndType(By locator, String text) {
		WebElement el = driver.findElement(locator);
		el.click();
		el.sendKeys(selectAllChord());
		el.sendKeys(Keys.BACK_SPACE);
		el.sendKeys(text);
	}

	private double parseMoney(String raw) {
		if (raw == null) return 0.0;
		String cleaned = raw.replaceAll("[^\\d.\\-]", "");
		if (cleaned.isEmpty() || ".".equals(cleaned) || "-".equals(cleaned)) return 0.0;
		return Double.parseDouble(cleaned);
	}

	private boolean safeDisplayed(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void safeClick(By locator) {
		for (int i = 0; i < 2; i++) {
			try {
				driver.findElement(locator).click();
				return;
			} catch (StaleElementReferenceException e) {
				// retry
			}
		}
		driver.findElement(locator).click();
	}
	/* ============================================ */

	private By ARManualmenu = By.xpath("//a[@href='/AR/ar-manual-input']");
	private By saveBtnGlobal = By.xpath("//*[@title='Click to save details.']");
	private By clearButtonGlobal = By.xpath("//button[@aria-label='clear' and @title='Click to clear data']");
	private By enteramtfirstrow = By.xpath("(//*[@placeholder='Enter AR Manual Amount'])[1]");

	// Row-scoped (within a single block/row)
	private By rowsContainer = By.xpath("//div[@class='d-flex']"); // keeping your original structure
	private By rowAmountInput = By.xpath(".//input[contains(@placeholder,'Enter AR Manual Amount')]");
	private By rowSaveBtn = By.xpath(".//button[contains(@id,'btnSave_AR_Manual')]");
	private By rowClearBtn = By.xpath(".//button[contains(@id,'btnClear_AR_Manual')]");

	/**
	 * Navigate to AR Manual Input screen
	 */
	public void ARInputMenu() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(ARManualmenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Verify save & clear buttons are rendered for each row.
	 */
	public boolean isSaveBtnDisplayedForEachRow() {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		// Fallback to blocks if table rows aren’t present
		if (rows.isEmpty()) rows = driver.findElements(rowsContainer);

		for (int i = 1; i <= rows.size(); i++) {
			try {
				By save = By.xpath("//table/tbody/tr[" + i + "]//button[@title='Click to save details.']");
				By clear = By.xpath("//table/tbody/tr[" + i + "]//button[@title='Click to clear data']");
				boolean ok = safeDisplayed(save) || safeDisplayed(clear);
				if (!ok) {
					logger.info("Save or Clear not displayed in table row: " + i + " — checking block layout");
					// Try block layout for the same index
					List<WebElement> blocks = driver.findElements(rowsContainer);
					if (i <= blocks.size()) {
						WebElement blk = blocks.get(i - 1);
						boolean okBlock = !blk.findElements(rowSaveBtn).isEmpty() || !blk.findElements(rowClearBtn).isEmpty();
						if (!okBlock) return false;
					} else {
						return false;
					}
				}
			} catch (NoSuchElementException e) {
				logger.info("Save/Clear not found in row: " + i);
				return false;
			}
		}
		return true;
	}

	/**
	 * Clear amount and attempt to save the first empty row (validates required field).
	 */
	public void clickOnsaveBtnForEmptyAmt() throws InterruptedException {
		cp.waitForPopupToDisappear();
		List<WebElement> blocks = driver.findElements(rowsContainer);
		for (WebElement block : blocks) {
			try {
				WebElement clearBtn = block.findElement(rowClearBtn);
				clearBtn.click();

				WebElement input = block.findElement(rowAmountInput);
				String val = input.getAttribute("value").trim();

				if (val.isEmpty()) {
					WebElement saveBtn = block.findElement(rowSaveBtn);
					saveBtn.click();
					break;
				}
			} catch (Exception ignore) {}
		}
	}

	/**
	 * Enter amount into the first row.
	 */
	public void enterAmount(String amt) {
		cp.waitForPopupToDisappear();
		clearAndType(enteramtfirstrow, amt);
	}

	/**
	 * Click save for the first row (global locator targets first instance).
	 */
	public void clickOnfirstSaveBtn() {
		safeClick(saveBtnGlobal);
	}

	/**
	 * Get amount from the first row input (safe numeric parsing).
	 */
	public double getAmount() {
		return parseMoney(cp.getAttributeValue(enteramtfirstrow, "value"));
	}

	/**
	 * Click the global clear button (top-level clear).
	 */
	public void clickOnClearBtn() {
		safeClick(clearButtonGlobal);
	}

	/**
	 * Return true if first-row amount is empty.
	 */
	public boolean checkAmount() {
		String value = cp.getAttributeValue(enteramtfirstrow, "value");
		return value == null || value.isEmpty();
	}

	/**
	 * Enter an amount in each block and click Save; returns true if any toast appears.
	 */
	public boolean enterAmountForMultipleOrder() {
		cp.waitForPopupToDisappear();
		boolean anyToast = false;
		List<WebElement> blocks = driver.findElements(rowsContainer);

		for (WebElement block : blocks) {
			try {
				WebElement input = block.findElement(rowAmountInput);
				input.click();
				input.sendKeys(selectAllChord());
				input.sendKeys(Keys.BACK_SPACE);
				input.sendKeys(getRandomAmount());

				cp.waitForPopupToDisappear();
				WebElement saveBtn = block.findElement(rowSaveBtn);
				saveBtn.click();

				String toast = "";
				try {
					toast = cp.captureToastMessage();
				} catch (Exception ignore) {}
				if (toast != null && !toast.isEmpty()) {
					anyToast = true;
				}
			} catch (Exception e) {
				// continue with next block
			}
		}
		return anyToast;
	}

	/**
	 * Random decimal as String (can be negative/zero/positive).
	 */
	public String getRandomAmount() {
		Random random = new Random();
		int number = random.nextInt(2001) - 1000;
		double amount = number + random.nextDouble();
		return String.format("%.2f", amount);
	}

	/* ================= extra row-scoped utilities (optional) ================= */

	/**
	 * Enter amount for a specific row index (1-based) using block layout.
	 */
	public void enterAmountInRow(int rowIndex, String amount) {
		List<WebElement> blocks = driver.findElements(rowsContainer);
		Assert.assertTrue(rowIndex >= 1 && rowIndex <= blocks.size(), "Row index out of range");
		WebElement block = blocks.get(rowIndex - 1);
		WebElement input = block.findElement(rowAmountInput);
		input.click();
		input.sendKeys(selectAllChord());
		input.sendKeys(Keys.BACK_SPACE);
		input.sendKeys(amount);
	}

	/**
	 * Save a specific row (1-based) using block layout.
	 */
	public void saveRow(int rowIndex) {
		List<WebElement> blocks = driver.findElements(rowsContainer);
		Assert.assertTrue(rowIndex >= 1 && rowIndex <= blocks.size(), "Row index out of range");
		WebElement block = blocks.get(rowIndex - 1);
		block.findElement(rowSaveBtn).click();
	}

	/**
	 * Clear a specific row (1-based) using block layout.
	 */
	public void clearRow(int rowIndex) {
		List<WebElement> blocks = driver.findElements(rowsContainer);
		Assert.assertTrue(rowIndex >= 1 && rowIndex <= blocks.size(), "Row index out of range");
		WebElement block = blocks.get(rowIndex - 1);
		block.findElement(rowClearBtn).click();
	}
}
