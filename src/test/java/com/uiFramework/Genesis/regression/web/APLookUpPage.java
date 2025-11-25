package com.uiFramework.Genesis.regression.web;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.ImportInvoices;

public class APLookUpPage extends TestBase {
	CommonMethods cm = null;
	CommonPage cp = null;
	ImportInvoices ii = null;
	int colIndex;

	@BeforeClass
	public void beforeclass() {
	cm = new CommonMethods(driver);
	cp = new CommonPage(driver);
	ii = new ImportInvoices(driver);
	//driver.navigate().refresh();

	
	}
	
	@Test(priority = 1)
	public void shouldDisplayCorrectInvoiceNumberAndAmountWhenSearchedByInvoice()
			throws TimeoutException, InterruptedException, FileNotFoundException {
		SoftAssert sAssert = new SoftAssert();
		  //ii.accountPayableMenu();//Hide when Suite run
	   	ii.APLookupMenu(); 
		ii.clickOnByInvoice();
		ii.selectCarrierOnByinvoice(ii.carrier);
		//ii.selectCarrierOnByinvoice("170");
		ii.selectInvoiceNoByinvoice(ii.invoiceNumber);
	//	ii.selectInvoiceNoByinvoice("3213");
		ii.searchBtnForInv();
		sAssert.assertEquals(ii.getAmountChargedSum(), ii.paybleAmount, "Pay Amount not match ON AP lookup page");
		sAssert.assertAll();
	}
	
	@Test(priority = 2)
	public void shouldDisplayInvoiceDataByInvoice() {
		SoftAssert sAssert = new SoftAssert();
		 colIndex = ii.getInvoiceGeneratedFor();
	}
	
	@Test(priority = 3)
	public void shouldDisplayClearButtonOnByInvoicePage() {
		SoftAssert sAssert = new SoftAssert();
		ii.clearByInvoiceData();
		sAssert.assertTrue(ii.isRecordNotFoundDisplayed(3), "Clear btn not work for by Invoice.");
		sAssert.assertAll();
	}
	
	@Test(priority = 4,dependsOnMethods= "shouldDisplayInvoiceDataByInvoice")
	public void shouldSearchDataInAPLookup() {
		SoftAssert sAssert = new SoftAssert();
		ii.clickOnByInvoice();
		ii.searchInvData(colIndex);	
	}
	
	@Test(priority = 5)
	public void shouldAllowUserToSearchAndCheckDueDate() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.clickOnByBatch();
		ii.selectFromAndToDate(ii.DueDate);
		ii.searchBtnForBatch();
		sAssert.assertEquals(ii.getDueDate(ii.DueDate), ii.DueDate, "Due date not match with search on lookup");
		sAssert.assertAll();
	}
	
	@Test(priority = 6)//, dependsOnMethods="shouldAllowUserToSearchAndCheckDueDate"
	public void shouldAllowUserToDownloadExcelFromBatchGrid() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.deleteExistingFiles();
	    ii.clickToDwlExcel();
		sAssert.assertTrue(ii.isFileDownloaded(), "FAIL:By Batch Excel on loopup File was not downloaded.");
		sAssert.assertAll();
		
	}
	
	
	
	
	
	

}
