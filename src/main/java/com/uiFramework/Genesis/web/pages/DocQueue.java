package com.uiFramework.Genesis.web.pages;

import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class DocQueue {
	WebDriver driver = null;
	CommonMethods cm;
	WaitHelper wt;
	CommonPage cp;
	OrderPage op;

	public static String ManifestNo;
	public static String MLBOL;
	public static String ldaa;

	private static final Logger log = Logger.getLogger(DocQueue.class.getName());

	public DocQueue(WebDriver driver) {
		this.driver = driver;
		this.cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		this.cp = new CommonPage(driver);
		this.op = new OrderPage(driver);
	}

	/* ---------------- Locators ---------------- */
	private By docMenu = By.xpath("//i[@title='Documents']");
	private By docGenMenu = By.xpath("//a[@href='/documents/document-queue']");
	private By GenerateDocMenu = By.xpath("//a[@href='/documents/generated-documents']");
	private By createLDADoc = By.xpath("//*[@id='documentQueueCreateLDADocumentation']");

	private By filter = By.xpath("//*[@id='panel1bh-content']//thead/tr[2]/th[3]//button[1]");
	private By not = By.xpath("//li[contains(text(), 'Not contains')]");
	private By regularChkbx = By.xpath("//*[@id='panel1bh-content']//div[1]/div/div[1]/label");
	private By recoChkbx = By.xpath("//*[@id='panel1bh-content']//div[1]/div/div[2]/label");
	private By CloseutChkbx = By.xpath("//*[@id='panel1bh-content']//div[1]/div/div[3]/label");

	private By lda = By.xpath(".//td[10]");
	private By consignee = By.xpath(".//td[7]");

	private By searchContain = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By searchContain1 = By.xpath("(//input[@placeholder='Contains...'])[2]");
	private By Norecord = By.xpath("//tr[contains(@class,'p-datatable-emptymessage')]");

	/* ---------------- Navigation ---------------- */

	/**
	 * Opens Documents → Document Queue.
	 */
	public void docQueueMenu() throws TimeoutException, InterruptedException {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(docMenu, 10);
		Thread.sleep(500); // brief stabilization for nested menu
		wt.waitToClick(docGenMenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Clicks on the "Document Generation" menu.
	 */
	public void ClickDocGen() {
		wt.waitToClick(docGenMenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Opens Documents → Generated Documents.
	 */
	public void clickGeneratedDoc() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(GenerateDocMenu, 10);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Clicks on the "Create LDA Document" button.
	 */
	public void clickLDABtn() {
		wt.waitToClick(createLDADoc, 10);
	}

	/* ---------------- Searching ---------------- */

	/**
	 * Filters by order in Document Queue using OrderPage.MGLOrderno.
	 */
	public void searchOrder() {
		cp.waitForLoaderToDisappear();
		log.info("Searching in Doc Queue for order: " + op.MGLOrderno);
		cp.searchColoumFilter(searchContain, op.MGLOrderno);
	}

	/**
	 * Filters by order (explicit value) in Document Queue.
	 */
	public void searchOrder1(String ordNo) {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchContain, ordNo);
	}

	/**
	 * Filters by order in Generated Documents using OrderPage.MGLOrderno.
	 */
	public void searchOrderInGenScreen() {
		cp.waitForLoaderToDisappear();
		log.info("Searching in Generated Docs for order: " + op.MGLOrderno);
		cp.searchColoumFilter(searchContain1, op.MGLOrderno);
	}

	/**
	 * Filters by order (explicit value) in Generated Documents.
	 */
	public void searchOrderInGenScreen1(String Orderno) {
		cp.waitForLoaderToDisappear();
		cp.searchColoumFilter(searchContain1, Orderno);
	}

	/* ---------------- Actions / Helpers ---------------- */

	/**
	 * If the "no record" row is not present, we assume documents already exist for the order.
	 * Method kept for backward compatibility.
	 */
	public void checkAndSelectOrder() {
		try {
			// small nudge to force grid update in some UIs
			List<WebElement> inputs = driver.findElements(By.xpath("(//input[@aria-invalid='false'])"));
			if (inputs.size() >= 2) {
				inputs.get(1).click();
			}
			boolean noRec = checkElementNorecord();
			if (!noRec) {
				log.info("Document already generated for main order (no 'Record not found' row).");
			}
		} catch (Exception ignored) { }
	}

	/**
	 * Reads Manifest, LBOL, and LDAA fields from the top rows of the grid
	 * (defensive against missing rows).
	 */
	public void getDocumentNumber() {
		try {
			ManifestNo = safeCellText(1, 3);
			MLBOL     = safeCellText(2, 3);
			ldaa      = safeCellText(2, 7);

			log.info("Manifest: " + ManifestNo);
			log.info("LBOL: " + MLBOL);
			log.info("LDAA: " + ldaa);
		} catch (Exception e) {
			log.warning("Unable to read document numbers cleanly: " + e.getMessage());
		}
	}

	private String safeCellText(int row, int col) {
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		if (rows.size() >= row) {
			WebElement cell = driver.findElement(By.xpath("//table/tbody/tr[" + row + "]/td[" + col + "]"));
			return cell.getText().trim();
		}
		return "";
	}

	/**
	 * Sets the "Not contains" filter and types into the 3rd column filter box.
	 */
	private void searchFilterData(String filtrVal) {
		WebElement filterInput = driver.findElement(
				By.xpath("//*[@id='panel1bh-content']//thead/tr[2]/th[3]//div/input"));
		filterInput.clear();
		filterInput.sendKeys(filtrVal);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Opens filter operator and chooses "Not contains".
	 */
	public void setFiltrNotCntains() {
		wt.waitToClick(filter, 10);
		wt.waitToClick(not, 10);
	}

	/**
	 * Negative filter check for Regular and toggle checkbox.
	 */
	public void checkRegularCheckboxBehaviour(String filtrVal) throws InterruptedException {
		Thread.sleep(300);
		setFiltrNotCntains();
		searchFilterData(filtrVal);
		Thread.sleep(500);
		assertAtMostOneRow();
		clickRegulartBtn();
	}

	/**
	 * Negative filter check for Reconsignment and toggle checkbox.
	 */
	public void checkReconsignmentCheckboxBehaviour(String filtrVal) throws InterruptedException {
		clickReconsinmntBtn();
		Thread.sleep(300);
		setFiltrNotCntains();
		searchFilterData(filtrVal);
		Thread.sleep(500);
		assertAtMostOneRow();
		clickReconsinmntBtn();
	}

	/**
	 * Negative filter check for Closeout and toggle checkbox.
	 */
	public void checkCloseoutCheckboxBehaviour(String filtrVal) throws InterruptedException {
		clickCloseoutBtn();
		Thread.sleep(300);
		setFiltrNotCntains();
		searchFilterData(filtrVal);
		Thread.sleep(500);
		assertAtMostOneRow();
		clickCloseoutBtn();
	}

	private void assertAtMostOneRow() {
		List<WebElement> rows = driver.findElements(
				By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
		int rowCount = rows.size();
		Assert.assertTrue(rowCount <= 1, "Test Failed: More than 1 row found!");
	}

	/**
	 * Extracts "of N entries" → N
	 */
	private int extractCountFromText(String text) {
		Pattern pattern = Pattern.compile("of\\s+(\\d+)\\s+entries");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		throw new RuntimeException("Could not extract count from: " + text);
	}

	/**
	 * Total record count in Generated Documents page.
	 */
	public int ExtrNoCntFrmGenertrPge() {
		cp.waitForLoaderToDisappear();
		WebElement entryText = driver.findElement(
				By.xpath("//*[@id='panel1bh-content']//div[1]/span[1]"));
		int initialCount = extractCountFromText(entryText.getText().trim());
		System.out.println("count: " + initialCount);
		return initialCount;
	}

	/* ---------------- Multi-select helpers ---------------- */

	/**
	 * Internal utility to find two rows with different values in the provided cell locator,
	 * then click their selection cells.
	 */
	private void createLDAForDiffConsignees(By element) {
		cp.waitForLoaderToDisappear();
		cp.max_pagination();
		cp.waitForLoaderToDisappear();

		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
		String firstVal = null, secondVal = null;
		WebElement firstCb = null, secondCb = null;

		for (WebElement row : rows) {
			try {
				String value = row.findElement(element).getText().trim();
				WebElement selectCell = row.findElement(By.xpath(".//td[1]"));

				if (firstVal == null) {
					firstVal = value;
					firstCb = selectCell;
				} else if (!value.equals(firstVal)) {
					secondVal = value;
					secondCb = selectCell;
					break;
				}
			} catch (StaleElementReferenceException | NoSuchElementException ignored) { }
		}

		if (firstCb != null && secondCb != null) {
			log.info("Selecting two rows with different values: '" + firstVal + "' vs '" + secondVal + "'");
			firstCb.click();
			secondCb.click();
		} else {
			Assert.fail("not find two rows with different text ");
		}
	}

	/**
	 * Select two rows with different consignees.
	 */
	public void createLDAForDiffConsignee() {
		createLDAForDiffConsignees(consignee);
	}

	/**
	 * Select two rows with different LDAs.
	 */
	public void createManifestForDiffLDA() {
		createLDAForDiffConsignees(lda);
	}

	/**
	 * For checking ability to merge different order types sharing same LTL/LDA value.
	 */
	public void checkDiffTypeOrderMerge() {
		cp.waitForLoaderToDisappear();
		cp.max_pagination();
		cp.waitForLoaderToDisappear();

		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
		String firstProc = null, secondProc = null, anchorVal = null;
		WebElement firstCb = null, secondCb = null;

		for (WebElement row : rows) {
			try {
				String ordProc = row.findElement(By.xpath(".//td[3]")).getText().trim();
				String ltlVal  = row.findElement(By.xpath(".//td[10]")).getText().trim();
				WebElement selectCell = row.findElement(By.xpath(".//td[1]"));

				if (firstProc == null && anchorVal == null) {
					firstProc = ordProc;
					anchorVal = ltlVal;
					firstCb = selectCell;
				} else if (!ordProc.equals(firstProc) && ltlVal.equals(anchorVal)) {
					secondProc = ordProc;
					secondCb = selectCell;
					break;
				}
			} catch (StaleElementReferenceException | NoSuchElementException ignored) { }
		}

		if (firstCb != null && secondCb != null) {
			log.info("Selecting two rows with different order types: '" + firstProc + "' vs '" + secondProc + "', same LTL/LDA: " + anchorVal);
			firstCb.click();
			secondCb.click();
		} else {
			Assert.fail("not find two rows with different text ");
		}
	}

	/* ---------------- Checkboxes toggles ---------------- */

	private void clickRegulartBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(regularChkbx, 10);
	}

	public void clickReconsinmntBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(recoChkbx, 10);
	}

	public void clickCloseoutBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(CloseutChkbx, 10);
	}

	/* ---------------- Utilities ---------------- */

	/**
	 * Returns true if the "Record not found" row is visible.
	 */
	public boolean checkElementNorecord() {
		try {
			WebElement norecord = driver.findElement(Norecord);
			return norecord.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
