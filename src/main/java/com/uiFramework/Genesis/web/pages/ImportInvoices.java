package com.uiFramework.Genesis.web.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class ImportInvoices {
    WebDriver driver = null;
    CommonMethods cm;
    WaitHelper wt;
    CommonPage cp;
    OrderPage op;
    private JavascriptExecutor js;
    private WebDriverWait wait;

    // ========= Cross-browser flags & portable paths =========
    private final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
    private final boolean isSafari;

    // Safari: default to ~/Downloads since SafariDriver can’t be pointed to custom dirs.
    // Chrome: project-local "downloadedFiles"
    private final Path downloadsRoot;
    private final String fileName = "format.xlsx";
    private final Path fullFile;
    private final String fullFilePath;

    public static String DueDate;
    public static String FiscalPeriod;
    public static double paybleAmount;
    public static String carrier;
    public static String BatchNo;
    public static List<String> invoiceData = new ArrayList<>();
    public static String invoiceNumber;

    public ImportInvoices(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver);
        this.wt = new WaitHelper(driver);
        this.cp = new CommonPage(driver);
        this.op = new OrderPage(driver);
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        String browser = getBrowserName(driver);
        this.isSafari = browser.contains("safari");

        this.downloadsRoot = isSafari
                ? Paths.get(System.getProperty("user.home"), "Downloads")
                : Paths.get(System.getProperty("user.dir"), "downloadedFiles");

        this.fullFile = downloadsRoot.resolve(fileName);
        this.fullFilePath = fullFile.toString();
    }

    // ========= Helpers (cross-platform clearing, clicking, reading) =========
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
            try {
                WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            } catch (Exception ignored) {}
        }
    }
    
    

    private String getTextOrValue(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        String val = el.getAttribute("value");
        if (val != null && !val.trim().isEmpty()) return val.trim();
        String txt = el.getText();
        return (txt != null) ? txt.trim() : "";
    }

    private void clickDialogOkIfPresent() {
        List<By> okLocators = Arrays.asList(
            By.xpath("//div[contains(@class,'MuiDialog-root')]//button[normalize-space()='OK']"),
            By.xpath("//div[contains(@class,'MuiDialog-root')]//button[normalize-space()='Ok']"),
            By.xpath("//div[contains(@class,'MuiDialog-root')]//button[contains(@id,'confmSubmit')]"),
            By.xpath("//div[contains(@role,'dialog')]//button[normalize-space()='OK']"),
            By.xpath("//div[contains(@role,'dialog')]//button[contains(@class,'p-button') and (normalize-space()='OK' or normalize-space()='Ok')]")
        );
        for (By by : okLocators) {
            List<WebElement> els = driver.findElements(by);
            if (!els.isEmpty() && els.get(0).isDisplayed()) {
                try { els.get(0).click(); return; } catch (Exception e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", els.get(0));
                    return;
                }
            }
        }
    }

    // ========= Locators =========
    private By docMenu = By.xpath("//i[@title='Accounts Payable']");
    private By docGenMenu = By.xpath("//a[@href='/AP/import-invoices']");
    private By payInvoiceMenu = By.xpath("//a[@href='/AP/pay-invoices']");
    private By batchInvoiceMenu = By.xpath("//a[@href='/AP/batch-invoices']");
    private By ApLookupMenu = By.xpath("//*[@href='/AP/lookup']");
    private By uploadFile = By.xpath("//div[1]/div/span/input");
    private By uploadBtn = By.xpath("//*[@id='APinvoiceExcelFile']/div[1]/div/button[1]");
    private By carrierDrpDwn = By.xpath("(//*[text()='Select Carrier'])[2]");
    private By searchDrpDwn = By.xpath("//*[@id='genesis']/div[4]/div[1]/div/input");
    private By carrOnpayInvoices = By.xpath("//*[@id='APInviceUploadInvoiceFiltercarrier']/span");
    private By carrErrMsg = By.xpath("//*[@id='panel1bh-content']//div/div[2]/div/span");
    private By dwnloadFormat = By.xpath("//*[@id='APInviceUploadInvoicebtnDownloadFormat']");
    private By errMsgOnPopup = By.xpath("//table/tbody/tr/td[8]/div[1]");
    private By okBtnerrMsgOnPopup = By.xpath("//*[@id=':r9:']/div/button"); // dynamic in some builds
    private By saveBatchBtn = By.id("APInviceUploadInvoicebtnSaveBatch");
    private By saveimportBatchBtn = By.id("APInviceBatchEditbtnSaveBatch");
    private By invoiceNo = By.xpath("//table/tbody/tr/td[6]/div/div/div/div/input");
    private By invoiceAmt = By.xpath("//table/tbody/tr/td[7]/div/div/div/div/span/input");
    private By payableAmt = By.xpath("//table/tbody/tr/td[8]/div/div/div/div/span/input");
    private By DueDateErrMsg = By.xpath("//*[contains(text(), \"Due Date is required\")]");
    private By InvoiceDateErrMsg = By.xpath("//*[contains(text(), \"Invoice Date is required\")]");
    private By FascalPeriodErrMsg = By.xpath("//*[contains(text(), \"Fiscal Period is required\")]");
    private By scheduleDate = By.xpath("//*[@id='APInviceSummaryinvoiceDate']/button");
    private By dateSel1 = By.xpath("//td[@data-p-today='true']");
    private By dueDate = By.xpath("//*[@id='APInviceSummarydueDate']/button");
    private By dueDateSel = By.xpath("//*[@aria-disabled=\"false\" and text() = '30']");
    private By fascalPeriod = By.id("APInviceSummaryfiscalPeriod");
    private By fascalPeriodSel = By.xpath("/html/body/div[4]/div[2]/ul/li[5]");
    private By saveBatch = By.xpath("//*[@title='Click to save batch']");
    private By invoiceNo1strow = By.xpath("//*[@placeholder='Enter Invoice No']");
    private By invoiceNo2ndrow = By.xpath("(//*[@placeholder='Enter Invoice No'])[2]");
    private By invoiceNo3rdrow = By.xpath("(//*[@placeholder='Enter Invoice No'])[3]");
    private By invoiceAmt1strow = By.xpath("//*[@placeholder='Enter Invoice Amount']");
    private By invoiceAmt2ndrow = By.xpath("(//*[@placeholder='Enter Invoice Amount'])[2]");
    private By invoiceAmt3rdrow = By.xpath("(//*[@placeholder='Enter Invoice Amount'])[3]");
    private By invoiceComment1strow = By.xpath("//*[@placeholder='Enter Invoice Comment']");
    private By invoiceComment2ndrow = By.xpath("(//*[@placeholder='Enter Invoice Comment'])[2]");
    private By invoicePayAmount = By.xpath("(//input[@placeholder='Enter Payable Amount'])[1]");
    private By invoicePayAmount2 = By.xpath("(//input[@placeholder='Enter Payable Amount'])[2]");
    private By invDate = By.xpath("//*[@placeholder='Invoice Date']");
    private By dueDateinput = By.xpath("//*[@placeholder='Due Date']");
    private By yesBtn = By.id("confmSubmit");
    private By gridtext = By.xpath("(//*[@class='p-column-title'])[1]");

    // batch listing
    private By filter = By.xpath("//*[@placeholder='Date Is...']");
    private By radioBtn = By.xpath("//*[contains(@class,'p-radiobutton-box')]");
    private By expandBtn = By.xpath("//*[@title='Click to expand row']");
    private By totalPayAmount = By.xpath("//*[@id='APInviceSummarypayableAmount']/input");
    private By totalAmount = By.xpath("(//tbody[@class='p-datatable-tbody']/tr/td[4])[1]");
    private By totalpayAmountonEdit = By.xpath("//*[@id='APInviceBatchEditpayableAmount']/input");

    // ap lookup
    private By byinvoice = By.xpath("//h6[normalize-space(.)='By Invoice #']");
    private By bybatch = By.xpath("//h6[normalize-space(.)='By Batch #']");
    private By bylbol = By.xpath("//h6[normalize-space(.)='By LBOL #']");
    private By bytruck = By.xpath("//h6[normalize-space(.)='By Truck #']");
    private By byauth = By.xpath("//h6[normalize-space(.)='By Auth #']");
    private By selectCarrier = By.xpath("//span[normalize-space(.)='Select Carrier']");
    private By searchval = By.xpath("//*[@class='p-dropdown-filter p-inputtext p-component']");
    private By selectInv = By.xpath("//span[normalize-space(.)='Select Invoice No']");
    private By serachBtninv = By.id("APlbolIbnvoicefilterSearchBtn");
    private By serachBtnBatch = By.id("APbatchListSearchFilterBtn");
    private By serachBtnlbol = By.id("APlbolfilterSearchBtn");
    private By serachBtntruck = By.id("APInviceTruckFiltersSearchBtn");
    private By serachBtnAuth = By.id("APAuthSearchFilterBtn");
    private By clearBtninv = By.id("APlbolIbnvoicefilterClearBtn");
    private By exportExcel = By.xpath("//*[@title='Click to export excel']");

    // ========= Navigation =========
    
    public void importInvMenuWithExpand() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(docMenu, 20);
        Thread.sleep(2000);
        wt.waitToClick(docGenMenu, 10);
        cp.waitForLoaderToDisappear();
    }
    
//    public void importInvMenuWithExpand() throws InterruptedException {
//        cp.waitForLoaderToDisappear();
//        WebElement Menu = wait.until(ExpectedConditions.elementToBeClickable(docMenu));
//        js.executeScript("arguments[0].click();", Menu);
//        WebElement Element = wait.until(ExpectedConditions.visibilityOfElementLocated(docGenMenu));
//        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", Element);
//        wait.until(ExpectedConditions.elementToBeClickable(docGenMenu)).click();
//        cp.waitForLoaderToDisappear();
//    }

    public void accountPayableMenu() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        Thread.sleep(2000);
        wt.waitToClick(docMenu, 20);
        cp.waitForLoaderToDisappear();
    }

    public void importInvMenu() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        Thread.sleep(2000);
        wt.waitToClick(docGenMenu, 20);
        cp.waitForLoaderToDisappear();
    }

    public void payInvoicesMenu() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        Thread.sleep(1000);
        wt.waitToClick(payInvoiceMenu, 20);
        cp.waitForLoaderToDisappear();
    }

    public void batchInvoicesMenu() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        Thread.sleep(1000);
        wt.waitToClick(batchInvoiceMenu, 20);
        cp.waitForLoaderToDisappear();
    }

    public void APLookupMenu() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        Thread.sleep(5000);
        wt.waitToClick(ApLookupMenu, 20);
        cp.waitForLoaderToDisappear();
    }
    
	private void scrollIntoView(WebElement el) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
		} catch (Exception ignored) {}
	}

	private void jsClick(WebElement el) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
	}
	
	private boolean isSafari() {
		try {
			String name = ((HasCapabilities) driver).getCapabilities().getBrowserName();
			return name != null && name.toLowerCase().contains("safari");
		} catch (Exception e) {
			return false;
		}
	}

	/** Safari-safe, resilient click helper */
	private void issafeClick(By locator) {
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

    // ========= Download / Upload =========
    public void downloadFileFormat() throws InterruptedException, FileNotFoundException {
        File oldFile = fullFile.toFile();
        if (oldFile.exists()) oldFile.delete();

        Thread.sleep(3000);
        issafeClick(dwnloadFormat);

        int timeoutSec = 40, waited = 0;
        while (!fullFile.toFile().exists() && waited < timeoutSec) {
            Thread.sleep(1000);
            waited++;
        }
        if (!fullFile.toFile().exists()) {
            throw new FileNotFoundException("Downloaded file not found at: " + fullFilePath);
        }
    }                                                                                                                                                                                               
    

    public void excelUpload() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        driver.findElement(uploadFile).sendKeys(fullFilePath);
        cp.waitForLoaderToDisappear();
        wt.waitToClick(uploadBtn, 10);
        Thread.sleep(2000);
        cp.waitForLoaderToDisappear();
    }

    public void clickUploadBtn() {
        wt.waitToClick(uploadBtn, 10);
        cp.waitForLoaderToDisappear();
    }

    // ========= Carrier selection =========
    public void carrSelOnPayInvSCreen() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdownButton = driver.findElement(By.xpath("//*[@id='APInviceUploadInvoiceFiltercarrier']/span"));
        dropdownButton.click();

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li")));
        List<WebElement> options = driver.findElements(By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li"));

        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                dropdownButton.click();
                Thread.sleep(1000);
            }

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='APInviceUploadInvoiceFiltercarrier']/span")));
            options = driver.findElements(By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li"));
            if (options.size() <= i) continue;

            WebElement option = options.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            cp.waitForLoaderToDisappear();

            List<WebElement> targetElement = driver.findElements(By.xpath("//*[contains(text(), 'Genesis')]"));
            if (targetElement.size() > 0) {
                break;
            }
        }
    }

    public void selectCarrier(String value) {
        cp.waitForLoaderToDisappear();
        cp.clickElement(carrierDrpDwn);
        cp.DrpDwnValueSel(searchDrpDwn, value);
        cp.waitForLoaderToDisappear();
    }

    public String getCarrier() {
        return getTextOrValue(carrOnpayInvoices);
    }

    public void clickDwmloadFormat() {
        cp.waitForLoaderToDisappear();
        driver.findElement(dwnloadFormat).click();
        cp.waitForLoaderToDisappear();
    }

    public String getCarrErrMsg() {
        return driver.findElement(carrErrMsg).getText();
    }

    // ========= Excel read/write =========
    public void writeGenDataToExcel() throws IOException {
        cp.waitForLoaderToDisappear();
        List<WebElement> authElements = driver.findElements(By.xpath("//*[contains(text(),'Genesis') or contains(text(),'GEN')]"));

        FileInputStream fis = new FileInputStream(fullFilePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int rowIndex = 1;
        for (int i = 0; i < authElements.size(); i++) {
            String text = authElements.get(i).getText();
            if (text == null || text.trim().isEmpty()) continue;

            Row row = sheet.getRow(rowIndex);
            if (row == null) row = sheet.createRow(rowIndex);

            Cell authCell = row.getCell(3);
            if (authCell == null) authCell = row.createCell(3);
            authCell.setCellValue(text);

            rowIndex++;
        }

        fis.close();
        try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    public void modifyExcelForFilledGenColumn(boolean updateCol0, boolean updateCol1, boolean updateCols4To6)
            throws IOException {
        FileInputStream fis = new FileInputStream(fullFilePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell genCell = row.getCell(3);
            if (genCell == null) continue;

            String genText = genCell.getStringCellValue().trim();
            if (genText.isEmpty()) continue;

            if (updateCol0) {
                Cell c = row.getCell(0);
                if (c == null) c = row.createCell(0);
                c.setCellValue(cm.getNumericString(7));
            } else {
                Cell c = row.getCell(0);
                if (c != null) c.setCellValue("");
            }

            if (updateCol1) {
                Cell c = row.getCell(1);
                if (c == null) c = row.createCell(1);
                c.setCellValue(cm.getNumericString(2));
            } else {
                Cell c = row.getCell(1);
                if (c != null) c.setCellValue("");
            }

            for (int col = 4; col <= 6; col++) {
                Cell c = row.getCell(col);
                if (c == null && updateCols4To6) c = row.createCell(col);
                if (updateCols4To6) {
                    if (c != null) c.setCellValue(cm.getNumericString(2));
                } else {
                    if (c != null) c.setCellValue("");
                }
            }
        }

        fis.close();
        try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // ========= Popups =========
    public String GetErrorMsg() {
        return driver.findElement(errMsgOnPopup).getText();
    }

    By closePopup = By.xpath("/html/body/div[4]/div[3]/div/h2/div/button");

    public void clickOK() {
        try {
            WebElement okBtn = driver.findElement(okBtnerrMsgOnPopup);
            if (okBtn.isDisplayed()) {
                cp.clickElement(okBtnerrMsgOnPopup);
            } else {
                clickDialogOkIfPresent();
            }
        } catch (Exception e) {
            clickDialogOkIfPresent();
        }
        cp.waitForLoaderToDisappear();
    }

    // ========= Save batch / import =========
    public void clickSaveBatch() {
        cp.waitForLoaderToDisappear();
        wt.waitToClickWithAction(saveBatchBtn, 10);
        cp.waitForLoaderToDisappear();
    }

    public void clickSaveimportBatch() {
        cp.waitForLoaderToDisappear();
        wt.waitToClickWithAction(saveimportBatchBtn, 10);
        cp.waitForLoaderToDisappear();
    }

    public void enterInvoiceNo(String invoice) {
        cp.waitForLoaderToDisappear();
        this.clickBackspace(invoiceNo);
        cp.clickAndSendKeys(invoiceNo, invoice);
        cp.waitForLoaderToDisappear();
    }

    public void enterInvoiceAmt(String invoice) {
        try {
            cp.waitForLoaderToDisappear();
            this.clickBackspace(invoiceAmt);
            cp.clickAndSendKeys(invoiceAmt, invoice);
            cp.waitForLoaderToDisappear();
        } catch (Exception e) {}
    }

    public void enterPayableAmt(String invoice) {
        try {
            cp.waitForLoaderToDisappear();
            this.clickBackspace(payableAmt);
            cp.clickAndSendKeys(payableAmt, invoice);
            cp.waitForLoaderToDisappear();
        } catch (Exception e) {}
    }

    public void clickBackspace(By ele) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
        clearElement(el);
    }

    public String getDueDateErrMsg() {
        return driver.findElement(DueDateErrMsg).getText();
    }

    public String getInvoiceDateErrMsg() {
        return driver.findElement(InvoiceDateErrMsg).getText();
    }

    public String getFascalPeriodErrMsg() {
        return driver.findElement(FascalPeriodErrMsg).getText();
    }

    public void invoiceDateSel() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(scheduleDate, 10);
        Thread.sleep(1000);
        wt.waitToClick(dateSel1, 10);
        cp.waitForLoaderToDisappear();
    }

    public void dueDateSel() throws InterruptedException {
        // kept as-is (calendar), but we usually prefer direct typing via SelDueDateSameAsinvoiceDate()
    }

    public void fascalPeriodSel() throws InterruptedException {
        wt.waitToClick(fascalPeriod, 10);
        Thread.sleep(1000);
        wt.waitToClick(fascalPeriodSel, 10);
        cp.waitForLoaderToDisappear();
    }

    public int getRowCount() {
        List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
        return rows.size();
    }

    String rowCountField = "//*[@id='panel1bh-content']/div/div/div/div[1]/div/div/div[5]/div/div/input";
    public int getRowCountValue() {
        WebElement element = driver.findElement(By.xpath(rowCountField));
        String text = element.getAttribute("value");
        return Integer.parseInt(text.trim());
    }

    By byInvoice = By.xpath("(//*[@id=\"panel1bh-header\"]/div[1]/div/div)[3]");
    public void clickByInvoice() {
        cp.waitForLoaderToDisappear();
        cp.clickElement(byInvoice);
        cp.waitForLoaderToDisappear();
    }

    By carrDrpDwnOnByInv = By.xpath("//*[@id=\"APlbolInvoicefiltercarrier\"]");
    By invoiceDrpDwnOnByInv = By.xpath("//*[@id=\"APlbolInvoicefilterinvoiceNo\"]");
    By inputOnByInv = By.xpath("//*[@id=\"genesis\"]/div[3]/div[1]/div/input");
    By invoiceNoSel = By.xpath("//div[3]/div[2]/ul/li[1]");

    public void selCarrInByInvoice(String val) {
        cp.waitForLoaderToDisappear();
        cp.clickElement(carrDrpDwnOnByInv);
        cp.DrpDwnValueSel(inputOnByInv, val);
        cp.waitForLoaderToDisappear();
    }

    public String getInvoiceNo() {
        cp.waitForLoaderToDisappear();
        cp.clickElement(invoiceDrpDwnOnByInv);
        cp.waitForLoaderToDisappear();
        return driver.findElement(invoiceNoSel).getText();
    }

    // ========= Pay invoice helpers =========
    public void carrSelOnPayInvScreenCreateInv() throws InterruptedException {
        List<WebElement> gridCount = driver.findElements(By.xpath("//*[@class='MuiBox-root css-yuee10']"));
        for (int j = 1; j <= 3; j++) {
            gridCount.get(j).click();
            Thread.sleep(500);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dropdownButton = driver.findElement(By.xpath("//*[@id='APInviceUploadInvoiceFiltercarrier']/span"));
        dropdownButton.click();

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li")));
        List<WebElement> options = driver.findElements(By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li"));

        for (int i = 0; i < options.size(); i++) {
            if (i > 0) {
                dropdownButton.click();
                Thread.sleep(1000);
            }

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='APInviceUploadInvoiceFiltercarrier']/span")));
            options = driver.findElements(By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li"));
            if (options.size() <= i) continue;

            WebElement option = options.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            cp.waitForLoaderToDisappear();

            List<WebElement> targetElement = driver.findElements(By.xpath("//*[contains(text(), 'Record not found')]"));
            if (targetElement.size() < 3) {
                List<WebElement> gridCount1 = driver.findElements(By.xpath("//*[@class='MuiBox-root css-yuee10']"));
                for (int k = 3; k >= 1; k--) {
                    gridCount1.get(k).click();
                    Thread.sleep(500);
                }
                break;
            }
        }
    }

    public void clickOnInvAmt() {
        driver.findElement(By.xpath("//*[@placeholder='Enter Invoice Amount']")).click();
        driver.findElement(By.xpath("//*[@placeholder='Enter Payable Amount']")).click();
    }

    String toastmsg;
    public void saveBatch() {
        cp.waitForLoaderToDisappear();
        cp.moveToElementAndClick(saveBatch);
        toastmsg = cp.captureToastMessage();
        cp.waitForLoaderToDisappear();
    }

    public void saveBatchForDueDate() throws InterruptedException {
        cp.waitForLoaderToDisappear();

        boolean isSaved = false;
        int retryCount = 0;

        while (!isSaved && retryCount < 10) {
            cp.waitForLoaderToDisappear();
            cp.moveToElementAndClick(saveBatch);

            toastmsg = cp.captureToastMessage();
            cp.waitForPopupToDisappear();
            if (toastmsg.equals("A batch has already been created for the selected due date.")) {

                DueDate = LocalDate.parse(DueDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                        .plusDays(1)
                        .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

                SelDueDateSameAsinvoiceDate(DueDate);
                retryCount++;
            } else {
                isSaved = true;
                cp.waitForLoaderToDisappear();
            }
        }
        if (!isSaved) {
            throw new RuntimeException("Unable to save batch after " + retryCount + " retries.");
        }
    }

    public String ToastMessage() {
        return toastmsg;
    }

    public void enterInvAmt(String amount) {
        cp.waitForPopupToDisappear();
        carrier = getSelCarrier();
        WebElement invoiceAmtElement = driver.findElement(invoiceAmt1strow);
        invoiceAmtElement.click();
        invoiceAmtElement.sendKeys(amount);
        driver.findElement(invoiceComment1strow).click();
        driver.findElement(invoiceComment1strow).sendKeys("inv comment");
    }

    public String getSelCarrier() {
        return cp.getMandatoryText(carrOnpayInvoices);
    }

    public double getPayAmount() {
        return Double.parseDouble(cp.getAttributeValue(invoicePayAmount, "value"));
    }

    public void enterInvAmtforSecondRow(String amount) throws InterruptedException {
        cp.waitForPopupToDisappear();
        cp.moveToElementAndClick(invoiceAmt2ndrow);
        WebElement invoiceAmtElement = driver.findElement(invoiceAmt2ndrow);
        invoiceAmtElement.click();
        invoiceAmtElement.sendKeys(amount);
        Thread.sleep(1000);
        driver.findElement(invoiceComment2ndrow).click();
        driver.findElement(invoiceComment2ndrow).sendKeys("inv comment");
    }

    public void enterInvno() {
        cp.waitForPopupToDisappear();
        invoiceNumber = "INV-" + System.currentTimeMillis();
        List<WebElement> invoiceFields = driver.findElements(By.xpath("//*[@placeholder='Enter Invoice No']"));
        cp.moveToElementAndClick(invoiceNo1strow);
        for (int i = 0; i < 2 && i < invoiceFields.size(); i++) {
            WebElement field = invoiceFields.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('readonly')", field);
            field.click();
            field.clear();
            field.sendKeys(invoiceNumber);
            driver.findElement(invoiceAmt1strow).click();

            String text = cp.getMandatoryText(gridtext);

            if (text.equals("Manifest No")) {
                List<WebElement> LBOLcount = driver.findElements(By.xpath("//td[contains(text(),'GL-')]"));
                String data = cp.getMandatoryText(By.xpath("//table/tbody[1]/tr[" + (i + 1) + "]/td[9]"));
                invoiceData.add(data);

            } else if (text.equals("Customer Order No")) {
                String data = cp.getMandatoryText(By.xpath("//table/tbody[1]/tr[" + (i + 1) + "]/td[3]"));
                invoiceData.add(data);

            } else if (text.equals("Pickup Location")) {
                String data = cp.getMandatoryText(By.xpath("//table/tbody[1]/tr[" + (i + 1) + "]/td[6]"));
                invoiceData.add(data);
            }
        }
    }

    public void invoiceDateSelAsCurrentDate() throws InterruptedException {
        Thread.sleep(200);
        clearAndType(invDate, getCurrentDate());
        cp.waitForLoaderToDisappear();
    }

    public void SelDueDateSameAsinvoiceDate(String date) throws InterruptedException {
        Thread.sleep(200);
        clearAndType(dueDateinput, date);
        cp.waitForLoaderToDisappear();
    }

    public String getCurrentDate() {
        return new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date());
    }

    public String getRandomFutureDateNextMonth() {
        LocalDate start = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        return start.plusDays((long) (Math.random() * start.lengthOfMonth()))
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    int count = 0;
    public int getLineItemCount() {
        List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));

        for (int i = 0; i < rows.size(); i++) {
            cp.moveToElementAndClick(By.xpath("//tbody/tr[" + (i + 1) + "]//input[contains(@id,'invoiceNo')]"));
            WebElement invoiceNo = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]//input[@placeholder='Enter Invoice No']"));
            WebElement invoiceAmt = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]//input[@placeholder='Enter Invoice Amount']"));
            WebElement payableAmt = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]//input[@placeholder='Enter Payable Amount']"));

            String val1 = invoiceNo.getAttribute("value");
            String val2 = invoiceAmt.getAttribute("value");
            String val3 = payableAmt.getAttribute("value");

            if (!val1.isEmpty() || !val2.isEmpty() || !val3.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public void clearPayAmount() throws InterruptedException {
        cp.moveToElementAndClick(invoicePayAmount);
        driver.findElement(invoicePayAmount).clear();
        Thread.sleep(3000);
        driver.findElement(invoiceComment1strow).click();
        driver.findElement(invoiceComment1strow).click();
    }

    public void enterPayAmount(String amount) throws InterruptedException {
        cp.moveToElementAndClick(payableAmt);
        Thread.sleep(1000);
        driver.findElement(invoicePayAmount).clear();
        Thread.sleep(1000);
        driver.findElement(payableAmt).click();
        Thread.sleep(1000);
        driver.findElement(payableAmt).sendKeys(amount);
        driver.findElement(invoiceComment1strow).click();
        Thread.sleep(2000);
    }

    public String getPopUpMsg() {
        return cp.getMandatoryText(By.xpath("//*[@class='confirm__title pt-1 pb-3']"));
    }

    public String getFiscalPeriod() {
        return driver.findElement(By.xpath("//input[@id='APInviceSummaryfiscalPeriod']")).getAttribute("value");
    }

    public void clickOnYes() {
        try {
            cp.waitAndClickWithJS(yesBtn, 10);
        } catch (Exception e) {}
    }

    public void searchBatch() {
        cp.waitForLoaderToDisappear();
        cp.searchColoumFilter(filter, DueDate);
        cp.waitForLoaderToDisappear();
    }

    public boolean isDisplayedRadioBtn() throws InterruptedException {
        driver.findElement(filter).sendKeys(Keys.TAB);
        Thread.sleep(5000);
        cp.waitForLoaderToDisappear();
        wt.waitForElement(radioBtn, 5);
        return driver.findElement(radioBtn).isDisplayed();
    }

    public void clickOnExpandBtn() throws InterruptedException {
        Thread.sleep(500);
        driver.findElement(expandBtn).click();
    }

    public double getTotalPaybleAmount() {
        return Double.parseDouble(cp.getAttributeValue(totalPayAmount, "value"));
    }

    public double getSumOfPayAmount() {
        double sum = 0.0;
        List<WebElement> payamt = driver.findElements(By.xpath("//*[@placeholder='Enter Payable Amount']"));

        for (int i = 0; i < 2 && i < payamt.size(); i++) {
            String value = cp.getAttributeValue(By.xpath("(//*[@placeholder='Enter Payable Amount'])[" + (i + 1) + "]"),
                    "value");
            sum += Double.parseDouble(value);
        }
        return sum;
    }

    public double getBatchPayableAmt() throws InterruptedException {
        Thread.sleep(3000);
        return Double.parseDouble(cp.getMandatoryText(By.xpath("(//*[@title='Click to edit'])[last()]/ancestor::tr/td[4]")));
    }

    public double getTotalAmount() {
        return Double.parseDouble(cp.getMandatoryText(totalAmount).trim());
    }

    public double getSumOfBatchAmt() throws InterruptedException {
        Thread.sleep(2000);

        List<WebElement> row = driver.findElements(By.xpath("//*[@title='Click to edit']"));
        double total = 0;

        for (int i = 0; i < row.size(); i++) {
            List<WebElement> amounts = driver.findElements(By.xpath("(//*[@title='Click to edit'])[" + (i + 1) + "]/ancestor::tr/td[4]"));
            if (!amounts.isEmpty()) {
                String text = amounts.get(0).getText().trim();
                if (!text.isEmpty()) total += Double.parseDouble(text);
            }
        }
        return total;
    }

    public String getCarrierCode() {
        return cp.getMandatoryText(By.xpath("(//*[@title='Click to edit'])[last()]/ancestor::tr/td[3]"));
    }

    public String getExpectedCarrier() {
        return carrier.split("-")[0].trim();
    }

    public String getBatchNo() {
        wt.waitForElement(By.xpath("(//tbody[@class='p-datatable-tbody']/tr/td[5])[last()]"), 5);
        return cp.getMandatoryText(By.xpath("(//tbody[@class='p-datatable-tbody']/tr/td[5])[last()]"));
    }

    public String getInvoiceNumber() {
        wt.waitForElement(By.xpath("//span[contains(text(), 'Invoice No')]"), 10);
        return cp.getMandatoryText(By.xpath("(//tbody[@class='p-datatable-tbody']/tr/td[6])[last()]"));
    }

    public void clickOnSubExpandBtn() {
        wt.waitToClick(By.xpath("(//*[@title='Click to expand row'])[last()]"), 8);
    }

    public boolean checkInvoiceData() {
        String text = cp.getMandatoryText(By.xpath("(//*[contains(text(), 'Listing')])[2]"));

        if (text.equals("LBOL Listing")) {
            List<WebElement> LBOLrows = driver.findElements(
                    By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/table/tbody//tr"));

            for (int i = 1; i <= LBOLrows.size(); i++) {
                WebElement cell = driver.findElement(
                        By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/table/tbody//tr[" + i + "]/td[9]"));
                String data = cell.getText().trim();
                if (invoiceData.contains(data)) return true;
            }

        } else if (text.equals("Auth Listing")) {
            List<WebElement> Authrows = driver.findElements(
                    By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/table/tbody//tr"));

            for (int i = 1; i <= Authrows.size(); i++) {
                WebElement cell = driver.findElement(
                        By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/table/tbody//tr[" + i + "]/td[3]"));
                String data = cell.getText().trim();
                if (invoiceData.contains(data)) return true;
            }

        } else if (text.equals("Truck Listing")) {
            List<WebElement> rows = driver.findElements(
                    By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/table/tbody//tr"));

            for (int i = 1; i <= rows.size(); i++) {
                WebElement cell = driver.findElement(
                        By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div/div/div/div[1]/table/tbody//tr[" + i + "]/td[6]"));
                String data = cell.getText().trim();
                if (invoiceData.contains(data)) return true;
            }
        }
        return false;
    }

    public void clickOnEditBtn() {
        cp.moveToElementAndClick(By.xpath("(//*[@title='Click to edit'])[last()]"));
        cp.waitForLoaderToDisappear();
    }

    public String getCarrierFromEdit() {
        return cp.getMandatoryText(By.xpath("//*[@id='APInviceBatchEditcarrier']/span"));
    }

    public boolean isCarrierNotEditable() {
        return driver.findElement(By.xpath("//*[@id='APInviceBatchEditcarrier']")).getAttribute("class").contains("p-disabled");
    }

    public boolean isDueDateNotEditable() {
        WebElement input = driver.findElement(By.xpath("//*[@id='APInviceBatchEditdueDate']//input"));
        return !input.isEnabled();
    }

    public String getBatchNoFormEdit() {
        return cp.getAttributeValue(By.xpath("//*[@id='APInviceBatchEditinvoiceBatchNo']"), "value");
    }

    public boolean isBatchEditable() {
        WebElement input = driver.findElement(By.xpath("//*[@id='APInviceBatchEditinvoiceBatchNo']"));
        return !input.isEnabled();
    }

    public String getFiscalPeriodFormEdit() {
        return cp.getMandatoryText(By.xpath("//*[@id='APInviceBatchEditfiscalPeriod']/span"));
    }

    public boolean isFiscalPeriodNotEditable() {
        WebElement field = driver.findElement(By.id("APInviceBatchEditfiscalPeriod"));
        String classAttr = field.getAttribute("class");
        return classAttr.contains("p-disabled");
    }

    public boolean isInvoiceDateNotEditable() {
        WebElement field = driver.findElement(By.xpath("//*[@id='APInviceBatchEditinvoiceDate']//input"));
        return !field.isEnabled();
    }

    public boolean isInvoiceBatchDateNotEditable() {
        WebElement input = driver.findElement(By.xpath("//*[@id='APInviceBatchEditinvoiceBatchDate']//input"));
        return !input.isEnabled();
    }

    public boolean isInvoiceNumberEditable() {
        WebElement field = driver.findElement(invoiceNo1strow);
        return field.isEnabled();
    }

    public boolean isInvoiceAmountEditable() {
        WebElement field = driver.findElement(invoiceAmt1strow);
        return field.isEnabled();
    }

    public boolean isPayAmountEditable() {
        WebElement field = driver.findElement(invoicePayAmount);
        return field.isEnabled();
    }

    public boolean isInvoiceCommentEditable() {
        WebElement field = driver.findElement(invoiceComment1strow);
        return field.isEnabled();
    }

    public void enterInvnoForthirdRow() {
        cp.waitForPopupToDisappear();

        makeInvoiceFieldEditable(3, invoiceNumber);
        enterInvoiceAmount(invoiceAmt3rdrow, "15");

        String label = cp.getMandatoryText(gridtext);
        String data = fetchInvoiceDataByLabel(label);

        if (!data.isEmpty()) {
            invoiceData.add(data);
        }
    }

    private void makeInvoiceFieldEditable(int rowIndex, String value) {
        String xpath = "(//*[@placeholder='Enter Invoice No'])[" + rowIndex + "]";
        WebElement field = driver.findElement(By.xpath(xpath));
        cp.moveToElementAndClick(invoiceNo3rdrow);
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('readonly')", field);
        field.click();
        field.clear();
        field.sendKeys(value);
    }

    private void enterInvoiceAmount(By locator, String amount) {
        WebElement amountField = driver.findElement(locator);
        amountField.click();
        amountField.clear();
        amountField.sendKeys(amount);
        driver.findElement(invoiceComment2ndrow).click();
    }

    private String fetchInvoiceDataByLabel(String label) {
        switch (label) {
            case "Manifest No":
                return cp.getMandatoryText(By.xpath("//table/tbody[1]/tr[3]/td[9]"));
            case "Customer Order No":
                return cp.getMandatoryText(By.xpath("//table/tbody[1]/tr[3]/td[3]"));
            case "Pickup Location":
                return cp.getMandatoryText(By.xpath("//table/tbody[1]/tr[3]/td[6]"));
            default:
                return "";
        }
    }

    public double getTotalPaybleAmountFromEdit() {
        WebElement element = driver.findElement(totalpayAmountonEdit);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        String value = cp.getAttributeValue(totalpayAmountonEdit, "value");
        return Double.parseDouble(value.replace(",", "").trim());
    }

    public int getRowCountValueOnEdit() {
        WebElement element = driver.findElement(By.xpath("//*[@id='APInviceBatchEditnoOfLineItems']"));
        String text = element.getAttribute("value");
        return Integer.parseInt(text.trim());
    }

    public void enterDublicateInvno() {
        makeInvoiceFieldEditable(1, invoiceNumber);
        enterInvoiceAmount(invoiceAmt1strow, "7");
    }

    public void clickOnRadioBtn() {
        driver.findElement(radioBtn).click();
    }

    public void clickOnPayInvoice() {
        driver.findElement(By.xpath("//*[@title='Click to pay invoice']")).click();
    }

    public void clickOnPreviewInvoice() {
        driver.findElement(By.xpath("//*[@title='Click to preview invoice']")).click();
    }

    // ========= File utilities =========
    public void deleteExistingFiles() {
        File dir = downloadsRoot.toFile();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) ->
                    name.endsWith(".xlsx") || name.endsWith(".xls") || name.endsWith(".pdf"));
            if (files != null) {
                for (File file : files) {
                    try { file.delete(); } catch (Exception ignored) {}
                }
            }
        }
    }

    public boolean isFileDownloaded() throws InterruptedException {
        File dir = downloadsRoot.toFile();
        File[] filesBefore = dir.listFiles();
        int initialCount = filesBefore != null ? filesBefore.length : 0;
        int timeoutSec = 40;
        int waited = 0;

        while (waited < timeoutSec) {
            Thread.sleep(1000);
            File[] filesAfter = dir.listFiles();
            if (filesAfter != null && filesAfter.length > initialCount) {
                return true;
            }
            waited++;
        }
        return false;
    }

    public boolean isNoRecordFoundDisplayed() {
        try {
            WebElement cell = driver.findElement(By.xpath("//td[contains(text(),'Record not found.')]"));
            return cell.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void paginationOnBatchInv() throws InterruptedException {
        driver.findElement(By.xpath("//*[@class='p-column-filter-clear-button p-link']")).click();
        Thread.sleep(2000);
        cp.waitForLoaderToDisappear();
        cp.clickClearButton();
        cp.paginationTest(By.xpath("//*[@title='Click to expand row']"), 1);
        cp.waitForLoaderToDisappear();
    }

    // ========= AP Lookup =========
    public void clickOnByInvoice() {
        wt.waitToClick(byinvoice, 5);
    }

    public void selectCarrierOnByinvoice(String value) {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(selectCarrier, 5);
        cp.DrpDwnValueSel(searchval, value);
        cp.waitForLoaderToDisappear();
    }

    public void selectInvoiceNoByinvoice(String value) {
        cp.waitForLoaderToDisappear();
        driver.findElement(selectInv).click();
        cp.DrpDwnValueSel(searchval, value);
        cp.waitForLoaderToDisappear();
    }

    public void searchBtnForInv() {
        wt.waitToClick(serachBtninv, 5);
        cp.waitForLoaderToDisappear();
    }

    public double getAmountCharged() {
        return Double.parseDouble(cp.getMandatoryText(By.xpath("//table/tbody/tr/td[4]")));
    }

    public double getAmountChargedSum() {
        List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
        double total = 0.0;

        for (int i = 0; i < rows.size(); i++) {
            WebElement cell = rows.get(i).findElement(By.xpath("./td[4]"));
            String text = cell.getText().trim();
            if (!text.isEmpty()) total += Double.parseDouble(text);
        }
        return total;
    }

    public int getInvoiceGeneratedFor() {
        SoftAssert sAssert = new SoftAssert();

        for (int i = 1; i <= 3; i++) {
            String invData = cp.getMandatoryText(By.xpath("//table/tbody/tr[1]/td[" + i + "]")).trim();
            if (!invData.isEmpty()) {
                int listIndex = invoiceData.indexOf(invData);
                sAssert.assertTrue(listIndex != -1,
                        "Invoice value '" + invData + "' is not present in expected list: " + invoiceData);
                return i;
            }
        }

        sAssert.fail("No matching invoice found in the first 3 columns.");
        return -1;
    }

    public void clearByInvoiceData() {
        wt.waitToClick(clearBtninv, 5);
        cp.waitForLoaderToDisappear();
    }

    public boolean isRecordNotFoundDisplayed(int i) {
        try {
            WebElement element = driver.findElement(By.xpath("(//h6[normalize-space(.)='Record not found.'])[" + i + "]"));
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void searchInvData(int i) {
        String value = invoiceData.get(0);
        By inputLocator = null;
        By searchButtonLocator = null;

        switch (i) {
            case 1:
                inputLocator = By.xpath("//*[@placeholder='Enter LBOL No']");
                searchButtonLocator = serachBtnlbol;
                break;
            case 2:
                wt.waitToClick(bytruck, 5);
                inputLocator = By.xpath("//*[@placeholder='Enter Truck No']");
                searchButtonLocator = serachBtntruck;
                break;
            case 3:
                wt.waitToClick(byauth, 5);
                inputLocator = By.xpath("//*[@placeholder='Enter Authorization Number']");
                searchButtonLocator = serachBtnAuth;
                break;
            default:
                throw new IllegalArgumentException("Invalid search type: " + i);
        }

        driver.findElement(inputLocator).sendKeys(value);
        wt.waitToClick(searchButtonLocator, 5);
    }

    public void clickOnByBatch() throws InterruptedException {
        Thread.sleep(3000);
        wt.waitToClick(bybatch, 5);
    }

    public void selectFromAndToDate(String date) {
        driver.findElement(By.xpath("//*[@placeholder='From Date']")).sendKeys(date);
        driver.findElement(By.xpath("//*[@placeholder='To Date']")).sendKeys(date);
    }

    public void searchBtnForBatch() {
        wt.waitToClick(serachBtnBatch, 5);
        cp.waitForLoaderToDisappear();
    }

    public String getDueDate(String date) {
        return cp.getMandatoryText(By.xpath("//td[contains(text(), '" + date + "')]"));
    }

    public void clickToDwlExcel() {
        wt.waitToClick(exportExcel, 6);
    }
}
