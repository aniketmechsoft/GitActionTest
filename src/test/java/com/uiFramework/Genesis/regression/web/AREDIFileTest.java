package com.uiFramework.Genesis.regression.web;

import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.AccountsReceivable;
import com.uiFramework.Genesis.web.pages.CommonPage;

public class AREDIFileTest extends TestBase  {	
	AccountsReceivable ar = null;
	CommonPage cp = null;
	CommonMethods cm;
	int initialCount;
	int afterCount;
	
	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		ar = new AccountsReceivable(driver);
		//driver.navigate().refresh();

	}
	
	@Test(priority = 1, alwaysRun=true)
	public void shouldShowEDIExportStatusAsPending() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ar.ClickOnEDIFileMenu();
		ar.getDefaultStatus();
		sAssert.assertEquals(ar.getDefaultStatus(), "Pending", "Default status not Pending");
		sAssert.assertAll();
	}
	
	@Test(priority = 2, alwaysRun=true)
	public void shouldDisplayCreatedInvoiceNumberOnEDIPage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.seacrhInvoiceNumber(ar.InvoiceNumber);
		sAssert.assertEquals(ar.getInvoiceNumberFromEDI(),ar.InvoiceNumber, "Invoice number not match/not Displayed EDI page");
		sAssert.assertAll();
	}
	
	@Test(priority = 3, alwaysRun=true)
	public void shouldDisplayAccurateInvoiceAmountOnEDIPage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(ar.getEDIInvAmount(),ar.InvoiceAmount, "Create invoice amount does not match in the EDI page");
		sAssert.assertAll();
	}
	
	@Test(priority = 4, alwaysRun=true)
	public void shouldValidateDiscountAmountOnEDIPage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(ar.getEDIInvDiscount(),ar.TotalDiscount, "Create invoice amount does not match in the EDI page");
		sAssert.assertAll();
	}
	
	@Test(priority = 5, alwaysRun=true)
	public void shouldShowGlOrderOnEDIPage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.clickOnExpandBtn();
		sAssert.assertTrue(ar.CheckGLOrdersOnAvailable(), "Created invoice orders not match on EDI listing");
		sAssert.assertAll();
	}
	
	@Test(priority = 6, dependsOnMethods="shouldShowGlOrderOnEDIPage")
	public void shouldDisplayOrderPriceOnEDIPage () throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ar.checkPricesAvailableOnEDIPage(), "Price not displyed on EDI Page");
		sAssert.assertAll();
	}
	
	@Test(priority = 7)
	public void shouldShowAccurateSubTotalSumOnEDIPage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(ar.calculateTotalPriceSum(), ar.SubTotal, "Create invoice amount does not match with sub total in the EDI page");
		sAssert.assertAll();
	}
	
	@Test(priority = 8, alwaysRun=true)
	public void shouldAllowUserToGenerateEDIFile() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.deleteExistingFiles();
		ar.selectFirstInvoice();
		ar.clickOnEdiFileExportBtn();
		sAssert.assertTrue(ar.isFileDownloaded(), "FAIL:EDI File was not downloaded/generated.");
		sAssert.assertAll();
	}
	
	@Test(priority = 9, alwaysRun=true)
	public void shouldChangeEDIExportStatusOnceFileExported() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.selectCompleteStatus();
		cp.Search();
		ar.seacrhInvoiceNumber(ar.InvoiceNumber);
		sAssert.assertEquals(ar.getExportStatus(),"Completed", "Invoice Export status not match");
	    sAssert.assertAll();
	}
	
	@Test(priority = 10)
	public void shouldGenerateEDIFilesForMultipleInvoices() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		cp.clickClearButton();
		initialCount = ar.getTotalEntriesCount();
		ar.deleteExistingFiles();
		ar.checkAndSelectTwoInvoices();
		ar.clickOnEdiFileExportBtn();
		sAssert.assertTrue(ar.isFileDownloaded(), "FAIL:EDI File was not downloaded/generated for Two invoice.");
		sAssert.assertAll();
	}
	
	@Test(priority = 11)
	public void shouldReduceCountAfterFileGeneration() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		afterCount = ar.getTotalEntriesCount();
		int diffint = initialCount - afterCount;
		sAssert.assertEquals(diffint, 2, "Entry count did not decrease By Two.");
		sAssert.assertAll();
	}
	
	@Test(priority = 12)
	public void shouldHandlePaginationCorrectlyOnEDIFilePage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.paginationOnListingPageforEDIPage();
		sAssert.assertAll();
	}
	
	@Test(priority = 13, dependsOnMethods="shouldChangeEDIExportStatusOnceFileExported", alwaysRun=true)
	public void shouldNotAllowEditingInvoiceAfterEDIFileGenerated() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.ClickOnInvoiceMenu();
		ar.seacrhInvoiceNumber(ar.InvoiceNumber);
		sAssert.assertTrue(ar.isEditButtonDisplayed(), "Edit button is displayed. when generate EDI file");
		sAssert.assertAll();
	}
	
	@Test(priority = 14, dependsOnMethods="shouldNotAllowEditingInvoiceAfterEDIFileGenerated", alwaysRun=true)
	public void shouldAllowUserToPrintPDFForInvoice() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.deleteExistingFiles();
		ar.exportPDFfile();
		sAssert.assertTrue(ar.isFileDownloaded(), "FAIL:PDF File was not downloaded. when print invoice");
		sAssert.assertAll();
	}
	
	@Test(priority = 15, dependsOnMethods="shouldAllowUserToPrintPDFForInvoice", alwaysRun=true)
	public void shouldUpdateStatusToPrintedWhenPDFExported() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.selectPrintedStatus();
		cp.Search();
		ar.seacrhInvoiceNumber(ar.InvoiceNumber);
		sAssert.assertEquals(ar.getInvoiceNumber(),ar.InvoiceNumber, "Invoice number not match/not Displayed in Printed status");
		sAssert.assertAll();
	}
	
	@Test(priority = 16,dependsOnMethods="shouldUpdateStatusToPrintedWhenPDFExported")
	public void shouldAllowUserToExportCoverXLSForInvoice() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.deleteExistingFiles();
		ar.exportcoverXLSfile();
		sAssert.assertTrue(ar.isFileDownloaded(), "FAIL:cover xls File was not downloaded. when export cover invoice");
		sAssert.assertAll();
	}
	
	@Test(priority = 17)
	public void shouldHandlePaginationCorrectlyOnInvoiceFilePage() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		ar.paginationOnListingPageforEDIPage();
		sAssert.assertAll();
	}


}
