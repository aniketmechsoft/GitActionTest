package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

	private By filter = By.xpath("(//*[@placeholder='Contains...'])[2]/following::span[1]");
	private By not = By.xpath("//li[contains(text(), 'Not Contains')]");
	private By regularChkbx = By.xpath("//*[@id='panel1bh-content']//div[2]/div/div[1]/label");
	private By recoChkbx = By.xpath("//*[@id='panel1bh-content']//div[2]/div/div[2]/label");
	private By CloseutChkbx = By.xpath("//*[@id='panel1bh-content']//div[2]/div/div[3]/label");

	private By lda = By.xpath(".//td[8]");//change  10 to 8 /for grid changes
	private By consignee = By.xpath("./td[5]");//change  7 to 5

	private By searchContain = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By searchContain1 = By.xpath("(//input[@placeholder='Contains...'])[2]");
	private By Norecord = By.xpath("//tr[contains(@class,'p-datatable-emptymessage')]");
	private By viewpod = By.xpath("//*[@title='Click to view split POD Details']");
	

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
			ManifestNo = safeCellText(2, 2);
			MLBOL     = safeCellText(3, 2);
			ldaa      = safeCellText(3, 6);

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
	 * @throws InterruptedException 
	 */
	private void searchFilterData(String filtrVal) throws InterruptedException {
		WebElement filterInput = driver.findElement(
				By.xpath("//*[@class='scrollable-pane']//input[@type='text']"));
		filterInput.clear();
		filterInput.sendKeys(filtrVal);
		Thread.sleep(2100);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Opens filter operator and chooses "Not contains".
	 * @throws InterruptedException 
	 */
	public void setFiltrNotCntains() throws InterruptedException {
		wt.waitToClick(filter, 10);
		wt.waitToClick(not, 10);
		Thread.sleep(2000);
		cp.waitForLoaderToDisappear();
	}

	/**
	 * Negative filter check for Regular and toggle checkbox.
	 */
	public void checkRegularCheckboxBehaviour(String filtrVal) throws InterruptedException {
		Thread.sleep(300);
		setFiltrNotCntains();
		Thread.sleep(2000);
		cp.waitForLoaderToDisappear();
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
				By.xpath("//*[@id='panel1bh-content']//table/tbody/tr[2]//td"));
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
	   public int ExtrNoCntFrmGenertrPge(WebDriver driver) {
	        cp.waitForLoaderToDisappear();
	        cp.waitForPopupToDisappear();

	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	        String text = wait.until(ExpectedConditions.refreshed(
	                ExpectedConditions.visibilityOfElementLocated(
	                        By.xpath("(//*[@class='v-sected spanTag'])[1]")
	                )
	        )).getText();

	        String countStr = text.replaceAll("\\D+", "");
	        log.info("Total count: " + countStr);

	        return Integer.parseInt(countStr);
	    }

	/* ---------------- Multi-select helpers ---------------- */

	/**
	 * Internal utility to find two rows with different values in the provided cell locator,
	 * then click their selection cells.
	 * @throws InterruptedException 
	 */
	private void createLDAForDiffConsignees(int colIndex) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.max_pagination();
		cp.waitForLoaderToDisappear();

		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
		String firstVal = null, secondVal = null;
		WebElement firstCb = null, secondCb = null;
		System.out.println("row size "+rows.size());
		for (int i = 2; i < rows.size(); i++) {
		  //  try {
		    	Thread.sleep(500);
		        String value = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]//td[" + colIndex + "]")).getText().trim();
		        System.out.println("value up " + value);
		        
		        // index-based td access (faster & stable)
		        WebElement selectCell = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]//td[1]"));

		        if (firstVal == null) {
		            firstVal = value;
		            firstCb = selectCell;
		            System.out.println("firstVal " + firstVal);
		            System.out.println("value " + value);
		        } else if (!value.equals(firstVal)) {
		            secondVal = value;
		            secondCb = selectCell;
		            System.out.println("Else secondVal " + secondVal);
		            System.out.println("Else firstVal " + firstVal);
		            break;
		        }

		 //  } catch (StaleElementReferenceException | NoSuchElementException ignored) {
		    }

		if (firstCb != null && secondCb != null) {
			log.info("Selecting two rows with different values: '" + firstVal + "' vs '" + secondVal + "'");
			firstCb.click();
			secondCb.click();
		} else {
			Assert.fail("not find two rows with different text ");
		}
	}
//}

	/**
	 * Select two rows with different consignees.
	 * @throws InterruptedException 
	 */
	public void createLDAForDiffConsignee() throws InterruptedException {
		createLDAForDiffConsignees(5);//for consingee
	}

	/**
	 * Select two rows with different LDAs.
	 * @throws InterruptedException 
	 */
	public void createManifestForDiffLDA() throws InterruptedException {
		createLDAForDiffConsignees(8);//lda
	}

	/**
	 * For checking ability to merge different order types sharing same LTL/LDA value.
	 * @throws InterruptedException 
	 */
	public void checkDiffTypeOrderMerge() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.max_pagination();
		cp.waitForLoaderToDisappear();

		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
		String firstProc = null, secondProc = null, anchorVal = null;
		WebElement firstCb = null, secondCb = null;

		for (int i = 2; i < rows.size(); i++) {
			try {
				String ordProc = driver.findElement(By.xpath("//*[@class='scrollable-pane']//tr[" + i +"]//td[1]")).getText().trim();
				String ldaVal  = driver.findElement(By.xpath(".//*[@class='scrollable-pane']//tr[" + i +"]//td[8]")).getText().trim();
				WebElement selectCell = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]//td[1]"));

				if (firstProc == null && anchorVal == null) {
					firstProc = ordProc;
					anchorVal = ldaVal;
					firstCb = selectCell;
					System.out.println("FistOrdprc "+firstProc);
					System.out.println("ldaVal "+anchorVal);
				} else if (!ordProc.equals(firstProc) && ldaVal.equals(anchorVal)) {
					secondProc = ordProc;
					secondCb = selectCell;
					System.out.println("secondProc "+secondProc);
					System.out.println("ldaval "+ldaVal);
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

	/**
	 * For checking ability to merge different order types sharing Different LTL/LDA value.
	 * @throws InterruptedException 
	 */
	public void checkDiffTypeOrderMergeDiffLDA() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.max_pagination();
		cp.waitForLoaderToDisappear();

		List<WebElement> rows = driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
		String firstProc = null, secondProc = null, anchorVal = null;
		WebElement firstCb = null, secondCb = null;

		for (int i = 2; i < rows.size(); i++) {
			try {
				String ordProc = driver.findElement(By.xpath("//*[@class='scrollable-pane']//tr[" + i +"]//td[1]")).getText().trim();
				String ldaVal  = driver.findElement(By.xpath(".//*[@class='scrollable-pane']//tr[" + i +"]//td[8]")).getText().trim();
				WebElement selectCell = driver.findElement(By.xpath("//table/tbody/tr[" + i + "]//td[1]"));

				if (firstProc == null && anchorVal == null) {
					firstProc = ordProc;
					anchorVal = ldaVal;
					firstCb = selectCell;
					System.out.println("FistOrdprc "+firstProc);
					System.out.println("ldaVal "+anchorVal);
				} else if (!ordProc.equals(firstProc) && !ldaVal.equals(anchorVal)) {
					secondProc = ordProc;
					secondCb = selectCell;
					System.out.println("secondProc "+secondProc);
					System.out.println("ldaval "+ldaVal);
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
		cp.waitForLoaderToDisappear();
	}

	public void clickReconsinmntBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(recoChkbx, 10);
		cp.waitForLoaderToDisappear();
	}

	public void clickCloseoutBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(CloseutChkbx, 10);
		cp.waitForLoaderToDisappear();
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
	
	/**
	 * This method is used to view pod
	 */
	public void clickToViewPod() {
		wt.waitToClick(viewpod, 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * Total record count in Generated Documents page.
	 */
	public int ExtrNoCntFrmSplitPODPge() {
		cp.waitForLoaderToDisappear();
		WebElement entryText = driver.findElement(
				By.xpath("//*[@class='p-paginator-top p-paginator p-component']//span[1]"));
		int initialCount = extractCountFromText(entryText.getText().trim());
		System.out.println("count: " + initialCount);
		return initialCount;
	}
	
	/**
	 * Scrolls through and validates grid data dynamically based on the grid type.
	 * Works for fix grid pattern
	 *
	 * @throws InterruptedException if thread sleep is interrupted.
	 */
	//This method for Remaining for optimization pages
	public void verifyColumnFilterForSplitePOD() throws InterruptedException {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    
	    List<WebElement> gridCells = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
	            By.xpath("//*[@id='panel1bh-content']//tbody//tr[2]//td")
	        ));
	    
	    int columnCount = gridCells.size();

	    for (int i = 1; i <= columnCount; i++) {

	        // Always re-locate fresh to avoid stale
	        String cellXPath = "//*[@id='panel1bh-content']//tbody//tr[2]//td[" + i + "]";
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
	        cp.waitForLoaderToDisappear();

	        String filteredText = driver.findElement(By.xpath(cellXPath)).getText().trim();
	        System.out.println("After filter, cell[" + i + "] text: " + filteredText);

	        Assert.assertTrue(filteredText.contains(text),
	                "Column filter not matching — expected to contain: " + text + " but got: " + filteredText);
	    }
	}
	
	/**
	 * This method used for get data from filer to check filter should persist
	 * @return
	 */
	public boolean getFilterData() {
	    String value = driver.findElement(
	        By.xpath("//*[@placeholder='Contains...']")).getAttribute("value");
	    System.out.println("Filter data " +value);
	    return value != null && !value.trim().isEmpty();
	}

	
	


}
