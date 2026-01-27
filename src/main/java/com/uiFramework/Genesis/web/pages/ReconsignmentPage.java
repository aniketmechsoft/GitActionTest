package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;

public class ReconsignmentPage {
    WebDriver driver = null;
    CommonPage cp;
    DropDownHelper DrpDwn;
    WaitHelper wt;
    JavaScriptHelper js;
    private WebDriverWait wait;

    public ReconsignmentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.cp = new CommonPage(driver);
        this.wt = new WaitHelper(driver);
        this.DrpDwn = new DropDownHelper(driver);
        this.js = new JavaScriptHelper(driver);
    }

    private By reconsignMenu = By.xpath("//i[@title='Reconsignment']");
    private By reconsign = By.xpath("//a[@href='/reconsignment']");
    private By addNewReconsignmentBtn = By.id("reconsginmentlistAddNewReconsignmentBtn");
    private By ordLookup = By.id("reconsignmentOrderDetbtnOrderLookup");
    private By ordNoonLookUp = By.id("reconsignmentOrderDetlblOrderNoPlaceholder");

    private By recnsignConsigneeDrpDwn = By.id("masterExternalUserDetreconsignedToConsignee");
    private By statusFiltr = By.xpath("//table/thead/tr[2]/th[15]/div/input");

    // shared dropdown search box
    private By listOption2 = By.xpath("/html/body/div[4]/div/ul/li[2]");
    private By DrpDwnVal1 = By.xpath("//*[@placeholder='Search...']");

    private static final int DEFAULT_TIMEOUT = 30;

    /** Row helper: scroll into view safely */
    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    /** Returns the table rows inside the expanded panel */
    private List<WebElement> tableRows() {
        return driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
    }

    /** Get first row’s GL-* value where LBO L (td[10]) is non-empty */
    private void getStatusValue(int arrLen, List<String> firstUniqueText) {
        List<WebElement> rows = tableRows();
        if (rows.isEmpty()) return;

        for (WebElement row : rows) {
            scrollIntoView(row);
            // td[10] within THIS row only
            WebElement lbolCell = row.findElement(By.xpath("//td[10]"));
            String lbolVal = lbolCell.getText().trim();
            if (!lbolVal.isEmpty()) {
                // GL-* cell within THIS row only (often 4th col, but use starts-with)
                String gl = row.findElement(By.xpath("//td[4]"))
                               .getText().trim();
                if (arrLen < firstUniqueText.size()) {
                    firstUniqueText.set(arrLen, gl);
                } else {
                    firstUniqueText.add(gl);
                }
                return;
            }
        }
    }

    public void getOrdersData(int arrLen, String ordStatus, List<String> firstUniqueText) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        ordStatusFiltr(ordStatus);
        getStatusValue(arrLen, firstUniqueText);
        cp.waitForLoaderToDisappear();
    }

    public void reconsignMenuWithExpand() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(reconsignMenu));
        js.executeScript("arguments[0].click();", menu);

        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(reconsign));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", link);
        wait.until(ExpectedConditions.elementToBeClickable(reconsign)).click();
        cp.waitForLoaderToDisappear();
    }

    public void addNewReconsignOrder() throws InterruptedException {
        cp.waitForPopupToDisappear();
        cp.waitForLoaderToDisappear();
        wt.waitToClick(addNewReconsignmentBtn, DEFAULT_TIMEOUT);
    }

    public void clickOrderLookup() {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(ordLookup, DEFAULT_TIMEOUT);
    }

    public void fetchLookupOrder(String ordNo) {
        cp.clickAndSendKeys(ordNoonLookUp, ordNo);
        cp.waitForLoaderToDisappear();
        cp.Search();
    }

    /** Type into the reconsign-to-consignee dropdown search */
    public void reconsignToConsigneeValidation(String consignee) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        cp.clickElement(recnsignConsigneeDrpDwn);
        cp.clickAndSendKeys(DrpDwnVal1, consignee);
        cp.waitForLoaderToDisappear();
    }

    public String getOriginalConsignee() {
        // Prefer reading from an input's value when available
        return cp.getMandatoryText(By.xpath("(//span[@class='p-dropdown-label p-inputtext'])[4]"));
    }

    public void selectLBOL(String consignee) throws InterruptedException {
        // same flow as validation: type to filter, then pick 2nd (example) if needed
        cp.waitForLoaderToDisappear();
        cp.clickElement(recnsignConsigneeDrpDwn);
        cp.clickAndSendKeys(DrpDwnVal1, consignee);
        cp.waitForLoaderToDisappear();
        // optionally pick the second item if that’s desired behavior
        try { wt.waitToClick(listOption2, 3); } catch (Exception ignored) {}
        cp.waitForLoaderToDisappear();
    }

    public int checkDrpDwnEleCount() {
        return driver.findElements(By.xpath("//ul[contains(@class,'p-autocomplete-items')]/li[position()>1]")).size();
    }

    private void ordStatusFiltr(String val) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(statusFiltr));
        input.clear();
        input.sendKeys(val);
        Thread.sleep(2200);
        cp.waitForLoaderToDisappear();
    }

    public void getValidationData(int arrLen, String ordStatus, List<String> validationOrdNo) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        ordStatusFiltr(ordStatus);
        getStatusValueForValidation(arrLen, validationOrdNo);
        cp.waitForLoaderToDisappear();
    }

    /** Pick first row’s GL-* (row-scoped) */
    private void getStatusValueForValidation(int arrLen, List<String> validationOrdNo) {
        List<WebElement> rows = tableRows();
        if (rows.isEmpty()) return;

        for (WebElement row : rows) {
            scrollIntoView(row);
            String gl = row.findElement(By.xpath("//td[4]"))
                           .getText().trim();
            if (!gl.isEmpty()) {
                if (arrLen < validationOrdNo.size()) {
                    validationOrdNo.set(arrLen, gl);
                } else {
                    validationOrdNo.add(gl);
                }
                return;
            }
        }
    }

    /** First row with EMPTY LBO L (td[10]) → return its GL-* */
    private void getStatusValueForTrkingValidation(int arrLen, List<String> trkingValidationOrdNo) {
        List<WebElement> rows = tableRows();
        if (rows.isEmpty()) return;

        for (WebElement row : rows) {
            String lbolVal = row.findElement(By.xpath("//td[10]")).getText().trim();
            if (lbolVal.isEmpty()) {
                scrollIntoView(row);
                String gl = row.findElement(By.xpath("//td[4]"))
                               .getText().trim();
                if (arrLen < trkingValidationOrdNo.size()) {
                    trkingValidationOrdNo.set(arrLen, gl);
                } else {
                    trkingValidationOrdNo.add(gl);
                }
                return;
            }
        }
    }

    public void getTrkingValidationData(int arrLen, String ordStatus, List<String> trkingValidationOrdNo) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        ordStatusFiltr(ordStatus);
        getStatusValueForTrkingValidation(arrLen, trkingValidationOrdNo);
        cp.waitForLoaderToDisappear();
    }

    By custDrpDwn = By.xpath("//*[@id=\"reconsignmentOrderDetcustomer\"]/div[3]");
    By DrpDwnVal = By.xpath("//*[@id=\"genesis\"]/div[5]/div[1]/div/input");

    public void withCustOrdLookup(String val) {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(custDrpDwn, DEFAULT_TIMEOUT);
        cp.waitForLoaderToDisappear();
        cp.DrpDwnValueSel(DrpDwnVal, val);
        cp.waitForLoaderToDisappear();
    }

    public void NumOfPiecesValidation() {
        SoftAssert sAssert = new SoftAssert();

        WebElement gLoPieces = driver.findElement(By.xpath("//*[@id=\"reconsignmentOrderDetLGOPieces\"]"));
        String attrVal = gLoPieces.getAttribute("value").trim();
        int base = Integer.parseInt(attrVal);

        WebElement shipPieces = driver.findElement(By.xpath("//*[@id=\"reconsginmentShipppmentDetnoOfPieces\"]"));
        scrollIntoView(shipPieces);
        shipPieces.clear();
        shipPieces.sendKeys(String.valueOf(base + 1));

        cp.save();
        cp.waitForLoaderToDisappear();

        // reset
        shipPieces.clear();
        shipPieces.sendKeys(attrVal);
    }

    public void LitNumOfPiecesValidation() {
        SoftAssert sAssert = new SoftAssert();

        WebElement gLoLit = driver.findElement(By.xpath("//*[@id=\"reconsignmentOrderDetLGOLiteraturePieces\"]"));
        String attrVal = gLoLit.getAttribute("value").trim();
        int base = Integer.parseInt(attrVal);

        WebElement shipLit = driver.findElement(By.xpath("//*[@id=\"reconsginmentShipppmentDetliteratureNoOfPieces\"]"));
        scrollIntoView(shipLit);
        shipLit.clear();
        shipLit.sendKeys(String.valueOf(base + 1));

        cp.save();
        cp.waitForLoaderToDisappear();

        // reset
        shipLit.clear();
        shipLit.sendKeys(attrVal);
    }

    /** Reads the generated reconsignment order number from its input field */
    public void getReconsignmentOrdNo(int arrIndex, List<String> firstUniqueText) {
        WebElement input = driver.findElement(By.xpath("//*[@id=\"reconsignmentOrderDetreconsignmentOrderNo\"]"));
        scrollIntoView(input);
        String val = input.getAttribute("value"); // value, not getText()
        if (arrIndex < firstUniqueText.size()) {
            firstUniqueText.set(arrIndex, val);
        } else {
            firstUniqueText.add(val);
        }
    }
    
    private By selectconsingee = By.id("masterExternalUserDetreconsignedToConsignee");
    private By list = By
			.xpath("//div[contains(@class,'p-dropdown-items-wrapper')]//ul[contains(@class,'p-dropdown-items')]/li");	
	private By clist = By.xpath("//div[contains(@class,'d-flex align-items-center mt-1 pitem ')]");
    /**
     * Selects the first available consignee from dropdown
     */
	public void selectAnyConsigneeOrSkip() throws InterruptedException {
		cp.waitForLoaderToDisappear();
		wait.until(ExpectedConditions.elementToBeClickable(selectconsingee)).click();

		wait.until(driver -> {
			List<WebElement> options = driver.findElements(clist);
			return options.size() > 1;
		});

		driver.findElement(By.xpath("//*[@placeholder='Enter Address 2']")).click();
		wait.until(ExpectedConditions.elementToBeClickable(selectconsingee)).click();
		List<WebElement> consignee = driver.findElements(clist);
		int consigneeCount = consignee.size();

	//	logger.info("Total consignee found in list: " + consigneeCount);

		if (consigneeCount == 1) {
	//		logger.warning("No Consignee found in dropdown. Skipping test.");
			throw new SkipException("No Consignee available in the dropdown.");
		}

		WebElement selectConsignee = consignee.get(0);
	//	logger.info("Seleted consignee is " + selectConsignee);
		wait.until(ExpectedConditions.visibilityOfElementLocated(clist));
		try {
			driver.findElements(clist).get(0).click();
		} catch (StaleElementReferenceException e) {
		//	logger.warning("StaleElementReferenceException caught. Retrying click...");
			wait.until(ExpectedConditions.elementToBeClickable(selectconsingee)).click();// try it work or not its add
																							// for outbox index
			driver.findElements(clist).get(0).click();
		}
		cp.waitForLoaderToDisappear();
	}


}
