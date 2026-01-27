package com.uiFramework.Genesis.regression.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.uiFramework.Genesis.base.TestBase;
import com.uiFramework.Genesis.common.CommonMethods;
import com.uiFramework.Genesis.helper.ReadJsonData;
import com.uiFramework.Genesis.web.pages.CommonPage;
import com.uiFramework.Genesis.web.pages.DataRepo;
import com.uiFramework.Genesis.web.pages.InbTruckPage;
import com.uiFramework.Genesis.web.pages.InboundPalletPage;
import com.uiFramework.Genesis.web.pages.OrderPage;

public class InboundPalletTest extends TestBase {
	InbTruckPage it = null;
	InboundPalletPage ip = null;
	CommonPage cp = null;
	CommonMethods cm;
	OrderPage op = null;
	int initialCount;
	int afterCount;
	String inboundPallet;
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
		cm = new CommonMethods(driver);
		op = new OrderPage(driver);
		ip = new InboundPalletPage(driver);
		cp = new CommonPage(driver);
		it = new InbTruckPage(driver);
	//	driver.navigate().refresh();

	}

	@Test(priority = 1,alwaysRun = true, groups = {"smoke"})
	public void CreateInboundPalletforMainOrder() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		//Thread.sleep(5000);		
		ip.createPallet();
		//Thread.sleep(1000);		
		ip.searchOrder();
		//Thread.sleep(1000);		
		sAssert.assertEquals(ip.getMGLorder(), ip.MGLOrderno, "Main Order not available to Create pallet");
		sAssert.assertAll();
	}

	@Test(priority = 2,alwaysRun = true, groups = {"smoke"})
	public void verifyInboundpalletCreateSuccefully() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.createPalletforMorder();
		Thread.sleep(1000);		
		sAssert.assertEquals(cp.captureToastMessage(), "Pallets are created successfully.",
				"pallet create message not found.");
		ip.getMIBPL();
		Thread.sleep(1000);		
		sAssert.assertAll();
	}

	@Test(priority = 3)
	public void shouldNotMergeInboundPalletWithDifferentPickup() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		//Thread.sleep(1000);		
		initialCount = ip.getTotalEntriesCount(driver);
		ip.createPallet();
		//Thread.sleep(1000);	
		ip.selectCheckbox();		
		ip.searchPickupLocation();
		Thread.sleep(1000);		
		ip.selectCheckbox();
		Thread.sleep(1000);		
		ip.mergeBtn();
		sAssert.assertEquals(cp.captureToastMessage(), "Can't merge orders, pickup locations are differents.",
				"pickup locations are differents.");
		sAssert.assertAll();
	}

	@Test(priority = 4)
	public void shouldPerformMergeWhenButtonClicked() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.selctDropAndpickup();
		ip.selectmulcheckBox();
		afterCount = ip.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.ONE, "Entry count did not increase by 1.");
		sAssert.assertAll();

	}

	@Test(priority = 5)
	public void shouldRemoveOrdersInEditPalletAndPalletAutoDelete() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		ip.editBtn();
		ip.verifyPickndDrop();
		inboundPallet = ip.getInboundPallet();
		GLorder = ip.getPalletorder();
		ip.checkRemovedOrders();
		sAssert.assertEquals(cp.getToastMessage(), "Pallet deleted successfully.", "not matched");
		sAssert.assertAll();

	}

	@Test(priority = 6, dependsOnMethods = "shouldRemoveOrdersInEditPalletAndPalletAutoDelete")
	public void shouldAutoDeletePalletAndAllowOrderToBeAdded() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.searchData(inboundPallet);
		sAssert.assertTrue(ip.checkElementNorecord(), "'No record found' is Not displayed.” Test Failed");
		ip.createPallet();
		ip.searchData(GLorder);
		sAssert.assertFalse(ip.checkElementNorecord(), "'No record found' is displayed. Test Failed");
		sAssert.assertAll();
	}

	@Test(priority = 7,alwaysRun = true, groups = {"smoke"})
	public void shouldCreateMultiplePallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		initialCount = ip.getTotalEntriesCount(driver);
		ip.createPallet();
		ip.selectMulcheckBoxForCretepallet();
		afterCount = ip.getTotalEntriesCount(driver);
		int diffint = afterCount - initialCount;
		String difference = String.valueOf(diffint);
		sAssert.assertEquals(difference, DataRepo.THREE, "Entry count did not increase by 3.");
		sAssert.assertAll();

	}

	@Test(priority = 8, dependsOnMethods = "shouldCreateMultiplePallet")
	public void shouldShowCreatedOrderInPalletListingPage() throws TimeoutException, InterruptedException {
		ip.orderInfo();
	}

	@Test(priority = 9,alwaysRun = true, groups = {"smoke"})
	public void shouldAddPalletInEditModeWithPiecesAndWeightWithNoPopup() throws TimeoutException, InterruptedException {
		ip.inboundMenu();
		ip.editBtn();
		inboundPallet = ip.getInboundPallet();
		ip.checkNoBtnonPopup();
		ip.checkAddedOrdersinPallet();
	}

	@Test(priority = 10, dependsOnMethods = "shouldAddPalletInEditModeWithPiecesAndWeightWithNoPopup")
	public void shouldNotAllowAddingSameOrderToAnotherPallet() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.waitForPopupToDisappear();
		cp.clickBackBtn();
		ip.createPallet();
		sAssert.assertTrue(ip.searchOrders(), "One or more removed orders are still displayed in the grid.");
		sAssert.assertAll();
	}

	@Test(priority = 11)
	public void shouldVerifyPalletQuantityAndStatusForInboundPallet()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		ip.editBtn();
		ip.removedPalletQty(DataRepo.PALLET_QTY);
		ip.backBtn();
		sAssert.assertEquals(ip.getPalletQty(), DataRepo.PALLET_QTY, "Pallet quantity not same on create and listing");
		sAssert.assertAll();
	}

	@Test(priority = 12)
	public void shouldAllowDeletedPalletToBeAvailableAfterPalletDeletion() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		ip.checkDeletedOrderAvailable();
		GLorder = ip.iGLorder();
		ip.deleteBtn();
		sAssert.assertEquals(cp.getToastMessage(), "Pallet deleted successfully.","not matched");
		ip.createPallet();
		ip.search1Order(GLorder);
		sAssert.assertEquals(ip.getGLorder(), GLorder, "Deleted pallet Order Not available to Add");
		sAssert.assertAll();
	}

	@Test(priority = 13)
	public void shouldNotDeletePalletIfQADone() throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		cp.searchSection();
		ip.selectQAdoneStatus();
		cp.Search();
		ip.searchQAdone(DataRepo.QA_DONE);
		ip.deleteBtnDirect();
		sAssert.assertEquals(cp.captureToastMessage(), "This pallet QA is already done.You can not delete it.","Pallet QA done.");
		sAssert.assertAll();
	}

	@Test(priority = 14)
	public void shouldNotAllowPalletEditingIfQADone()
			throws TimeoutException, InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		ip.searchQAdone(DataRepo.QA_DONE);
		ip.editBtn();
		sAssert.assertFalse(ip.verifyRemovePalleBtnDisable().isEnabled(), "Remove order btn is Editabel when QA done.");
		sAssert.assertFalse(ip.verifyAddPalleBtnDisable().isEnabled(), "Add order btn is Editabel when QA done.");
		sAssert.assertAll();

	}

	@Test(priority = 15, dataProvider = "getData", groups = {"smoke"}, enabled = true)
	public void shouldApplyFiltersCorrectlyOnInboundPallet(HashMap<String, String> input) throws InterruptedException, TimeoutException {
		ip.inboundMenu();
		cp.searchSection();
		cp.clickClearButton();
		String jsonPath = System.getProperty("user.dir") + "\\src\\main\\resources\\configfile\\xpath.JSON";

		List<Integer> indicesToSelect = Arrays.asList(55, 2, 3);
		cp.selectMultiplePickuprByIndex(indicesToSelect);
		cp.validateDataInGrid(17);

		List<Integer> indicesToSelect1 = Arrays.asList(2, 1, 3);
		cp.selectMultipleDropByIndex(indicesToSelect1);
		cp.validateDataInGrid(18);
		List<Integer> indicesToSelect2 = Arrays.asList(4, 3);
		ip.selectMultiplestatusByIndex(indicesToSelect2);
		cp.validateDataInGrid(15);
		ip.searchAndValidateTruckNo();
		ip.searchAndValidatePalletNo();
		Map<String, String> customerOrderMap = CommonMethods.getXPathSet("inboundCustomerOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerify(customerOrderMap);
		Map<String, String> inboundOrderNomap = CommonMethods.getXPathSet("inboundOrderNo", jsonPath);
		cp.extractInputFromIconSearchVerify(inboundOrderNomap);
		cp.searchAndValidateDateByColumn(10);
		cp.searchAndValidateToDateByColumn(10);
		cp.pickFrom();

	}

//	@Test(priority = 16, groups = "smoke")
//	public void shouldCheckPaginationOnInboundPallet() throws TimeoutException, InterruptedException {
//		ip.inboundMenu();
//		ip.paginationOnListing();
//
//	}

//	@Test(priority = 17)
//	public void shouldCheckPaginationOnCreateInboundPallet() throws TimeoutException, InterruptedException {
//		ip.inboundMenu();
//		ip.createPallet();
//		ip.paginationOnCeatePallet();
//	}
//
//	@Test(priority = 18, groups = "smoke", enabled = false)
//	public void shouldApplyColumnFiltersOnInboundPallet() throws InterruptedException, IOException, TimeoutException {
//		ip.inboundMenu();
//		ip.checkColoumFilter();
//
//	}
	
	@Test(priority = 13, alwaysRun = true, groups = {"smoke"})
	public void shouldCheckColumnFilter() throws InterruptedException, TimeoutException {
		SoftAssert sAssert = new SoftAssert();
		ip.inboundMenu();
		cp.verifyColumnFilter("frozen-pane");
		cp.verifyColumnFilter("scrollable-pane");
		sAssert.assertAll();
	}

	@Test(priority = 19,groups = {"smoke"})
	public void shouldApplyColumnFiltersOnCreateInboundPallet() throws InterruptedException, IOException, TimeoutException {
		ip.inboundMenu();
		ip.createPallet();
		cp.verifyColumnFilter("frozen-pane");
		cp.verifyColumnFilter("scrollable-pane");

	}

	@AfterClass()
	public void shouldCheckOrderStatusAfterInoundPalletCreate() throws InterruptedException {
		SoftAssert sAssert = new SoftAssert();
		cp.orderpageMenulanding();
		sAssert.assertEquals(cp.getOrderStatus(op.getMGLOrderno()),"Inbound Truck Load Pending", "Not match!");
		sAssert.assertAll();
	}

}
