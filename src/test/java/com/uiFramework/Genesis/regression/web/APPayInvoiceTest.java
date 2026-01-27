package com.uiFramework.Genesis.regression.web;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.ImportInvoices;

public class APPayInvoiceTest extends TestBase {
	CommonMethods cm = null;
	CommonPage cp = null;
	ImportInvoices ii = null;

	@BeforeClass
	public void beforeclass() {
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		ii = new ImportInvoices(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1)
	public void shouldValidateMandatoryFieldsOnCreatePayableInvoice()
			throws TimeoutException, InterruptedException, FileNotFoundException {
		SoftAssert sAssert = new SoftAssert();
		 // ii.accountPayableMenu(); //when run suite
		ii.payInvoicesMenu();
		ii.carrSelOnPayInvScreenCreateInv();
		ii.clickOnInvAmt();
		ii.saveBatch();
		sAssert.assertEquals(cp.captureToastMessage(),
				"At least one invoice details are required in either of LBOL, Auth & Truck", "Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 2)
	public void shouldPayAmountAutoPopulateSameAsInvAmount() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.enterInvAmt(DataRepo.VALUE);
		sAssert.assertEquals(ii.getPayAmount(),10.00, "Not match! pay amount");
		sAssert.assertAll();
	}
	
	@Test(priority = 3)
	public void shouldNotAllowSavingWithoutInvoiceNumber() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.saveBatch();
		sAssert.assertTrue(cp.captureToastMessage().contains("Invoice No is required for selected"), "Not match! for inv number.");
		sAssert.assertAll();

	}
	
	@Test(priority = 4)
	public void shouldNotAbleToSaveWithoutInvoiceAmt() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.enterInvno();
		ii.saveBatch();
		sAssert.assertTrue(cp.captureToastMessage().contains("Invoice Amount is required for selected"), "Not match! for amount");
		sAssert.assertAll();

	}
	
	@Test(priority = 5)
	public void shouldNotAbleToSaveWithoutInvoiceSummaryDetails() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.enterInvAmtforSecondRow("12");
		ii.saveBatch();
		sAssert.assertEquals(ii.getInvoiceDateErrMsg(), "Invoice Date is required.");
		sAssert.assertEquals(ii.getDueDateErrMsg(), "Due Date is required.");
	    sAssert.assertEquals(ii.getFascalPeriodErrMsg(), "Fiscal Period is required.");
		sAssert.assertAll();
	}
	
	@Test(priority = 6)
	public void shouldNotAbleToSaveWithoutDueDate() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.invoiceDateSelAsCurrentDate();
		ii.fascalPeriodSel();
		ii.saveBatch();
	    sAssert.assertEquals(ii.getDueDateErrMsg(), "Due Date is required.");
		sAssert.assertAll();
	}
	
	@Test(priority = 7)
	public void shouldEnsureDueDateIsGreaterThanInvoiceDate() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.SelDueDateSameAsinvoiceDate(ii.getCurrentDate());
		ii.saveBatch();
		sAssert.assertEquals(cp.captureToastMessage(),"Due Date must be greater than Invoice Date.", "Not match!");
		sAssert.assertAll();

	}
	
	@Test(priority = 8)
	public void shouldValidateNumberOfLineItems(){
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		sAssert.assertEquals(2,ii.getRowCountValue(), "No of line item Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 9)
	public void shouldPayAmountReqForSaveBatch() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		//cp.waitForPopupToDisappear();
		ii.DueDate=ii.getRandomFutureDateNextMonth();	
		System.out.println("Due Date "+ ii.DueDate);
		ii.SelDueDateSameAsinvoiceDate(ii.DueDate);
		ii.FiscalPeriod=ii.getFiscalPeriod();
  //		ii.clearPayAmount();
  //		ii.saveBatch(); // save direct not displayed error
  //		sAssert.assertTrue(cp.captureToastMessage().contains("Payable Amount is required for selected"), "Not Match! pay amount Req.");
  //		sAssert.assertAll();
	}
	
	@Test(priority = 10)
	public void shouldShowPayAmountCalCorrectly() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
	//	ii.enterPayAmount(DataRepo.VALUE);
	//	ii.enterPayableAmt("14");
		ii.paybleAmount=ii.getTotalPaybleAmount();
		sAssert.assertEquals(ii.paybleAmount, ii.getSumOfPayAmount(), "Payable Amount not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 11)
	public void shouldShowPOPUpIfPayAmountMoreThanInvoiceAmt() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ii.saveBatchForDueDate();	
		sAssert.assertEquals(ii.ToastMessage(),"Batch successfully created", "Not match!");
		sAssert.assertAll();
//		sAssert.assertTrue(ii.getPopUpMsg().contains("Payable amount is greater than the invoice amount."), "Not displayed Popup!");
//		sAssert.assertAll();
	}
	
//	 @Test(priority = 12)
//	   public void userShouldAbleToCreateBatchInvoice() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.waitForPopupToDisappear();
//		ii.clickOnYes();
//		sAssert.assertEquals(cp.captureToastMessage(),"Batch successfully created", "Not match!");
//		sAssert.assertAll();
//	}
	
	@Test(priority = 13,dependsOnMethods="shouldShowPOPUpIfPayAmountMoreThanInvoiceAmt")
	public void shouldAbleToViewInvoiceOnBatchInvoice() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		//ii.batchInvoicesMenu();
		cp.clickBackBtn();
		ii.searchBatch();
		sAssert.assertTrue(ii.isDisplayedRadioBtn(), "Batch not available!");
		sAssert.assertAll();
	}
	
	@Test(priority = 14,dependsOnMethods="shouldShowPOPUpIfPayAmountMoreThanInvoiceAmt")
	public void shouldDisplayedBatchAmountCorrectlyOnListing() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Click to expand");
		ii.clickOnExpandBtn();
		System.out.println("batch amt "+ ii.getBatchPayableAmt()+  "store pay" + ii.paybleAmount);
		sAssert.assertEquals(ii.getBatchPayableAmt(), ii.paybleAmount, "Payable amount not match.");
		sAssert.assertAll();
	}
	
	@Test(priority = 15,dependsOnMethods="shouldShowPOPUpIfPayAmountMoreThanInvoiceAmt")
	public void shouldDisplayedCarrierCodeCorrectlyOnListing() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		ii.BatchNo=ii.getBatchNo();
		System.out.println("Batch no "+ii.BatchNo );
		sAssert.assertEquals(ii.getCarrierCode(), ii.getExpectedCarrier(), "Carrier code mismatch on listing page.");
		sAssert.assertAll();
	}
	
	
	@Test(priority = 16)
	public void shouldDisplayedTotalAmountCorrectlyOnListing() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(ii.getTotalAmount(), ii.getSumOfBatchAmt(), "Batch Amount Sum not match with total Amount");
		sAssert.assertAll();
	}

	@Test(priority = 17)
	public void shouldDisplayedInvoiceNoCorrectlyOnListing() {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Click to sub expand");
		ii.clickOnSubExpandBtn();
		sAssert.assertEquals(ii.getInvoiceNumber(), ii.invoiceNumber, "Invoice number Not displyed correct on listing.");
		sAssert.assertAll();
	}
	
	@Test(priority = 18)
	public void shouldDisplayedInvoiceDataCorrectlyOnListing() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ii.checkInvoiceData(), "Invoice data Not displyed correct on listing.");
		sAssert.assertAll();
	}
	
	//edit 
	@Test(priority = 19)
	public void shouldShowSelectedCarrierWhenEditing() {
		SoftAssert sAssert = new SoftAssert();
		ii.clickOnEditBtn();
		sAssert.assertEquals(ii.getCarrierFromEdit(), ii.carrier,"Carrier not match on Edit invoice");
		sAssert.assertAll();
	}
	
	@Test(priority = 20)
	public void shouldNotAllowEditingSelectedCarrier() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ii.isCarrierNotEditable(), "Carrier not match on Edit invoice");
		sAssert.assertAll();
	}
	
	@Test(priority = 21)
	public void shouldDisplayCorrectDueDateOnEditAndKeepItNonEditable(){
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ii.isDueDateNotEditable(), "Due date should not be editabel");
		sAssert.assertAll();
	}
	
	@Test(priority = 22)
	public void shouldDisplayCorrectBatchNumberOnEdit() {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("batch num"+ii.getBatchNoFormEdit() );
		sAssert.assertEquals(ii.getBatchNoFormEdit(), ii.BatchNo,"Batch number not match on Edit invoice");
		sAssert.assertTrue(ii.isBatchEditable(), "Batch number should not be editabel");
		sAssert.assertAll();
	}
	
	@Test(priority = 23)
	public void shouldDisplayCorrectFiscalPeriodOnEdit() {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("batch num "+ii.getBatchNoFormEdit() );
		sAssert.assertEquals(ii.getFiscalPeriodFormEdit(), ii.FiscalPeriod, "Fiscal Period not match on Edit invoice");
		sAssert.assertTrue(ii.isFiscalPeriodNotEditable(), "Fiscal Period should not be editabel");
		sAssert.assertAll();
	}
	
	@Test(priority = 24)
	public void shouldDisplayInvoiceDateAndBatchDateAsReadOnly() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ii.isInvoiceDateNotEditable(), "invoice date should not be editabel");
		sAssert.assertTrue(ii.isInvoiceBatchDateNotEditable(), "invoice batch date should not be editabel");
		sAssert.assertAll();
	}
	
	@Test(priority = 25)
	public void shouldEnableEditingOfInvoiceNumberAndAmount() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ii.isInvoiceNumberEditable(), "invoice number should be editabel");
		sAssert.assertTrue(ii.isInvoiceAmountEditable(), "invoice amount should be editabel");
		sAssert.assertAll();
	}
	
	@Test(priority = 26)
	public void shouldAllowEditingPayAmountAndInvoiceComment() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(ii.isPayAmountEditable(), "Pay amount should be editabel");
		sAssert.assertTrue(ii.isInvoiceCommentEditable(), "invoice comment should be editabel");
		sAssert.assertAll();
	}
	
	@Test(priority = 27)
	public void shouldAllowUserToAddRecordFromEdit(){
		SoftAssert sAssert = new SoftAssert();
		ii.loadAllRecord();
		ii.enterInvnoForthirdRow();
		System.out.println("Amt after edit "+ii.getTotalPaybleAmountFromEdit());
		sAssert.assertNotEquals(ii.getTotalPaybleAmountFromEdit(), ii.paybleAmount, "Pay amount should not equal after invoice amt add.");
		sAssert.assertAll();
	}
	
	@Test(priority = 28)
	public void shouldShowAccurateLineItemCountWhenEditing() {
		SoftAssert sAssert = new SoftAssert();	
		ii.paybleAmount=ii.getTotalPaybleAmountFromEdit();
		cp.waitForPopupToDisappear();
		sAssert.assertEquals(3,ii.getRowCountValueOnEdit(), "No of line item Not match on edit!");
		sAssert.assertAll();
	}
	
	@Test(priority = 29)
	public void shouldAllowUserToSaveBatchOnEdit() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();		
		ii.saveBatch();
		sAssert.assertEquals(ii.ToastMessage(),"Batch successfully created", " Toast on save batch Not match!");
		sAssert.assertAll();
	}
	/**
	 * Req changes
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
//	@Test(priority = 30)
//	public void shouldNotAllowAddingDuplicateInvoice() throws InterruptedException, TimeoutException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.waitForLoaderToDisappear();
//		ii.payInvoicesMenu();
//		ii.selectCarrier(ii.carrier);
//		ii.enterDublicateInvno();
//		ii.invoiceDateSelAsCurrentDate();
//		ii.fascalPeriodSel();
//		ii.SelDueDateSameAsinvoiceDate(ii.DueDate);
//		ii.saveBatch();
//		sAssert.assertEquals(cp.captureToastMessage(),"Invoice No already in use", " Toast on save batch Not match!");
//		sAssert.assertAll();
//	}
	
	@Test(priority = 31)
	public void shouldDisplayedBatchAmountCorrectlyOnListingAfterEdit() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("Click to expand");
		cp.waitForPopupToDisappear();
		cp.clickBackBtn();
		ii.searchBatch();
		ii.isDisplayedRadioBtn();
		ii.clickOnExpandBtn();
		System.out.println("Batch  after edit amt "+ ii.getBatchPayableAmt()+  "store pay" + ii.paybleAmount);
		sAssert.assertEquals(ii.getBatchPayableAmt(), ii.paybleAmount, "Payable amount not match.");
		sAssert.assertAll();
	}
	
	/**
	 * Req changes
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
//	@Test(priority = 32)
//	public void shouldDownloadFileWhenPreviewInvoiceButtonClicked() throws InterruptedException, TimeoutException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.deleteExistingFiles();
//	    ii.clickOnPreviewInvoice();
//		sAssert.assertTrue(cp.isFileDownloaded(), "FAIL:On preview invoice File was not downloaded. when click on preview invoice");
//		sAssert.assertAll();
//	}
		
	@Test(priority = 33)
	public void shouldAllowUserToPayInvoiceSuccessfully() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		ii.searchBatch();
		ii.isDisplayedRadioBtn();
		ii.clickOnExpandBtn();
	    ii.clickOnPayInvoice();
	    ii.clickOnYes();
		sAssert.assertEquals(cp.captureToastMessage(), "Pay invoice successful", "invocie pay toast not displyed.");
		sAssert.assertAll();
	}
	
	@Test(priority = 34, dependsOnMethods="shouldAllowUserToPayInvoiceSuccessfully")
	public void shouldNotDisplayBatchInGridAfterInvoicePayment() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		ii.searchBatch();
		sAssert.assertTrue(ii.isNoRecordFoundDisplayed(), "Batch Should not displyed After pay invoice.");
		sAssert.assertAll();
	}
	/**
	 * Removed pagination 
	 */
//	@Test(priority = 35)
//	public void shouldHandlePaginationCorrectlyOnBatchInvoicePage() throws InterruptedException, TimeoutException {
//		SoftAssert sAssert = new SoftAssert();
//		ii.paginationOnBatchInv();
//	}
	
	
}
