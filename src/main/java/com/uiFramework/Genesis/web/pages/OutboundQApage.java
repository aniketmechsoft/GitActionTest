package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class OutboundQApage {
	WebDriver driver = null;
	CommonMethods cm;
	WaitHelper wt;
	CommonPage cp;
	int sumofPieces;
	int sumofWeight; // reserved if you add weight calc later

	private static final Logger logger = Logger.getLogger(OutboundQApage.class.getName());

	public OutboundQApage(WebDriver driver) {
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.cp = new CommonPage(driver);
	}

	private By tracking = By.xpath("//i[@title='Tracking & ETA']");
	private By QAMenu = By.xpath("//i[@title='QA']");
	private By outboundQAMenu = By.xpath("//a[@href='/qa/outbound-qa']");
	private By manifest = By.xpath("//*[@id='outboundQamanifest']");
	private By ldaname = By.xpath("(//*[@class='labelBoxK__labelData'])[2]");
	private By markQty = By.id("outboundQaMarkReceivedQtySameAsOrderQtyBtn");
	private By updatPiecesBtn = By.id("outboundQaUpdateReceivedPiecesBtn");
	private By recDate = By.xpath("//*[@id='outboundQareceivedDate']/button");
	private By voicemailDate = By.xpath("//*[@id='outboundQavoiceMailDate']/button");
	private By dateSel = By.xpath("//td[@data-p-today='true']");
	private By status = By.xpath("//*[@id='outboundQastatus']");
	private By DrpDwnVal = By.xpath("//*[@id='genesis']/div[4]/div[1]/div/input");
	private By DrpDwnClose = By.xpath("//*[@id='genesis']/div[4]/div[1]/button");
	private By completed = By.xpath("//span[contains(text(),'Completed')]");
	private By LDADrpDwn = By.xpath("//*[@id='outboundQaLDA']/span");
	private By searchfirstcoloum = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By ele = By.xpath("/html/body/div[4]/div/ul/li[2]");
	private By DrpDwnVal1 = By.xpath("/html/body/div[4]/div/ul/li/div/input");
	private By manifestDrpDwn = By.xpath("//*[@id='outboundQamanifest']");

	private By orders = By.xpath("//i[@title='Orders']");
	private By orderpage = By.xpath("//a[@href='/order']");

	/**
	 * Navigate to Outbound QA.
	 */
	public void outboundQAMenu() throws TimeoutException, InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(tracking, 10);
		Thread.sleep(1000);
		wt.waitToClick(QAMenu, 10);
		Thread.sleep(2000);
		wt.waitToClick(outboundQAMenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Auto-pick an LDA (walk options until manifest has enough items).
	 */
	public void selectLDA() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement dropdownButton = driver.findElement(LDADrpDwn);
		dropdownButton.click();

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='genesis']/div[5]/div[2]/ul/li")));
		List<WebElement> options = driver.findElements(By.xpath("//*[@id='genesis']/div[5]/div[2]/ul/li"));
		logger.info("Dropdown for LDA options: " + options.size());

		for (int i = 0; i < options.size(); i++) {
			if (i > 0) {
				dropdownButton.click();
				cp.waitForLoaderToDisappear();
				Thread.sleep(1000);
			}
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='genesis']/div[5]/div[2]/ul/li")));
			options = driver.findElements(By.xpath("//*[@id='genesis']/div[5]/div[2]/ul/li"));

			if (options.size() <= i) continue;

			WebElement option = options.get(i);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
			Thread.sleep(250);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
			cp.waitForLoaderToDisappear();

			WebElement manifestDropdown = driver.findElement(manifestDrpDwn);
			Thread.sleep(300);
			manifestDropdown.click();

			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[5]/div/ul/li[position() >= 2]")));
			List<WebElement> manifestOptions = driver.findElements(By.xpath("/html/body/div[5]/div/ul/li[position() >= 2]"));
			logger.info("Manifest options size for this LDA: " + manifestOptions.size());

			if (manifestOptions.size() > 5) {
				manifestDropdown.click(); // close
				break;
			} else {
				manifestDropdown.click(); // close and keep searching
			}
		}
	}

	/**
	 * Click OK on generic popup if present.
	 */
	public void HandlePopup() {
		List<WebElement> okBtns = driver.findElements(By.xpath("//*[@id='genesis']/div[2]/div[3]/div/div[2]/button"));
		if (!okBtns.isEmpty()) {
			try {
				okBtns.get(0).click();
			} catch (Exception ignored) {}
		}
	}

	/**
	 * Pick a Manifest; if "Tracking not updated" popup appears, try the next.
	 */
	public void selectManifest() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement dropdownButton = driver.findElement(manifestDrpDwn);
		dropdownButton.click();

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[5]/div/ul/li[position() >= 2]")));
		List<WebElement> options = driver.findElements(By.xpath("/html/body/div[5]/div/ul/li[position() >= 2]"));
		logger.info("Manifest options found: " + options.size());

		for (int i = 0; i < options.size(); i++) {
			if (i > 0) {
				dropdownButton.click();
				Thread.sleep(500);
			}

			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("/html/body/div[5]/div/ul/li[position() >= 2]")));
			options = driver.findElements(By.xpath("/html/body/div[5]/div/ul/li[position() >= 2]"));

			if (options.size() <= i) continue;

			WebElement option = options.get(i);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
			Thread.sleep(200);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);

			driver.findElement(By.xpath("//*[@id='outboundQaSearchFilterBtn']")).click();
			cp.waitForLoaderToDisappear();
			Thread.sleep(700);

			List<WebElement> okBtns = driver.findElements(By.xpath("//*[@id='genesis']/div[2]/div[3]/div/div[2]/button"));
			if (okBtns.isEmpty() || !okBtns.get(0).isDisplayed()) {
				// no blocking popup -> success
				break;
			} else {
				okBtns.get(0).click();
			}
		}
	}

	public String getManifestNo() {
		return cp.getMandatoryText(manifest);
	}

	public String getLDA() {
		return cp.getMandatoryText(ldaname);
	}

	/**
	 * Enter received order pieces > available (row 1).
	 */
	public void RecOrdExtraPiecesEnter(int num) {
		try {
			// only row 1 per your original behavior
			WebElement sourceSpan = driver.findElement(By.xpath("//table/tbody/tr[1]/td[7]/div/span[2]"));
			int base = parseIntSafe(sourceSpan.getText());
			WebElement input = driver.findElement(By.xpath("//table/tbody/tr[1]/td[7]/div/span/input"));
			input.click();
			input.clear();
			input.sendKeys(String.valueOf(base + num));
		} catch (Exception ignored) {}
	}

	/**
	 * Enter received literature pieces > available (row 1).
	 */
	public void RecOrdExtraLitrEntr(int num) {
		try {
			WebElement sourceSpan = driver.findElement(By.xpath("//table/tbody/tr[1]/td[8]/div/span[2]"));
			int base = parseIntSafe(sourceSpan.getText());
			WebElement input = driver.findElement(By.xpath("//table/tbody/tr[1]/td[8]/div/span/input"));
			input.click();
			input.clear();
			input.sendKeys(String.valueOf(base + num));
		} catch (Exception ignored) {}
	}

	public void markSameQty() {
		cp.waitForLoaderToDisappear();
		wt.waitToClickwtException(markQty, 10);
		cp.waitForLoaderToDisappear();
	}

	public int GetEnteredOrdVal() {
		String raw = driver.findElement(By.xpath("//table/tbody/tr[1]/td[7]/div/span/input")).getAttribute("value");
		return parseIntSafe(raw);
	}

	public int GetOrdVal() {
		String raw = driver.findElement(By.xpath("//table/tbody/tr[1]/td[7]/div/span[2]")).getText();
		return parseIntSafe(raw);
	}

	public int GetLitRecVal() {
		String raw = driver.findElement(By.xpath("//table/tbody/tr[1]/td[8]/div/span[2]")).getText();
		return parseIntSafe(raw);
	}

	public int GetLitRecEnteredVal() {
		String raw = driver.findElement(By.xpath("//table/tbody/tr[1]/td[8]/div/span/input")).getAttribute("value");
		return parseIntSafe(raw);
	}

	public int checkThatLitQtyDefaultZero() {
		int pieces = GetLitRecVal();
		if (pieces == 0) {
			return GetLitRecEnteredVal();
		} else {
			throw new SkipException("SKIPPED: Literature pieces not zero for index");
		}
	}

	public String getLDAfromDropdown() {
		// Often the selected label is inside a span label element; adapt as needed for your UI
		try {
			return cp.getMandatoryText(By.xpath("//*[@id='outboundQaLDA']//span[contains(@class,'p-dropdown-label')]"));
		} catch (Exception e) {
			// fallback to value attribute if it's an input
			return cp.getAttributeValue(LDADrpDwn, "value");
		}
	}

	public String getLDAfromGrid() {
		return cp.getMandatoryText(ldaname);
	}

	public String getManifestNofromDropdown() {
		String val = cp.getAttributeValue(manifestDrpDwn, "value");
		return val == null ? "" : val.replaceAll("\\D+", "");
	}

	public String getManifestfromGrid() {
		return cp.getMandatoryText(By.xpath("(//*[@class='labelBoxK__labelData'])[1]"));
	}

	public int getTotalPiecesfromGrid() {
		String text = cp.getMandatoryText(By.xpath("(//*[@class='labelBoxK__labelData'])[3]")).trim();
		return parseIntSafe(text);
	}

	/**
	 * Sum order + lit pieces across all rows (after Mark Same Qty).
	 */
	public int getSumofOrderPiecesNo() {
		sumofPieces = 0; // reset accumulator
		markSameQty();
		try {
			List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
			for (int i = 1; i <= rows.size(); i++) {
				String ordRaw = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[7]/div/span/input"))
						.getAttribute("value");
				sumofPieces += parseIntSafe(ordRaw);

				String litRaw = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]/td[8]/div/span/input"))
						.getAttribute("value");
				sumofPieces += parseIntSafe(litRaw);
			}
		} catch (Exception e) {
			logger.info("Failed while summing pieces: " + e.getMessage());
		}
		return sumofPieces;
	}

	public String getndClearRecDate() {
		markSameQty();
		WebElement receivedDate = driver.findElement(By.xpath("//input[@placeholder='Received Date']"));
		String recdate = receivedDate.getAttribute("value").split(" ")[0];
		receivedDate.clear();
		return recdate;
	}

	public void clickOnUpdateRecPiece() {
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		cp.moveToElementAndClick(updatPiecesBtn);
		cp.waitForLoaderToDisappear();
	}

	public void ReceivedDateSelection() throws InterruptedException {
		wt.waitToClick(recDate, 10);
		Thread.sleep(300);
		wt.waitToClick(dateSel, 10);
	}

	public String getndClearVoiceMailDate() {
		markSameQty();
		WebElement voiceMDate = driver.findElement(By.xpath("//*[@id='outboundQavoiceMailDate']/input"));
		String val = voiceMDate.getAttribute("value").split(" ")[0];
		voiceMDate.clear();
		return val;
	}

	public void voiceMailDateSelection() throws InterruptedException {
		wt.waitToClick(voicemailDate, 10);
		Thread.sleep(300);
		wt.waitToClick(dateSel, 10);
		wt.waitToClick(voicemailDate, 10);
	}

	public String getReceivedDate() {
		return cp.getAttributeValue(By.xpath("//*[@id='outboundQareceivedDate']/input"), "value").split(" ")[0];
	}

	public String getVoicemailDate() {
		return cp.getAttributeValue(By.xpath("//*[@id='outboundQavoiceMailDate']/input"), "value").split(" ")[0];
	}

	public void selStatus() {
		cp.waitForPopupToDisappear();
		wt.waitToClick(status, 10);
		cp.waitForLoaderToDisappear();
		wt.waitToClick(completed, 5);
		wt.waitToClick(DrpDwnClose, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Select manifest by search term (LBOL) from dropdown.
	 */
	public void selectLBOL(String val) throws InterruptedException {
		Thread.sleep(500);
		cp.clickElement(manifestDrpDwn);
		cp.clickAndSendKeys(DrpDwnVal1, val);
		cp.waitForLoaderToDisappear();
		cp.clickElement(ele); // pick first matching
		cp.waitForLoaderToDisappear();
	}

	public void selectLDA(String val) throws InterruptedException {
		Thread.sleep(500);
		cp.clickElement(LDADrpDwn);
		cp.DrpDwnValueSel(DrpDwnVal, val);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Expose update button (to assert disabled/enabled state).
	 */
	public WebElement verifyUpdatePiecsBtnDisable() {
		return driver.findElement(updatPiecesBtn);
	}

	// listing-cell locator for Order Status
	By getOrderStatus = By.xpath("//table/tbody/tr[1]/td[19]");

	public String getOrderStatus(String order) {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchfirstcoloum, order);
		return cp.getMandatoryText(getOrderStatus);
	}

	public void orderpageMenulanding() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClickWithAction(orders, 30);
		Thread.sleep(1000);
		wt.waitToClick(orderpage, 10);
		cp.waitForLoaderToDisappear();
	}

	// -------- helpers --------

	private int parseIntSafe(String raw) {
		if (raw == null) return 0;
		try {
			return Integer.parseInt(raw.replaceAll("[^\\d]", "").trim());
		} catch (Exception e) {
			return 0;
		}
	}
}
