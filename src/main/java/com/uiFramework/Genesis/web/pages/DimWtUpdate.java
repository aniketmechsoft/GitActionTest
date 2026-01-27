package com.uiFramework.Genesis.web.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;

import org.apache.poi.ss.usermodel.*;

public class DimWtUpdate {
	WebDriver driver = null;
	CommonPage cp;
	CommonMethods cm;
	private WebDriverWait wait;
	WaitHelper wt;
	private JavascriptExecutor js;
	protected static final Logger logger = Logger.getLogger(DimWtUpdate.class.getName());

	public DimWtUpdate(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
		this.cp = new CommonPage(driver);
		this.wt = new WaitHelper(driver);
		this.js = (JavascriptExecutor) driver;
		this.cm = new CommonMethods(driver);
	}

	/* --------------------- helpers --------------------- */
	private WebElement waitClickable(By loc, int sec) {
		return new WebDriverWait(driver, Duration.ofSeconds(sec))
				.until(ExpectedConditions.elementToBeClickable(loc));
	}
	private WebElement waitVisible(By loc, int sec) {
		return new WebDriverWait(driver, Duration.ofSeconds(sec))
				.until(ExpectedConditions.visibilityOfElementLocated(loc));
	}
	private void jsClick(By loc) {
		WebElement el = driver.findElement(loc);
		js.executeScript("arguments[0].click();", el);
	}
	private int parseIntSafe(String s, int def) {
		try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
	}
	private File mostRecentMatching(File dir, String startsWith, String endsWith) {
		File[] matches = dir.listFiles(f ->
				f.getName().startsWith(startsWith) && f.getName().endsWith(endsWith));
		if (matches == null || matches.length == 0) return null;
		return Arrays.stream(matches).max(Comparator.comparingLong(File::lastModified)).orElse(null);
	}
	/* --------------------------------------------------- */

	private By ARmenu = By.xpath("//i[@title='Accounts Receivable']");
	private By dimweightmenu = By.xpath("//a[@href='/AR/dimensional-weight-update']");
	private By saveDimweight = By.xpath("//button[@title='Click to save dimensional weight']");
	private By ordercheckbox = By.xpath("//table/tbody/tr[1]/td[1]/div");
	private By enterDimweight = By.xpath("//input[@placeholder= 'Enter Dim. Weight']");
	private By enterLitDimweight = By.xpath("//input[@placeholder='Enter Lit. Dim. Weight']");
	private By norecord = By.xpath("//td[contains(text(),'Record not found.')]");
	private By fetchordertext = By.xpath("//*[@id='panel1bh-content']/div/div//div/div/div[2]/table/tbody/tr[1]/td[3]");
	private By fetchcustordertext = By.xpath("//*[@id='panel1bh-content']/div/div/div//div/div[2]/table/tbody/tr[1]/td[6]");
	private By exportFileDataBtn = By.xpath("//*[@id='arDimensionalWeightExportFilterDataBtn']");
	private By uploadFile = By.xpath("//*[@id='ARdimensionalWeightExcelFile']/div[1]/div/span/input");
	private By uploadBtn = By.xpath("//*[@id='ARdimensionalWeightExcelFile']/div[1]/div/button[1]");
	private By importDimWtBtn = By.xpath("//*[@id='arDimensionalWeightImportDimWeightBtn']");
	private By errorMsgOnPopup = By.xpath("//table/tbody/tr[1]/td[4]");
	private By ClseErrorPopup = By.xpath("//*[@id=':r6:']/div/button"); // may be dynamic; method adds fallbacks
	private By selAllCkkbox = By.xpath("//table/thead/tr/th[1]/div/div");
	private By dimWtOnLanding = By.xpath("//table/tbody/tr/td[9]/div/span/input");
	private By litDimWtOnLanding = By.xpath("//table/tbody/tr/td[12]/div/span/input");
	private By dimWtOnUpload = By.xpath("//table/tbody/tr[1]/td[5]/div/span/input");
	private By litDimWtOnUpload = By.xpath("//table/tbody/tr[1]/td[7]/div/span/input");
	private By ordNoOnLanding = By.xpath("//*[@id='arDimensionalWeightorderNo']");
	private By uploadPopupOrdNo = By.xpath("//table/tbody/tr[1]/td[3]");
	private By uploadPopupDimWt = By.xpath("//table/tbody/tr[1]/td[5]");
	private By uploadPopupLitDimWt = By.xpath("//table/tbody/tr[1]/td[7]");

//	private String downloadDir = System.getProperty("user.dir") + "\\downloadedFiles";
//	private String fileName = "Dimensional Weight Format Data.xlsx";
//	private String fullFilePath = downloadDir + "\\" + fileName;
	
//	private String downloadDir = System.getProperty("user.dir") + "\\downloadedFiles";
//	private String fileName = "Dimensional Weight Format Data.xlsx";
//	private String fullFilePath = downloadDir + "\\" + fileName;
	
	 // ---------- DOWNLOADS (kept your file name; added OS/browser-safe resolution) ----------
    private final String fileName = "Dimensional Weight Format Data.xlsx";
    private final File projectDownloadsDir =
            new File(System.getProperty("user.dir") + File.separator + "downloadedFiles"); // your original
    private final File targetDownloadsDir =
            new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "downloads"); // our factory default
    private final File homeDownloadsDir =
            new File(System.getProperty("user.home") + File.separator + "Downloads"); // Safari default
    private String fullFilePath = new File(targetDownloadsDir, fileName).getAbsolutePath(); // preserved field name

	/* ===================== NAV ===================== */

	public void goToDimWeightScreen() {
		cp.waitForLoaderToDisappear();
		jsClick(ARmenu);
		WebElement menu = waitVisible(dimweightmenu, 10);
		js.executeScript("arguments[0].scrollIntoView({block:'center'})", menu);
		waitClickable(dimweightmenu, 10).click();
		cp.waitForLoaderToDisappear();
	}

	/* ===================== VALIDATION ===================== */

	public boolean checkDataPresent() {
		boolean noRecordsFound = !driver.findElements(norecord).isEmpty();
		if (noRecordsFound) {
			logger.warning("No records found. Skipping the test.");
			throw new SkipException("No records found. Skipping the test.");
		}
		return true;
	}

	/* ===================== BASIC ACTIONS ===================== */

	public void clickSaveDimWeight() {
		cp.clickElement(saveDimweight);
	}

	public void enterDimweight(String alphanumeric) {
		cp.clickAndSendKeys(enterDimweight, alphanumeric);
	}

	public void enterLitDimweight(String alphanumeric) {
		cp.clickAndSendKeys(enterLitDimweight, alphanumeric);
		String expectedNumeric = alphanumeric.replaceAll("[^0-9]", "");
		wait.until(ExpectedConditions.attributeToBe(enterLitDimweight, "value", expectedNumeric));
		String currentValue = driver.findElement(enterLitDimweight).getAttribute("value").trim();
		if (!currentValue.equals(expectedNumeric)) {
			logger.severe("Unexpected Lit Dim Weight value. Expected: '" + expectedNumeric + "', but got: '" + currentValue + "'");
		}
	}

	public void enterDimweightwith101Numbers(String alphanumeric) {
		cp.clickAndSendKeys(enterDimweight, alphanumeric);
	}

	public void enterLitDimweightwith101Numbers(String alphanumeric) {
		cp.clickAndSendKeys(enterLitDimweight, alphanumeric);
		String expectedNumeric = alphanumeric.replaceAll("[^0-9]", "");
		if (expectedNumeric.length() > 100) expectedNumeric = "9999999999"; // UI cap behavior
		wait.until(ExpectedConditions.attributeToBe(enterLitDimweight, "value", expectedNumeric));
		String currentValue = driver.findElement(enterLitDimweight).getAttribute("value").trim();
		if (!currentValue.equals(expectedNumeric)) {
			logger.severe("Lit Dim Weight > 100 value mismatch. Expected: '" + expectedNumeric + "', but got: '" + currentValue + "'");
		}
	}

	public int enterDimweightValue(int value) {
		cp.clickAndSendKeys(enterDimweight, String.valueOf(value));
		return value;
	}

	public int enterLitDimweightValue(int value) {
		cp.clickAndSendKeys(enterLitDimweight, String.valueOf(value));
		return value;
	}

	public void selectCheckbox() {
		cp.waitForLoaderToDisappear();
		wt.waitToClickWithAction(ordercheckbox, 10);
		cp.waitForLoaderToDisappear();
	}

	public void searchAndValidateOrderNumber() throws InterruptedException {
		cp.searchAndValidateOrderNumber(fetchordertext);
		cp.waitForPopupToDisappear();
	}

	public void searchAndValidateCustomerOrderNumber() throws InterruptedException {
		cp.searchAndValidateCustomerOrderNumber(fetchcustordertext);
		cp.waitForPopupToDisappear();
	}

	public void selectCheckbox(int rowNum) throws InterruptedException {
		Thread.sleep(5000);
		By cb = By.xpath("//table/tbody/tr[" + rowNum + "]/td[1]//input[@class='p-checkbox-input']");
		WebElement checkbox = waitClickable(cb, 10);
		if (!checkbox.isSelected()) checkbox.click();
	}

	public void fillInputInColumn(int rowNum, int columnIndex, String value) {
		By input = By.xpath("//table/tbody/tr[" + rowNum + "]/td[" + columnIndex + "]/div/span/input");
		WebElement inputField = waitClickable(input, 10);
		inputField.click();
		inputField.clear();
		inputField.sendKeys(value);
	}

	public void fillFirstThreeRows(String value) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		//for (int i = 1; i <= 3; i++) {	
			Thread.sleep(10000);
			driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]//input[@class='p-checkbox-input']")).click();
			driver.findElement(By.xpath("//table/tbody/tr[2]/td[1]//input[@class='p-checkbox-input']")).click();
			//selectCheckbox(i);
			fillInputInColumn(1, 9, value);
			fillInputInColumn(1, 12, value);
		//}
		this.clickSaveDimWeight();
	}

	/* ===================== EXPORT / IMPORT ===================== */

	public void downloadFileFormat() throws InterruptedException, FileNotFoundException {
//		// clear old exact file if present
//		 deleteIfExists(new File(projectDownloadsDir, fileName));
//	     deleteIfExists(new File(targetDownloadsDir, fileName));
//	     deleteIfExists(new File(homeDownloadsDir, fileName));
//		
//		File exact = new File(fullFilePath);
//		if (exact.exists()) {
//			logger.info("Old file found. Deleting: " + exact.getName());
//			exact.delete();
//		}
//
//		wt.waitToClickWithAction(exportFileDataBtn, 10);
//
//		// wait for expected name first
//		int timeoutSec = 30, waited = 0;
//		while (!new File(fullFilePath).exists() && waited < timeoutSec) {
//			Thread.sleep(1000);
//			waited++;
//		}

		// fallback: pick most recent matching if name got "(1)" appended
//		if (!new File(fullFilePath).exists()) {
//			File recent = mostRecentMatching(new File(downloadDir), "Dimensional Weight", ".xlsx");
//			if (recent != null) {
//				fullFilePath = recent.getAbsolutePath();
//			}
//		}
//
//		if (!new File(fullFilePath).exists()) {
//			throw new FileNotFoundException("Downloaded file not found!");
//		}
//		System.out.println("Downloaded: " + fullFilePath);
		
		 deleteIfExists(new File(projectDownloadsDir, fileName));
	        deleteIfExists(new File(targetDownloadsDir, fileName));
	        deleteIfExists(new File(homeDownloadsDir, fileName));

	        wt.waitToClickWithAction(exportFileDataBtn, 30);

	        // wait up to 60s for a finished .xlsx to appear in any known folder
	        File downloaded = waitForDownload(fileName, 60);
	        if (downloaded == null) {
	            throw new FileNotFoundException("Downloaded file not found !");
	        }
	        fullFilePath = downloaded.getAbsolutePath(); // preserve your field usage
	        System.out.println("Downloaded1: " + fullFilePath);
	}
	
	  private void deleteIfExists(File f) {
	        try { if (f.exists()) f.delete(); } catch (Exception ignored) {}
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

	public void updateFileFormat(boolean updateTracking, boolean updateETA) throws IOException {
		try (FileInputStream fis = new FileInputStream(fullFilePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheetAt(0);
			int last = sheet.getLastRowNum();
			int maxRowsToKeep = 50;

			// clear columns if toggles are off (indexes are zero-based)
			if (!updateTracking || !updateETA) {
				for (int i = 1; i <= Math.min(last, maxRowsToKeep); i++) {
					Row r = sheet.getRow(i);
					if (r == null) continue;
					if (!updateTracking) {
						Cell c = r.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						c.setCellType(CellType.STRING);
						c.setCellValue("");
					}
					if (!updateETA) {
						Cell c = r.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						c.setCellType(CellType.STRING);
						c.setCellValue("");
					}
				}
			}

			// fill values if toggles are on
			for (int i = 1; i <= Math.min(last, maxRowsToKeep); i++) {
				Row row = sheet.getRow(i);
				if (row == null) continue;

				if (updateTracking) {
					Cell trackingCell = row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					trackingCell.setCellValue(cm.getNumericString(2));
				}
				if (updateETA) {
					Cell etaCell = row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					etaCell.setCellValue(cm.getNumericString(2));
				}
			}

			// remove rows beyond 50 (optional trim)
			for (int i = last; i > maxRowsToKeep; i--) {
				Row r = sheet.getRow(i);
				if (r != null) sheet.removeRow(r);
			}

			try (FileOutputStream fos = new FileOutputStream(fullFilePath)) {
				workbook.write(fos);
				fos.flush();
			}
		}
		logger.info("File updated: " + fullFilePath + " — kept first 50 rows (trimmed extras).");
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
	 private File[] candidateDirs() {
	      return new File[]{ projectDownloadsDir, targetDownloadsDir, homeDownloadsDir };
	  }
	 
	public void ClickImportBtn() {
		cp.waitForLoaderToDisappear();
		wt.waitToClickWithAction(importDimWtBtn, 10);
		cp.waitForLoaderToDisappear();
	}

	public void excelUpload() {
		cp.waitForLoaderToDisappear();
		File f = new File(fullFilePath);
		if (!f.exists()) {
			logger.severe("Upload file not found at: " + fullFilePath);
			return;
		}
		driver.findElement(uploadFile).sendKeys(fullFilePath);
		wt.waitToClick(uploadBtn, 10);
		cp.waitForLoaderToDisappear();
	}

	/* ===================== POPUP / GRID ===================== */

	public String getErrorMsg() {
		return driver.findElement(errorMsgOnPopup).getText();
	}

	public void closeErrDescPopup() {
		cp.waitForLoaderToDisappear();
		try {
			wt.waitToClick(ClseErrorPopup, 5);
		} catch (Exception e) {
			// fallbacks: try a common close button or ESC overlay close
			List<WebElement> closes = driver.findElements(By.xpath("//button[@title='Click to close' or @aria-label='close']"));
			if (!closes.isEmpty()) {
				closes.get(0).click();
			} else {
				try { ((JavascriptExecutor) driver).executeScript("document.activeElement.blur()"); } catch (Exception ignore) {}
			}
		}
		cp.waitForLoaderToDisappear();
	}

	public void selTop5RecFromUploadedExcel() {
		driver.findElement(selAllCkkbox).click();
		cp.waitForLoaderToDisappear();

		for (int i = 1; i <= 5; i++) {
			By cb = By.xpath("//table/tbody/tr[" + i + "]/td[1]//input[@type='checkbox']");
			try {
				WebElement checkbox = waitClickable(cb, 5);
				if (!checkbox.isSelected()) checkbox.click();
			} catch (Exception ignored) {}
		}
		cp.waitForLoaderToDisappear();
	}

	public boolean uploadedDataCount() {
		List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr"));
		return rows.size() > 1;
	}

	public String getuploadPopupOrdNo() {
		return driver.findElement(uploadPopupOrdNo).getText();
	}

	public String getuploadPopupDimWt() {
		// td, not input → use text
		return driver.findElement(uploadPopupDimWt).getText();
	}

	public String getuploadPopupLitDimWt() {
		// td, not input → use text
		return driver.findElement(uploadPopupLitDimWt).getText();
	}

	public int getLandingLitDimWt() {
		try {
			String value = driver.findElement(litDimWtOnLanding).getAttribute("value");
			return parseIntSafe(value, -1);
		} catch (Exception e) {
			logger.warning("Could not parse Landing Lit Dim Wt. Returning -1.");
			return -1;
		}
	}

	public int getLandingDimWt() {
		try {
			String value = driver.findElement(dimWtOnLanding).getAttribute("value");
			return parseIntSafe(value, -1);
		} catch (Exception e) {
			logger.warning("Could not parse Landing Dim Wt. Returning -1.");
			return -1;
		}
	}

	public void enterDimweightValOnUpload(String value) {
		cp.clickAndSendKeys1(dimWtOnUpload, value);
	}

	public String enterLitDimweightValOnUpload(String value) {
		cp.clickAndSendKeys1(litDimWtOnUpload, value);
		return value;
	}

	public void searchOrdNoOnLanding(String val) {
		cp.clickAndSendKeys(ordNoOnLanding, val);
		cp.Search();
	}
}
