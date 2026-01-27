package com.uiFramework.Genesis.regression.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CloseOutPage;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class CloseOutTest extends TestBase {
	CloseOutPage cop = null;
	CommonMethods cm = null;
	CommonPage cp = null;
	WaitHelper wt = null;
	NavigationPages np = null;
	JavaScriptHelper js = null;
	Random random = new Random();
	int totalActwait;
	int totalActpieces;
	String ordernumber = "ORDER-" + System.currentTimeMillis() + "-" + random.nextInt(1000);

	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
		test = extent.createTest(getClass().getSimpleName());
		cop = new CloseOutPage(driver);
		np = new NavigationPages(driver);
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		wt = new WaitHelper(driver);
		js = new JavaScriptHelper(driver);

	}

	@Test(priority = 1, alwaysRun = true)
	public void shouldValidateMandatoryFieldsOnOrderCreationBeforeSave() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.closeOutMenu();
		cop.AddNewOrderBtn();
		cp.save();
		sAssert.assertEquals(cop.getMandFieldCount(), 10, "Mandatory field count not match for save btn.");
		sAssert.assertAll();
	}

	@Test(priority = 2)
	public void shouldValidateMandatoryFieldsOnOrderCreationBeforeConfirm() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cop.clickOnConfirmBtn();
		sAssert.assertEquals(cop.getMandFieldCount(), 14, "Mandatory field count not match confirm btn.");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldNotPopulateConsigneeDropdownIfCustomerNotSelected() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cop.checkConsingee();
		sAssert.assertTrue(cop.isConsigneeListNotFound(), "Consignee list not empty");
		sAssert.assertAll();
	}

	@Test(priority = 4)
	public void shouldNotPopulateOrderTypeDropdownWhenCustomerNotSelected() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cop.checkOrderType();
		sAssert.assertTrue(cop.isDropDownEmpty(), "Order type is not empty");
		sAssert.assertAll();
	}

	@Test(priority = 5, alwaysRun = true)
	public void shouldAllowUserToSelectCustomerAndConsignee() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.selectAnyValidCustomerForCloseOutOrFail();
		cop.selectAnyConsigneeOrSkip();
		cop.selectFirstOrderType();
		cop.enterCustOrdNum(ordernumber);

	}

	@Test(priority = 6)
	public void shouldSetDefaultCarrierUponConsigneeSelection() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isCarrierDefaultSelected(), "Orignal carrier not selected by default");
		sAssert.assertAll();
	}

	@Test(priority = 7)
	public void shouldDisplayCloseOutTypeDropdown() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.clickOnCloseOutType();
		sAssert.assertTrue(cop.checkCloseOutTypeOption(), "Close Out type missing in drop down");
		sAssert.assertAll();
	}

	@Test(priority = 8, alwaysRun = true)
	public void shouldSetOrderStatusToDraftAfterSave() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.addSpecialInstuction(cm.getcharacterString(20));
		cop.clickOnCloseOutType();
		cop.selectRepReturn();
		cp.save();
		System.out.println("o Status" + cop.getOriginOrderStatus());
		sAssert.assertEquals(cop.getOriginOrderStatus(), "Draft", "Order status should be draft");
		sAssert.assertAll();
	}
	
	@Test(priority = 9, alwaysRun = true)
	public void shouldFetchConsigneeAddressDetails() throws Throwable {
		cop.getAdrees1("201 rd");
		cop.state("Newyork");
		cop.getCity("Newyork city");
		cop.getZipcode("30291");
	}

	@Test(priority = 10)
	public void shouldRequirePickUpDetailsOnConfirm() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.clickOnConfirmBtn();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Scheduled Date and Time is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(2), "Rep name is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(3), "Pickup Date and Time is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(4), "Pickup signed by is required", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void shouldValidateWeightAndShowToastOnConfirm() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.addPickUpDetails();
		cop.clickOnConfirmBtn();
		sAssert.assertEquals(cp.captureToastMessage(),
				"Please ensure that either actual weight or literature weight is provided", "Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 12, alwaysRun = true)
	public void shouldCalculateRatedWeightAndLitWeightCorrectly() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.enterActWeight(cm.getNumericString(2));
		cop.enterDimWeight(cm.getNumericString(2));
		cop.enterPieces(cm.getNumericString(2));
		cop.enterLitWeight(cm.getNumericString(2));
		cop.enterLitDimWeight(cm.getNumericString(2));
		cop.enterLitPieces(cm.getNumericString(2));
		sAssert.assertEquals(cop.ratedWeight(), cop.getMaxWt(),"Rate weight should be max of actual or dim wt.");
		sAssert.assertEquals(cop.ratedLitweight(), cop.getMaxLitWt(),"Rated lit weight max of lit actual or litDim wt.");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 13, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldShowErrorWhenSavingEmptyCustomerAccessorial() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cp.save();
		cp.waitForLoaderToDisappear();
		cop.clickAccessorialButton();
		sAssert.assertEquals(cop.addAccessorial(), "Customer Accessorial is required. | Price is required.",
				"customer accessorial not matched");
		sAssert.assertAll();
	}
//
	@Test(priority = 14, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldAddEditAndDeleteCustomerAccessorialSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.saveCustAccess();
		cop.editCustAccess(cm.getNumericString(2));
		cop.deleteCustAccess();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer accessorial deleted successfully", "Not Matched");
		sAssert.assertAll();
	}
//
	@Test(priority = 15 , dependsOnMethods="shouldSetOrderStatusToDraftAfterSave", alwaysRun = true)
	public void shouldSaveCustomerAccessorialAfterDeleteAndValidateCarrierValidation() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.saveCustAccessAfterDelete();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer accessorial save successfully", "Not Matched.");
		sAssert.assertEquals(cop.addCarrAccessorial(), "Carrier Accessorial is required. | Price is required.",
				"Not matched.");
		sAssert.assertAll();

	}

	@Test(priority = 16, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldAddedEditAndDeleteCarrierAccessorialSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.saveCarrAccess(cm.getNumericString(2), cm.getAlphaNumericString(5));
		cop.editCarrAccess(cm.getNumericString(2));
		cop.deleteCarrAccess();
		sAssert.assertEquals(cp.captureToastMessage(), "Carrier accessorial deleted successfully", "Not matched.");
		sAssert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave", alwaysRun = true)
	public void shouldSaveCarrierAccessorialAfterDeleteAndValidateCommentValidation() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.saveCarrAccessAfterDelete(cm.getNumericString(2),cm.getAlphaNumericString(5));
		sAssert.assertEquals(cp.captureToastMessage(), "Carrier accessorial save successfully", "Not Match");
		sAssert.assertEquals(cop.addComment(), "Type is required. | Subject is required.", "Not Matched");
		sAssert.assertAll();
	}
	
	@Test(priority = 18, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldAddEditAndDeleteCommentSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.saveComment(cm.getAlphaNumericString(5), cm.getAlphaNumericString(5));
		cop.editComment(cm.getAlphaNumericString(5));
		cop.deleteComment();
		sAssert.assertEquals(cp.captureToastMessage(), "Comment deleted successfully", "Not MAtch!");
		sAssert.assertAll();
	}
	
	@Test(priority = 19, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldSaveCommentAfterDeleteAndValidateFileUploadErrors() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.saveCommentAfterDelete(cm.getAlphaNumericString(5), cm.getAlphaNumericString(5));
		sAssert.assertEquals(cp.captureToastMessage(), "Comment save successfully", "Not match!");
		sAssert.assertEquals(cop.addFile(), "Document Name is required. | File is required.", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 20, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldUploadFileAndShowSuccessToast() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.savefile(cm.getAlphaNumericString(5));
		sAssert.assertEquals(cp.captureToastMessage(), "File save successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 21, dependsOnMethods="shouldSetOrderStatusToDraftAfterSave")
	public void shouldAllowUserToDownloadUploadedFileInOrder() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		cop.deleteExistingFiles();
		cop.downloadUploadedFile();
		sAssert.assertTrue(cop.isFileDownloaded(), "FAIL:File not downloaded");
		sAssert.assertAll();
	}

	@Test(priority = 22, dependsOnMethods="shouldUploadFileAndShowSuccessToast")
	public void shouldAllowUserToDeleteFile() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.deleteFile();
		sAssert.assertEquals(cp.captureToastMessage(), "File deleted successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 23)
	public void shouldDisplayOrderConfirmToastAfterConfirmingOrder() {
		SoftAssert sAssert = new SoftAssert();
		cop.clickToTenderOrder();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 24, dependsOnMethods="shouldDisplayOrderConfirmToastAfterConfirmingOrder")
	public void shouldUpdateOrderStatusAfterConfirm() {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("order Sta" + cop.getOriginOrderStatus());
		sAssert.assertEquals(cop.getOriginOrderStatus(), "Closed", "Order status should be Closed");
		sAssert.assertAll();
	}
	
	@Test(priority = 25, dependsOnMethods="shouldUpdateOrderStatusAfterConfirm")
	public void shouldAllowUserToPrintManifestDocument() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.deleteExistingFiles();
		cop.printManifest();
		sAssert.assertTrue(cop.isFileDownloaded(), "FAIL: Manifest not downloaded!");
		sAssert.assertAll();
	}
	
	@Test(priority = 26, dependsOnMethods="shouldUpdateOrderStatusAfterConfirm")
	public void shouldAllowUserToPrintLbolDocument() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.deleteExistingFiles();
		cop.printLBOL();
		sAssert.assertTrue(cop.isFileDownloaded(), "FAIL: LBOL document not downloaded!");
		sAssert.assertAll();
	}
	
	@Test(priority = 27, dependsOnMethods="shouldUpdateOrderStatusAfterConfirm")
	public void shouldUpdateOutboundQAStatusAfterOrderConfirm() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getQAStatus(),"QA Passed", "QA status Should be QA passed!");
		sAssert.assertAll();
	}
	
 /**	
 * Closeout line for Rep Return
 */
	
	@Test(priority=28)//, dependsOnMethods="shouldUpdateOrderStatusAfterConfirm"
	public void shouldSetConsigneeToOrigin() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		cop.clickOnCloseOutLine();
		sAssert.assertEquals(cop.getConsingeeTo(),"Origin", "Consignee to should be selected as Origin");
		sAssert.assertAll();
	}
	
	@Test(priority=29, dependsOnMethods="shouldSetConsigneeToOrigin")
	public void shouldValidatePiecesAndLiterPiecesOnCloseOutLines(){
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getPieces(), cop.getLinePieces(), "No of Pieces not Same On CloseOut line.");
		sAssert.assertEquals(cop.getLitPieces(), cop.getLineLitPieces(), "No of literature Pieces not Same On CloseOut line.");
		sAssert.assertAll();
	}
	
	@Test(priority=30, dependsOnMethods="shouldSetConsigneeToOrigin")
	public void shouldValidateMandatoryFieldsOnCloseOutLine(){
		SoftAssert sAssert = new SoftAssert();
		cop.saveLine();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Origin Source is required.", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(2), "Consignee is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(3), "Consignee Address is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(4), "Order Type is required.", "Not Match!");	
		sAssert.assertAll();
	}
	
	@Test(priority=31, dependsOnMethods="shouldSetConsigneeToOrigin")
	public void shouldRequireWarehouseWhenWarehouseCheckboxSelected(){
		SoftAssert sAssert = new SoftAssert();
		cop.clickWHcheckBox();
		cop.saveLine();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(4), "Warehouse is required.", "Not Match!");	
		sAssert.assertAll();
	}
	
	@Test(priority=32, dependsOnMethods="shouldRequireWarehouseWhenWarehouseCheckboxSelected")
	public void shouldNotAllowBlankConsigneeAddress(){
		SoftAssert sAssert = new SoftAssert();
		cop.selectConsigneeType("Customer");
		sAssert.assertTrue(cop.isConsigneeAddressEditableAndNotEmpty(), "Consignee Pickup should not empty");
		sAssert.assertAll();
	}
	
	@Test(priority=33, dependsOnMethods="shouldRequireWarehouseWhenWarehouseCheckboxSelected")
	public void shouldNotAllowUserToEnterMorePiecesThanAvailable(){
		SoftAssert sAssert = new SoftAssert();
		cop.enterActualPiecesForlineLine(cop.getMorePieces());
		cop.enterContactNumber(cm.getNumericString(5));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Pieces enter more than available");
		sAssert.assertAll();
	}
	
	@Test(priority=34, dependsOnMethods="shouldRequireWarehouseWhenWarehouseCheckboxSelected")
	public void shouldNotAllowUserToEnterMoreLiteraturePiecesThanAvailable(){
		SoftAssert sAssert = new SoftAssert();
		cop.eneterLitPiecesForLine(cop.getMoreLitPieces());
		cop.enterContactNumber(cm.getNumericString(5));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, " Literature Pieces enter more than available");
		sAssert.assertAll();
	}
	
	@Test(priority=35)//, dependsOnMethods="shouldNotAllowBlankConsigneeAddress"
	public void shouldAllowUserToSaveCloseOutLine() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
		cop.enterContactNumber(cm.getNumericString(5));
		cop.enterContactEmail("automate@email.com");
		cop.selectWarehouse();
		System.out.println("line pieces "+cop.getHalfPieces());
		cop.enterActualPiecesForlineLine(cop.getHalfPieces());
		cop.enterDimWeightForLine(cm.getNumericString(2));
		cop.selectFirstOrderTypeForline();
		cop.enterActualWtForLine(cm.getNumericString(2));
		cop.eneterLitPiecesForLine(cop.getHalfLitPieces());
		cop.enterLiteratureWeightForLine(cm.getNumericString(2));
		cop.enterLiterDimWeightForLine(cm.getNumericString(2));
		cop.saveLine();
		sAssert.assertEquals(cop.getToastText(), "Order details save successfully", "Not Match!");	
		sAssert.assertAll();
	}
	
	@Test(priority = 36)
	public void shouldCalculateRatedWeightAndLitWeightCorrectlyOnLine() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.ratedWeightOnLine(), cop.getMaxWtFromLine(),"Rate weight should be max of actual or dim wt. on line");
		sAssert.assertEquals(cop.ratedLitweightOnLine(), cop.getMaxLitWtFromLine(),"Rated lit weight max of lit actual or litDim wt. on line");
		sAssert.assertAll();
	}
	
	@Test(priority = 37, dependsOnMethods="shouldAllowUserToSaveCloseOutLine")
	public void shouldValidateOriginEqualsCurrentLocation() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getOriginLocation(), cop.getCurrentLocation(),"Origin and current location not match");
		sAssert.assertAll();
	}
	
	@Test(priority = 38, dependsOnMethods="shouldAllowUserToSaveCloseOutLine")
	public void shouldDisplayCloseOutLineStatusAsDraft(){
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getLineStatus(), "Draft","Origin and current location not match");
		sAssert.assertAll();	
	}
	
	@Test(priority = 39, dependsOnMethods="shouldAllowUserToSaveCloseOutLine")
	public void shouldDisplayDeliveryStatusAsReadOnly() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isDeliveryFieldNonEditable(),"Delivery field Should Non editable");
		sAssert.assertAll();
	}
	
	@Test(priority = 40 ,alwaysRun=true)
	public void shouldConfirmCloseOutLineSuccessfully() {
		SoftAssert sAssert = new SoftAssert();
		cop.clickOnConfirmBtnOnLine();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully" ,"Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 41, dependsOnMethods="shouldConfirmCloseOutLineSuccessfully")
	public void shouldUpdateCloseOutLineStatusAfterConfirm() {
		SoftAssert sAssert = new SoftAssert();
		cop.pickupLocation=cop.getWarehouse();
		cop.iOriginOrderNo=cop.getOrderNumber();
		System.out.println("pickup:WH location "+ cop.pickupLocation);
		System.out.println("Origin order no "+cop.iOriginOrderNo);
		cop.iLBOL=cop.getLBOLNumber();
		sAssert.assertEquals(cop.getLineStatus(), "Inbound Pallet Pending" ,"Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 42, dependsOnMethods="shouldConfirmCloseOutLineSuccessfully")
	public void shouldEnsurePODCheckBoxIsNotSelected() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertFalse(cop.isPODChecked(), "POD Checkbox should be unselected on close out line");
		sAssert.assertAll();
	}

	@Test(priority = 43)
	public void shouldAllowUserToCreateOrderWithoutWarehouse() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cop.CloseLinePopup();
		cop.createCloseOutLineWithoutWHProcess();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully", "Not match!");
		sAssert.assertAll();

	}

	@Test(priority = 44, dependsOnMethods = "shouldAllowUserToCreateOrderWithoutWarehouse")
	public void shouldDisplayOrderStatusAsOutboundPalletPending() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getLineStatus(), "Outbound Pallet Pending", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 45)
	public void shouldAllowUserToDeleteLineItem() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		cop.CloseLinePopup();
		cop.createCloseOutLineWitWHProcess();
		cop.CloseLinePopup();
		cop.deleteFirstLine();
		sAssert.assertEquals(cp.captureToastMessage(), "Order deleted successfully", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 46)
	public void shouldNotAllowDeletingLineItemWhenOrderIsOutboundPending() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.deleteObtPendingOrder();
		sAssert.assertEquals(cp.captureToastMessage(), "Order not in draft or inbound pallet pending stage",
				"Not Match!");
		sAssert.assertAll();
	}

	/**
	 * Consignee Termination
	 * 
	 * @throws TimeoutException
	 *
	 */

	@Test(priority = 47)
	public void shouldRequireAuditorDetailsForConsigneeTermination() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		String ordernumber = "ORDER-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
		cop.closeOutMenu();
		cop.AddNewOrderBtn();
		cop.selectAnyValidCustomerForCloseOutOrFail();
		cop.selectAnyConsigneeOrSkip();
		cop.selectFirstOrderType();
		cop.enterCustOrdNum(ordernumber);
		cop.clickOnCloseOutType();
		cop.selectConsigneeTer();
		cp.save();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Auditor Name is required.", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(2), "Mobile number is required.", "Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 48)
	public void shouldRequirePickupDetailsForConsigneeTermination() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cop.addAuditorDetails(cm.getAlphaNumericString(10), cm.getNumericString(10));
		cop.clickOnConfirmBtn();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Scheduled Date and Time is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(2), "Rep name is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(3), "Pickup Date and Time is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(4), "Pickup signed by is required", "Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 49)
	public void shouldAllowUserToCreateOrderForConsigneeTermination() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.addPickUpDetails();
		cop.addMetricsForOriginOrder();
		cop.clickToTenderOrder();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 50)
	public void shouldUpdateOrderStatusAfterConfirmingConsigneeTermination() {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("order Sta" + cop.getOriginOrderStatus());
		sAssert.assertEquals(cop.getOriginOrderStatus(), "Closed", "Order status should be Closed.");
		sAssert.assertAll();
	}

	@Test(priority = 51, dependsOnMethods = "shouldUpdateOrderStatusAfterConfirmingConsigneeTermination")
	public void shouldValidatePiecesAndLitPiecesOnCloseOutForConsTermination() {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		cop.clickOnCloseOutLine();
		sAssert.assertEquals(cop.getPieces(), cop.getLinePieces(), "No of Pieces not Same On CloseOut line.");
		sAssert.assertEquals(cop.getLitPieces(), cop.getLineLitPieces(),
				"No of literature Pieces not Same On CloseOut line.");
		sAssert.assertAll();
	}

	@Test(priority = 52, dependsOnMethods = "shouldValidatePiecesAndLitPiecesOnCloseOutForConsTermination")
	public void shouldValidateMandatoryFieldsOnCloseOutLineForConsTermination() {
		SoftAssert sAssert = new SoftAssert();
		cop.saveLine();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Consign To is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(2), "Consignee is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(3), "Consignee Address is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(4), "Order Type is required.", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 53)
	public void shouldAllowUserToCreateOriginOrderForConsTermType() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.selectConsigneeTo("Origin");
		cop.selectConsigneeType("Customer");
		cop.enterContactNumber(cm.getNumericString(10));
		cop.enterContactEmail("automate@email.com");
		System.out.println("line pieces " + cop.getHalfPieces());
		cop.enterActualPiecesForlineLine(cop.getHalfPieces());
		cop.enterDimWeightForLine(cm.getNumericString(2));
		cop.selectFirstOrderTypeForline();
		cop.enterActualWtForLine(cm.getNumericString(2));
		cop.eneterLitPiecesForLine(cop.getHalfLitPieces());
		cop.enterLiteratureWeightForLine(cm.getNumericString(2));
		cop.enterLiterDimWeightForLine(cm.getNumericString(2));
		cop.clickOnConfirmBtnOnLine();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 54)
	public void shouldDisplayOutboundPalletPendingStatusForConsTermOrder() {
		SoftAssert sAssert = new SoftAssert();
		cop.OriginOrderNoObpl = cop.getOrderNumber();
		cop.pickupLocationObpl = cop.getSplitCurrentLocation();	
		cop.OLBOL = cop.getLBOLNumber();
		System.out.println("Origin order no Direct Return " + cop.OriginOrderNoObpl);
		System.out.println("LBOL number for Origin " + cop.OLBOL);
		System.out.println("Pickup location " + cop.pickupLocationObpl);
		sAssert.assertEquals(cop.getLineStatus(), "Outbound Pallet Pending", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 55)
	public void shouldPreventSelectingWarehouseProcessed() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.CloseLinePopup();
		cp.waitForLoaderToDisappear();
		cop.OLBOL = cop.getLBOLNumber();
		System.out.println("Obpl LBOL " + cop.OLBOL);
		cop.clickOnCloseOutLine();
		cop.selectConsigneeTo("New Consignee");
		sAssert.assertTrue(cop.isWarehouseNotEditabel(), "Warehouse process check box Should Non selectable.");
	}

	@Test(priority = 56)
	public void shouldNotDisplayOriginConsigneeInDropdown() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("consignee is " + cop.getConsignee());
		sAssert.assertTrue(cop.isConsigneeNotDisplayed(cop.getConsignee()),
				"Origin order consignee should not Present.");
		sAssert.assertAll();
	}

	@Test(priority = 57)
	public void shouldNotAllowBlankConsigneeAddressOnConsigneeSelection() {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("close drop");
		cop.clickOnClearBtn();
		cop.selectConsigneeTo("New Consignee");
		cop.enterContactNumber(cm.getNumericString(1));
		cop.selectCosigneeForLine();
		sAssert.assertTrue(cop.isConsigneeAddressEditableAndNotEmpty(), "Consignee Pickup should not empty");
		sAssert.assertAll();
	}

	@Test(priority = 58)
	public void shouldNotAllowEnteringMorePiecesThanAvailableForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		cop.selectSameLDAForLine(cop.getSameLDA());
		cop.enterActualPiecesForlineLine(cop.getMorePieces());
		cop.enterContactNumber(cm.getNumericString(5));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Pieces enter more than available");
		sAssert.assertAll();
	}

	@Test(priority = 59)
	public void shouldNotAllowEnteringMoreLiteraturePiecesThanAvailableForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		cop.eneterLitPiecesForLine(cop.getMoreLitPieces());
		cop.enterContactNumber(cm.getNumericString(5));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, " Literature Pieces enter more than available");
		sAssert.assertAll();
	}

	@Test(priority = 60)
	public void shouldAllowUserToSaveCloseOutLineForSameLDA() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.enterContactNumber(cm.getNumericString(5));
		cop.enterContactEmail("automate@email.com");
		cop.enterActualPiecesForlineLine(cop.getHalfPieces());
		cop.enterDimWeightForLine(cm.getNumericString(2));
		cop.selectFirstOrderTypeForline();
		cop.enterActualWtForLine(cm.getNumericString(2));
		cop.eneterLitPiecesForLine(cop.getHalfLitPieces());
		cop.enterLiteratureWeightForLine(cm.getNumericString(2));
		cop.enterLiterDimWeightForLine(cm.getNumericString(2));
		cop.saveLine();
		sAssert.assertEquals(cop.getToastText(), "Order details save successfully", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 61, dependsOnMethods = "shouldAllowUserToSaveCloseOutLineForSameLDA")
	public void shouldDisplayCloseOutLineStatusAsDraftForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getLineStatus(), "Draft", "Order status not match.");
		sAssert.assertAll();
	}

	@Test(priority = 62, dependsOnMethods = "shouldDisplayCloseOutLineStatusAsDraftForNewConsignee")
	public void shouldDisplayDeliveryStatusAsReadOnlyForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isDeliveryFieldNonEditable(), "Delivery field Should Non editable");
		sAssert.assertAll();
	}

	@Test(priority = 63, alwaysRun = true)
	public void shouldConfirmCloseOutLineSuccessfullyForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		cop.SameLDAOrderNo = cop.getOrderNumber();
		System.out.println("SameLDAOrderNo order no " + cop.SameLDAOrderNo);
		cop.clickOnConfirmBtnOnLine();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 64, dependsOnMethods = "shouldConfirmCloseOutLineSuccessfullyForNewConsignee")
	public void shouldUpdateCloseOutLineStatusAfterConfirmForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getLineStatus(), "Outbound QA Pending", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 65, dependsOnMethods = "shouldUpdateCloseOutLineStatusAfterConfirmForNewConsignee")
	public void shouldEnsurePODCheckBoxIsNotSelectedForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertFalse(cop.isPODChecked(), "POD Checkbox should be unselected on close out line");
		sAssert.assertAll();
	}

	@Test(priority = 66)
	public void shouldNotAllowDeletingLineItemWhenStatusOutboundQAPending() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.CloseLinePopup();
		cop.deleteObtQAPending();
		sAssert.assertEquals(cp.captureToastMessage(), "Order not in draft or inbound pallet pending stage",
				"Not Match!");
		sAssert.assertAll();
	} 
	
	/**
	 * Transfer of sample
	 *
	 */
	@Test(priority = 67)
	public void shouldAllowUserToCreateOrderForTransferOfSample() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		String ordernumber = "ORDER-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
		cop.closeOutMenu();
		cop.AddNewOrderBtn();
		cop.selectAnyValidCustomerForCloseOutOrFail();
		cop.selectAnyConsigneeOrSkip();
		cop.selectFirstOrderType();
		cop.enterCustOrdNum(ordernumber);
		cop.clickOnCloseOutType();
		cop.selectTrasferSample();
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(), "Order details save successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 68)
	public void shouldAllowUserToPrintTemplateForSingleOrder() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		cop.selectFirstOrder();
		cop.deleteExistingFiles();
		cop.clickOnPrintTem();
		sAssert.assertTrue(cop.isFileDownloaded(), "Close Out template not download.");
		sAssert.assertAll();
	}

	@Test(priority = 69, dependsOnMethods = "shouldAllowUserToCreateOrderForTransferOfSample")
	public void shouldDisplayPopupForReqMessageForWeightAndPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.selectFirstOrder();
		cop.confirmBtnOnListing();
		sAssert.assertTrue(cop.isErrorMessagePresent("Actual weight or literature actual weight required."),
				"Error msg not displyed for Weight.");
		sAssert.assertTrue(cop.isErrorMessagePresent("Number of pieces or literature number of pieces required."),
				"Error msg not displyed for Pieces.");
		sAssert.assertAll();
	}

	@Test(priority = 70)
	public void shouldDisplayPopupForReqPickUpDetails() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isErrorMessagePresent("Pickup Details are required."),
				"Error msg not displyed for PickUp details.");
		sAssert.assertAll();
	}

	
	@Test(priority = 71)
	public void shouldShowActualWtMandIfNoOfPiecesAddedAndLitWtAddedThenLitPieceMan() throws InterruptedException  {
		SoftAssert sAssert = new SoftAssert();
		cop.clickOkBtn();
		cop.editFirstorder();
		cop.addPickUpDetails();
		cop.addNoOfPiecesAndLitWtOriginOrder();
		cp.save();
		sAssert.assertEquals(cop.getMandatoryTextActWt(), "Actual Weight should be greater than 0", "Not match!");
		sAssert.assertEquals(cop.getMandatoryTextActLitPiece(), "Literature No Of Pieces should be greater than 0", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 72)
	public void shouldDefaultNoOfPalletisOne() throws InterruptedException  {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getNoOfPallet(), "1", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 73)
	public void shouldAllowUserToConfirmOrderFromListing() throws InterruptedException, TimeoutException  {
		SoftAssert sAssert = new SoftAssert();
		cop.addMetricsForOriginOrder();
		cp.save();
		cop.closeSubMenu();  //cp.clickBackBtn(); pop up stuck because used menu
		cop.selectFirstOrder();
		cop.confirmBtnOnListing();
		sAssert.assertEquals(cop.getToastText(), "Selected order confirm successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 74, dependsOnMethods = "shouldAllowUserToConfirmOrderFromListing")
	public void shouldDisplayOrderStatusAsClosedAfterConfirmation() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getOriginOrderStatusOnListing(), "Closed", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 75)
	public void shouldGenerateDocumentsForOrder() {
		SoftAssert sAssert = new SoftAssert();
		cop.editFirstorder();
		sAssert.assertTrue(cop.checkDocNoPresent(), "Documents are Not present when Confirm Order.");
		sAssert.assertAll();
	}

	@Test(priority = 76)
	public void shouldSetConsigneeToNewConsigneeForTransferSample() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.clickOnCloseOutLine();
		sAssert.assertEquals(cop.getConsingeeTo(), "New Consignee", "Consignee to should be selected as New Consignee");
		sAssert.assertAll();
	}

	@Test(priority = 77)
	public void shouldValidateMandFieldsOnCloseOutForNewConsignee() {
		SoftAssert sAssert = new SoftAssert();
		cop.saveLine();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Consignee is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(2), "Consignee Address is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(3), "Consignee LDA is required", "Not Match!");
		sAssert.assertEquals(cop.getManTextOnPickupDetails(4), "Order Type is required.", "Not Match!");
		sAssert.assertAll();
	}

	@Test(priority = 78)
	public void shouldDisplayWarehouseProcessedAsNonSelectableForNewConsignee() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isWarehouseNotEditabel(), "Warehouse process check box Should Non selectable.");
	}

	@Test(priority = 79)
	public void shouldNotDisplayOriginConsInDropdownForNewConsTransferSample() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		System.out.println("consignee is " + cop.getConsignee());
		sAssert.assertTrue(cop.isConsigneeNotDisplayed(cop.getConsignee()),
				"Origin order consignee should not Present On New Con Ord.");
		sAssert.assertAll();
	}

	@Test(priority = 80)
	public void shouldRequireConsigneeAddressWhenConsigneeSelected() {
		SoftAssert sAssert = new SoftAssert();
		cop.clearConsigneeData();
		cop.enterContactNumber(cm.getNumericString(1));
		cop.selectCosigneeForLine();
		sAssert.assertTrue(cop.isConsigneeAddressEditableAndNotEmpty(),
				"Consignee Pickup should not empty for new consigne order");
		sAssert.assertAll();
	}

	@Test(priority = 81)
	public void shouldNotAllowEnteringMorePiecesThanAvailableForNewConTransferSample() {
		SoftAssert sAssert = new SoftAssert();
		cop.selectSameLDAForLine(cop.getSameLDA());
		cop.enterActualPiecesForlineLine(cop.getMorePieces());
		cop.enterContactNumber(cm.getNumericString(5));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Pieces enter more than available");
		sAssert.assertAll();
	}

	@Test(priority = 82)
	public void shouldNotAllowEnteringMoreLiterPiecesThanAvailableForNewConTransferSample() {
		SoftAssert sAssert = new SoftAssert();
		cop.eneterLitPiecesForLine(cop.getMoreLitPieces());
		cop.enterContactNumber(cm.getNumericString(5));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, " Literature Pieces enter more than available");
		sAssert.assertAll();
	}

	@Test(priority = 83)
	public void shouldClearCloseOutLineFieldsWhenClearBtnClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.selectFirstOrderTypeForline();
		cop.saveLine();
		cop.clickOnClearBtn();
		cop.saveLine();
		sAssert.assertEquals(cop.getManTextOnPickupDetails(1), "Consignee is required",
				"Clear btn not working. consigee still selected");
		sAssert.assertAll();
	}
	
	@Test(priority = 84)
	public void shouldAllowUserToSaveCloseOutLineForDifferentLDAForTransferSample() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.selectCosigneeForLine();
		cop.enterContactNumber(cm.getNumericString(10));
		cop.enterContactEmail("automate@email.com");
		System.out.println("Orgin LDA " +cop.getOriginLDACode());
		cop.selectDifferentLDA(cop.getOriginLDACode());
		cop.enterActualPiecesForlineLine(cop.getHalfPieces());
		cop.enterDimWeightForLine(cm.getNumericString(2));
		cop.selectFirstOrderTypeForline();
		cop.enterActualWtForLine(cm.getNumericString(2));
		cop.eneterLitPiecesForLine(cop.getHalfLitPieces());
		cop.enterLiteratureWeightForLine(cm.getNumericString(2));
		cop.enterLiterDimWeightForLine(cm.getNumericString(2));
		cop.saveLine();
		sAssert.assertEquals(cop.getToastText(), "Order details save successfully", "Not Match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 85, dependsOnMethods = "shouldAllowUserToSaveCloseOutLineForDifferentLDAForTransferSample")
	public void shouldSetLineStatusToDraftForNewConsInTransferSample() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getLineStatus(), "Draft", "Order Status not match.");
		sAssert.assertAll();
	}
	
	@Test(priority = 86, dependsOnMethods = "shouldSetLineStatusToDraftForNewConsInTransferSample")
	public void shouldPreventEditingDeliveryStatusFieldForNewConsTransferSample() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isDeliveryFieldNonEditable(), "Delivery field Should Non editable.");
		sAssert.assertAll();
	}

	@Test(priority = 87, alwaysRun = true)
	public void shouldConfirmCloseOutLineSuccessfullyForNewConsTransferSample() {
		SoftAssert sAssert = new SoftAssert();
		cop.DiffLDAOrderNo = cop.getOrderNumber();
		cop.LDAPickuplocation=cop.getSplitCurrentLocation();
		System.out.println("DiffLDAOrderNo LDA order no " + cop.DiffLDAOrderNo);
		System.out.println("pickup location diffe lda " + cop.LDAPickuplocation);
		cop.clickOnConfirmBtnOnLine();
		sAssert.assertEquals(cop.getToastText(), "Order confirm successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 88, dependsOnMethods="shouldConfirmCloseOutLineSuccessfullyForNewConsTransferSample")
	public void shouldUpdateCloseOutLineStatusAfterConfirmForDifferentLDA() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(cop.getLineStatus(), "Outbound Pallet Pending", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 89, dependsOnMethods = "shouldUpdateCloseOutLineStatusAfterConfirmForDifferentLDA")
	public void shouldEnsurePODCheckBoxIsNotSelectedForNewConsTransferSample() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertFalse(cop.isPODChecked(), "POD Checkbox should be unselected on close out line");
		sAssert.assertAll();
	}
	
	@Test(priority = 90 )
	public void shouldDisableSaveAndConfirmBtnAfterOrderConfirmation() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertTrue(cop.isSaveAndConfirmBtnDisableOnLine(), "Save and Confirm Btn should disable");
		sAssert.assertAll();
	}
	
	@Test(priority = 91)
	public void shouldMatchOrderDetailsOnListingPage() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cop.CloseLinePopup();
		cop.getOrderData();
		cp.clickBackBtn();
		sAssert.assertEquals(cop.getOrderNo(), cop.MGLOrderno, "Orirgin order not match on Listing");
		sAssert.assertEquals(cop.getCustomerOrderNo(), cop.MCusorderNo, "Customer Order no mismatch on list");
		sAssert.assertEquals(cop.getPickupCon(), cop.PickupConsignee, "pick up consignee mismatch");
		sAssert.assertEquals(cop.getCarrierCode(), cop.MLDACode, "carrier code mismatch");
		sAssert.assertAll();
	}
	
	@Test(priority = 92, dependsOnMethods="shouldMatchOrderDetailsOnListingPage")
	public void shouldValidateTotalPiecesAndWeightOnListingPage() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		totalActpieces = Integer.parseInt(cop.Noofpieces) + Integer.parseInt(cop.Litnoofpieces);
		totalActwait = Integer.parseInt(cop.Actualwt) + Integer.parseInt(cop.Litactualwt);
		sAssert.assertEquals(cop.getActualpieces(), String.valueOf(totalActpieces), "Actual pieces mismatch on list");
		sAssert.assertEquals(cop.getActulwt(), String.valueOf(totalActwait), "Actual weight mismatch on list");
		sAssert.assertAll();

	}
	
	@Test(priority = 93)
	public void shouldAllowUserToDownloadTemplateForMultipleOrders() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.deleteExistingFiles();
		cop.selectMultiOrderOnListing();
		cop.clickOnPrintTem();
		sAssert.assertTrue(cop.isFileDownloaded(), "Close Out template not download for multiple order.");
		sAssert.assertAll();	
	}
	
	@Test(priority=94)
	public void shouldNotAllowDeletingOrderOnceClosed() {
		SoftAssert sAssert = new SoftAssert();
		cop.deleteCloseOrder();
		sAssert.assertEquals(cp.captureToastMessage(), "Order is not in draft stage", "Not match!");
		sAssert.assertAll();
	}
	
	String customer;
	@Test(priority=95)
	public void shouldVerifyCustomerOrderNumberForCloseOutLine() {
		SoftAssert sAssert = new SoftAssert();
		customer=cop.getCustomer();
		cop.clickOnExpandBtn();
		sAssert.assertEquals(cop.getLineCustOrderNo(), cop.MCusorderNo + "-D1", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority=96)
	public void shouldShowErrorForDuplicateCustomerOrderNumber() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cop.orderCreationforDuplicateCustomer(cop.MCusorderNo ,customer);
		cop.clickOnCloseOutType();
		cop.selectTrasferSample();
		cp.save();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer order number already exist.", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 97, enabled = false)
	public void shouldCheckfilterForCloseoutOrders() throws InterruptedException {
		cp.waitForPopupToDisappear();
		cp.clickBackBtn();;
		cp.searchSection();
		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
		cp.selectMultipleCustomerByIndex(indicesToSelect1);
		cp.validateDataInGrid(4);	
		List<Integer> indicesToSelect = Arrays.asList(13, 2, 8);
		cop.selectMultipleConsigneesByIndex(indicesToSelect);
		cp.validateDataInGrid(6);		
		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 3);
		cop.selectMultipleLDAByIndex(indicesToSelect3);
		cp.validateDataInGrid(9);	
		cp.clickClearButton();
		List<Integer> indicesToSelect5 = Arrays.asList(3, 19, 8);
		cp.selectMultipleUsersByIndex(indicesToSelect5);
		cp.validateDataInGrid(12);	
		List<Integer> indicesToSelect8 = Arrays.asList(1, 2, 3);
		cp.selectMultiplestatusByIndex(indicesToSelect8);
		cp.validateDataInGrid(11);	
		cp.clickClearButton();
		cop.searchAndValidateOrderNumber();
		cop.searchAndValidateCustomerOrderNumber();
		cp.clickClearButton();
		cop.searchAndValidatePickupLDACode();
		cp.clickClearButton();
		cp.searchAndValidateDateByColumn(13);
		cp.searchAndValidateToDateByColumn(13);
		cp.clickClearButton();
		
	}
	
	String orderData;
	@Test(priority=98)
	public void shouldDoInboundProcessForCloseoutOrder() throws TimeoutException, InterruptedException {
		System.out.println("order no "+cop.iOriginOrderNo);
		np.inboundPalletProcess(cop.iOriginOrderNo);
	
		orderData= np.getIBPL();
		System.out.println("ibpl no "+orderData);
		np.inboundTruckProcess(orderData);
		orderData= np.getTruckNo();
		System.out.println("truck no "+orderData);
		np.inboundQAProcess(orderData);
	//	np.createOutboundPallet("GL-9683-D1", "Bensenville Facility",  "Warehouse", "Origin", 0);
	}
	
	@Test(priority=99)
	public void shouldDoOutProcessForCloseoutOrder() throws TimeoutException, InterruptedException {
	System.out.println("iOriginOrderNo "+cop.iOriginOrderNo );
	System.out.println("cop.pickupLocation "+cop.pickupLocation );
	System.out.println("cop.OriginOrderNoObpl "+cop.OriginOrderNoObpl );
	System.out.println("cop.pickupLocationObpl "+cop.pickupLocationObpl );
	System.out.println("cop.DiffLDAOrderNo "+cop.DiffLDAOrderNo );
	System.out.println("cop.LDAPickuplocation "+cop.LDAPickuplocation );
	
	np.createOutboundPallet(cop.iOriginOrderNo, cop.pickupLocation,  "WAREHOUSE", "Origin", 0);
	np.createOutboundPallet(cop.OriginOrderNoObpl, cop.pickupLocationObpl,  "LDA", "Origin", 1);
	np.createOutboundPallet(cop.DiffLDAOrderNo, cop.LDAPickuplocation,  "LDA", "LDA", 2);
	
//	np.obtTruckProcess("Bensenville Facility", "OBPL-3084", "Warehouse", "Origin");
	System.out.println("cop.pickupLocation "+cop.pickupLocation );
	System.out.println(" np.dataArray[0]"+ np.dataArray[0]);
	System.out.println("cop.pickupLocationObpl "+cop.pickupLocationObpl );
	System.out.println("np.dataArray[1]"+np.dataArray[1] );
	System.out.println("cop.LDAPickuplocation"+cop.LDAPickuplocation );
	System.out.println("np.dataArray[2]"+np.dataArray[2] );
	
	np.obtTruckProcess(cop.pickupLocation,     np.dataArray[0], "WAREHOUSE", "Origin");
	//np.obtTruckProcess("Bensenville Facility","OBPL-3100", "Warehouse", "Origin");
	np.obtTruckProcess(cop.pickupLocationObpl, np.dataArray[1], "LDA", "Origin");
	//np.obtTruckProcess("JET TRANSPORTATION: 1085 Cranbury South River Rd (Unit 6)", "OBPL-3101", "LDA", "Origin");
    np.obtTruckProcess(cop.LDAPickuplocation, np.dataArray[2], "LDA", "LDA");
	//np.obtTruckProcess("JET TRANSPORTATION: 1085 Cranbury South River Rd (Unit 6)", "OBPL-3102", "LDA", "LDA");

	np.docGenerationProcess(cop.DiffLDAOrderNo, 0);
	np.docGenerationProcess(cop.SameLDAOrderNo, 1);
	
	np.trkingDetailUpdateProcess();
	
	np.outboundQAProcess(np.ldaaArray[0], np.manifestArray[0]);
	np.outboundQAProcess(np.ldaaArray[1], np.manifestArray[1]);

	np.consigneeQAProcess(np.lbolArray[0]);
	np.consigneeQAProcess(np.lbolArray[1]);
	
	np.returnPODProcess(cop.iOriginOrderNo);
	np.returnPODProcess(cop.OriginOrderNoObpl);

	
	}
	
	
	
}
