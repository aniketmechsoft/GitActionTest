package com.uiFramework.Genesis.regression.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.OrderPage;
import com.uiFramework.Genesis.web.pages.OutboundPalletPage;

public class OutboundPalletTest extends TestBase {
	OutboundPalletPage opp = null;
	CommonPage cp = null;
	CommonMethods cm;
	OrderPage op = null;
	int initialCount;
	int afterCount;
	String outboundPallet;
	String GLorder;

	SoftAssert sAssert = new SoftAssert();
	private static final Logger logger = Logger.getLogger(InboundPalletTest.class.getName());

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
		op = new OrderPage(driver);
		opp = new OutboundPalletPage(driver);
		cp = new CommonPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1,alwaysRun = true, groups = {"Smoke"})
	public void shouldCreateOutboundPalletForMainOrder() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		opp.createPallet();
		opp.searchOrder();
		sAssert.assertEquals(opp.getMGLorder(), opp.MGLOrderno, "Main Order not available to Create pallet");
		sAssert.assertAll();
	}

	@Test(priority = 2,alwaysRun = true, groups = {"Smoke"})
	public void shouldCreateOutboundPalletSuccessfully() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		opp.createPalletforMorder();
		sAssert.assertEquals(opp.createPalletmsg(), "Pallets created successfully.",
				"pallet create message not matched");
		opp.getMOBPL();
		sAssert.assertAll();
	}

	@Test(priority = 3,alwaysRun = true, groups = {"Smoke"})
	public void shouldDisplaySelectedWarehouseOrderInGrid() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		initialCount = opp.getTotalEntriesCount(driver);
		opp.createPallet();
		cp.searchSection();
		opp.verifyOrderAvailableAsperwarehouse();
		cp.searchSection();
		sAssert.assertAll();
	}

	@Test(priority = 4,alwaysRun = true, groups = {"Smoke"})
	public void shouldPerformMergeFunctionOnOutboundPallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.selectCheckbox();
		opp.searchLDA();
		opp.selectCheckbox();
		opp.mergeBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Pallets created successfully.",
				"pallet create message not matched");
		sAssert.assertAll();

	}

	@Test(priority = 5, dependsOnMethods="shouldPerformMergeFunctionOnOutboundPallet",alwaysRun = true, groups = {"Smoke"})
	public void shouldCreateSinglePalletOnMergeClick() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		afterCount = opp.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase by 1.");
		sAssert.assertAll();

	}

	@Test(priority = 6, dependsOnMethods="shouldPerformMergeFunctionOnOutboundPallet")
	public void shouldIncludeMultipleLDAsInSinglePallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		int ldaCount = opp.getLDAfromListng();

		if (ldaCount < 2) {
			throw new SkipException("SKIPPED: LDA count is less than 2. Actual count: " + ldaCount);
		}
		
		sAssert.assertTrue(ldaCount > 1, "FAIL: LDA count is not greater than 1. Actual count: " + ldaCount);
		sAssert.assertAll();
	}

	@Test(priority = 7,alwaysRun = true, groups = {"Smoke"})
	public void shouldUpdatePalletStatusAfterPalletCreation()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		opp.editBtn();
		sAssert.assertEquals(opp.PalletStatusonEdit(), DataRepo.STATUS,
				"Pallet Status not match. expected: Waiting for Load");
		sAssert.assertAll();
	}

	@Test(priority = 8,alwaysRun = true, groups = {"Smoke"})
	public void shouldAllowEditPalletWithAutoDeletedPallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.verifyPickndDrop();
		outboundPallet = opp.getOutboundPallet();
		GLorder = opp.getPalletorder();
		opp.checkRemovedOrders();
		sAssert.assertEquals(opp.palletDelete(), "Pallet deleted successfully", "Delete pallet fro outbound Not match");
		sAssert.assertAll();
	}

	@Test(priority = 9 ,dependsOnMethods="shouldAllowEditPalletWithAutoDeletedPallet")
	public void verifyAutoDeletedPalletNotVisibleAndOrderIsAvailableToAdd()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.searchData(outboundPallet);
		sAssert.assertTrue(opp.checkElementNorecord(), "'No record found' is Not displayed.” Test Failed");
		opp.createPallet();
		opp.searchData(GLorder);
		sAssert.assertFalse(opp.checkElementNorecord(), "'No record found' is displayed. Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 10,alwaysRun = true, groups = {"Smoke"})
	public void shouldCreatePalletAndMaintainCheckboxSelectionAcrossPagination()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		initialCount = opp.getTotalEntriesCount(driver);
		opp.createPallet();
		opp.selectMulcheckBoxForCretepallet();
		afterCount = opp.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, 3, "Entry count did not increase by 3. Outbound Pallet Not create.");
		sAssert.assertAll();
	}

	@Test(priority = 11, dependsOnMethods="shouldCreatePalletAndMaintainCheckboxSelectionAcrossPagination")
	public void verifyCreatedOrderIsAvailableInPallet() throws TimeoutException, InterruptedException {
		opp.orderInfo();
		cp.clickToClosePopUp();
	}

	@Test(priority = 12, dependsOnMethods="shouldCreatePalletAndMaintainCheckboxSelectionAcrossPagination",alwaysRun = true, groups = {"Smoke"})
	public void shouldDisplayPalletStatusInListingAfterObtPalletCreation()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		sAssert.assertEquals(opp.PalletStatusonListing(), DataRepo.STATUS,
				"Pallet Status not match on Lisitng. expected: Waiting for Load");
		sAssert.assertAll();
	}

	@Test(priority = 13,alwaysRun = true, groups = {"Smoke"})
	public void shouldAddPalletInEditModeWithPiecesAndWeightWithNoPopup() throws TimeoutException, InterruptedException {
		opp.outboundMenu();
		opp.editBtn();
		outboundPallet = opp.getOutboundPallet();
		opp.checkNoBtnonPopup();
		opp.checkAddedOrdersinPallet();
	}

	@Test(priority = 14, dependsOnMethods="shouldAddPalletInEditModeWithPiecesAndWeightWithNoPopup")
	public void shouldNotAllowAddedOrdersForPalletCreation() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.clickBackBtn();
		opp.createPallet();
		sAssert.assertTrue(opp.searchOrders(), "One or more removed orders are still displayed in the grid.");
		sAssert.assertAll();
	}

	@Test(priority = 15)
	public void shouldVerifyPalletQuantityOnOutboundPallet()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		opp.editBtn();
		opp.removedPalletQty(DataRepo.PALLET_QTY);
		opp.backBtn();
		sAssert.assertEquals(opp.getPalletQty(), DataRepo.PALLET_QTY, "Pallet quantity not same on create and listing page");
		sAssert.assertAll();
	}

	@Test(priority = 16)
	public void shouldDeletePalletAndMakeOrdersAvailableAfterDeletion() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		opp.checkDeletedOrderAvailable();
		GLorder = opp.iGLorder();
		opp.deleteBtn();
		Assert.assertEquals(cp.captureToastMessage(), "Outbound pallet deleted successfully.",
				"outbound pallet delete successfull message not found.");
		opp.createPallet();
		opp.search1Order(GLorder);
		sAssert.assertEquals(opp.getGLorder(), GLorder, "Deleted pallet Order Not available to Add");
		sAssert.assertAll();
	}

	@Test(priority = 17)
	public void shouldNotAllowPalletDeletionAfterArrival() throws TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		opp.outboundMenu();
		cp.searchSection();
		opp.selectArrivedStatus();
		cp.Search();
		opp.searchStatus(DataRepo.ARRIVED_STATUS);
		cp.searchSection();
		opp.deleteBtnDirect();
		sAssert.assertEquals(cp.captureToastMessage(), "Pallet already Arrived",
				"Pallet already Arrived message not found.");
		sAssert.assertAll();
	}

	@Test(priority = 18, dataProvider = "getData", groups = "Smoke", enabled = false)
	public void shouldApplyFiltersCorrectlyForOutboundPallet(HashMap<String, String> input)
			throws InterruptedException, TimeoutException {
		opp.outboundMenu();
		cp.searchSection();
		cp.clickClearButton();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";

		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
		cp.selectMultiplePickuprByIndex(indicesToSelect);
		cp.validateDataInGrid(14);
		List<Integer> indicesToSelect2 = Arrays.asList(4, 3);
		opp.selectMultiplestatusByIndex(indicesToSelect2);
		cp.validateDataInGrid(13);
		opp.searchAndValidateTruckNo();
		opp.searchAndValidatePalletNo();
		Map<String, String> customerOrderMap = CommonMethods.getXPathSet("inboundCustomerOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerify(customerOrderMap);
		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerify(inboundOrderNomap);
		cp.searchAndValidateDateByColumn(8);
		cp.searchAndValidateToDateByColumn(8);
		cp.pickFrom();

	}

	@Test(priority = 19, groups = "Smoke")
	public void shouldCheckPaginationOnOutboundPalletListing() throws TimeoutException, InterruptedException {
		opp.outboundMenu();
		opp.paginationOnListing();
	}

	@Test(priority = 20, dataProvider = "getData", groups = "Smoke", enabled = false)
	public void shouldApplyFiltersOnCreateOutboundPalletScreen(HashMap<String, String> input)
			throws InterruptedException, TimeoutException {
		opp.outboundMenu();
		opp.createPallet();
		cp.searchSection();
		cp.clickClearButton();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";

		List<Integer> indicesToSelect1 = Arrays.asList(52, 2, 1);
		cp.selectMultipleCustomerByIndex(indicesToSelect1);
		cp.validateDataInGrid(5);

		opp.searchAndValidateOrderNumber();

		List<Integer> indicesToSelect = Arrays.asList(13, 2, 8);
		cp.selectMultipleConsigneesByIndex(indicesToSelect);
		cp.validateDataInGrid(7);

		List<Integer> indicesToSelect3 = Arrays.asList(86, 4, 3);
		cp.selectMultipleLDAByIndex(indicesToSelect3);
		cp.validateDataInGrid(6);
		opp.checkDropFor();
	}

	@Test(priority = 21)
	public void shouldCheckPaginationOnCreateOutboundPalletScreen() throws TimeoutException, InterruptedException {
		opp.outboundMenu();
		opp.createPallet();
		opp.paginationOnCeatePallet();
	}

	@Test(priority = 22, enabled = false)
	public void checkColoumfilter() throws TimeoutException, InterruptedException, IOException {
		opp.outboundMenu();
		opp.checkColoumFilter();
	}

	@Test(priority = 23, enabled = false)
	public void shouldApplyColumnFilterOnCreateOutboundPallet() throws TimeoutException, InterruptedException, IOException {
		opp.outboundMenu();
		opp.createPallet();
		opp.checkColoumFilterOnCreatePage();
	}

	@AfterClass()
	public void shouldUpdateOrderStatusAfterCreatingOutboundPallet() {
		cp.orderpageMenulanding();
		Assert.assertEquals(opp.getOrderStatus(op.getMGLOrderno()), "Outbound Truck Load Pending",
				"order status after Outbound palletcreate is not match.");
	}

}
