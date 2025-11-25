package com.uiFramework.Genesis.LDAPages;

import java.io.File;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class DocAndUploadPODPage extends LdaQAPage{

	WebDriver driver;
	OrderPage op = null;
	CommonPage cp;
	CommonMethods cm = null;
	WaitHelper wt;

	public DocAndUploadPODPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.cp = new CommonPage(driver);
		cm = new CommonMethods(driver);
		this.wt = new WaitHelper(driver);
		op = new OrderPage(driver);

	}

	private By documentmenu = By.xpath("//i[@title='Documents']");
	private By hcpodmenu = By.xpath("(//i[@title='Documents'])[2]");
	private By documenno = By.xpath("//*[@placeholder='Enter Document Number']");
	private By uploadbtn = By.xpath("//*[@aria-label='Upload']");
	private By printbtn = By.xpath("//*[@title='Click to print']");
	private By printDocBtn = By.xpath("//*[@title='Click to print selected documents']");
	private By splitpod = By.xpath("//a[@href='/documents/split-pod']");
	private By viewpodbtn = By.xpath("//*[@title='Click to preview document']");
	private By deletepodbtn = By.xpath("//*[@title='Click here to delete POD']");
	private By downloadpod = By.xpath("//*[@title='Click here to download POD']");
	private By closepod = By.xpath("//*[@data-testid='CloseIcon']");
	private By searchcontain = By.xpath("//*[@placeholder='Contains...']");
	private By searchstatus = By.xpath("(//*[@placeholder='Contains...'])[3]");
	private By doctype = By.xpath("(//*[.='Select Document Type'])[3]");
	private By fetchcustordernotext = By.xpath("//table/tbody/tr/td[7]");
	private By fetchdocumentnotext = By.xpath("//table/tbody/tr/td[3]");
	private By fetchordertext = By.xpath("//table/tbody/tr/td[4]");
	
	
	/**
	 * This method used to click document menu
	 */
	public void clickDocumentMenu() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(documentmenu, 10);
		cp.waitForLoaderToDisappear();
	}
	/**
	 * This method used to click to HCPOD menu
	 */
	public void clickHcpodDocMenu() {
		cp.waitForLoaderToDisappear();
		wt.waitToClick(hcpodmenu, 10);
		cp.waitForLoaderToDisappear();
	}
	
	/**
	 * This method used to click and enter document number
	 * @param lbol
	 */
	public void enterLbolNo(String lbol) {
		cp.clickAndSendKeys(documenno, lbol);
	}
	
	/**
	 * This method used to click on print icon button
	 */
	public void clickOnPrintBtn() {
		wt.waitToClick(printbtn, 10);
	}
	
	/**
	 * This method used to click on select first check box
	 */
	public void selectDocument() {
		cp.waitForPopupToDisappear();
		driver.findElement(By.xpath("(//*[contains(@class,'p-checkbox-input')])[1]")).click();
	}
	
	/**
	 * This method used to click on print document btn
	 */
	public void clickToPrintDocBtn() {
		wt.waitToClickWithAction(printDocBtn, 10);
	}
	
	/**
	 * This method used to add file with .pdf extension from download file location
	 */
	public void addPDFFile() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By fileInputLocator = By.xpath("//input[@type='file']");
		WebElement fileInput = wait.until(ExpectedConditions.presenceOfElementLocated(fileInputLocator));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileInput);

		String folderPath = System.getProperty("user.dir") + "\\downloadedFiles";
		File folder = new File(folderPath);
		File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));

		if (files != null && files.length > 0) {
		    String filePath = files[0].getAbsolutePath(); // first PDF
		    fileInput.sendKeys(filePath);
		} else {
		    throw new RuntimeException("No PDF files found in the folder!");
		}

	}
	/**
	 * This method used to click upload button
	 */
	public void clickUploadBtn() {
		wt.waitToClick(uploadbtn, 10);
	}
	/**
	 * This method use to get uploaded file/POD name
	 * @return
	 */
	public String getPODName() {
		return cp.getMandatoryText(By.xpath("//tbody//tr//td[2]"));
	}
	/**
	 * This method used to click document and open split pod page
	 */
	public void openSplitPODMenu() {
		clickDocumentMenu();
		wt.waitToClick(splitpod, 10);
		cp.waitForLoaderToDisappear();
	}
	/**
	 * This method used to click on view pod icon and click icon
	 */
	public void viewPOD() {
		wt.waitToClick(viewpodbtn, 10);
		wt.waitToClick(closepod, 10);
	}
	/**
	 * This method used to click on download btn to download POD
	 */
	public void downloadPOD() {
		wt.waitToClick(downloadpod, 10);
	}
	/**
	 * This method used to delete uploaded pod
	 */
	public void deletePOD() {
		wt.waitToClick(deletepodbtn, 10);
		cp.waitAndClickWithJS(Yes, 10);
	}
	/**
	 * This method used to search document number
	 * @param docname
	 */
	public void searchDocName(String docname) {
		cp.searchColoumFilter(searchcontain, docname);
	}
	
	/**
	 * This method is used to Select multiple customers by index
	 * 
	 * @param indices
	 * @throws InterruptedException
	 */
	public void selectMultipleDoctypeByIndex(List<Integer> indices) throws InterruptedException {
		cp.waitForLoaderToDisappear();
		cp.selectMultipleByIndex(doctype, indices, "Document Type");
	}
	
	/** 
	 * This methods is used to Search and validate customer order number 
	 * @throws InterruptedException
	 */
	public void searchAndValCustOrderNumber() throws InterruptedException {
		cp.searchAndValidateCustomerOrderNumber(fetchcustordernotext);
		cp.waitForPopupToDisappear();
	}
	
	/** 
	 * This methods is used to Search and validate  document number
	 * @throws InterruptedException
	 */
	public void searchAndValDocNumber() throws InterruptedException {
		cp.searchAndValidateCustomerOrderNumber(fetchdocumentnotext);
		cp.waitForPopupToDisappear();
	}
	/** 
	 * This methods is used to Search and validate order number 
	 * @throws InterruptedException
	 */
	public void searchAndValidateOrderNumber() throws InterruptedException {
		cp.clickClearButton();
		cp.searchAndValidateOrderNumber(fetchordertext);
		cp.waitForPopupToDisappear();
	}
	/**
	 * This method used to search POD/document status
	 * @param status
	 */
	public void searchPodStatus(String status) {
		cp.searchColoumFilter(searchstatus, status);
	}
	
	/**
	 *This method used to return true if delete button disable
	 * @return
	 */
	public boolean isDeletePodButtonDisabled() {
	    WebElement deleteBtn = driver.findElement(deletepodbtn);
	    return !deleteBtn.isEnabled();  // returns true if disabled
	}

	
}
