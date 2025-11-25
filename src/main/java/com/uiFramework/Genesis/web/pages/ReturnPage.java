package com.uiFramework.Genesis.web.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.helper.DropDownHelper;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;

public class ReturnPage {
    WebDriver driver = null;
    CommonPage cp;
    DropDownHelper DrpDwn;
    WaitHelper wt;
    JavaScriptHelper js;
    private WebDriverWait wait;

    public ReturnPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.cp = new CommonPage(driver);
        this.wt = new WaitHelper(driver);
        this.DrpDwn = new DropDownHelper(driver);
        this.js = new JavaScriptHelper(driver);
    }

    private By returnMenu = By.xpath("//i[@title='Return']");
    private By returnn = By.xpath("//a[@href='/return']");
    private By addNewReturnBtn = By.id("returnListbtnAddNewReturn");
    private By ordLookup = By.id("returnOrdeDetailsbtnOrderLookup");
    private By ordNoonLookUp = By.id("returnOrdeDetailsorderNo");

    // shared dropdown search box (Prime-like overlay)
    private By listOption2 = By.xpath("/html/body/div[4]/div/ul/li[2]");
    private By DrpDwnVal1 = By.xpath("/html/body/div[4]/div/ul/li/div/input");
    private By recnsignConsigneeDrpDwn = By.id("masterExternalUserDetreconsignedToConsignee");

    private By statusFiltr = By.xpath("//table/thead/tr[2]/th[19]/div/div/input");

    private static final int DEFAULT_TIMEOUT = 30;

    /** Helpers */
    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private List<WebElement> tableRows() {
        return driver.findElements(By.xpath("//*[@id='panel1bh-content']//table/tbody/tr"));
    }

    /** First row whose td[14] (LBOL?) is non-empty → capture GL- value (td[4]) */
    private void getStatusValue(int arrLen, List<String> firstUniqueText) {
        List<WebElement> rows = tableRows();
        if (rows.isEmpty()) return;

        for (WebElement row : rows) {
            String lbolVal = row.findElement(By.xpath(".//td[14]")).getText().trim();
            if (!lbolVal.isEmpty()) {
                scrollIntoView(row);
                String runningVal = row.findElement(By.xpath(".//td[4]")).getText().trim();
                if (arrLen < firstUniqueText.size()) firstUniqueText.set(arrLen, runningVal);
                else firstUniqueText.add(runningVal);
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

    public void returnMenuWithExpand() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(returnMenu));
        js.executeScript("arguments[0].click();", menu);

        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(returnn));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", link);
        wait.until(ExpectedConditions.elementToBeClickable(returnn)).click();
        cp.waitForLoaderToDisappear();
    }

    public void addNewReturnOrder() throws InterruptedException {
        cp.waitForPopupToDisappear();
        cp.waitForLoaderToDisappear();
        wt.waitToClick(addNewReturnBtn, DEFAULT_TIMEOUT);
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

    /** Type into reconsign-to-consignee dropdown (used by screen) */
    public void reconsignToConsigneeValidation(String consignee) {
        cp.waitForLoaderToDisappear();
        cp.clickElement(recnsignConsigneeDrpDwn);
        cp.clickAndSendKeys(DrpDwnVal1, consignee);
        cp.waitForLoaderToDisappear();
    }

    /** Better: try reading value from an input if present, fallback to text */
    public String getOriginalConsignee() {
        try {
            return cp.getAttributeValue(By.xpath("(//input[@id='masterExternalUserDetreconsignedToConsignee'])[1]"), "value");
        } catch (Exception ignore) {
            return driver.findElement(By.xpath("(//div/div[8]/div/div)[1]")).getText().trim();
        }
    }

    public void selectLBOL(String consignee) {
        cp.waitForLoaderToDisappear();
        cp.clickElement(recnsignConsigneeDrpDwn);
        cp.clickAndSendKeys(DrpDwnVal1, consignee);
        // pick 2nd suggestion if list is present
        try { wt.waitToClick(listOption2, 3); } catch (Exception ignored) {}
        cp.waitForLoaderToDisappear();
    }

    public int checkDrpDwnEleCount() {
        return driver.findElements(By.xpath("/html/body/div[4]/div/ul/li")).size();
    }

    private void ordStatusFiltr(String val) {
        cp.waitForLoaderToDisappear();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(statusFiltr));
        input.clear();
        input.sendKeys(val);
        cp.waitForLoaderToDisappear();
    }

    public void getValidationData(int arrLen, String ordStatus, List<String> validationOrdNo) throws InterruptedException {
        cp.waitForLoaderToDisappear();
        ordStatusFiltr(ordStatus);
        getStatusValueForValidation(arrLen, validationOrdNo);
        cp.waitForLoaderToDisappear();
    }

    private void getStatusValueForValidation(int arrLen, List<String> validationOrdNo) {
        List<WebElement> rows = tableRows();
        if (rows.isEmpty()) return;

        for (WebElement row : rows) {
            scrollIntoView(row);
            String runningVal = row.findElement(By.xpath(".//td[4]")).getText().trim();
            if (!runningVal.isEmpty()) {
                if (arrLen < validationOrdNo.size()) validationOrdNo.set(arrLen, runningVal);
                else validationOrdNo.add(runningVal);
                return;
            }
        }
    }

    /** First row whose td[14] is EMPTY → capture GL- value (td[4]) */
    private void getStatusValueForTrkingValidation(int arrLen, List<String> trkingValidationOrdNo) {
        List<WebElement> rows = tableRows();
        if (rows.isEmpty()) return;

        for (WebElement row : rows) {
            String lbolVal = row.findElement(By.xpath(".//td[14]")).getText().trim();
            if (lbolVal.isEmpty()) {
                scrollIntoView(row);
                String runningVal = row.findElement(By.xpath(".//td[4]")).getText().trim();
                if (arrLen < trkingValidationOrdNo.size()) trkingValidationOrdNo.set(arrLen, runningVal);
                else trkingValidationOrdNo.add(runningVal);
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

    public void actualWtValidation() {
        SoftAssert sAssert = new SoftAssert();
        WebElement weightInput = driver.findElement(By.xpath("//*[@id='returnMetricsDetailsactualWeight']/input"));

        Actions actions = new Actions(driver);
        actions.scrollToElement(weightInput).perform();

        String originalVal = weightInput.getAttribute("value");
        weightInput.clear();
        weightInput.sendKeys("0");

        cp.save();
        cp.waitForLoaderToDisappear();

        WebElement cElement = driver.findElement(By.xpath("(//*[@id='panel1bh-content']/div/div/div/div/div[1]/div/div/div/div)[4]"));
        String errorMsg = cElement.getText().trim();
        sAssert.assertEquals(errorMsg, "Actual Weight should be greater than 0", "Validation message mismatch!");

        weightInput.clear();
        weightInput.sendKeys(originalVal);
    }

    public void litNoOfPiecesValidation() {
        SoftAssert sAssert = new SoftAssert();

        WebElement piecesInput = driver.findElement(By.xpath("//*[@id='returnMetricsDetailsliteratureNoOfPieces']/input"));

        Actions actions = new Actions(driver);
        actions.scrollToElement(piecesInput).perform();
        actions.moveToElement(piecesInput).click().perform();

        String originalVal = piecesInput.getAttribute("value");
        piecesInput.clear();
        piecesInput.sendKeys("0");

        cp.save();
        cp.waitForLoaderToDisappear();

        WebElement cElement = driver.findElement(
                By.xpath("//*[starts-with(normalize-space(text()), 'Literature No Of Pieces should be')]"));
        String errorMsg = cElement.getText().trim();

        sAssert.assertEquals(errorMsg, "Literature No Of Pieces should be greater than 0", "Validation message mismatch!");

        piecesInput.clear();
        piecesInput.sendKeys(originalVal);
    }

    public void numOfPiecesValidation() {
        SoftAssert sAssert = new SoftAssert();

        WebElement elementA = driver.findElement(By.xpath("//*[@id='returnOrdeDetailsLGOPieces']/input"));
        String attrVal = elementA.getAttribute("value").trim();
        int intVal = Integer.parseInt(attrVal);

        WebElement elementB = driver.findElement(By.xpath("//*[@id='returnMetricsDetailsnoOfPieces']/input"));

        Actions actions = new Actions(driver);
        actions.scrollToElement(elementB).perform();
        actions.moveToElement(elementB).click().perform();

        elementB.clear();
        elementB.sendKeys(String.valueOf(intVal + 1));

        cp.save();
        cp.waitForLoaderToDisappear();

        elementB.clear();
        elementB.sendKeys(attrVal);
    }

    public void litNumOfPiecesValidation2() {
        SoftAssert sAssert = new SoftAssert();

        WebElement elementA = driver.findElement(By.xpath("//*[@id='returnOrdeDetailsLGOLiteraturePieces']/input"));
        String attrVal = elementA.getAttribute("value").trim();
        int intVal = Integer.parseInt(attrVal);

        WebElement elementB = driver.findElement(By.xpath("//*[@id='returnMetricsDetailsliteratureNoOfPieces']/input"));

        Actions actions = new Actions(driver);
        actions.scrollToElement(elementB).perform();
        actions.moveToElement(elementB).click().perform();

        elementB.clear();
        elementB.sendKeys(String.valueOf(intVal + 1));

        cp.save();
        cp.waitForLoaderToDisappear();

        elementB.clear();
        elementB.sendKeys(attrVal);
    }

    /** Reads Return Order No (prefers input value; falls back to text) */
    public void getReturnOrdNo(int arrIndex, List<String> firstUniqueText) {
        String value = null;
        try {
            WebElement input = driver.findElement(By.xpath("//*[contains(@id,'returnOrderNo')]/input | //input[contains(@id,'returnOrderNo')]"));
            scrollIntoView(input);
            value = input.getAttribute("value");
        } catch (Exception ignore) {
            WebElement label = driver.findElement(By.xpath("//*[@id='panel1bh-content']/div/div/div/div/div[1]/div/div"));
            scrollIntoView(label);
            value = label.getText().trim();
        }

        if (arrIndex < firstUniqueText.size()) firstUniqueText.set(arrIndex, value);
        else firstUniqueText.add(value);
    }

    public void compareCustomerAndOrderLocation() {
        WebElement element1 = driver.findElement(By.xpath("//*[@id='returnOrdeDetailscustomer']"));
        String val1 = element1.getText().trim();

        WebElement element2 = driver.findElement(By.xpath("//*[@id='returnDestinationDetailscustomerLocation']/span"));
        String val2 = element2.getText().trim();

        if (val1.startsWith(val2) || val2.startsWith(val1)) {
            System.out.println("Both attribute values start with the same prefix");
        } else {
            System.out.println("Attribute values start with different prefixes");
        }
    }

    public void safeType(WebElement element, String value) {
        Actions actions = new Actions(driver);
        actions.scrollToElement(element).perform();
        actions.moveToElement(element).click().perform();
        element.clear();
        element.sendKeys(value);
    }

    By cnfirmBtn = By.xpath("//*[@title='Click to confirm/tender']");

    public void confirmOrder() {
        cp.clickElement(cnfirmBtn);
    }
}
