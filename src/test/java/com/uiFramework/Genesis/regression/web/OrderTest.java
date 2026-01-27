package com.uiFramework.Genesis.regression.web;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.AssertJUnit;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class OrderTest extends TestBase {
	OrderPage op = null;
	CommonMethods cm = null;
	CommonPage cp = null;
	WaitHelper wt = null;
	JavaScriptHelper js = null;
	int totalActwait;
	int totalActpieces;
	Random random = new Random();
	String ordernum = "ORDERc-" + System.currentTimeMillis() + "-" + random.nextInt(100);
	
	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
		op = new OrderPage(driver);
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		wt = new WaitHelper(driver);
		js = new JavaScriptHelper(driver);
		//driver.navigate().refresh();

	}

	@Test(priority = 1, alwaysRun = true, groups = {"smoke"})
	public void shouldDisplayOrderFormOnCreateButtonClick() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.createOrder();
		AssertJUnit.assertEquals(op.fetchText(), "Order & Address Details");
		sAssert.assertAll();
	}

	@Test(priority = 2)
	public void shouldShowErrorMessagesForMandatoryFields() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.testFieldErrorMessages(DataRepo.SAVE_ACTION);
		op.testFieldErrorMessages(DataRepo.CONFIRM_ACTION);
		AssertJUnit.assertFalse(op.distributorFiled().isEnabled());
		sAssert.assertAll();
	}

	@Test(priority = 3,alwaysRun = true, groups = {"smoke"})
	public void shouldKeepConsigneeDropdownEmptyUntilCustomerSelected() throws Throwable {
		op.selectAnyWarehouseOrSkip();
		op.isConsigneeDropdownEmpty();
		op.selectAnyValidCustomerOrFail();
		op.selectAnyConsigneeOrSkip();
	}

	@Test(priority = 4)
	public void shouldValidateLocalCarrierTextAndQAStatusAndPODUnset() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.getLocalcarrierText();
		op.verifySetContainFieldBasedOnBOLType(DataRepo.VALUE);
		sAssert.assertEquals(op.qaStatus(), DataRepo.QA_PENDING, "QA status not matched");
		sAssert.assertTrue(op.getPodCheckboxValue() == null || op.getPodCheckboxValue().isEmpty(),
				"Checkbox should be unselected");
		sAssert.assertAll();
	}

	@Test(priority = 5,alwaysRun = true, groups = {"smoke"})
	public void shouldValidateFullAddressDetails() throws Throwable {
		op.getAdrees1(DataRepo.ADDRESS);
		op.getAdrees2();
		op.getAdrees3();
		op.state(DataRepo.STATE);
		op.getCity(DataRepo.CITY);
		op.getZipcode(DataRepo.ZIP);
	}

	@Test(priority = 6)
	public void shouldFetchConsigneeAddressDetails() throws Throwable {
		op.getAdrees1(DataRepo.ADDRESS);
		op.state(DataRepo.STATE);
		op.getCity(DataRepo.CITY);
		op.getZipcode(DataRepo.ZIP);
	}

	@Test(priority = 7,alwaysRun = true, groups = {"smoke"})
	public void shouldShowToastWhenConfirmingWithoutWeightOrPieces() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.enterCustOrdNum(cm.getNumericString(8));
		op.selectFirstOrderType();
		op.enterSapOrdNum(cm.getNumericString(5));
		op.enterParentOrdNum(cm.getNumericString(5));
		op.enterWaveNum(cm.getNumericString(5));
		op.AddNote(cm.getcharacterString(20));
		op.confirm();
		sAssert.assertEquals(cp.captureToastMessage(),
				"Please ensure that either actual weight or literature weight is provided");
		sAssert.assertAll();
	}

	@Test(priority = 8,alwaysRun = true, groups = {"smoke"})
	public void shouldCalculateRatedWeightAndLitWeightCorrectly() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.enterActWeight(DataRepo.VALUE);
		op.enterDimWeight(DataRepo.DIM_WEIGHT);
		op.enterPieces(DataRepo.VALUE);
		op.enterLitWeight(DataRepo.LIT_WEIGHT);
		op.enterLitDimWeight(DataRepo.LIT_DIM_WEIGHT);
		op.enterLitPieces(DataRepo.LIT_PIECES);
		sAssert.assertEquals(op.ratedWeight(), DataRepo.DIM_WEIGHT, "Rate weight should be max of actual or dim wt.");
		sAssert.assertEquals(op.ratedLitweight(), DataRepo.LIT_DIM_WEIGHT, "Rated lit weight max of lit actual or litDim wt.");
		sAssert.assertAll();
	}

	@Test(priority = 9, alwaysRun = true, groups = {"smoke"})
	public void shouldVerifyDraftOrderStatusAfterSave() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cp.save();
		sAssert.assertEquals(op.verifyOrderStatus(), "Draft", "Not Matched!");
		sAssert.assertAll();
	}
	
	@Test(priority = 10,alwaysRun = true, groups = {"smoke"})
	public void shouldGenerateBarcodeAfterOrderConfirmation() throws Throwable {
		op.verifyBarcodeAfterConfirmation(DataRepo.VALUE);
		op.getOrderData();
	}
	
	@Test(priority = 11,alwaysRun = true, groups = {"smoke"})
	public void shouldShowErrorWhenSavingEmptyCustomerAccessorial() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.clickAccessorialButton();
		sAssert.assertEquals(op.addAccessorial(), "Customer Accessorial is required. | Price is required.",
				"customer accessorial not matched");
		sAssert.assertAll();
	}

	@Test(priority = 12, alwaysRun = true, groups = {"smoke"}, dependsOnMethods="shouldShowErrorWhenSavingEmptyCustomerAccessorial")
	public void shouldDeleteCustomerAccessorialSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCustAccess();
		op.editCustAccess(DataRepo.PRICE2);
		op.deleteCustAccess();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer accessorial deleted successfully", "Not Matched");
		sAssert.assertAll();
	}


	@Test(priority = 13,alwaysRun = true, groups = {"smoke"})
	public void shouldSaveCustomerAccessorialAfterDeleteAndValidateCarrierValidation() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCustAccessAfterDelete();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer accessorial save successfully", "Not Matched.");
		sAssert.assertEquals(op.addCarrAccessorial(), "Carrier Accessorial is required. | Price is required.",
				"Not matched.");
		sAssert.assertAll();

	}

	@Test(priority = 14,alwaysRun = true, groups = {"smoke"})
	public void shouldAddEditAndDeleteCarrierAccessorialSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCarrAccess(DataRepo.PRICE, DataRepo.TEXT);
		op.editCarrAccess(DataRepo.PRICE2);
		op.deleteCarrAccess();
		sAssert.assertEquals(cp.captureToastMessage(), "Carrier accessorial deleted successfully", "Not matched.");
		sAssert.assertAll();
	}

	@Test(priority = 15 ,alwaysRun = true, groups = {"smoke"})
	public void shouldSaveCarrierAccessorialAfterDeleteAndValidateCommentValidation()
			throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCarrAccessAfterDelete(DataRepo.PRICE, DataRepo.TEXT);
		sAssert.assertEquals(cp.captureToastMessage(), "Carrier accessorial save successfully", "Not Match");
		sAssert.assertAll();
	}
	
	@Test(priority = 16 ,alwaysRun = true, groups = {"smoke"})
	public void shouldSaveTypeAndsubjectReqOnComment() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(op.addComment(), "Type is required. | Subject is required.", "Not Matched");
		sAssert.assertAll();
	}

	@Test(priority = 17)
	public void shouldAddEditAndDeleteCommentSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveComment("PDMA", DataRepo.SUBJECT, DataRepo.COMMENT);
		op.deleteComment();
		sAssert.assertEquals(cp.captureToastMessage(), "Comment deleted successfully", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 18,alwaysRun = true, groups = {"smoke"})
	public void shouldSaveCommentAfterDelete()
			throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCommentAfterDelete("Contact", DataRepo.SUBJECT, DataRepo.COMMENT);
		sAssert.assertEquals(cp.getToastMessage(), "Comment save successfully", "Not match!");
		//boolean flag = cp.toastMsgReceivedSuccessfully();
		//sAssert.assertTrue(flag, "Comment save successfully");
		sAssert.assertAll();
	}
	
	@Test(priority = 19,alwaysRun = true, groups = {"smoke"})
	public void shouldShowMandatoryFieldOnSaveFiles()throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(op.addFile(), "Document Name is required. | File is required.", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 20, groups = {"smoke"}, dependsOnMethods="shouldShowMandatoryFieldOnSaveFiles")
	public void shouldUploadFileAndShowSuccessToast() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.savefile("update price");
		sAssert.assertEquals(cp.captureToastMessage(), "File save successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 21, dependsOnMethods = "shouldUploadFileAndShowSuccessToast")
	public void shouldDeleteFileSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.deleteFile();
		sAssert.assertEquals(cp.getToastMessage(), "File deleted successfully", "Not match!");
		sAssert.assertAll();
	}

//	@Test(priority = 19,alwaysRun = true, groups = {"smoke"})
//	public void shouldGenerateBarcodeAfterOrderConfirmation() throws Throwable {
//		op.verifyBarcodeAfterConfirmation(DataRepo.VALUE);
//		op.getOrderData();
//	}

	@Test(priority = 22, dependsOnMethods = "shouldGenerateBarcodeAfterOrderConfirmation",alwaysRun = true, groups = {"smoke"})
	public void shouldMatchOrderDetailsOnListingPage() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		op.serachorder();
		sAssert.assertEquals(op.getWarehouse(), op.MWarehouse, "Warehouse mismatch");
		sAssert.assertEquals(op.getCustomer(), op.MCustomer, " Customer mismatch");
		sAssert.assertEquals(op.getCustomerOrder(), op.MCusorderNo, "Customer Order mismatch");
		sAssert.assertAll();
	}

	@Test(priority = 23, dependsOnMethods = "shouldGenerateBarcodeAfterOrderConfirmation",alwaysRun = true, groups = {"smoke"})
	public void shouldValidateTotalPiecesAndWeightOnListingPage() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		totalActpieces = Integer.parseInt(op.Noofpieces) + Integer.parseInt(op.Litnoofpieces);
		totalActwait = Integer.parseInt(op.Actualwt) + Integer.parseInt(op.Litactualwt);
		sAssert.assertEquals(op.getActualpieces(), String.valueOf(totalActpieces), "Actual pieces mismatch");
		sAssert.assertEquals(op.getActulwt(), String.valueOf(totalActwait), "Actual weight mismatch");
		sAssert.assertAll();
	}

	@Test(priority = 24)
	public void shouldValidateOrderStatusCountOnLabelBadges() throws Throwable {
		op.landingOrderListing();
		op.checkStatusCountOneByone();
	}

	@Test(priority = 25)
	public void shouldShowErrorForDuplicateCustomerOrderNumber() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		op.orderCreationforDuplicateCustomer(op.MCusorderNo);
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(), "Duplicate customer order number.", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 26, dependsOnMethods="shouldShowErrorForDuplicateCustomerOrderNumber")
	public void shouldNotGenerateBarcodeAndShowCorrectOrderStatusOnConfirm()
			throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		String ordernum = "D" + System.currentTimeMillis() + "-" + random.nextInt(10);
		op.enterCustOrdNum(ordernum);
		op.verifyNoBarcodeAfterConfirmation(DataRepo.VALUE);
		sAssert.assertEquals(op.verifyOrderStatus(), "Inbound Pallet Pending", "Not matched!");
		sAssert.assertAll();
	}

	@Test(priority = 27)
	public void shouldShowErrorOnConfirmFromListingWithMissingFields() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		op.orderCreation(ordernum);
		op.cityDropdown();
		//String actualMessage = 
		op.orderConfirmfromListing(ordernum);
//		actualMessage = actualMessage.replaceAll("GL-\\d+ :", "").trim();
//		sAssert.assertEquals(actualMessage, "Actual weight or literature actual weight required.", "Not matched.");
//		sAssert.assertAll();
		sAssert.assertTrue(op.isErrorMessagePresent("Actual weight or literature actual weight required."),
				"Error msg not displyed for Weight.");
		sAssert.assertTrue(op.isErrorMessagePresent("Number of pieces or literature number of pieces required."),
				"Error msg not displyed for Pieces.");
		op.clickOk();
		sAssert.assertAll();
	}

	@Test(priority = 28, dependsOnMethods = "shouldShowErrorOnConfirmFromListingWithMissingFields")
	public void shouldDeleteOrderSuccessfullyWhenInDraftStatusInDraft() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.clickOk();
		op.deleteOrder();
		sAssert.assertEquals(cp.captureToastMessage(), "Order deleted successfully", "Not matched!");
		sAssert.assertAll();
	}

	@Test(priority = 29)
	public void shouldCreateDistributorOrderWithRequiredDetails() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
	//	String ordernum = "ORDER-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
		op.landingOrderListing();
		cp.createOrder();
		op.selectAnyWarehouseOrSkip();
		op.selectAnyValidCustomerOrFail();
		op.selectAnyConsigneeOrSkip();
		op.orderSource(DataRepo.ORDER_SOURCE);
		op.selectAnyDistributorOrSkip();
		op.enterCustOrdNum(cm.getNumericString(8));
		op.selectFirstOrderType();
		op.getPickupLocationText();
		op.enetrpieces(DataRepo.VALUE);
		sAssert.assertEquals(cp.captureToastMessage(), "Order details save successfully", "Not matched");
		sAssert.assertAll();
	}

	@Test(priority = 30, dependsOnMethods = "shouldCreateDistributorOrderWithRequiredDetails")
	public void shouldDisplayBarcodeAfterDistributorOrderConfirmation() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		op.verifyBarcodeAfterConfirmationOnListing();
		sAssert.assertTrue(op.isPrintButtonDisplayed(), "Print button should be displayed");
		op.verifyBarcodeIfDisplayed();
		cp.clickBackBtn();

	}

	@Test(priority = 31)
	public void shouldNotGenerateBarcodeWhenCheckboxIsUnchecked() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
	//	String ordernum1 = "ORDER-" + System.currentTimeMillis() + "-" + random.nextInt(100);
		op.landingOrderListing();
		op.orderCreation(cm.getNumericString(8));
		op.enetrpieces(DataRepo.VALUE);
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		cp.clickBackBtn();
		op.untickBarcodeGenertion();
		op.verifyBarcodeAfterConfirmationOnListing();
		sAssert.assertFalse(op.isPrintButtonDisplayed(), "Print button should NOT be displayed.");
		sAssert.assertAll();
	}

	@Test(priority = 32)
	public void shouldConfirmMultipleOrdersAndDisplaySuccessToast() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.verifyBarcodeIfDisplayed();
		op.landingOrderListing();
		op.orderCreationforDuplicateCustomer(cm.getNumericString(8));
		op.enetrpieces(DataRepo.VALUE);
		op.landingOrderListing();
		op.orderCreationforDuplicateCustomer(cm.getNumericString(8));
		op.enetrpieces(DataRepo.VALUE);
		op.landingOrderListing();
		op.confirmTwoOrders();
		sAssert.assertEquals(cp.captureToastMessage(), "Selected order confirm successfully", "Not matched");
		sAssert.assertAll();
	}

	@Test(priority = 33, dependsOnMethods = "shouldConfirmMultipleOrdersAndDisplaySuccessToast")
	public void shouldAllowOrderDeletionAfterConfirmation() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.deleteOrder();
		sAssert.assertEquals(cp.captureToastMessage(), "Order deleted successfully", "Not match");
		sAssert.assertAll();
	}

	@Test(priority = 34)
	public void shouldNotAllowDeletionWhenOrderInPalletStage() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		op.serchStatus(DataRepo.PENDING);
		op.deleteOrder();
		sAssert.assertEquals(cp.captureToastMessage(), "Order not in draft or inbound pallet pending stage",
				"Not match");
		op.onConfirmFieldDisable();
		sAssert.assertAll();
	}

	@Test(priority = 35, enabled = true)
	public void shouldFilterOrdersByVariousFieldsSuccessfully() throws InterruptedException {
		op.landingOrderListing();
		cp.searchClick();
		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
		cp.selectMultipleCustomerByIndex(indicesToSelect1);
		cp.validateDataInGridForScroll(3);
		List<Integer> indicesToSelect = Arrays.asList(13, 2, 8);
		cp.selectMultipleConsigneesByIndex(indicesToSelect);
		cp.validateDataInGridForScrollForConsignee(5);
		List<Integer> indicesToSelect2 = Arrays.asList(6, 9, 8);
		cp.selectMultipleDistributorByIndex(indicesToSelect2);
		cp.validateDataInGridForScroll(2);
		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 3);
		cp.selectMultipleLDAByIndex(indicesToSelect3);
		cp.validateDataInGridForScroll(8);
		List<Integer> indicesToSelect4 = Arrays.asList(1, 2, 11);
		cp.selectMultipleLTLByIndex(indicesToSelect4);
		cp.validateDataInGridForScroll(9);
		List<Integer> indicesToSelect5 = Arrays.asList(6, 19, 14);
		cp.selectMultipleUsersByIndex(indicesToSelect5);
		cp.validateDataInGridForScroll(16);
		List<Integer> indicesToSelect6 = Arrays.asList(1, 2, 4);
		cp.selectMultipleWarehouseByIndex(indicesToSelect6);
		cp.validateDataInGridForScroll(1);
		List<Integer> indicesToSelect7 = Arrays.asList(1, 2, 3);
		cp.selectMultipleCreationModeByIndex(indicesToSelect7);
		cp.validateDataInGridForScroll(13);
		List<Integer> indicesToSelect8 = Arrays.asList(1, 3, 8);
		cp.selectMultiplestatusByIndex(indicesToSelect8);
		cp.validateDataInGridForScroll(15);
		cp.clickClearButton();
		op.searchAndValidateOrderNumber();
		op.searchAndValidateCustomerOrderNumber();
		cp.searchAndValidateDateByColumn(17);
		cp.searchAndValidateToDateByColumn(17);
		op.searchAndValidateWaveNum();
	}

	@Test(priority = 36, alwaysRun = true, groups = {"smoke"})
	public void shouldCheckColumnFilter() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		cp.verifyColumnFilter("frozen-pane");
		cp.verifyColumnFilter("scrollable-pane");
		sAssert.assertAll();
	}
	
//	@Test(priority = 34, enabled = false)
//	public void shouldFilterOrderListingUsingColumnFilters() throws InterruptedException, IOException {
//		op.landingOrderListing();
//		op.checkColoumFilterForOrderListing();
//	}
	
/**
 * Removed pagination logic 
 */
//	@Test(priority = 35)
//	public void shouldPaginateOrderListingCorrectly() {
//		op.landingOrderListing();
//		op.paginationTest();
//	}
	
	@Test(priority = 37, alwaysRun = true, groups = {"Smoke"})
	public void shouldCheckPDFDownloadedOnOrderPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpPDF();
		sAssert.assertTrue(cp.isFileDownloadWithExtension(".pdf"), "FAIL: File Extension not match With Downloaded file.");
		sAssert.assertAll();
	}
	
	@Test(priority = 38, alwaysRun = true, groups = {"Smoke"})
	public void shouldCheckExcelDownloadedOnOrderPage() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.deleteExistingFiles();
		cp.clickToExpExcel();
		sAssert.assertTrue(cp.isFileDownloadWithExtension(".xlsx"), "FAIL: File Extension not match With Downloaded file.");
		sAssert.assertAll();
	}

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = cm
				.getJsonData(System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\DynamicData.JSON");
		return new Object[][] { { data.get(0) } };
	}

}
