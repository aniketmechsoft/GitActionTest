package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class ConsigneeQA {
	WebDriver driver = null;
	CommonMethods cm;
	WaitHelper wt;
	CommonPage cp;

	public ConsigneeQA(WebDriver driver) {
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.cp = new CommonPage(driver);
	}

	/* ------------------------- small resilient helpers ------------------------- */
	private WebElement waitVisible(By locator, int sec) {
		return new WebDriverWait(driver, Duration.ofSeconds(sec))
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	private WebElement waitClickable(By locator, int sec) {
		return new WebDriverWait(driver, Duration.ofSeconds(sec))
				.until(ExpectedConditions.elementToBeClickable(locator));
	}
	private void jsClick(WebElement el) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
	}
	private String safeText(By locator) {
		try { return driver.findElement(locator).getText(); } catch (Exception e) { return ""; }
	}
	private String safeAttr(By locator, String attr) {
		try { return driver.findElement(locator).getAttribute(attr); } catch (Exception e) { return ""; }
	}
	private int parseIntSafe(String s) {
		try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
	}
	/** Parse the integer AFTER a slash in strings like "5/12". Falls back to digits-only. */
	private int parseAfterSlash(String raw) {
		if (raw == null) return 0;
		String t = raw.trim();
		if (t.contains("/")) {
			String tail = t.substring(t.lastIndexOf('/') + 1).replaceAll("[^0-9-]", "").trim();
			return parseIntSafe(tail);
		}
		return parseIntSafe(t.replaceAll("[^0-9-]", ""));
	}
	/** Attempts to collect dropdown options from PrimeNG-like overlays, with fallbacks. */
	private List<WebElement> getOverlayOptions() {
		List<By> candidates = List.of(
			By.xpath("/html/body/div[4]/div/ul/li[position() >= 2]"),
			By.xpath("//div[contains(@class,'p-dropdown-panel')]//li[not(contains(@class,'p-disabled'))]"),
			By.xpath("//div[@role='listbox']//li[not(contains(@class,'p-disabled'))]")
		);
		for (By b : candidates) {
			List<WebElement> found = driver.findElements(b);
			if (!found.isEmpty()) return found;
		}
		return new ArrayList<>();
	}
	/* -------------------------------------------------------------------------- */

	private By docMenu = By.xpath("//i[@title='Documents']");
	private By QAMenu = By.xpath("//*[@id='root']/div[3]/div[1]/div/div/ul/li[8]/div");
	private By consigneeQAMenu = By.xpath("//*[@id='root']/div[3]/div[1]/div/div/ul/div/div/div/ul[3]/a/li");
	private By lbolDrpDwn = By.xpath("//*[@id='consigneeQaLBOL']");
	private By status = By.xpath("//*[@id='consigneeQastatus']");
	private By DrpDwnClose = By.xpath("//*[@id='genesis']/div[4]/div[1]/button");
	private By delSignByErrMsg = By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[4]/div/div/div/div");
	private By delDateByErrMsg = By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[3]/div/div/div[2]/div");
	private By edtIcon = By.xpath("//*[@id='consigneeQaEditBtn']");
	private By textOnPopup = By.xpath("//*[@id='consigneeQaUpdatenotes']");
	private By markQty = By.id("consigneeQaMarkOrderQtyAsReceivedQtyBtn");
	private By saveVal = By.id("consigneeQaSaveDetailsBtn");
	private By lbol = By.xpath("//*[@id='consigneeQaLBOL']");
	private By scheduleDate = By.xpath("//*[@id='consigneeQaschedDeliveryDateTime']/button");
	private By scheduleDelievery = By.xpath("//*[@id='consigneeQadeliveryDateTime']/button");
	private By dateSel = By.xpath("//td[@data-p-today='true']");
	private By delSignBy = By.xpath("//*[@id='consigneeQadeliverySignedBy']");
	private By completed = By.xpath("//span[contains(text(),'Completed')]");
	private By recPieces = By.xpath("//*[@placeholder='Received Pieces']");
	private By ele = By.xpath("/html/body/div[4]/div/ul/li[2]");
	private By DrpDwnVal1 = By.xpath("/html/body/div[4]/div/ul/li/div/input");

	/**
	 * Navigates to the Consignee QA Menu.
	 */
	public void consigneeQAMenu() throws TimeoutException, InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(docMenu, 10);
		wt.waitToClick(QAMenu, 10);
		Thread.sleep(500);
		wt.waitToClick(consigneeQAMenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Tries LBOL options until delivery datetime field becomes enabled.
	 */
	public void LBOLSelOfTrkingUpdatedOrder() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement dropdownButton = waitClickable(By.id("consigneeQaLBOL"), 10);
		List<WebElement> options;

		for (int attempt = 0; attempt < 30; attempt++) { // safety cap
			dropdownButton.click();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//body")));
			Thread.sleep(200);

			options = getOverlayOptions();
			if (options.isEmpty()) {
				Thread.sleep(200);
				continue;
			}

			for (int i = 0; i < options.size(); i++) {
				if (i > 0) {
					dropdownButton.click();
					Thread.sleep(150);
					options = getOverlayOptions();
					if (options.size() <= i) break;
				}
				WebElement option = options.get(i);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
				Thread.sleep(150);
				jsClick(option);

				WebElement searchBtn = waitClickable(By.id("consigneeQaSearchFilterBtn"), 5);
				searchBtn.click();
				Thread.sleep(400);

				WebElement targetElement = driver.findElement(By.xpath("//*[@id='consigneeQadeliveryDateTime']/input"));
				if (targetElement.isEnabled()) return; // success
			}
		}
	}

	/**
	 * Updates extra pieces on first row (adds num).
	 */
	public void RecOrdExtraPiecesEnter(int num) {
		try {
			List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
			if (rows.isEmpty()) return;

			int i = 1; // first row
			WebElement sourceSpan = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[4]/div/span[2]"));
			int base = parseAfterSlash(sourceSpan.getText());
			int incrementedValue = base + num;

			WebElement inputField = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[4]/div/span/input"));
			inputField.click();
			inputField.clear();
			inputField.sendKeys(String.valueOf(incrementedValue));
		} catch (Exception ignored) {}
	}

	/**
	 * Updates extra literature pieces on first row (adds num).
	 */
	public void RecOrdExtraLitrEntr(int num) {
		try {
			List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
			if (rows.isEmpty()) return;

			int i = 1;
			WebElement sourceSpan = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[5]/div/span[2]"));
			int base = parseAfterSlash(sourceSpan.getText());
			int incrementedValue = base + num;

			WebElement inputField = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[5]/div/span/input"));
			inputField.click();
			inputField.clear();
			inputField.sendKeys(String.valueOf(incrementedValue));
		} catch (Exception ignored) {}
	}

	public String getDelSignByErrMsg() { return safeText(delSignByErrMsg); }
	public String getDelDateByErrMsg() { return safeText(delDateByErrMsg); }

	public String addEditNote(String Val) {
		wt.waitToClick(edtIcon, 10);
		cp.clickAndSendKeys(textOnPopup, Val);
		cp.save();
		return Val;
	}

	/**
	 * Uploads a demo file through the hidden file input.
	 */
	public void upload_file() throws InterruptedException {
		Thread.sleep(500);
		List<WebElement> inputs = driver.findElements(By.cssSelector("input[type='file']"));
		if (inputs.isEmpty()) return;
		WebElement hiddenInput = inputs.get(0);
		((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", hiddenInput);
		hiddenInput.sendKeys(System.getProperty("user.dir") + "\\src\\main\\resources\\attachments\\demo.txt");
		cp.waitForLoaderToDisappear();
	}

	public int GetRecVal() {
		String rawText = safeText(By.xpath("//table/tbody/tr[1]/td[5]/div/span[2]"));
		return parseAfterSlash(rawText);
	}

	public int GetRecEnteredVal() {
		String raw = safeAttr(By.xpath("//table/tbody/tr[1]/td[5]/div/span/input"), "value");
		return parseAfterSlash(raw);
	}

	public int GetOrdVal() {
		String rawText = safeText(By.xpath("//table/tbody/tr[1]/td[4]/div/span[2]"));
		return parseAfterSlash(rawText);
	}

	public int GetEnteredOrdVal() {
		String raw = safeAttr(By.xpath("//table/tbody/tr[1]/td[4]/div/span/input"), "value");
		return parseAfterSlash(raw);
	}

	public void markSameQty() {
		cp.waitForLoaderToDisappear();
		wt.waitToClickwtException(markQty, 10);
		cp.waitForLoaderToDisappear();
	}

	public void ClickSaveValBtn() throws InterruptedException {
		Thread.sleep(250);
		cp.waitForLoaderToDisappear();
		wt.waitToClickwtException(saveVal, 10);
		cp.waitForLoaderToDisappear();
	}

	By markAsCompBtn = By.xpath("(//*[@id='consigneeQaSaveDetailsBtn'])[2]");

	public void MarkAsCompletedBtn() throws InterruptedException {
		Thread.sleep(250);
		cp.waitForLoaderToDisappear();
		wt.waitToClickwtException(markAsCompBtn, 10);
		cp.waitForLoaderToDisappear();
	}

	public void scheduleDateSelection() throws InterruptedException {
		wt.waitToClick(scheduleDate, 10);
		Thread.sleep(200);
		wt.waitToClick(dateSel, 10);
	}

	public void scheduleDelieverySelection() throws InterruptedException {
		Thread.sleep(200);
		wt.waitToClick(scheduleDelievery, 10);
		Thread.sleep(200);
		wt.waitToClick(dateSel, 10);
	}

	public void enterSignBy(String name) {
		cp.clickAndSendKeys(delSignBy, name);
	}

	public String getLBOLNo() {
		String value = safeAttr(lbol, "value");
		if (value == null) return "";
		String[] parts = value.trim().split("\\s+");
		return parts.length >= 3 ? parts[2] : value.trim();
	}

	/**
	 * Select LBOL via dropdown filter (kept your flow but with sturdier waits).
	 */
	public void selectLBOL(String val) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		Thread.sleep(250);
		cp.clickElement(lbolDrpDwn);
		cp.waitForLoaderToDisappear();

		// type into overlay input (fallback to any visible filter input in overlay)
		if (driver.findElements(DrpDwnVal1).isEmpty()) {
			List<WebElement> filters = driver.findElements(By.xpath("//div[contains(@class,'p-dropdown-panel')]//input"));
			if (!filters.isEmpty()) {
				filters.get(0).sendKeys(val);
			}
		} else {
			cp.clickAndSendKeys(DrpDwnVal1, val);
		}
		cp.waitForLoaderToDisappear();

		// click first matching option
		List<WebElement> options = getOverlayOptions();
		if (!options.isEmpty()) jsClick(options.get(0));

		cp.waitForLoaderToDisappear();

		// close overlay cleanly
		try { cp.clickElement(lbolDrpDwn); } catch (Exception ignored) {}
		try { jsClick(waitVisible(ele, 2)); } catch (Exception ignored) {}
	}

	public void selStatus() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(status, 10);
		cp.waitForLoaderToDisappear();
		wt.waitToClick(completed, 5);
		cp.waitForLoaderToDisappear();
		wt.waitToClick(DrpDwnClose, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Expects: Received Pieces disabled AND Mark as Received disabled (post-QA).
	 */
	public boolean verifyMarkAsRecBtnAndRecPiecesField() {
		WebElement receivedPieces = driver.findElement(recPieces);
		WebElement markReceivedBtn = driver.findElement(markQty);

		boolean isReceivedPiecesDisabled = !receivedPieces.isEnabled();
		boolean isMarkReceivedDisabled = !markReceivedBtn.isEnabled();

		return isReceivedPiecesDisabled && isMarkReceivedDisabled;
	}

	/**
	 * Download should be disabled while Mark Received remains enabled (pre-QA).
	 */
	public boolean isDownloadDisabledAndMarkReceivedEnabled() {
		try {
			WebElement downloadBtn = driver.findElement(By.id("consigneeQaDownlaodFileBtn"));
			WebElement markReceivedBtn = driver.findElement(By.id("consigneeQaMarkOrderQtyAsReceivedQtyBtn"));
			return !downloadBtn.isEnabled() && markReceivedBtn.isEnabled();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
