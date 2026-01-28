package com.uiFramework.Genesis.regression.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.InbTruckPage;
import com.uiFramework.Genesis.web.pages.InboundQAPage;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class InboundQATest extends TestBase {
	InbTruckPage it = null;
	InboundQAPage iqa = null;
	CommonPage cp = null;
	CommonMethods cm;
	OrderPage op = null;

	SoftAssert sAssert = new SoftAssert();
	private static final Logger logger = Logger.getLogger(InboundTruckTest.class.getName());

	@DataProvider
	public Object[][] getData() throws IOException {
		List<HashMap<String, String>> data = cm
				.getJsonData(System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\DynamicData.JSON");

		return new Object[][] { { data.get(0) } };
	}

	@BeforeClass(alwaysRun = true)
	public void befreclass() {
	//	test = extent.createTest(getClass().getSimpleName());
		cm = new CommonMethods(driver);
		it = new InbTruckPage(driver);
		cp = new CommonPage(driver);
		iqa = new InboundQAPage(driver);
		op = new OrderPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1, alwaysRun = true, groups = {"smoke"})
	public void verifyPalletAvailable() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		iqa.inboundQaMenu();
		iqa.searchOrder();
		sAssert.assertEquals(iqa.getMTruckNo(), iqa.MTruckNo, "Main Truck not available for inbound QA.");
		sAssert.assertAll();
	}

	@Test(priority = 2, alwaysRun = true, groups = {"smoke"})
	public void verifyToasmsgOnQAPass() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		iqa.viewButton();
		iqa.positiveSave();
		sAssert.assertEquals(cp.captureToastMessage(), "QA Processed Successfully.", "Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void verifyTruckDataDisplayedCorrectlyOnView() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		iqa.inboundQaMenu();
		iqa.searchPendingStatus(DataRepo.QA_PENDING);
		iqa.getListTruckData();
		iqa.viewButton();
		iqa.getQAviewData();
		iqa.validateTruckdata();
		sAssert.assertAll();
	}

	@Test(priority = 4, dependsOnMethods="verifyTruckDataDisplayedCorrectlyOnView")
	public void verifyMandatoryPiecesOnSaveBtn() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		iqa.clearpieces();
		iqa.saveBtn();
		sAssert.assertEquals(cp.captureToastMessage(),
				"Please enter received order /literature pieces for every customer order.", "Not found.");
		cp.waitForLoaderToDisappear();
		cp.waitForPopupToDisappear();
		sAssert.assertAll();
	}

	@Test(priority = 5, dependsOnMethods="verifyTruckDataDisplayedCorrectlyOnView")
	public void checkDefaultZeroPopulate() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(iqa.checkThatlitQtyDefaultZero(2), DataRepo.ZERO,
				"FAIL: Does not have default value '0' when pieces is zero.");
		sAssert.assertAll();
	}

	@Test(priority = 6, dependsOnMethods="verifyTruckDataDisplayedCorrectlyOnView")
	public void validateInboundQAscreen() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		iqa.checkValidationQA();
		sAssert.assertEquals(iqa.ToastfoQAsuccess, "QA Processed Successfully.","Not match.");
		sAssert.assertAll();
	}

	@Test(priority = 7)
	public void validateTotalPiecesinTruck() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForLoaderToDisappear();
		sAssert.assertEquals(iqa.getSumofOrderPieces(), iqa.totalPiecesinTruck(),
				"Total Pieces count not match for Truck and Orders");
		iqa.getGLorder();
		cp.clickBackBtn();
		sAssert.assertAll();
	}

	@Test(priority = 8)
	public void checkValidationOnScanningPieces()
			throws TimeoutException, InterruptedException {
		iqa.inboundQaMenu();
		iqa.searchPendingStatus(DataRepo.QA_PENDING);
		iqa.viewButton();
		iqa.checkValidationQAScanning();
	}
	
	@Test(priority = 9, alwaysRun = true, groups = {"smoke"})
	public void shouldCheckColumnFilter() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		iqa.inboundQaMenu();
		cp.verifyColumnFilterForFixGrid();
		sAssert.assertAll();
	}

	@Test(priority = 10, groups = {"smoke"}, enabled = true)
	public void ValidateFilterOnQAListing() throws InterruptedException, TimeoutException {
		iqa.inboundQaMenu();
		cp.waitForLoaderToDisappear();
		cp.searchSection();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";
		
		cp.clickClearButton(); 
		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
		cp.selectMultiplePickuprByIndex(indicesToSelect);
		cp.validateDataInGrid(7);

		List<Integer> indicesToSelect1 = Arrays.asList(1, 2, 3);
		cp.selectMultipleDropByIndex(indicesToSelect1);
		cp.validateDataInGrid(8);

		List<Integer> indicesToSelect2 = Arrays.asList(1, 2, 33);
		cp.selectMultipleLTLByIndex(indicesToSelect2);
		cp.validateDataInGrid(5);

		List<Integer> indicesToSelect3 = Arrays.asList(5, 3, 2);
		cp.selectMultiplestatusByIndex(indicesToSelect3);
		cp.validateDataInGrid(6);

		iqa.searchAndValidateTruckNo();
		cp.clickClearButton();
		iqa.searchAndValidateTruckName();
		cp.clickClearButton();

		Map<String, String> palletNo = CommonMethods.getXPathSet("inboundTruckPalletNo", jsonPath);
		cp.extractInputFromIconSearchVerify(palletNo);

		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundTruckOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerifyTwoIcons(inboundOrderNomap);

		cp.clickClearButton();

	}

//	@Test(priority = 11, groups = "smoke")
//	public void checkPagination() throws TimeoutException, InterruptedException {
//		iqa.inboundQaMenu();
//		iqa.paginationOnListing();
//		cp.waitForLoaderToDisappear();
//	}

	@Test(priority = 12)
	public void checkQAStatusFailedforInbound() throws InterruptedException {
		System.out.println("Scann GL is: " + iqa.scannGL);
		if (iqa.yesBtncheck()) {
			op.landingOrderListing();

			String actual = iqa.getOrderQAStatus(iqa.scannGL).trim();
			String expected = "QA Failed".trim();

			logger.info(actual.startsWith(expected) + " result");
			Assert.assertTrue(actual.startsWith(expected), "QA Pass status not with order.");
		} else {
			logger.info("Skipping QA Status check as Yes button was not displayed.");
		}
	}
	
	@Test(priority = 13, groups = {"smoke"})
	public void checkQAStatusforInbound() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.orderpageMenulanding();
		sAssert.assertEquals(iqa.getOrderQAStatus(it.MGLOrderno), "QA Passed", "QA Pass status not with order.");
		sAssert.assertAll();	
	}

	@AfterClass()
	public void checkOrderStatusAfterInboundQAdone() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		op.landingOrderListing();
		sAssert.assertEquals(cp.getOrderStatus(it.MGLOrderno),"Outbound Pallet Pending", "Not match.");
		sAssert.assertAll();
	}

}
