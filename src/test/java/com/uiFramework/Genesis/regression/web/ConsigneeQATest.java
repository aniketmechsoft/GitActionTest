package com.uiFramework.Genesis.regression.web;

import java.util.concurrent.TimeoutException;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.ConsigneeQA;
import com.uiFramework.Genesis.web.pages.DocQueue;
import com.uiFramework.Genesis.web.pages.OrderPage;
import com.uiFramework.Genesis.web.pages.OutboundQApage;

public class ConsigneeQATest extends TestBase {
	ConsigneeQA cqa = null;
	CommonPage cp = null;
	DocQueue dq=null;
	OutboundQApage oqa = null;
	OrderPage op=null;
	CommonMethods cm;
	int initialCount;
	int afterCount;
	String lbolNo;

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
		test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		cqa = new ConsigneeQA(driver);
		dq= new DocQueue(driver);
		oqa = new OutboundQApage(driver);
		op= new OrderPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1,alwaysRun = true, groups = {"smoke"})
	public void shouldDisplayEditableFieldsForTrackingUpdatedRequest() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cqa.consigneeQAMenu();
		cqa.LBOLSelOfTrkingUpdatedOrder();
		lbolNo = cqa.getLBOLNo();
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Plese select one record.");
//		sAssert.assertAll();
	}

	@Test(priority = 2, dependsOnMethods="shouldDisplayEditableFieldsForTrackingUpdatedRequest")
	public void shouldNotAllowEnteringExtraReceivedOrderPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cqa.RecOrdExtraPiecesEnter(1);
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Received order quantity can not be greater than actual order quantity.");
		sAssert.assertAll();
	}

	@Test(priority = 3, dependsOnMethods="shouldDisplayEditableFieldsForTrackingUpdatedRequest")
	public void shouldNotAllowEnteringExtraLiteratureOrderPieces() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		cqa.RecOrdExtraLitrEntr(1);
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Received Literature quantity can not be greater than actual order quantity.");
		sAssert.assertAll();
	}
	
	@Test(priority = 4,alwaysRun = true, groups = {"smoke"}, dependsOnMethods="shouldDisplayEditableFieldsForTrackingUpdatedRequest")
	public void shouldValidateReceivedOrderPiecesWhenMarkSameQtyBtnClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cqa.markSameQty();
		initialCount = cqa.GetEnteredOrdVal();
		afterCount = cqa.GetOrdVal();
		sAssert.assertEquals(initialCount, afterCount);
		sAssert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods="shouldValidateReceivedOrderPiecesWhenMarkSameQtyBtnClicked")
	public void shouldValidateLiteratureOrderPiecesWhenMarkSameQtyBtnClicked() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		initialCount = cqa.GetRecVal();
		afterCount = cqa.GetRecEnteredVal();
		sAssert.assertEquals(initialCount, afterCount);
		sAssert.assertAll();
	}
	
	@Test(priority = 6, dependsOnMethods="shouldValidateLiteratureOrderPiecesWhenMarkSameQtyBtnClicked")
	public void shouldAllowUserToSaveDetailsInDraft() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
        cqa.ClickSaveValBtn();
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "QA details update successfully.");
		sAssert.assertAll();
	}
	
	@Test(priority = 7)
	public void shouldAllowUserToOpenDraftRequest() throws InterruptedException {
		cqa.selectLBOL(lbolNo);
		cp.Search();
	}
	
	@Test(priority = 8, dependsOnMethods="shouldAllowUserToOpenDraftRequest")
	public void shouldAllowUserToUploadExcelFile() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cqa.upload_file();
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Plese select one record.");
//		sAssert.assertAll();

	}
	
	@Test(priority = 9)
	public void shouldNotAllowSavingFileWithoutTime() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cqa.MarkAsCompletedBtn();
		sAssert.assertEquals(cqa.getDelDateByErrMsg(), "Delivery Date & Time is required.");
		sAssert.assertEquals(cqa.getDelSignByErrMsg(), "Delivery Signed By is required");
		sAssert.assertAll();
	}
	
	@Test(priority = 10,alwaysRun = true, groups = {"smoke"})
	public void shouldAllowUserToCompleteRequest() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		cqa.scheduleDelieverySelection();
		cqa.scheduleDateSelection();
		cqa.enterSignBy("David Convey");
		Thread.sleep(1000);
		cqa.MarkAsCompletedBtn();
	 	boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "QA details update successfully.");
		sAssert.assertEquals(cp.captureToastMessage(), "QA details update successfully", "Toast not Displayed For consignee QA done.");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 11)
	public void shouldAllowUserToOpenCompletedRequest() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
        cqa.selStatus();
        cqa.selectLBOL(lbolNo);
		cp.Search();
		sAssert.assertTrue(cqa.verifyMarkAsRecBtnAndRecPiecesField(), "MarkAsRec btn & received Pieces field should be open");
		sAssert.assertAll();
	}
	
	@Test(priority = 12, dependsOnMethods="shouldAllowUserToOpenCompletedRequest")
	public void shouldAllowUserToUpdatePiecesAfterConsingeeQADone() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertFalse(oqa.isReceivedPiecesDisabled(), "QA done Order is Disable, it should be open");
		sAssert.assertAll();
	}

//	@Test(priority = 12)//, alwaysRun = true, dependsOnMethods = "ToCheckUserIsAbleToOpenCompletedReq"
//	public void shouldCompleteConsigneeQAForMainOrder() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//     //   cqa.selectLBOL(dq.MLBOL);
//        cqa.selectLBOL("3630");
//		cp.Search();
//		boolean flag = cqa.isDownloadDisabledAndMarkReceivedEnabled();
//		if (flag) {
//	        cqa.upload_file();
//	        cp.waitForPopupToDisappear();
//	        cqa.scheduleDelieverySelection();
//	        cqa.scheduleDateSelection();
//	        cqa.enterSignBy("David");
//	        Thread.sleep(1000);
//	        cqa.ClickSaveValBtn();
//
//	        boolean toastFlag = cp.toastMsgReceivedSuccessfully();
//	        sAssert.assertTrue(toastFlag, "FAIL: QA details not updated successfully.");
//	    } else {
//	        throw new SkipException("SKIPPED: Required condition not met — Download button should be disabled and Mark as Received button should be enabled.");
//	    }
//
//	    sAssert.assertAll();
//		
//		
//	}
	
	@AfterClass()
	public void checkOrderStatusAfterorderLoad() throws InterruptedException {
		cp.orderpageMenulanding();
		Assert.assertEquals(cp.getOrderStatus(op.MGLOrderno), "Closed", "Order status not match! as 'Closed'.");
	}
	
//	@AfterClass()
//	public void checkOrderStatusAfterorderLoad() {
//		driver.close();
//	}

}
