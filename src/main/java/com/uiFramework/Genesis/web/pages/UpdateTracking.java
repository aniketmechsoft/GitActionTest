package com.uiFramework.Genesis.web.pages;

import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

public class UpdateTracking {
    WebDriver driver = null;
    CommonMethods cm;
    WaitHelper wt;
    CommonPage cp;

    public UpdateTracking(WebDriver driver) {
        this.driver = driver;
        this.cm = new CommonMethods(driver);
        this.wt = new WaitHelper(driver);
        this.cp = new CommonPage(driver);

        // ensure our known download dirs exist (won’t hurt on macOS/Win)
        try { Files.createDirectories(projectDownloadsDir.toPath()); } catch (Exception ignored) {}
        try { Files.createDirectories(targetDownloadsDir.toPath()); } catch (Exception ignored) {}
    }

    // ---------- LOCATORS ----------
    private By docMenu = By.xpath("//i[@title='Tracking & ETA']");
    private By docGenMenu = By.xpath("//a[@href='/outbound-truck/update-tracking-no-eta']");
    private By ordertrackMenu = By
            .xpath("//*[@id='root']/div[3]/div[1]/div/div/ul/div/div/div/ul[2]/a");
    private By createLDADoc = By.xpath("//*[@title='Click to update the details']");
    private By updateBtn = By
            .xpath("//*[@id=\"root\"]/div[5]/div[3]/div[2]/div/div/div/div/div/div[3]/div/button");
    private By ltl = By.id("outBoundTruckETALTLCarriers");
    private By anonymsClick = By.xpath("//table/tbody/tr[1]/td[8]");
    private By firstCheckbox = By.xpath("//table/tbody/tr[1]/td[1]/div");
    private By statusDrpDwn = By.xpath("//*[@id='outBoundTruckETAstatus']");
    private By inputPath = By.xpath("//*[@id='genesis']/div[4]/div[1]/div[2]/input");
    private By dateSel = By
            .xpath("//table/tbody/tr[1]/td[6]/div/div/div/div/span/button");
    private By enterGrdTrkNo = By
            .xpath("//table/tbody/tr[1]/td[5]/div/div/div/input");
    private By selDrpDwnVal = By.xpath("//*[@id='genesis']/div[4]/div[2]/ul/li");
    private By downloadFileFormatBtn = By.id("etaBulkImportDownloadBtn");
    private By uploadFile = By
            .xpath("//*[@id='etaBulkImportETAExcelFile']/div[1]/div/span/input");
    private By uploadBtn = By
            .xpath("//*[@id='etaBulkImportETAExcelFile']/div[1]/div/button[1]");
    private By succRowCount = By
            .xpath("//*[@id='tabpanel-0undefined']//table/tbody/tr");
    private By selectLTL = By.id("outBoundTruckETALTLCarriers_selectall");
    private By todayCell = By.xpath("//td[@data-p-today='true']");

    // ---------- DOWNLOADS (kept your file name; added OS/browser-safe resolution) ----------
    private final String fileName = "Tracking No & ETA File Format.xlsx";
    private final File projectDownloadsDir =
            new File(System.getProperty("user.dir") + File.separator + "downloadedFiles"); // your original
    private final File targetDownloadsDir =
            new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "downloads"); // our factory default
    private final File homeDownloadsDir =
            new File(System.getProperty("user.home") + File.separator + "Downloads"); // Safari default
    private String fullFilePath = new File(targetDownloadsDir, fileName).getAbsolutePath(); // preserved field name

    // ---------- NAV / BASIC ACTIONS (unchanged logic) ----------
    public void updteTrackMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        wt.waitToClick(docMenu, 20);
        Thread.sleep(2000);
        wt.waitToClick(docGenMenu, 10);
        cp.waitForLoaderToDisappear();
    }

    public void trackOrderMenu() throws TimeoutException, InterruptedException {
        cp.waitForLoaderToDisappear();
        Thread.sleep(2000);
        wt.waitToClick(ordertrackMenu, 10);
        cp.waitForLoaderToDisappear();
    }

    public void clickUpdateBtn() throws InterruptedException {
        cp.clickElement(anonymsClick);
        cp.clickElement(anonymsClick);
        wt.waitToClickWithAction(createLDADoc, 10);
        cp.waitForLoaderToDisappear();
    }

    public void clickUpdate() throws InterruptedException {
        cp.clickElement(anonymsClick);
        cp.clickElement(updateBtn);
        // cp.waitForLoaderToDisappear();
    }

    public void selTruckChkbox() {
        cp.waitForLoaderToDisappear();
        cp.waitForPopupToDisappear();
        wt.waitToClickWithAction(firstCheckbox, 10);
    }

    public void EnterTrkNo() {
        cp.waitForLoaderToDisappear();
        cp.waitForPopupToDisappear();
        cp.clickAndSendKeys(enterGrdTrkNo, "158963");
    }

    public void selDate() throws InterruptedException {
        cp.waitForPopupToDisappear();
        wt.waitToClick(dateSel, 10);
        Thread.sleep(1000);
        clickTodayDate();
    }

    public void selStatus(String status) throws InterruptedException {
        wt.waitToClick(statusDrpDwn, 10);
        cp.waitForLoaderToDisappear();
        this.selValSrchChkDrpDwn(status);
        cp.waitForLoaderToDisappear();
    }

//    public void selValSrchChkDrpDwn(String DrpDwnValue) {
//        driver.findElement(inputPath).click();
//        driver.findElement(inputPath).clear();
//        driver.findElement(inputPath).sendKeys(DrpDwnValue);
//        driver.findElement(selDrpDwnVal).click();
//    }
    
    public void selValSrchChkDrpDwn(String drpDwnValue) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputPath));

        String browserName = ((RemoteWebDriver) driver)
                                .getCapabilities()
                                .getBrowserName()
                                .toLowerCase();

        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);

        if (browserName.contains("safari")) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value = arguments[1];", input, drpDwnValue);

            input.sendKeys(Keys.BACK_SPACE);
        } else {
            input.sendKeys(drpDwnValue);
        }

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(selDrpDwnVal));
        option.click();
    }

    // ---------- DOWNLOAD / UPDATE / UPLOAD ----------
    public void downloadFileFormat() throws InterruptedException, FileNotFoundException {
        // delete old copies from all known locations
        deleteIfExists(new File(projectDownloadsDir, fileName));
        deleteIfExists(new File(targetDownloadsDir, fileName));
        deleteIfExists(new File(homeDownloadsDir, fileName));

        wt.waitToClickWithAction(downloadFileFormatBtn, 30);

        // wait up to 60s for a finished .xlsx to appear in any known folder
        File downloaded = waitForDownload(fileName, 60);
        if (downloaded == null) {
            throw new FileNotFoundException("Downloaded file not found !");
        }
        fullFilePath = downloaded.getAbsolutePath(); // preserve your field usage
        System.out.println("Downloaded1: " + fullFilePath);
    }

    public void updateFileFormat(boolean updateTracking, boolean updateETA) throws IOException {
        // ensure path still valid (handles Safari/Chrome differences)
        File file = ensureDownloadedFile();
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Random rand = new Random();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String todayDate = LocalDate.now().format(dateFormat);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell truckCell = row.getCell(0);
            String truckNoStr = "";

            if (truckCell != null) {
                if (truckCell.getCellType() == CellType.NUMERIC) {
                    truckNoStr = String.valueOf((int) truckCell.getNumericCellValue());
                } else if (truckCell.getCellType() == CellType.STRING) {
                    truckNoStr = truckCell.getStringCellValue().trim();
                }
            }

            if (!truckNoStr.isEmpty()) {
                System.out.println("Updating row " + i + " for Truck No: " + truckNoStr);

                if (updateTracking) {
                    Cell trackingCell = row.getCell(1);
                    if (trackingCell == null) trackingCell = row.createCell(1);
                    trackingCell.setCellValue("TRK" + String.format("%06d", rand.nextInt(999999)));
                } else {
                    Cell trackingCell = row.getCell(1);
                    if (trackingCell != null) {
                        trackingCell.setBlank();
                    }
                }

                if (updateETA) {
                    Cell etaCell = row.getCell(2);
                    if (etaCell == null) etaCell = row.createCell(2);
                    etaCell.setCellValue(todayDate);
                } else {
                    Cell etaCell = row.getCell(2);
                    if (etaCell != null) {
                        etaCell.setBlank();
                    }
                }
            }
        }

        fis.close();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            fos.flush();
        }
        workbook.close();
        System.out.println("File updated: " + file.getAbsolutePath());
    }

    public void excelUpload() {
        cp.waitForLoaderToDisappear();
        File file = ensureDownloadedFile();

        WebElement input = driver.findElement(uploadFile);
        // if hidden (Safari often), temporarily unhide so sendKeys works
        if (!input.isDisplayed() || !input.isEnabled()) {
            try {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.display='block'; arguments[0].removeAttribute('hidden'); arguments[0].style.opacity=1;",
                        input);
            } catch (Exception ignored) {}
        }
        input.sendKeys(file.getAbsolutePath());
        wt.waitToClick(uploadBtn, 10);
        cp.waitForLoaderToDisappear();
    }

    // ---------- LTL / TABLE / VALIDATION (unchanged logic) ----------
    public void selectLTL() throws InterruptedException {
        wt.waitToClick(ltl, 10);
        Thread.sleep(1000);
        cp.clickElement(selectLTL);
        cp.Search();
        cp.waitForLoaderToDisappear();
    }

    Map<String, String> enteredPairs = new HashMap<>();

    public void enterTrackingDetailsInLoop() throws InterruptedException {
        cp.waitForLoaderToDisappear();
        List<WebElement> items = getTableRows();
        
        for (int i = 0; i < 5 && i < items.size(); i++) {
        	
        	if (items.isEmpty()) {
                throw new SkipException("No rows found in table. Skipping test.");
            }
        	
            WebElement row = items.get(i);  
            row.click();

            String value = generateValue(i);
            int rowIndex = i + 1;

            String dateVal = fillInputAndDate(rowIndex, value);
            enteredPairs.put(value, dateVal);
            System.err.println(enteredPairs);
        }

        Thread.sleep(3000);
        clickUpdateBtn();
        cp.waitForPopupToDisappear();
        clickUpdate();
    }

    private List<WebElement> getTableRows() {
        return driver.findElements(By.xpath("//table/tbody/tr/td[1]"));
    }

    private String generateValue(int index) {
        return "Val_" + index;
    }

    // *** kept EXACTLY your original behaviour here ***
    private String fillInputAndDate(int rowIndex, String value) throws InterruptedException {
        WebElement input = driver.findElement(
                By.xpath("//table/tbody/tr[" + rowIndex + "]/td[5]/div/div/div/input"));
        input.click();
        input.clear();
        input.sendKeys(value);

        WebElement dateField = driver.findElement(
                By.xpath("//table/tbody/tr[" + rowIndex + "]/td[6]/div/div/div/div/span/button"));
        dateField.click();
        Thread.sleep(1000);
        clickTodayDate();

        return dateField.getAttribute("value");
    }

    private void clickTodayDate() {
        driver.findElement(By.xpath("//td[@data-p-today='true']")).click();
    }

    public int getTableRowCount() {
        List<WebElement> rows = driver.findElements(succRowCount);
        return rows.size();
    }

    public void TruckDataValidation() {
        cp.waitForLoaderToDisappear();

        List<WebElement> xpath4List = driver
                .findElements(By.xpath("//table/tbody/tr/td[5]/div/div/div/input"));
        List<WebElement> xpath5List = driver.findElements(
                By.xpath("//table/tbody/tr/td[6]/div/div/div/div/span/input"));

        Set<String> outputSet = xpath4List.stream().map(e -> e.getAttribute("value"))
                .collect(Collectors.toSet());

        Set<String> dateSet = xpath5List.stream().map(e -> e.getAttribute("value"))
                .collect(Collectors.toSet());

        for (Map.Entry<String, String> entry : enteredPairs.entrySet()) {
            String value = entry.getKey();
            String date = entry.getValue();

            boolean outputFound = outputSet.contains(value);
            boolean dateFound = dateSet.stream().anyMatch(d -> d.contains(date));

            if (outputFound && dateFound) {
                System.out.println("Match for: " + value + " | " + date);
            } else {
                System.out.println("Mismatch for: " + value + " | " + date);
            }
        }
    }

    // ---------- INTERNALS ----------
    private boolean isSafari() {
        try {
            if (driver instanceof RemoteWebDriver) {
                String name = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
                return name != null && name.toLowerCase().contains("safari");
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void deleteIfExists(File f) {
        try { if (f.exists()) f.delete(); } catch (Exception ignored) {}
    }

    private File ensureDownloadedFile() {
        File f = new File(fullFilePath);
        if (f.exists()) return f;

        // try known places if path changed (e.g., browser switch)
        File resolved = resolveLatestDownloaded(fileName);
        if (resolved != null) {
            fullFilePath = resolved.getAbsolutePath();
            return resolved;
        }
        throw new IllegalStateException("Downloaded file not available in known locations.");
    }

    private File[] candidateDirs() {
        return new File[]{ projectDownloadsDir, targetDownloadsDir, homeDownloadsDir };
    }

    private File resolveLatestDownloaded(String expectedName) {
        String base = stripExt(expectedName).toLowerCase();
        File newest = null;
        long newestTs = -1;
        for (File dir : candidateDirs()) {
            File[] files = dir.listFiles((d, name) ->
                    name.toLowerCase().endsWith(".xlsx")
                            && stripExt(name).toLowerCase().startsWith(base));
            if (files == null) continue;
            for (File f : files) {
                if (inProgress(dir, f)) continue;
                long ts = f.lastModified();
                if (ts > newestTs) {
                    newest = f; newestTs = ts;
                }
            }
        }
        return newest;
    }

    private File waitForDownload(String expectedName, int timeoutSec) throws InterruptedException {
        String base = stripExt(expectedName).toLowerCase();
        long end = System.currentTimeMillis() + timeoutSec * 1000L;

        while (System.currentTimeMillis() < end) {
            File f = resolveLatestDownloaded(expectedName);
            if (f != null && stripExt(f.getName()).toLowerCase().startsWith(base)) {
                return f;
            }
            Thread.sleep(1000);
        }
        return null;
    }

    private boolean inProgress(File dir, File file) {
        // if a temp marker exists next to the target, treat as in-progress
        String bare = stripExt(file.getName());
        return new File(dir, bare + ".crdownload").exists()
                || new File(dir, bare + ".part").exists()
                || new File(dir, file.getName() + ".download").exists(); // Safari bundle during download
    }

    private String stripExt(String name) {
        int i = name.lastIndexOf('.');
        return i > 0 ? name.substring(0, i) : name;
    }
}
