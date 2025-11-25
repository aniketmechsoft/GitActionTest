package com.uiFramework.Genesis.web.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.helper.WaitHelper;

public class InboundPalletPage extends OrderPage {
    WebDriver driver = null;
    CommonMethods cm;
    CommonPage cp = null;
    SoftAssert softAssert = new SoftAssert();
    DropDownHelper DrpDwn;
    WaitHelper wt;
    String Pickuplocation;
    String Droplocation;
    String GLorder;
    int initialPieces;
    int initialWeight;
    int orderPieces;
    int orderWeight;
    String iGLorder;
    public static String MIBPL;
    List<String> glOrders = new ArrayList<>();
    String[] glOrdersOnpopup;
    List<String> popupGL = new ArrayList<>();
    List<String> RemovedglOrders = new ArrayList<>();
    List<String> OrderProcess = new ArrayList<>();
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(InboundPalletPage.class.getName());

    public InboundPalletPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.cm = new CommonMethods(driver);
        this.cp = new CommonPage(driver);
        this.wt = new WaitHelper(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.DrpDwn = new DropDownHelper(driver);
    }

    // ---------- Robust helpers for Safari/Chrome ----------
    private WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void safeClick(By locator) {
        try {
            waitClickable(locator).click();
        } catch (Exception e1) {
            try {
                WebElement el = waitVisible(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            } catch (Exception e2) {
                throw e1; // bubble original if JS also fails
            }
        }
    }

    private void clearAndType(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.click();
        try { el.clear(); } catch (Exception ignored) {}
        el.sendKeys(text);
    }

    private String safeGetText(By locator) {
        return waitVisible(locator).getText().trim();
    }
    // ------------------------------------------------------

	private By palletMenu = By.xpath("//i[@title='Pallet']");
	private By barMenu = By.xpath("//i[@title='Barcode Printing']");
	private By inboundpalletMenu = By.xpath("//a[@href='/pallet/inbound-pallet']");
	private By createPallet = By.xpath("//button[@title='Click to create new pallet']");
	By searchOrer = By.xpath("(//input[@placeholder='Contains...'])[1]");
	By checkbox = By.xpath("//*[@class='p-checkbox p-component']");
	By heighlighChekBox = By.xpath("//*[@class='p-checkbox p-component p-highlight']");
	private By getpickuplocation = By.xpath("//tr[2]/td[15]");
	private By droplocation = By.xpath("//tr[2]/td[16]");
	By getGLorder = By.xpath("(//tr[2]/td[2])[1]");
	private By filtericon = By.xpath("(//*[@class='ml-1 cursor-pointer'])[8]");
	private By selectedFiletr = By.xpath("(//*[@class='ml-1 cursor-pointer'])[8]");
	private By searchPickup = By.xpath("(//input[@class='p-column-filter p-inputtext p-component bordius0px'])[7]");
	private By searcDrop = By.xpath("(//input[@class='p-column-filter p-inputtext p-component'])[7]");
	By notContains = By.xpath("//li[text()='Not Contains']");
	private By contains = By.xpath("//li[text()='Contains']");
	private By glorder = By.xpath("(//td[@class='p-frozen-column'])[1]");
	private By mergeBtn = By.id("inboundPalletOrderMergeBTN");
	private By createPBtn = By.xpath("//button[@title='Click to create different pallets of orders']");
	private By createpalletBtn = By.id("inboundPalletOrderCreatePalletBTN");
	private By popupsaveBtn = By.xpath("//button[@id='inboundPalletOrderSaveBTN']");
	private By tost = By.xpath("//div[@role='alert']//div[2]");
	private By closepopup = By.xpath("//*[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit text-white css-1amtie4']");
	private By editBtn = By.xpath("(//button[@id='inboundPalletEditBtn'])[1]");
	private By deleteBtn = By.xpath("(//button[@title='Delete Pallet'])[1]");
	private By getPalletNoonList = By.xpath("(//table[1]/tbody[1]/tr[2]/td[2])[1]");
	private By multicheckbx = By.xpath("(//div[@class='datatable-wrapper'])[1]//tr[1]//td[1]");// Increase tr in loop
	By rmulticheckbx = By.xpath("(//div[@class='datatable-wrapper'])[2]//tr[1]//td[1]");// Increase tr in loop
	private By multiGLorder = By.xpath("(//div[@class='datatable-wrapper'])[1]//td[contains(text(), 'GL-')]");// get order
	private By searchstatus = By.xpath("(//input[@placeholder='Contains...'])[9]");
	private By clearfilter = By.xpath("//*[@data-pc-section='clearicon']");
	// edit pallet xpath
	private By multiOrderprocess = By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[1]//td[3]");// Increase tr in loop
	private By removePallet = By.xpath("//button[@id='inboundPalletDetailsRemoveOrderMappingBtn']");
	private By addPallet = By.xpath("//button[@id='inboundPalletDetailsMapOrdersToPalletBtn']");
	private By yesBtn = By.id("confmSubmit");
	By noBtn = By.id("confmNo");
	private By searchContain = By.xpath("(//input[@placeholder='Contains...'])[1]");
	private By Norecord = By.xpath("//tr[@class='p-datatable-emptymessage']");
	private By totalPieces = By.xpath("(//div[@class='labelBoxK__labelData'])[3]");
	private By totalWeight = By.xpath("(//div[@class='labelBoxK__labelData'])[4]");
	By palletPickup = By.xpath("(//div[@class='labelBoxK__labelData'])[5]");
	By palletDrop = By.xpath("(//div[@class='labelBoxK__labelData'])[6]");
	private By backBtn = By.xpath("//button[@title='Click to go back to previous page']");
	By nextPagination = By.xpath("//button[@class='p-paginator-next p-paginator-element p-link']");
	By backPagination = By.xpath("(//button[@aria-label='Page 1'])[1]");
	private By orderInfo = By.xpath("(//div[@title='Click to see orders present in pallet'])[1]");
	By closeInfo = By.xpath("//*[@data-testid='CloseIcon']");
	By infoGrid = By.xpath("//*[@id=\"genesis\"]/div[4]//table/tbody/tr/td[1]");// only gl order
	private By apalletqty = By.xpath("(//input[@id='inboundPalletDetailslblPalletQty'])[1]");// addQty
	private By rpalletqty = By.xpath("(//input[@id='inboundPalletDetailslblPalletQty'])[2]");// removeQty
	private By palletQty = By.xpath("//tr[1]//td[3]");
	private By getPalletNo = By.xpath("//div[@class='labelBoxK__labelData' and contains(text(), 'IBPL-')]");
	private By trcukNo = By.xpath("(//div[@class='labelBoxK__labelData'])[7]");
	private By palletStatus = By.xpath("(//div[@class='labelBoxK__labelData'])[9]");
	private By searchSection = By.xpath("(//div[@class='d-flex align-items-center'])[1]");
	private By getTruckNo = By.xpath("//table[1]/tbody[1]/tr[1]/td[11]");
	private By selectpickup = By.xpath("(//*[.='Select Pickup Location'])[3]");
	By loader = By.xpath("//*[@src='/static/media/loading.db43a6dd94d78914920a.gif']");
	private By selectstatusippage = By.xpath("(//*[.='2 items selected'])[3]");
	private By QAdone = By.xpath("(//div[@class='p-multiselect-checkbox'])[4]");
	private By closestatusdropdown = By.xpath("//*[@class='p-multiselect-close p-link']");

    public void inboundMenu() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(barMenu, 10);
        wt.waitToClick(palletMenu, 10);
        safeClick(inboundpalletMenu);
        cp.waitForLoaderToDisappear();
    }

    public String getMGLorder() {
        return cp.getMandatoryText(getGLorder);
    }

    public void searchOrder() {
        cp.waitForLoaderToDisappear();
        cp.searchColoumFilter(searchContain, MGLOrderno);
    }

    public void searchOrd(String ordNo) {
        cp.waitForLoaderToDisappear();
        cp.searchColoumFilter(searchContain, ordNo);
    }

    public void createPalletforMorder() throws InterruptedException {
    	Thread.sleep(2000);
        wt.waitToClick(checkbox, 15);
        wt.waitToClick(createPBtn, 15);
        wt.waitToClick(popupsaveBtn, 15);
        cp.waitForLoaderToDisappear();
    }

    public void getMIBPL() {
        MIBPL = cp.getMandatoryText(By.xpath("//table/tbody/tr[2]/td[2]"));
        logger.info("Created IBPL is " + MIBPL);
    }

    public int getTotalEntriesCount(WebDriver driver) {
        cp.waitForLoaderToDisappear();
        WebElement paginatorText = driver.findElement(By.xpath("(//*[@class='v-sected spanTag'])[1]"));
        String text = paginatorText.getText();
        String countStr = text.replaceAll(".*of\\s+(\\d+).*", "$1");
        logger.info("Total count" + countStr);
        return Integer.parseInt(countStr);
    }

    public void createPallet() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        cp.waitForPopupToDisappear();
        wt.waitToClick(createPallet, 15);
        cp.waitForLoaderToDisappear();
    }

    public void selectCheckbox() throws TimeoutException {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(checkbox, 8);
        getGLorder();
    }

    public String getGLorder() {
        return cp.getMandatoryText(getGLorder);
    }

    public void searchPickupLocation() throws TimeoutException, InterruptedException {
        Pickuplocation = safeGetText(getpickuplocation);
        dropLocation();
        safeClick(filtericon);
        safeClick(notContains);
        try {
            wt.waitForElement(loader, 3);
            cp.waitForLoaderToDisappear();
        } catch (Exception e) {
            logger.info("Showing error while searching column filter");
        }
        clearAndType(searchPickup, Pickuplocation);
    }

    public void searchDropLocation() throws TimeoutException, InterruptedException {
        dropLocation();
        cp.waitForLoaderToDisappear();
        clearAndType(searchPickup, Droplocation);
    }

    public void mergeBtn() {
        try {
            safeClick(mergeBtn);
            cp.waitAndClickWithJS(popupsaveBtn, 3);
        } finally {
            cp.closePopupIfPresent("//*[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit text-white css-1amtie4']");
        }
    }

    public void clickonContains() throws TimeoutException, InterruptedException {
        safeClick(checkbox);
        waitVisible(selectedFiletr);
        cp.waitAndClickWithJS(selectedFiletr, 4);
        safeClick(contains);
        try {
            wt.waitForElement(loader, 3);
            cp.waitForLoaderToDisappear();
        } catch (Exception e) {
            logger.info("Showing error while searching column filter");
        }
    }

    public String dropLocation() {
        Droplocation = safeGetText(droplocation);
        return Droplocation;
    }

    public void selectmulcheckBox() throws InterruptedException {
        wt.waitToClick(checkbox, 10);

        List<WebElement> checkboxes = driver
                .findElements(By.xpath("//td[@class='p-selection-column p-frozen-column']"));

        for (int i = 0; i < 3 && i < checkboxes.size(); i++) {
            checkboxes.get(i).click();

            List<WebElement> glOrderElements = driver.findElements(By.xpath("(//td[@class='p-frozen-column'])"));
            if (i < 3 && i < glOrderElements.size()) {
                String glOrder = glOrderElements.get(i).getText().trim();
                glOrders.add(glOrder);
                logger.info("GL Orders: " + glOrders);
            }
        }

        logger.info("GL Orders1: " + glOrders);
        wt.waitToClick(mergeBtn, 10);
        Thread.sleep(2000);

        WebElement glOrderCell = driver.findElement(
                By.xpath("//tbody[@class='p-datatable-tbody']//tr[@role='row']/td[1][contains(text(), 'GL-')]"));
        String glOrderText = glOrderCell.getText();
        glOrdersOnpopup = Arrays.stream(glOrderText.split(",")).map(String::trim).toArray(String[]::new);
        matchOrders();
        cp.clickToClosePopUp();
    }

    private void matchOrders() {
        SoftAssert sAssert = new SoftAssert();

        for (String glOrder : glOrders) {
            sAssert.assertTrue(Arrays.asList(glOrdersOnpopup).contains(glOrder),
                    "GL Order not found in popup: " + glOrder);
        }
        cp.waitAndClickWithJS(popupsaveBtn, 3);
        logger.info("All GL orders matched. Save button clicked.");
        cp.captureToastMessage();
        sAssert.assertAll();
    }

    public String getInboundPallet() {
        logger.info("IBPL " + safeGetText(getPalletNo));
        return safeGetText(getPalletNo);
    }

    public void editBtn() {
        cp.waitForLoaderToDisappear();
        safeClick(editBtn);
        cp.waitForLoaderToDisappear();
    }

    public void deleteBtn() {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(closeInfo, 30);
        wt.waitToClick(deleteBtn, 10);
        cp.waitAndClickWithJS(yesBtn, 10);
        cp.waitForLoaderToDisappear();
    }

    public void deleteBtnDirect() {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(deleteBtn, 10);
        cp.waitAndClickWithJS(yesBtn, 5);
        cp.waitForLoaderToDisappear();
    }

    public void checkDeletedOrderAvailable() {
        WebElement orderinfo = driver
                .findElement(By.xpath("(//div[@title='Click to see orders present in pallet'])[1]"));
        orderinfo.click();
        iGLorder = cp.getMandatoryText(infoGrid);
    }

    public String iGLorder() {
        return iGLorder;
    }

    public void search1Order(String order) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        cp.searchColoumFilter(searchOrer, order);
    }

    public void verifyPickndDrop() {
        String palletpickup = cp.getMandatoryText(palletPickup);
        String palletdrop = cp.getMandatoryText(palletDrop);

        softAssert.assertEquals(Pickuplocation, palletpickup,
                "Pickup location mismatch! Expected: " + Pickuplocation + ", Found: " + palletpickup);

        softAssert.assertEquals(Droplocation, palletdrop,
                "Drop location mismatch! Expected: " + Droplocation + ", Found: " + palletdrop);

        logger.info("Pickup and drop locations compared. Verifying assertions...");
        softAssert.assertAll();
    }

    public String pickupLocation() {
        return cp.getMandatoryText(palletPickup);
    }

    public void checkRemovedOrders() {
        glOrders.clear();
        List<WebElement> glOrderCells = driver
                .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]"));
        if (glOrderCells.size() > 1) {
            for (int i = glOrderCells.size(); i >= 2; i--) {
                try {
                    WebElement cb = driver
                            .findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[1]"));
                    cb.click();

                    List<WebElement> glOrderElements = driver
                            .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[2]"));
                    for (WebElement element : glOrderElements) {
                        glOrders.add(element.getText());

                        initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
                        initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

                        orderPieces = Integer.parseInt(driver
                                .findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[8]"))
                                .getText().trim());
                        orderWeight = Integer.parseInt(driver
                                .findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//tr[" + i + "]//td[9]"))
                                .getText().trim());

                        int FinalPieces = initialPieces - orderPieces;
                        int FinalWeight = initialWeight - orderWeight;

                        wt.waitToClick(removePallet, 10);
                        cp.waitForLoaderToDisappear();
                        cp.captureToastMessage();
                        cp.waitForPopupToDisappear();

                        initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
                        initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

                        logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
                        logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

                        Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final Pieces!");
                        Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
                    }
                    logger.info("Removing GL Orders: " + glOrders);

                } catch (NoSuchElementException e) {
                    logger.info("Pallet edit exception" + e);
                }
            }

            List<WebElement> glOrderRemoved = driver
                    .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'GL-')]"));
            if (glOrderRemoved.isEmpty()) {
                logger.info("No GL orders found in removed orders table.");
            } else {
                for (WebElement glorder : glOrderRemoved) {
                    RemovedglOrders.add(glorder.getText());
                }
            }
            for (String Glorder : glOrders) {
                softAssert.assertTrue(RemovedglOrders.contains(Glorder), "Missing order: " + Glorder);
            }
            checkOrderProsessForInbound();
            RemovelastOrderFrompallet();
            cp.clickToClosePopUp();
        } else {
            logger.info("Order not available to remove from pallet");
            checkOrderProsessForInbound();
            RemovelastOrderFrompallet();
            cp.clickToClosePopUp();
        }
        softAssert.assertAll();
    }

    public void RemovelastOrderFrompallet() {
        safeClick(multicheckbx);
        getPalletorder();
        safeClick(removePallet);
        cp.waitAndClickWithJS(yesBtn, 10);
    }

    public String getPalletorder() {
        GLorder = cp.getMandatoryText(multiGLorder);
        logger.info("GLorder");
        return GLorder;
    }

    public void searchData(String data) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        cp.searchColoumFilter(searchContain, data);
    }

    public String noRecordFound() {
        return driver.findElement(Norecord).getText();
    }

    public boolean checkElementNorecord() {
        try {
            WebElement norecord = driver.findElement(Norecord);
            return norecord.isDisplayed();

        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void checkOrderProsessForInbound() {
        List<WebElement> orderProcess = driver
                .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'Re')]"));
        if (orderProcess.isEmpty()) {
            logger.info("No GL Order found for Order Process");
        } else {
            for (WebElement orderType : orderProcess) {
                logger.info("Checking Order Process..");
                String text = orderType.getText();
                OrderProcess.add(text);

                if (text.equalsIgnoreCase("Reconsignment")) {
                    Assert.fail("GL Order contains 'Reconsignment', which is not allowed.");
                }
            }
        }
    }

    public void backBtn() throws InterruptedException {
        Thread.sleep(2000); // keep minimal fixed wait to allow animation settle on Safari
        cp.waitForLoaderToDisappear();
        cp.moveToElementAndClick(backBtn);
        cp.waitForLoaderToDisappear();
    }

    public void selectMulcheckBoxForCretepallet() {
        glOrders.clear();
        List<WebElement> checkboxes = driver
                .findElements(By.xpath("//td[@class='p-selection-column p-frozen-column']//input[@type='checkbox']"));
        for (int i = 0; i < 3 && i < checkboxes.size(); i++) {
            try {
                if (i >= checkboxes.size())
                    break;

                checkboxes.get(i).click();
                List<WebElement> glOrderElements = driver.findElements(By.xpath("//td[@class='p-frozen-column']"));
                if (i < 3 && i < glOrderElements.size()) {
                    String glOrder = glOrderElements.get(i).getText().trim();
                    glOrders.add(glOrder);
                    logger.info("GL Orders: " + glOrders);
                }
            } catch (Exception e) {
                logger.info("Checkbox at index " + i + " not clickable: " + e.getMessage());
            }
        }

        logger.info("GL Orders: " + glOrders);

        safeClick(nextPagination);
        cp.waitForLoaderToDisappear();

        safeClick(backPagination);
        cp.waitForLoaderToDisappear();

        List<WebElement> refreshedCheckboxes = driver
                .findElements(By.xpath("//td[@class='p-selection-column p-frozen-column']//input[@type='checkbox']"));

        for (int i = 0; i < 3 && i < refreshedCheckboxes.size(); i++) {
            Assert.assertTrue(refreshedCheckboxes.get(i).isSelected(), "Checkbox is NOT selected after pagination.");
        }

        safeClick(createPBtn);

        List<WebElement> glOrderc = driver.findElements(
                By.xpath("//tbody[@class='p-datatable-tbody']//tr[@role='row']/td[1][contains(text(), 'GL-')]"));
        for (WebElement element : glOrderc) {
            popupGL.add(element.getText().trim());
        }
        logger.info("Popup GL" + popupGL);

        matchPopOrders();
        cp.clickToClosePopUp();
    }

    public void matchPopOrders() {
        try {
            SoftAssert sAssert = new SoftAssert();
            for (String glOrder : glOrders) {
                logger.info("Before Orders: " + glOrder);

                boolean found = popupGL.stream().anyMatch(popupOrder -> popupOrder.trim().equalsIgnoreCase(glOrder.trim()));

                sAssert.assertTrue(found, "GL Order not found in popup: " + glOrder);

                logger.info("After Orders: " + glOrder);
            }
            wt.waitToClick(popupsaveBtn, 5);
            logger.info("All GL orders matched. Save button clicked.");
            cp.captureToastMessage();
            sAssert.assertAll();
        } finally {
            cp.closePopupIfPresent("(//*[@data-testid='HighlightOffIcon'])[2]");
        }

    }

    public void orderInfo() {
        SoftAssert sAssert = new SoftAssert();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        cp.waitForLoaderToDisappear();
        for (int i = 0; i < 3; i++) {
            try {
                List<WebElement> orderinfo = driver
                        .findElements(By.xpath("//div[@title='Click to see orders present in pallet']"));
                if (i >= orderinfo.size()) {
                    logger.info("No more order info icons to click at index: " + i);
                    break;
                }
                wait.until(ExpectedConditions.visibilityOf(orderinfo.get(i)));
                orderinfo.get(i).click();
                String iGLorder = cp.getMandatoryText(infoGrid);

                sAssert.assertTrue(popupGL.contains(iGLorder), "GL Order '" + iGLorder + "' not found in popupGL list.");
                wait.until(ExpectedConditions.visibilityOfElementLocated(closeInfo));
                wt.waitToClick(closeInfo, 30);

            } catch (Exception e) {
                logger.info("Error at index " + i + ": " + e.getMessage());
                logger.info("Order info method error");
            }
            sAssert.assertAll();
        }
    }

    public void checkAddedOrdersinPallet() {
        SoftAssert sAssert = new SoftAssert();
        RemovedglOrders.clear();
        glOrders.clear();
        List<WebElement> glOrderCells = driver
                .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'GL-')]"));
        if (glOrderCells.size() > 0) {
            for (int i = 3; i >= 1; i--) {
                try {
                    WebElement cb = driver
                            .findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[1]"));
                    cb.click();

                    List<WebElement> glOrderElements = driver
                            .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[2]"));
                    for (WebElement element : glOrderElements) {
                        glOrders.add(element.getText());

                        initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
                        initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

                        orderPieces = Integer.parseInt(driver
                                .findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[8]"))
                                .getText().trim());
                        orderWeight = Integer.parseInt(driver
                                .findElement(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//tr[" + i + "]//td[9]"))
                                .getText().trim());

                        int FinalPieces = initialPieces + orderPieces;
                        int FinalWeight = initialWeight + orderWeight;

                        safeClick(addPallet);
                        cp.captureToastMessage();
                        cp.waitForLoaderToDisappear();
                        cp.waitForPopupToDisappear();

                        initialPieces = Integer.parseInt(driver.findElement(totalPieces).getText().trim());
                        initialWeight = Integer.parseInt(driver.findElement(totalWeight).getText().trim());

                        logger.info("Expected Final Pieces: " + FinalPieces + ", Actual: " + initialPieces);
                        logger.info("Expected Final Weight: " + FinalWeight + ", Actual: " + initialWeight);

                        Assert.assertEquals(initialPieces, FinalPieces, "Mismatch in Total and final weight!");
                        Assert.assertEquals(initialWeight, FinalWeight, "Mismatch in Total and final weight!");
                    }

                } catch (NoSuchElementException e) {
                    logger.info("Pallet edit exception" + e);
                }
            }
            List<WebElement> glOrderAdded = driver
                    .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]"));
            if (glOrderAdded.isEmpty()) {
                logger.info("No GL orders found in Added grid orders table.");
            } else {
                for (WebElement glorder : glOrderAdded) {
                    RemovedglOrders.add(glorder.getText());
                }
            }
            for (String Glorder : glOrders) {
                sAssert.assertTrue(RemovedglOrders.contains(Glorder), "Missing Added order: " + Glorder);
                logger.info("Checked Order available in Added grid");
            }

        } else {
            logger.info("Order not available to Add in pallet");
        }
        sAssert.assertAll();
    }

    public void checkNoBtnonPopup() {
        SoftAssert sAssert = new SoftAssert();
        cp.waitForLoaderToDisappear();
        List<WebElement> glOrderCells = driver
                .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[1]//td[contains(text(), 'GL-')]"));
        if (glOrderCells.size() > 1) {
            logger.info("More than one GL order found. Skipping checkNoBtnonPopup method.");
            return;
        }

        String inboundPallet = getInboundPallet();
        safeClick(multicheckbx);
        getPalletorder();
        safeClick(removePallet);
        cp.waitAndClickWithJS(noBtn, 4);
        logger.info("checking No button on Pop up");
        sAssert.assertTrue(!getInboundPallet().isEmpty(), "Inbound pallet should not be empty.");
        sAssert.assertTrue(!getPalletorder().isEmpty(), "Pallet order should not be empty.");
        sAssert.assertAll();
    }

    public boolean searchOrders() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        for (String order : RemovedglOrders) {
            cp.searchColoumFilter(searchOrer, order);
            cp.waitForLoaderToDisappear();
            try {
                WebElement emptyRow = driver.findElement(By.xpath("//tr[@class='p-datatable-emptymessage']"));
                if (!emptyRow.isDisplayed()) {
                    return false;
                }
            } catch (NoSuchElementException e) {
                return false;
            }
        }
        return true;
    }

    public String palletStatus() {
        return cp.getMandatoryText(palletStatus);
    }

    public void checkPalletStatus() {
        String truck = cp.getMandatoryText(trcukNo);

        if (truck.isEmpty()) {
            Assert.assertEquals(
                    palletStatus().toLowerCase(),
                    ReadJsonData.getNestedValue("palletStatus", "expected").toLowerCase(),
                    ReadJsonData.getNestedValue("palletStatus", "message"));
        } else {
            Assert.assertEquals(
                    palletStatus(),
                    ReadJsonData.getNestedValue("palletStatusLoad", "expected"),
                    ReadJsonData.getNestedValue("palletStatusLoad", "message"));
        }
    }

    public void removedPalletQty(String qty) throws InterruptedException {
        checkPalletStatus();
        List<WebElement> glOrderCells = driver
                .findElements(By.xpath("(//div[@class='p-datatable-wrapper'])[2]//td[contains(text(), 'GL-')]"));

        if (!glOrderCells.isEmpty()) {

            safeClick(rmulticheckbx);
            // force validation with blank/backspace then add
            WebElement rp = waitVisible(rpalletqty);
            rp.sendKeys(Keys.BACK_SPACE);
            safeClick(addPallet);
            softAssert.assertEquals(
                    cp.captureToastMessage(),
                    ReadJsonData.getNestedValue("PalletQty", "expected"),
                    ReadJsonData.getNestedValue("PalletQty", "message"));
            addPalletQty(qty);

        } else {
            throw new SkipException("Skipped because no GL- orders available.");
        }
    }

    public void addPalletQty(String qty) {
        cp.waitForLoaderToDisappear();
        clearAndType(rpalletqty, qty);
        safeClick(addPallet);
        cp.waitForPopupToDisappear();
        softAssert.assertEquals(getPalletQtyAdd(), getPalletQtyRemove(), "Pallet quantity not same");
        softAssert.assertAll();
    }

    public String getPalletQtyAdd() {
        WebElement qtyElement = driver.findElement(apalletqty);
        return qtyElement.getAttribute("value");
    }

    public String getPalletQtyRemove() {
        WebElement qtyElement = driver.findElement(rpalletqty);
        return qtyElement.getAttribute("value");
    }

    public String getPalletQty() {
        return cp.getMandatoryText(palletQty);
    }

    public void searchAndValidatePalletNo() throws InterruptedException {
        cp.clickClearButton();
        try {
            cp.searchAndValidatePalletNo(getPalletNoonList); // assuming exists in CommonPage
        } catch (Exception e) {
            logger.info("Pallet no filter skip..");
        }
    }

    public void searchAndValidateTruckNo() throws InterruptedException {
        cp.clickClearButton();
        try {
            cp.searchAndValidateTruckNo(getTruckNo);
        } catch (Exception e) {
            logger.info("Truck number filter skip..");
        }
    }

    public void searchQAdone(String done) {
        cp.waitForLoaderToDisappear();
        cp.searchColoumFilter(searchstatus, done);
    }

    public void clearIconndSearch() {
        safeClick(clearfilter);
        cp.waitForLoaderToDisappear();
    }

    public void selectQAdoneStatus() {
        safeClick(selectstatusippage);
        safeClick(QAdone);
        safeClick(closestatusdropdown);
    }

    public WebElement verifyRemovePalleBtnDisable() {
        return driver.findElement(removePallet);
    }

    public WebElement verifyAddPalleBtnDisable() {
        return driver.findElement(addPallet);
    }

    public void paginationOnListing() {
        cp.clickClearButton();
        cp.paginationTest(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'IBPL-')]"), 2);
    }

    public void paginationOnCeatePallet() {
        cp.clickClearButton();
        cp.paginationTest(By.xpath("//td[@class='p-frozen-column' and contains(text(), 'GL-')]"), 2);
    }

    public void checkColoumFilter() throws InterruptedException, IOException {
        String commonPath = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "configfile", "Locators.JSON"
        ).toString();

        String jsonContent = Files.readString(Paths.get(commonPath));
        JSONObject root = new JSONObject(jsonContent);

        String pageName = "inboundPalletPage";

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
                    cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
                            columnIndex);
                } else if (method.equals("numeric")) {
                    cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
                            columnIndex);
                } else if (method.equals("date")) {
                    cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
                            columnIndex);
                }
            }
        } else {
            logger.info("Page not found: " + pageName);
        }
    }

    public void checkColoumFilterCreatePage() throws InterruptedException, IOException {

        String commonPath = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "configfile", "Locators.JSON"
        ).toString();

        String jsonContent = Files.readString(Paths.get(commonPath));
        JSONObject root = new JSONObject(jsonContent);

        String pageName = "inboundPalletCreatePage";

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
                    cp.validateColumnFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
                            columnIndex);
                } else if (method.equals("numeric")) {
                    cp.validateNumericFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
                            columnIndex);
                } else if (method.equals("date")) {
                    cp.validateDateFilters(firstRowXPath, filterIconXPath, filterInputXPath, tableRowsXPath,
                            columnIndex);
                }
            }
        } else {
            logger.info("Page not found: " + pageName);
        }
    }

    public void selectMultiplestatusByIndex(List<Integer> indices) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        cp.selectMultipleByIndex(selectstatusippage, indices, "Status");
    }
}
