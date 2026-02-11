package com.uiFramework.Genesis.regression.web;

import java.util.concurrent.TimeoutException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.ConsigneeQA;
import com.uiFramework.Genesis.web.pages.DocQueue;
import com.uiFramework.Genesis.web.pages.OrderPage;
import com.uiFramework.Genesis.web.pages.OutboundQApage;

public class OutboundQATest extends TestBase {
	ConsigneeQA cqa = null;
	OutboundQApage oqa = null;
	CommonPage cp = null;
	OrderPage op=null;
	DocQueue dq=null;
	CommonMethods cm;
	int initialCount;
	int afterCount;
	String manifestNo;
	String LDA;
	String CurrentRecDate;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
	//	test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		cqa = new ConsigneeQA(driver);
		oqa = new OutboundQApage(driver);
		op = new OrderPage(driver);
		dq= new DocQueue(driver);
		//driver.navigate().refresh();

	}
	
	@Test(priority = 1,alwaysRun = true, groups = {"smoke"})
	public void shouldAcceptManifestForMainOrder() throws InterruptedException, TimeoutException{
		SoftAssert sAssert = new SoftAssert();
		oqa.outboundQAMenu();
		System.out.println("lda is"+ op.MLDA);
		System.out.println("Manifest no is"+ dq.ManifestNo);
        oqa.selectLDA(op.MLDA);
        oqa.selectLBOL(dq.ManifestNo);
		cp.Search();
		oqa.HandlePopup();
		oqa.markSameQty();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(), "QA details update successfully", "Toast not Displayed For outbound QA done.");
		sAssert.assertAll();
	}
	
	@Test(priority = 2, alwaysRun = true)
	public void shouldDisplayEditableFieldsForTrackingUpdatedRequest() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.outboundQAMenu();
		cp.clickClearButton();
		oqa.unSelInprocessStatus();
		oqa.selectLDA();
		Thread.sleep(2000);
		oqa.selectManifest();
		LDA =oqa.getLDA();
		manifestNo = oqa.getManifestNo();
		System.out.println("LDA is"+ LDA);
		System.out.println("manifestNo is "+ manifestNo);
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Please select one record.");
//		sAssert.assertAll();
	}
	
	@Test(priority = 3)
	public void shouldCheckMandateMsgIfOrderNotSelected() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.selectAllcheckbox();
		oqa.clickOnUpdateRecPiece();
		//boolean flag = cp.toastMsgReceivedSuccessfully();
		System.out.println("msg "+ cp.captureToastMessage());
		sAssert.assertEquals(cp.captureToastMessage(), "Please select at least one record.", "Not Found!");
		sAssert.assertAll();

	}
	
	@Test(priority = 4, dependsOnMethods="shouldCheckMandateMsgIfOrderNotSelected")
	public void shouldNotAllowEnteringExtraReceivedOrderPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		oqa.RecOrdExtraPiecesEnter(1);
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Received order quantity can not be greater than actual order quantity while do Outbound QA.");
		sAssert.assertAll();
	}
	
	@Test(priority = 5,dependsOnMethods="shouldCheckMandateMsgIfOrderNotSelected")
	public void shouldPopulateDefaultValueAsZero() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		sAssert.assertEquals(oqa.checkThatLitQtyDefaultZero(), 0, "Lit pieces not popuplate zero.");
		sAssert.assertAll();
	}
	
	@Test(priority = 6,dependsOnMethods="shouldNotAllowEnteringExtraReceivedOrderPieces")
	public void shouldNotAllowEnteringExtraLiteratureOrderPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		oqa.RecOrdExtraLitrEntr(1);
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Received Literature quantity can not be greater than actual order quantity while do Outbound QA.");
		sAssert.assertAll();
	}
	
	@Test(priority = 7,alwaysRun = true,dependsOnMethods="shouldNotAllowEnteringExtraLiteratureOrderPieces")
	public void shouldValidateReceivedOrderPiecesWhenMarkSameQtyButtonClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.markSameQty();
		initialCount = oqa.GetEnteredOrdVal();
		afterCount = oqa.GetOrdVal();
		sAssert.assertEquals(initialCount, afterCount, "Mark as Received button not showing correct count(order pieces) on Outbound QA page");
		sAssert.assertAll();
	}
	
	@Test(priority = 8, dependsOnMethods="shouldValidateReceivedOrderPiecesWhenMarkSameQtyButtonClicked")
	public void shouldValidateLitOrderPiecesWhenMarkSameQtyButtonClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		initialCount = oqa.GetLitRecVal();
		afterCount = oqa.GetLitRecEnteredVal();
		sAssert.assertEquals(initialCount, afterCount, "Mark as Received button not showing correct count(lit pieces) on Outbound QA page");
		sAssert.assertAll();
	}
	
	@Test(priority = 9, dependsOnMethods="shouldValidateLitOrderPiecesWhenMarkSameQtyButtonClicked")
	public void shouldDisplaySelectedLDAInGrid() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(oqa.getLDAfromGrid(), oqa.getLDAfromDropdown(), "LDA not match i mnifest Details Section");
		sAssert.assertAll();
	}
	
	@Test(priority = 10, dependsOnMethods="shouldDisplaySelectedLDAInGrid")
	public void shouldDisplaySelectedManifestInGrid() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(oqa.getManifestfromGrid(), oqa.getManifestNofromDropdown(), "Manifest Number not match in Manifest Details");
		sAssert.assertAll();
	}
	
	@Test(priority = 11, dependsOnMethods="shouldDisplaySelectedManifestInGrid")
	public void shouldDisplayCorrectTotalPiecesCount() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(oqa.getTotalPiecesfromGrid(), oqa.getSumofOrderPiecesNo(), "Total Pieces count not match with sum of the Order Pieces.");
		sAssert.assertAll();	
	}
	
	@Test(priority = 12, dependsOnMethods="shouldDisplayCorrectTotalPiecesCount")
	public void shouldMakeReceivedDateMandatory() {
		SoftAssert sAssert = new SoftAssert();
		oqa.selectFirstcheckbox();
		CurrentRecDate= oqa.getndClearRecDate();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(),"Received Date is required.","Toast message not Displyed for when Received date blank.");
		sAssert.assertAll();
	}
	
	@Test(priority = 13, dependsOnMethods="shouldMakeReceivedDateMandatory")
	public void shouldMakeVoiceMailDateMandatory() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.ReceivedDateSelection();
		CurrentRecDate= oqa.getndClearVoiceMailDate();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(),"Voice Mail Date is required.", "Toast message not Displyed for when voice mail date blank.");
		sAssert.assertAll();	
	}
	
	@Test(priority = 14, dependsOnMethods="shouldMakeVoiceMailDateMandatory")
	public void shouldVerifyReceivedAndVoicemailDatesAreCurrentDate() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.voiceMailDateSelection();
		sAssert.assertEquals(CurrentRecDate ,oqa.getReceivedDate(), "Received date Should not displayed as current date.");
		sAssert.assertEquals(CurrentRecDate ,oqa.getVoicemailDate(), "Voice Mail date Should not displayed as current date.");
		sAssert.assertAll();
	}
	
	String order;
	int ordercount;
	@Test(priority = 15, alwaysRun = true)
	public void shouldAllowUserToPartialOutboundQA() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.Search();
		ordercount= oqa.selectAllcheckbox();
		order=oqa.selectFirstcheckbox();
		System.out.println("QA completed orer is "+ order);
		oqa.markSameQty();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(), "QA details update successfully", "Toast not Displayed For outbound QA done.");
		sAssert.assertAll();
	}
	
    @Test(priority = 16, dependsOnMethods="shouldAllowUserToPartialOutboundQA")
	public void shouldUpdateOrderStatusAfterPartialOutboundQADone() throws InterruptedException {
    	SoftAssert sAssert = new SoftAssert();
		oqa.orderpageMenulanding();
		String status = cp.getOrderStatus(order);
		boolean isValid = status.equals("Consignee Delivery Pending") || status.equals("Consignee Delivery Schedule Pending");
		sAssert.assertTrue(isValid, "Consignee QA pending status not matched. Found: " + status);
		sAssert.assertAll();
	}
    
	@Test(priority = 17, dependsOnMethods="shouldAllowUserToPartialOutboundQA")
	public void shouldCheckFisrtOrderShouldBeDisable() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		oqa.outboundQAMenu();
		oqa.checkMulOrderInManifest(ordercount);
		oqa.selectLDA(LDA);
        oqa.selectLBOL(manifestNo);
		cp.Search();
		sAssert.assertTrue(oqa.isReceivedPiecesDisabled(), "QA done Order is not Disable");
		sAssert.assertAll();
	}
	
//	@Test(priority = 18, alwaysRun = true)
//	public void shouldAllowUserToCompleteOutboundQA() throws InterruptedException, TimeoutException {
//		SoftAssert sAssert = new SoftAssert();
//		//oqa.outboundQAMenu();
//		cp.clickClearButton();
//		oqa.checkMulOrderInManifest(ordercount);
//		oqa.selectLDA(LDA);
//        oqa.selectLBOL(manifestNo);
//		cp.Search();
//		oqa.markSameQty();
//		oqa.clickOnUpdateRecPiece();
//		sAssert.assertEquals(cp.captureToastMessage(), "QA details update successfully", "Toast not Displayed For outbound QA done.");
//		sAssert.assertAll();
//	}
    
	@Test(priority = 19)
	public void shouldAllowUserToOpenCompletedQA() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
        oqa.selCompeleteStatus();
        oqa.selectLDA(LDA);
        oqa.selectLBOL(manifestNo);
		cp.Search();
		sAssert.assertTrue(oqa.isUpdatePiecesBtnNotVisible(), "Update btn Show after QA Done (complete status.)");
		sAssert.assertAll();
	}
	
	@Test(priority = 20)
	public void shouldUpdateOrderStatusAfterOutboundQADone() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		oqa.orderpageMenulanding();
		//String status = cp.getOrderStatus(op.MGLOrderno);
		//boolean isValid = status.equals("Consignee Delivery Pending") || status.equals("Consignee Delivery Schedule Pending");
		//sAssert.assertTrue(isValid, "Consignee QA pending status not matched. Found: " + status);
		sAssert.assertEquals(cp.getOrderStatus(op.MGLOrderno), "Consignee Delivery Schedule Pending", "Not match!");
		sAssert.assertAll();
	}
	
	@Test(priority = 21, dependsOnMethods="shouldUpdateOrderStatusAfterOutboundQADone")
	public void shouldUpdateOrderStatusAfterSchedDellivery() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.editOrder();
		cqa.scheduleDateSelection();
		oqa.saveDetails();
		sAssert.assertEquals(oqa.getOrderStatus(), "Consignee Delivery Pending", "Not match!");
		sAssert.assertAll();
	}
	
//	@AfterClass()
//	public void shouldUpdateOrderStatusAfterSchedDellivery() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		oqa.editOrder();
//		cqa.scheduleDelieverySelection();
//		oqa.saveDetails();
//		sAssert.assertEquals(oqa.getOrderStatus(), "Consignee Delivery Pending", "Not match!");
//		sAssert.assertAll();
//	}

}
