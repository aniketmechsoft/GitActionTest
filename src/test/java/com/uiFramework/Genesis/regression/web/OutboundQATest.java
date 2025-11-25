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
	
//	@Test(priority = 1,alwaysRun = true, groups = {"Smoke"})
//	public void shouldAcceptManifestForMainOrder() throws InterruptedException, TimeoutException{
//		SoftAssert sAssert = new SoftAssert();
//		oqa.outboundQAMenu();
//		System.out.println("lda is"+ op.MLDA);
//        oqa.selectLDA(op.MLDA);
//        oqa.selectLBOL(dq.ManifestNo);
//		cp.Search();
//		oqa.HandlePopup();
//		oqa.markSameQty();
//		oqa.clickOnUpdateRecPiece();
//		sAssert.assertEquals(cp.captureToastMessage(), "QA details update successfully", "Toast not Displayed For outbound QA done.");
//		sAssert.assertAll();
//	}
	
	@Test(priority = 2, alwaysRun = true)
	public void shouldDisplayEditableFieldsForTrackingUpdatedRequest() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.outboundQAMenu();
		cp.clickClearButton();
		oqa.selectLDA();
		Thread.sleep(2000);
		oqa.selectManifest();
		LDA =oqa.getLDA();
		manifestNo = oqa.getManifestNo();
		System.out.println("LDA is"+ LDA);
		System.out.println("manifestNo is"+ manifestNo);
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Please select one record.");
//		sAssert.assertAll();
	}
	
	@Test(priority = 3)
	public void shouldNotAllowEnteringExtraReceivedOrderPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.RecOrdExtraPiecesEnter(1);
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Received order quantity can not be greater than actual order quantity while do Outbound QA.");
		sAssert.assertAll();

	}
	
	@Test(priority = 4)
	public void shouldPopulateDefaultValueAsZero() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		sAssert.assertEquals(oqa.checkThatLitQtyDefaultZero(), 0, "Lit pieces not popuplate zero.");
	}
	
	@Test(priority = 5)
	public void shouldNotAllowEnteringExtraLiteratureOrderPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		oqa.RecOrdExtraLitrEntr(1);
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Received Literature quantity can not be greater than actual order quantity while do Outbound QA.");
		sAssert.assertAll();

	}
	
	@Test(priority = 6, alwaysRun = true)
	public void shouldValidateReceivedOrderPiecesWhenMarkSameQtyButtonClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.markSameQty();
		initialCount = oqa.GetEnteredOrdVal();
		afterCount = oqa.GetOrdVal();
		sAssert.assertEquals(initialCount, afterCount, "Mark as Received button not showing correct count(order pieces) on Outbound QA page");
		sAssert.assertAll();
	}
	
	@Test(priority = 7)
	public void shouldValidateLitOrderPiecesWhenMarkSameQtyButtonClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		initialCount = oqa.GetLitRecVal();
		afterCount = oqa.GetLitRecEnteredVal();
		sAssert.assertEquals(initialCount, afterCount, "Mark as Received button not showing correct count(lit pieces) on Outbound QA page");
		sAssert.assertAll();
	}
	
	@Test(priority = 8)
	public void shouldDisplaySelectedLDAInGrid() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(oqa.getLDAfromGrid(), oqa.getLDAfromDropdown(), "LDA not match i mnifest Details Section");
		sAssert.assertAll();
		
	}
	@Test(priority = 9)
	public void shouldDisplaySelectedManifestInGrid() {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(oqa.getManifestfromGrid(), oqa.getManifestNofromDropdown(), "Manifest Number not match in Manifest Details");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 10)
	public void shouldDisplayCorrectTotalPiecesCount() {
		SoftAssert sAssert = new SoftAssert();	
		sAssert.assertEquals(oqa.getTotalPiecesfromGrid(), oqa.getSumofOrderPiecesNo(), "Total Pieces count not match with sum of the Order Pieces.");
		sAssert.assertAll();
		
	}
	@Test(priority = 11)
	public void shouldMakeReceivedDateMandatory() {
		SoftAssert sAssert = new SoftAssert();
		CurrentRecDate= oqa.getndClearRecDate();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(),"Received Date is required.","Toast message not Displyed for when Received date blank.");
		sAssert.assertAll();
	}
	
	@Test(priority = 12)
	public void shouldMakeVoiceMailDateMandatory() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.ReceivedDateSelection();
		CurrentRecDate= oqa.getndClearVoiceMailDate();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(),"Voice Mail Date is required.", "Toast message not Displyed for when voice mail date blank.");
		sAssert.assertAll();	
	}
	
	@Test(priority = 13)
	public void shouldVerifyReceivedAndVoicemailDatesAreCurrentDate() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		oqa.voiceMailDateSelection();
		sAssert.assertEquals(CurrentRecDate ,oqa.getReceivedDate(), "Received date Should not displayed as current date.");
		sAssert.assertEquals(CurrentRecDate ,oqa.getVoicemailDate(), "Voice Mail date Should not displayed as current date.");
		sAssert.assertAll();
	}
	
	@Test(priority = 14, alwaysRun = true)
	public void shouldAllowUserToCompleteOutboundQA() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.Search();
		oqa.markSameQty();
		oqa.clickOnUpdateRecPiece();
		sAssert.assertEquals(cp.captureToastMessage(), "QA details update successfully", "Toast not Displayed For outbound QA done.");
		sAssert.assertAll();
	}
	
	@Test(priority = 15,dependsOnMethods="shouldAllowUserToCompleteOutboundQA")
	public void shouldAllowUserToOpenCompletedQA() throws InterruptedException{
		SoftAssert sAssert = new SoftAssert();
        oqa.selStatus();
        oqa.selectLDA(LDA);
        oqa.selectLBOL(manifestNo);
		cp.Search();
		sAssert.assertFalse(oqa.verifyUpdatePiecsBtnDisable().isEnabled(), "Update btn should bedisable in complete status.");
		sAssert.assertAll();
	}
	
//	@AfterClass()
//  @Test(priority = 16)
//	public void shouldUpdateOrderStatusAfterOutboundQADone() throws InterruptedException {
//		oqa.orderpageMenulanding();
//		String status = oqa.getOrderStatus(op.MGLOrderno);
//		boolean isValid = status.equals("Consignee Delivery Pending") || status.equals("Consignee Delivery Schedule Pending");
//		Assert.assertTrue(isValid, "Consignee QA pending status not matched. Found: " + status);
//	}

}
