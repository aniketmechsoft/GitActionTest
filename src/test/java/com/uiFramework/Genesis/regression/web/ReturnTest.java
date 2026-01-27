package com.uiFramework.Genesis.regression.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.NavigationPages;
import com.uiFramework.Genesis.web.pages.ReconsignmentPage;
import com.uiFramework.Genesis.web.pages.ReturnPage;

public class ReturnTest extends TestBase {
	
	ReconsignmentPage rp = null;
	CommonMethods cm = null;
	CommonPage cp = null;
	WaitHelper wt = null;
	JavaScriptHelper js = null;
	String consignee;
	NavigationPages np;
	ReturnPage rep;

	private List<String> firstUniqueText;
	private List<String> validationOrdNo;
	private List<String> trkingValidationOrdNo;

	@BeforeClass(alwaysRun = true)
	public void beforeclass() {
	//	test = extent.createTest(getClass().getSimpleName());
		rp = new ReconsignmentPage(driver);
		cm = new CommonMethods(driver);
		cp = new CommonPage(driver);
		wt = new WaitHelper(driver);
		js = new JavaScriptHelper(driver);
		np = new NavigationPages(driver);
		rep = new ReturnPage(driver);
	//	driver.navigate().refresh();
		
		firstUniqueText = new ArrayList<>(Arrays.asList("", "", ""));
		validationOrdNo = new ArrayList<>(Arrays.asList("GL-9999", "GL-9999"));
		trkingValidationOrdNo = new ArrayList<>(Arrays.asList("GL-9999"));

	}

	@Test(priority = 1)
	public void shouldAbleTopickOrderAsPerRequiredStatus() throws InterruptedException, java.util.concurrent.TimeoutException {
		SoftAssert sAssert = new SoftAssert();
//		String[] array = { "GL-9351", "GL-9313", "GL-9272" };
		rp.getOrdersData(0, "Outbound Pallet Pending", firstUniqueText);
		rp.getOrdersData(1, "Outbound QA Pending", firstUniqueText);
		rp.getOrdersData(2, "Consignee Delivery Schedule Pending", firstUniqueText);
		System.out.println("👉 Final Captured Status List: " + firstUniqueText);
		rp.getValidationData(0, "Draft", validationOrdNo);
		rp.getValidationData(1, "Reconsignment", validationOrdNo);
	System.out.println("👉 Final Captured Status List: " + validationOrdNo);
		rp.getTrkingValidationData(0, "Outbound Pallet Pending", trkingValidationOrdNo);
		System.out.println("👉 Final Captured Status List: " + trkingValidationOrdNo);
	//	np.outboundPalletProcess_array(array);
	}
    
	@Test(priority = 2)
	public void SouldAbleToNavigateOnReconsignmentPage() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		rep.returnMenuWithExpand();
		rep.addNewReturnOrder();
		
		rep.clickOrderLookup();

	}
   
//	@Test(priority = 3)
//	public void ShouldGetsErrorWhenFetchDraftOrders() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		Thread.sleep(2000);
//		rep.clickOrderLookup();
//		rep.fetchLookupOrder(validationOrdNo.get(0));
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Order details not found");
//		sAssert.assertAll();
//		
//	}
//	
//	@Test(priority = 4)
//
// void ShouldGetsErrorWhenFetchReturnOrders() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.waitForPopupToDisappear();
//		Thread.sleep(2000);
//		rep.fetchLookupOrder(validationOrdNo.get(1));
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Order details not found");
//		sAssert.assertAll();
//		
//	}
//	
//	@Test(priority = 5)
//	public void ShouldGetsErrorWhenFetchWithoutTrkingDetailsOrders() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		cp.waitForPopupToDisappear();
//		Thread.sleep(2000);
//		rep.fetchLookupOrder(trkingValidationOrdNo.get(0));
//		boolean flag = cp.toastMsgReceivedSuccessfully();
//		sAssert.assertTrue(flag, "Paperwork Pending");
//		sAssert.assertAll();
//		
//	}
	
	@Test(priority = 6)
	public void ShouldFetchWithValidDetailsOrders() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		Thread.sleep(2000);
		rep.fetchLookupOrder("9279");
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertFalse(flag, "Paperwork Pending");
		sAssert.assertAll();
		
	}
	
//	@Test(priority = 7)
//	public void shouldNotAbleToSeeSameConsignee() throws InterruptedException {
//		SoftAssert sAssert = new SoftAssert();
//		consignee = rep.getOriginalConsignee();
//		rep.reconsignToConsigneeValidation(consignee);
//		sAssert.assertEquals(rep.checkDrpDwnEleCount(), 1);
//	}
	
	@Test(priority =  8)
	public void ShouldAbleToSelectConsigneeWithCarrierAndAddressDetails() {
		rep.compareCustomerAndOrderLocation();
		rep.actualWtValidation();
		rep.litNoOfPiecesValidation();
		rep.litNumOfPiecesValidation2();
		rep.numOfPiecesValidation();
//		cp.save();
		rep.getReturnOrdNo(0, firstUniqueText);
	}
	
	@Test(priority = 95)
	public void shouldDoOutProcessForCloseoutOrder() throws TimeoutException, InterruptedException {
//		System.out.println("iOriginOrderNo " + cop.iOriginOrderNo);
//		System.out.println("cop.pickupLocation " + cop.pickupLocation);
//		System.out.println("cop.OriginOrderNoObpl " + cop.OriginOrderNoObpl);
//		System.out.println("cop.pickupLocationObpl " + cop.pickupLocationObpl);
//		System.out.println("cop.DiffLDAOrderNo " + cop.DiffLDAOrderNo);
//		System.out.println("cop.LDAPickuplocation " + cop.LDAPickuplocation);
//
//		np.createOutboundPallet(cop.iOriginOrderNo, cop.pickupLocation, "Warehouse", "Origin", 0);
//		np.createOutboundPallet(cop.OriginOrderNoObpl, cop.pickupLocationObpl, "LDA", "Origin", 1);
//		np.createOutboundPallet(cop.DiffLDAOrderNo, cop.LDAPickuplocation, "LDA", "LDA", 2);
//
////	np.obtTruckProcess(DataRepo.WAREHOUSE, "OBPL-3084", "Warehouse", "Origin");
//		System.out.println("cop.pickupLocation " + cop.pickupLocation);
//		System.out.println(" np.dataArray[0]" + np.dataArray[0]);
//		System.out.println("cop.pickupLocationObpl " + cop.pickupLocationObpl);
//		System.out.println("np.dataArray[1]" + np.dataArray[1]);
//		System.out.println("cop.pickupLocationObpl" + cop.pickupLocationObpl);
//		System.out.println("np.dataArray[2]" + np.dataArray[2]);
//
//		np.obtTruckProcess(cop.pickupLocation, np.dataArray[0], "Warehouse", "Origin");
//		np.obtTruckProcess(cop.pickupLocationObpl, np.dataArray[1], "LDA", "Origin");
//		np.obtTruckProcess(cop.pickupLocationObpl, np.dataArray[2], "LDA", "LDA");
//
//		np.docGenerationProcess(cop.DiffLDAOrderNo, 0);
//		np.docGenerationProcess(cop.SameLDAOrderNo, 1);
//
//		np.trkingDetailUpdateProcess();
//
//		np.outboundQAProcess(np.ldaaArray[0], np.manifestArray[0]);
//		np.outboundQAProcess(np.ldaaArray[1], np.manifestArray[1]);
//
//		np.consigneeQAProcess(np.lbolArray[0]);
//		np.consigneeQAProcess(np.lbolArray[1]);
//
//		np.returnPODProcess(cop.iOriginOrderNo);
//		np.returnPODProcess(cop.OriginOrderNoObpl);

	}

}
