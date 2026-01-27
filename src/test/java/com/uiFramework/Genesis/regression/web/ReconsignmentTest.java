package com.uiFramework.Genesis.regression.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.TimeoutException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.JavaScriptHelper;
import com.uiFramework.Genesis.helper.WaitHelper;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.NavigationPages;
import com.uiFramework.Genesis.web.pages.OrderPage;
import com.uiFramework.Genesis.web.pages.ReconsignmentPage;

public class ReconsignmentTest extends TestBase {
	ReconsignmentPage rp = null;
	CommonMethods cm = null;
	CommonPage cp = null;
	WaitHelper wt = null;
	JavaScriptHelper js = null;
	String consignee;
	NavigationPages np;
	OrderPage op ;

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
		op = new OrderPage(driver);
	//	driver.navigate().refresh();
		
		firstUniqueText = new ArrayList<>(Arrays.asList("", "", ""));
		validationOrdNo = new ArrayList<>(Arrays.asList("", ""));
		trkingValidationOrdNo = new ArrayList<>(Arrays.asList(""));

	}

	@Test(priority = 1)
	public void shouldAbleTopickOrderAsPerRequiredStatus() throws InterruptedException, java.util.concurrent.TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		rp.getOrdersData(0, "Outbound Pallet Pending", firstUniqueText);
		System.out.println("👉 Final Captured Status List: " + firstUniqueText);
		rp.getOrdersData(1, "Outbound QA Pending", firstUniqueText);
		rp.getOrdersData(2, "Consignee Delivery Schedule Pending", firstUniqueText);
		System.out.println("👉 Final Captured Status List: " + firstUniqueText);
		rp.getValidationData(0, "Draft", validationOrdNo);
		rp.getValidationData(1, "Returned", validationOrdNo);
		System.out.println("👉 Final Captured Status List: " + validationOrdNo);
		rp.getTrkingValidationData(0, "Outbound Pallet Pending", trkingValidationOrdNo);
		System.out.println("👉 Final Captured Status List: " + trkingValidationOrdNo);
	}
    
	@Test(priority = 2)
	public void SouldAbleToNavigateOnReconsignmentPage() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		rp.reconsignMenuWithExpand();
		rp.addNewReconsignOrder();

	}
   
	@Test(priority = 3)
	public void ShouldGetsErrorWhenFetchDraftOrders() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
	//	cp.waitForPopupToDisappear1();
		Thread.sleep(2000);
		rp.clickOrderLookup();
		rp.fetchLookupOrder(validationOrdNo.get(0));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Order details not found");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 4)
	public void ShouldGetsErrorWhenFetchReturnOrders() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		Thread.sleep(2000);
		rp.fetchLookupOrder(validationOrdNo.get(1));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Order details not found");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 5)
	public void ShouldGetsErrorWhenFetchWithoutTrkingDetailsOrders() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		Thread.sleep(2000);
		rp.fetchLookupOrder(trkingValidationOrdNo.get(0));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertTrue(flag, "Paperwork Pending");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 6)
	public void ShouldFetchWithValidDetailsOrders() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		Thread.sleep(2000);
		rp.fetchLookupOrder(firstUniqueText.get(0));
		boolean flag = cp.toastMsgReceivedSuccessfully();
		sAssert.assertFalse(flag, "Paperwork Pending");
		sAssert.assertAll();
		
	}
	
	@Test(priority = 7)
	public void shouldNotAbleToSeeSameConsignee() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		consignee = rp.getOriginalConsignee();
		rp.reconsignToConsigneeValidation(consignee);
		sAssert.assertEquals(rp.checkDrpDwnEleCount(), 1);
	}
	
	@Test(priority = 8)
	public void shouldselectConsignee() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		//rp.reconsignMenuWithExpand();
		
		rp.selectAnyConsigneeOrSkip();
	//	consignee = rp.getOriginalConsignee();
		//rp.reconsignToConsigneeValidation(consignee);
		//sAssert.assertEquals(rp.checkDrpDwnEleCount(), 1);
	}
	
	
	@Test(priority = 9,alwaysRun = true, groups = {"Smoke"})
	public void shouldShowErrorWhenSavingEmptyCustomerAccessorial() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		cp.save();
		op.clickAccessorialButton();
		sAssert.assertEquals(op.addAccessorial(), "Customer Accessorial is required. | Price is required.",
				"customer accessorial not matched");
		sAssert.assertAll();
	}

	@Test(priority = 10, alwaysRun = true, groups = {"Smoke"})
	public void shouldAddCustomerAccessorialSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCustAccess();
		op.editCustAccess(DataRepo.PRICE2);
		op.deleteCustAccess();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer accessorial deleted successfully", "Not Matched");
		sAssert.assertAll();
	}

	@Test(priority = 11,alwaysRun = true, groups = {"Smoke"})
	public void shouldSaveCustomerAccessorialAfterDeleteAndValidateCarrierValidation() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCustAccessAfterDelete();
		sAssert.assertEquals(cp.captureToastMessage(), "Customer accessorial save successfully", "Not Matched.");
		sAssert.assertEquals(op.addCarrAccessorial(), "Carrier Accessorial is required. | Price is required.",
				"Not matched.");
		sAssert.assertAll();

	}

	@Test(priority = 12,alwaysRun = true, groups = {"Smoke"})
	public void shouldAddEditAndDeleteCarrierAccessorialSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCarrAccess(DataRepo.PRICE, DataRepo.TEXT);
		op.editCarrAccess(DataRepo.PRICE2);
		op.deleteCarrAccess();
		sAssert.assertEquals(cp.captureToastMessage(), "Carrier accessorial deleted successfully", "Not matched.");
		sAssert.assertAll();
	}

	@Test(priority = 13 ,alwaysRun = true, groups = {"Smoke"})
	public void shouldSaveCarrierAccessorialAfterDeleteAndValidateCommentValidation()
			throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCarrAccessAfterDelete(DataRepo.PRICE, DataRepo.TEXT);
		sAssert.assertEquals(cp.captureToastMessage(), "Carrier accessorial save successfully", "Not Match");
		sAssert.assertAll();
	}
	
	@Test(priority = 13 ,alwaysRun = true, groups = {"Smoke"})
	public void shouldSaveTypeAndsubjectReqOnComment()
			throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(op.addComment(), "Type is required. | Subject is required.", "Not Matched");
		sAssert.assertAll();
	}

	@Test(priority = 14)
	public void shouldAddEditAndDeleteCommentSuccessfully() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveComment("PDMA", DataRepo.SUBJECT, DataRepo.COMMENT);
		op.deleteComment();
		sAssert.assertEquals(cp.captureToastMessage(), "Comment deleted successfully", "Not MAtch!");
		sAssert.assertAll();
	}

	@Test(priority = 15,alwaysRun = true, groups = {"Smoke"})
	public void shouldSaveCommentAfterDeleteAndValidateFileUploadErrors()
			throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.saveCommentAfterDelete("Contact", DataRepo.SUBJECT, DataRepo.COMMENT);
		sAssert.assertEquals(cp.captureToastMessage(), "Comment save successfully", "Not match!");
		sAssert.assertEquals(op.addFile(), "Document Name is required. | File is required.", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 16)
	public void shouldUploadFileAndShowSuccessToast() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.savefile("update price");
		sAssert.assertEquals(cp.captureToastMessage(), "File save successfully", "Not match!");
		sAssert.assertAll();
	}

	@Test(priority = 17, dependsOnMethods = "shouldUploadFileAndShowSuccessToast")
	public void shouldDeleteFileAndVerifyOrderStatusAsDraft() throws Throwable {
		SoftAssert sAssert = new SoftAssert();
		op.deleteFile();
		sAssert.assertEquals(cp.captureToastMessage(), "File deleted successfully", "Not match!");
		sAssert.assertEquals(op.verifyOrderStatus(), "Draft", "Not Matched!");
		sAssert.assertAll();
	}


}
