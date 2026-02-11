package com.uiFramework.Genesis.web.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.helper.WaitHelper;

public class InboundQAPage extends InbTruckPage {
    WebDriver driver = null;
    CommonMethods cm;
    CommonPage cp = null;
    DropDownHelper DrpDwn;
    WaitHelper wt;
    JavaScriptHelper js;
    Map<String, String> litruckqData = new HashMap<>();
    Map<String, String> viewData = new HashMap<>();
    SoftAssert softAssert = new SoftAssert();
    int totalPiecesSum = 0;
    String GLorder;
    public String scannGL;
    public String message;
    private WebDriverWait wait;
    SoftAssert sAssert = new SoftAssert();
    public String ToastfoQAsuccess;
    private static final Logger logger = Logger.getLogger(InbTruckPage.class.getName());

    // Cross-browser helpers
    private final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
    private final boolean isSafari;

    public InboundQAPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.cm = new CommonMethods(driver);
        this.cp = new CommonPage(driver);
        this.wt = new WaitHelper(driver);
        this.DrpDwn = new DropDownHelper(driver);
        this.js = new JavaScriptHelper(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.isSafari = getBrowserName(driver).contains("safari");
    }

    private static String getBrowserName(WebDriver d) {
        try {
            Capabilities caps = ((RemoteWebDriver) d).getCapabilities();
            return (caps.getBrowserName() != null) ? caps.getBrowserName().toLowerCase() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private Keys selectAllModifier() {
        return isMac ? Keys.COMMAND : Keys.CONTROL;
    }

    private void clearElement(WebElement el) {
        try {
            el.sendKeys(Keys.chord(selectAllModifier(), "a"));
            el.sendKeys(Keys.DELETE);
        } catch (Exception ignore) {
            try { el.clear(); } catch (Exception ignored) {}
        }
    }

    private void clearAndType(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.click();
        clearElement(el);
        el.sendKeys(text);
    }

    private void safeClick(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {}
        }
    }

    private boolean isDisplayed(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // listing xpath
    private By docMenu = By.xpath("//i[@title='Documents']");
    private By qaMenu = By.xpath("//i[@title='QA']");
    private By inboundqaMenu = By.xpath("//a[@href='/qa/inbound-qa']");
    private By searchtruckno = By.xpath("(//input[@placeholder='Contains...'])[1]");
    private By viewbtn = By.xpath("//table/tbody/tr[2]/td[1]//button");
    private By truckliNo = By.xpath("//table/tbody/tr[2]/td[2]");
    private By getliPalletqty = By.xpath("(//span[@class='MuiTypography-root MuiTypography-button css-rscwwo'])[1]");
    private By truckliStatus = By.xpath("//table/tbody/tr[2]/td[4]");
    private By truckliPickup = By.xpath("//table/tbody/tr[2]/td[5]");
    private By truckliDrop = By.xpath("//table/tbody/tr[2]/td[6]");
    private By iBtn = By.xpath("//div[@title='Click to see pallets present in truck']");
    private By closeiBtn = By.xpath("//*[@data-testid='CloseIcon']");
    private By getliTruckno = By.xpath("//tbody[1]/tr[2]/td[2]");
    private By getliTruckname = By.xpath("//*[@class='scrollable-pane']//tr[2]//td[1]");
    // view xpath
    private By truckNo = By.xpath("(//div[@class='labelBoxK__labelData'])[1]");
    private By truckPickup = By.xpath("(//div[@class='labelBoxK__labelData'])[5]");
    private By truckDrop = By.xpath("(//div[@class='labelBoxK__labelData'])[6]");
    private By truckStatus = By.xpath("(//div[@class='labelBoxK__labelData'])[7]");
    private By palletQty = By.xpath("(//div[@class='labelBoxK__labelData'])[8]");
    private By totalpieces = By.xpath("(//div[@class='labelBoxK__labelData'])[3]");
    private By markasreceivedqty = By.xpath("//button[@title='Click to mark received quantity same as order quantity']");
    private By save = By.xpath("//button[@id='inboundQAArrivedTruckDetailsSaveQtyBtn']");
    private By ibplCount = By.xpath("//tr//td[contains(text(), 'IBPL-')]");
    private By glorderCounts = By.xpath("(//*[@class='p-datatable-tbody'])[2]//td[1]");
    private By scanninput = By.xpath("//input[@id='txtBarcodeScan']");
    private By getQAStatus = By.xpath("//table/tbody/tr/td[14]");
    private By yesbtn = By.xpath("//button[@id='confmSubmit']");
    private By enterpc = By.xpath("//*[@placeholder='Enter pieces']");

    /**
     * Open QA menu
     */
    public void inboundQaMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        safeClick(docMenu);
        safeClick(qaMenu);
        Thread.sleep(300);
        safeClick(inboundqaMenu);
        cp.waitForLoaderToDisappear();
    }

    /** Get truck number on listing */
    public String getMTruckNo() {
        return cp.getMandatoryText(truckliNo);
    }

    /** Search order by truck no */
    public void searchOrder() {
        cp.waitForLoaderToDisappear();
        logger.info("Truck no on Inbound QA screen " + MTruckNo);
        cp.searchColoumFilter(searchtruckno, MTruckNo);
        logger.info("Truck No match. processing QA for truck " + getMTruckNo());
    }

    /** Collect listing data */
    public void getListTruckData() throws InterruptedException {
        safeClick(iBtn);
        Thread.sleep(500);
        int counts = 0;
        List<WebElement> ibpl = driver.findElements(By.xpath("//*[@id='simple-popover']//table/tbody/tr"));
        for (int i = 0; i < ibpl.size(); i++) {
            String text = cp.getMandatoryText(By.xpath("//*[@id='simple-popover']//table/tbody/tr[" + (i + 1) + "]/td[3]"));
            counts += Integer.parseInt(text.trim());
        }
        litruckqData.put("palletQTY", String.valueOf(counts));

        if (isDisplayed(closeiBtn, 3)) {
            safeClick(closeiBtn);
        }

        litruckqData.put("TruckNo", cp.getMandatoryText(truckliNo));
        litruckqData.put("TruckStatus", cp.getMandatoryText(truckliStatus));
        litruckqData.put("pickupLocation", cp.getMandatoryText(truckliPickup));
        litruckqData.put("DropLocation", cp.getMandatoryText(truckliDrop));
        logger.info("TruckData On QA listing: " + litruckqData);
    }

    /** Collect view data */
    public void getQAviewData() {
        viewData.put("TruckNo", cp.getMandatoryText(truckNo));
        viewData.put("PalletQTY", cp.getMandatoryText(palletQty));
        viewData.put("TruckStatus", cp.getMandatoryText(truckStatus));
        viewData.put("PickupLocation", cp.getMandatoryText(truckPickup));
        viewData.put("DropLocation", cp.getMandatoryText(truckDrop));
        logger.info("TruckData On QA view: " + viewData);
    }

    /** Click view button */
    public void viewButton() {
        wt.waitToClick(viewbtn, 10);
        cp.waitForLoaderToDisappear();
    }

    /** Validate listing vs view data */
    public void validateTruckdata() {
    	SoftAssert sAssert = new SoftAssert();
        for (Map.Entry<String, String> entry : litruckqData.entrySet()) {
            String expectedValue = entry.getValue();
            sAssert.assertTrue(viewData.containsValue(expectedValue),
                    "FAIL: Data mismatch between Listing and View. Missing: '" + expectedValue + "'");
        }
        sAssert.assertAll();
    }

    /** Save with conditional YES popup handling */
    private boolean isYesButtonDisplayed = false;

    public void saveBtnWithYesbtn() {
        isYesButtonDisplayed = false;
        safeClick(save);
        try {
            WebElement yesButton = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(yesbtn));
            if (yesButton != null && yesButton.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesButton);
                logger.info("Yes button clicked.");
                isYesButtonDisplayed = true;
            }
        } catch (Exception e) {
            logger.info("No confirmation dialog shown after save.");
        }
    }

    public boolean yesBtncheck() {
        return isYesButtonDisplayed;
    }
 
    /**
     * This method use to clear enter pieces in any scanned already
     * @throws InterruptedException 
     */
    public void clearpieces() throws InterruptedException {
    	wt.waitToClick(enterpc, 5);
    	driver.findElement(enterpc).sendKeys(Keys.BACK_SPACE);
    	//driver.findElement(enterpc).clear();
    	//driver.findElement(enterpc).clear();
    	driver.findElement(enterpc).sendKeys(Keys.BACK_SPACE);
    	Thread.sleep(500);
    }
    
    /** Simple save click */
    public void saveBtn() {
        safeClick(save);
    }
    
    /**
     * This method used to do QA for mail order
     */
    public void positiveSave() {
    	safeClick(markasreceivedqty);
    	saveBtn();
    }

    /** Main QA validation flow */
    public void checkValidationQA() {
    	SoftAssert sAssert = new SoftAssert();
        if ("QA Process".equals(qaStatus()) || "QA Done".equals(qaStatus())) {
            logger.info("QA already in process or done. Skipping.");
            return;
        }
        List<WebElement> IBPLcountList = driver.findElements(ibplCount);

        for (int j = 0; j < IBPLcountList.size(); j++) {
            clickIBPLAtIndex(j);
            cp.waitForLoaderToDisappear();

            validateEnterPieces();

            safeClick(markasreceivedqty);

            validateTotalPieces();

            if (IBPLcountList.size() > 1 && j < IBPLcountList.size() - 1) {
            	sAssert.assertEquals(qaStatus(), "QA Inprocess", "QA status mismatch for intermediate pallets");
            } else if (j == IBPLcountList.size() - 1) {
                logger.info("Last inbound pallet processed");
                sAssert.assertEquals(qaStatus(), "QA Done", "QA status should be 'QA Done'.");
            }
        }
        sAssert.assertAll();
    }

    private void clickIBPLAtIndex(int index) {
        List<WebElement> IBPLcountList = driver.findElements(ibplCount);
        IBPLcountList.get(index).click();
    }

    /** Enter pieces > max and assert toast */
    private void validateEnterPieces() {
    	SoftAssert sAssert = new SoftAssert();
        List<WebElement> allAddons = driver.findElements(By.xpath("//span[@class='p-inputgroup-addon']"));
        cp.waitForPopupToDisappear();
        for (WebElement addon : allAddons) {
        	cp.waitForPopupToDisappear();
            int maxValue = Integer.parseInt(addon.getText().replace("/", "").trim());
            WebElement parentGroup = addon.findElement(By.xpath("./ancestor::div[contains(@class, 'p-inputgroup')]"));
            WebElement inputField = parentGroup.findElement(By.xpath(".//input[@placeholder='Enter pieces']"));

            String exceedValue = String.valueOf(maxValue + 1);
            clearElement(inputField);
            inputField.sendKeys(exceedValue);

            sAssert.assertTrue(cp.toastMsgReceivedSuccessfully(),
                    "Toast message not shown when exceeding piece limit");
            logger.info("Toast msg: " + cp.captureToastMessage());
            cp.waitForPopupToDisappear();
        }
        sAssert.assertAll();
    }

    /** Validate total pieces calculation after mark-as-received */
    private void validateTotalPieces() {
    	SoftAssert sAssert = new SoftAssert();
        totalPiecesSum = 0;
        List<WebElement> GLcount = driver.findElements(glorderCounts);
        logger.info("GL orders in IBPL: " + GLcount.size());

        for (int i = 0; i < GLcount.size(); i++) {
            WebElement totalPiecesCell =
                    driver.findElement(By.xpath("(//*[@id='panel1bh-content'])[3]//tbody//tr[" + (i + 1) + "]//td[4]"));

            int recIndex = (2 * i) + 1;
            int reclitIndex = (2 * i) + 2;

            WebElement recPC = driver.findElement(By.xpath("(//input[@placeholder='Enter pieces'])[" + recIndex + "]"));
            WebElement reclitPC = driver.findElement(By.xpath("(//input[@placeholder='Enter pieces'])[" + reclitIndex + "]"));

            int recPCVal = Integer.parseInt(recPC.getAttribute("value"));
            int reclitPCVal = Integer.parseInt(reclitPC.getAttribute("value"));
            int sum = recPCVal + reclitPCVal;

            int totalPiecesValue = Integer.parseInt(totalPiecesCell.getText().trim());
            sAssert.assertEquals(sum, totalPiecesValue, "Sum of pieces != displayed total");

            totalPiecesSum += totalPiecesValue;
        }
        saveBtn();
        ToastfoQAsuccess = cp.captureToastMessage();
        logger.info("Toast msg is " + ToastfoQAsuccess);
        cp.waitForLoaderToDisappear();
        logger.info("Total Pieces across GLs: " + totalPiecesSum);
        sAssert.assertAll();
    }

    public String getSumofOrderPieces() {
        return String.valueOf(totalPiecesSum);
    }

    public String totalPiecesinTruck() {
        return cp.getMandatoryText(totalpieces);
    }

    public String getGLorder() {
        GLorder = cp.getMandatoryText(By.xpath("(//tr//td[1])[2]"));
        return GLorder;
    }

    public String checkThatlitQtyDefaultZero(int i) {
        if (getPieces(i).equals("0")) {
            return cp.getAttributeValue(By.xpath("(//input[@placeholder='Enter pieces'])[" + i + "]"), "value");
        } else {
            logger.info("Literature pieces not zero. Skipping index " + i);
            throw new SkipException("SKIPPED: Literature pieces not zero for index " + i);
        }
    }

    public String getPieces(int i) {
        String pc = cp.getMandatoryText(By.xpath("(//span[@class='p-inputgroup-addon'])[" + i + "]"));
        return pc.replace("/ ", "");
    }

    public String qaStatus() {
        return cp.getMandatoryText(truckStatus);
    }

    public void searchPendingStatus(String status) {
        cp.searchColoumFilter(By.xpath("(//*[@placeholder='Contains...'])[4]"), status);
    }

    /** Scanning validation */
    public void checkValidationQAScanning() {
    	SoftAssert sAssert = new SoftAssert();
        if (isQAInProgressOrDone()) {
            logger.info("QA already in process or done. Skipping method...");
            return;
        }
        List<WebElement> IBPLList = driver.findElements(ibplCount);
        logger.info("IBPL count " + IBPLList.size());

        outerLoop:
        for (int i = 0; i < IBPLList.size(); i++) {
            driver.findElements(ibplCount).get(i).click();
            cp.waitForLoaderToDisappear();
            List<WebElement> GLList = driver.findElements(glorderCounts);
            logger.info("GL count " + GLList.size());

            for (int j = 0; j < GLList.size(); j++) {
                String barcode = constructBarcode(j + 1);
                logger.info("Barcode: " + barcode);
                driver.findElement(scanninput).sendKeys(barcode);

                try {
                    if (isPopupVisible()) {
                        if (handleInvalidBarcodePopup()) {
                            continue;
                        }
                    } else {
                        processValidScan(barcode, j + 1);
                        break outerLoop;
                    }
                } catch (Exception e) {
                    logger.warning("Exception while checking scan result: " + e.getMessage());
                }
            }
            sAssert.assertAll();
        }
        try { cp.clickToClosePopUp(); } catch (Exception ignored) {}
    }

    private boolean isQAInProgressOrDone() {
        String status = qaStatus();
        return "QA Process".equals(status) || "QA Done".equals(status);
    }

    private String constructBarcode(int rowIndex) {
        String GLorder = cp.getMandatoryText(By.xpath("(//*[@id='panel1bh-content'])[3]//tbody//tr[" + rowIndex + "]//td[1]"));
        String CustOrder = cp.getMandatoryText(By.xpath("(//*[@id='panel1bh-content'])[3]//tbody//tr[" + rowIndex + "]//td[3]"));
        return GLorder + "|" + CustOrder + "|1";
    }

    private boolean isPopupVisible() {
        List<WebElement> okButtons = driver.findElements(By.xpath("//button[@title='Click to ok']"));
        return !okButtons.isEmpty() && okButtons.get(0).isDisplayed();
    }

    private boolean handleInvalidBarcodePopup() {
        WebElement okBtn = driver.findElement(By.xpath("//button[@title='Click to ok']"));
        String error = cp.getMandatoryText(By.xpath("//div[@class='MuiGrid-root MuiGrid-grid-xs-12 css-nwhc3l']"));
        String msg = error.substring(error.lastIndexOf("|") + 1).trim().replaceFirst("^\\d+\\s*", "");
        String expected = "barcode is invalid!.";

        logger.info("Error: " + msg);
        okBtn.click();

        if (msg.equalsIgnoreCase(expected)) {
            logger.info("Invalid barcode message matched. Skipping this order.");
            return true;
        }
        return false;
    }

    private void processValidScan(String barcode, int rowIndex) {
    	SoftAssert sAssert = new SoftAssert();
        String scanned = driver.findElement(By.xpath("(//*[@class='p-datatable-tbody'])[3]//tr//td")).getText().trim();

        sAssert.assertEquals(barcode, scanned,
                "Scanned barcode mismatch. Scanned: " + scanned + " | Expected: " + barcode);
        logger.info("Scanned pieces: " + scanned);

        safeClick(markasreceivedqty);

        WebElement pieceInput = driver.findElement(By.xpath("(//*[@id='panel1bh-content'])[3]//tbody//tr[" + rowIndex
                + "]//td[5]//input[@placeholder='Enter pieces']"));
        clearElement(pieceInput);
        pieceInput.sendKeys("0");

        saveBtn();
        sAssert.assertEquals(
                cp.captureToastMessage(),"Scanned number of pieces does not match the received pieces.",
                "Less enter scanned pieces message not found.");

        verifyScannedInfoAndCount(rowIndex, barcode);
        saveBtnWithYesbtn();
        cp.captureToastMessage();

        if (!scanned.equals("0") && !scanned.isEmpty()) {
            safeClick(markasreceivedqty);
            validateTotalPieces();
        } else {
            logger.warning("Scan success assumed, but pieces = 0.");
        }
        sAssert.assertAll();
    }

    public void verifyScannedInfoAndCount(int rowIndex, String barcode) {
        driver.findElement(
                By.xpath("(//*[@id='panel1bh-content'])[3]//tbody//tr[" + rowIndex + "]//td[5]//div[@title]")).click();

        String infoBarcode = cp.getMandatoryText(By.xpath("//*[@id='barcode-popover']//table/tbody/tr/td[1]"));
        sAssert.assertEquals(infoBarcode, barcode,
                "Info popover barcode mismatch. Scanned: " + infoBarcode + " | Expected: " + barcode);

        if (isDisplayed(closeiBtn, 2)) {
            safeClick(closeiBtn);
        }

        scannGL = infoBarcode.split("\\|")[0].trim();

        WebElement pieceInput = driver.findElement(By.xpath("(//*[@id='panel1bh-content'])[3]//tbody//tr[" + rowIndex
                + "]//td[5]//input[@placeholder='Enter pieces']"));
        clearElement(pieceInput);
        pieceInput.sendKeys("1");
    }

    /** Filter validations */
    public void searchAndValidateTruckNo() throws InterruptedException {
        try {
            cp.searchAndValidateTruckNo(getliTruckno);
        } catch (Exception e) {
            logger.info("Truck number filter skipped");
        }
    }

    public void searchAndValidateTruckName() throws InterruptedException {
        try {
            cp.searchAndValidateTruckName(getliTruckname);
        } catch (Exception e) {
            logger.info("Truck name filter skipped");
        }
    }

    public void checkColoumfilter() throws InterruptedException, IOException {
        Path jsonPath = Paths.get(System.getProperty("user.dir"),
                "src", "main", "resources", "configfile", "Locators.JSON");

        String jsonContent = Files.readString(jsonPath);
        JSONObject root = new JSONObject(jsonContent);

        String pageName = "inboundQAPage";

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
                } else if (method.equals("numeric")) {
                    cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
                } else if (method.equals("date")) {
                    cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath, columnIndex);
                }
            }
        } else {
            logger.warning("Page not found in JSON: " + pageName);
        }
    }

    /** Pagination */
    public void paginationOnListing() {
        cp.paginationTest(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"), 2);
    }

    /** Get QA status from listing for a given order 
     * @throws InterruptedException */
    public String getOrderQAStatus(String order) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        searchTruckno(order); 
        Thread.sleep(2100);
        cp.waitForLoaderToDisappear();
        return cp.getMandatoryText(getQAStatus);
    }

    // Helper used in getOrderQAStatus (kept same behavior)
    public void searchTruckno(String order) {
        try {
            clearAndType(searchtruckno, order);
            cp.waitForLoaderToDisappear();
        } catch (StaleElementReferenceException e) {
            clearAndType(searchtruckno, order);
        }
    }
}
