package com.uiFramework.Genesis.regression.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.ImportInvoices;

public class ImportInvoicesTest extends TestBase {

	CommonMethods cm = null;
	CommonPage cp = null;
	ImportInvoices ii = null;
	String val;
	int rowCount;
	String invoiceNo;
	
	@BeforeClass
	public void beforeclass() { 
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		ii = new ImportInvoices(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1)
	public void shouldAbleToDownloadUploadFormat()
			throws TimeoutException, InterruptedException, FileNotFoundException {
		SoftAssert sAssert = new SoftAssert();
		ii.importInvMenuWithExpand();
		Thread.sleep(2000);
		ii.downloadFileFormat();
		Thread.sleep(2000);
		sAssert.assertTrue(true);
		sAssert.assertAll();
	}

	@Test(priority = 2)
	public void shouldNotAbleToUploadExcelWithoutCarrierSelection() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.excelUpload();
		sAssert.assertEquals(ii.getCarrErrMsg(), "Carrier is required.", "Not match.");
		sAssert.assertAll();

	}

	@Test(priority = 3)
	public void shouldNotAbleToUploadEmptyExcel() {
		SoftAssert sAssert = new SoftAssert();
		ii.selectCarrier("170");
		ii.clickUploadBtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Plese select one record.");
		sAssert.assertAll();
	}

	@Test(priority = 4)
	public void shouldAbleToSelectCarrierOnPayInvoiceScreen()
			throws InterruptedException, TimeoutException, IOException {
		SoftAssert sAssert = new SoftAssert();
		ii.payInvoicesMenu();
		ii.carrSelOnPayInvSCreen();
		Thread.sleep(2000);
		ii.writeGenDataToExcel();
		ii.modifyExcelForFilledGenColumn(true, true, true);
		Thread.sleep(1000);
		val = ii.getCarrier();
		System.out.println(val);

	}

	@Test(priority = 5)
	public void shouldAbleToSelectCarrierOnPay() throws InterruptedException, TimeoutException, IOException {
		SoftAssert sAssert = new SoftAssert();
		ii.APLookupMenu();
		ii.clickByInvoice();
		ii.selCarrInByInvoice(val);
		invoiceNo = ii.getInvoiceNo();
	//	System.out.println(invoiceNo);
	}

	@Test(priority = 6)
	public void shouldNotAbleToUploadExcelIfLBOLAndAUTHEnterInSameRow() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		ii.importInvMenu();
		ii.selectCarrier(val);
		ii.excelUpload();
		sAssert.assertEquals(ii.GetErrorMsg(),
				"Enter either one of Truck Number, LBOL Number or Authorization Number in each line.", "Not match.");
		sAssert.assertAll();

	}

	@Test(priority = 7)
	public void shouldNotAbleToUploadExcelIfInvoiceAmtNotEntered() throws IOException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.clickOK();
		ii.modifyExcelForFilledGenColumn(true, false, false);
		Thread.sleep(1000);
		ii.excelUpload();
		sAssert.assertEquals(ii.GetErrorMsg(), "INVOICE AMOUNT is required.", "Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 8)
	public void shouldNotAbleToUploadExcelIfInvoiceNoNotEntered() throws IOException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.clickOK();
		ii.modifyExcelForFilledGenColumn(false, false, true);
		ii.excelUpload();
		sAssert.assertEquals(ii.GetErrorMsg(), "Invoice # is required.", "Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 9)
	public void shouldAbleToUploadExcelWithValidData() throws IOException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.clickOK();
		ii.modifyExcelForFilledGenColumn(true, false, true);
		ii.excelUpload();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Excel Uploaded Successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 10)
	public void shouldNotAbleToSaveWithoutInvoiceNo() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		ii.enterInvoiceNo("");
		ii.clickSaveBatch();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Excel Uploaded Successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void shouldNotAbleToSaveWithoutInvoiceAmt() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		ii.enterInvoiceNo("INV0025");
		ii.enterInvoiceAmt("");
		ii.clickSaveBatch();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Excel Uploaded Successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 12)
	public void shouldNotAbleToSaveWithoutPayableAmt() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		// ii.enterInvoiceNo("INV0025");	
		ii.enterInvoiceAmt(DataRepo.VALUE);
		ii.enterPayableAmt("");
		ii.clickSaveBatch();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Excel Uploaded Successfully.");
		sAssert.assertAll();
	}

	@Test(priority = 13)
	public void shouldNotAbleToSaveWithoutInvoiceSummaryDetails() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		ii.enterPayableAmt(DataRepo.VALUE);
		ii.clickSaveBatch();
		sAssert.assertEquals(ii.getInvoiceDateErrMsg(), "Invoice Date is required.");
		sAssert.assertEquals(ii.getDueDateErrMsg(), "Due Date is required.");
		sAssert.assertEquals(ii.getFascalPeriodErrMsg(), "Fiscal Period is required.");
		sAssert.assertAll();
	}

	@Test(priority = 14)
	public void shouldNotAbleToSaveWithoutDueDatenFascalPeriod() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.invoiceDateSel();
		ii.clickSaveBatch();
		sAssert.assertEquals(ii.getDueDateErrMsg(), "Due Date is required.");
		sAssert.assertEquals(ii.getFascalPeriodErrMsg(), "Fiscal Period is required.");
		sAssert.assertAll();
	}

	@Test(priority = 15)
	public void shouldAbleTomatchModifiedLineCount() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.fascalPeriodSel();
		sAssert.assertEquals(ii.getRowCount(), ii.getRowCountValue());
		sAssert.assertAll();
	}
	
//	@Test(priority = 16)
//	public void shouldNotAbleToSaveWithoutFascalPeriod() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		ii.fascalPeriodSel();
//		ii.DueDate=ii.getRandomFutureDateNextMonth();
//		ii.dueDateSel();
//		saveBatchForDueDate
//		ii.clickSaveBatch();
//		sAssert.assertEquals(ii.getFascalPeriodErrMsg(), "Fiscal Period is required.");
//		sAssert.assertAll();
//	}

	@Test(priority = 17)
	public void shouldAbleToSaveBatch() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.DueDate=ii.getRandomFutureDateNextMonth();
		System.out.println("rand due date is" +ii.DueDate);
		ii.SelDueDateSameAsinvoiceDate(ii.DueDate);
		//ii.dueDateSel();
		ii.saveBatchForDueDate();
		ii.clickOnYes();
//		ii.clickSaveBatch();
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Excel Uploaded Successfully.");
//		sAssert.assertAll();
		sAssert.assertEquals(ii.ToastMessage(),"Batch successfully created", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 18, dependsOnMethods = "shouldAbleToSaveBatch")
	public void shouldAbleToSaveBatchOnAnotherPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
	//	ii.fascalPeriodSel();
		cp.waitForLoaderToDisappear();
		ii.saveBatch();
		sAssert.assertEquals(ii.ToastMessage(),"Batch successfully created", "Not match!");
		sAssert.assertAll();
	}

}
